package ru.dipech.resumes.storage.serialization;

import ru.dipech.resumes.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataSerializationStrategy implements SerializationStrategy {

    @Override
    public void serialize(OutputStream os, Resume resume) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, Contact> contacts = resume.getContacts();
            writeCollection(dos, contacts.entrySet(), entry -> writeContact(dos, entry.getValue(), entry.getKey()));
            Map<SectionType, Section> sections = resume.getSections();
            writeCollection(dos, sections.entrySet(), entry -> {
                SectionType sectionType = entry.getKey();
                Section section = entry.getValue();
                dos.writeUTF(sectionType.name());
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dos.writeUTF(((TextSection) section).getText());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> listSectionItems = ((ListSection) section).getItems();
                        writeCollection(dos, listSectionItems, dos::writeUTF);
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> organizations = ((OrganizationsSection) section).getOrganizations();
                        writeCollection(dos, organizations, org -> {
                            Contact contact = org.getContact();
                            writeContact(dos, contact, null);
                            List<Experience> experiences = org.getExperiences();
                            writeCollection(dos, experiences, exp -> {
                                dos.writeUTF(exp.getDateFrom().toString());
                                dos.writeUTF(exp.getDateTo().toString());
                                dos.writeUTF(exp.getTitle());
                                dos.writeUTF(processStringBeforeWrite(exp.getText()));
                            });
                        });
                        break;
                }
            });
        }
    }

    @Override
    public Resume deserialize(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume();
            resume.setUuid(dis.readUTF());
            resume.setFullName(dis.readUTF());
            readCollection(dis, () -> {
                ContactType contactType = ContactType.valueOf(dis.readUTF());
                Contact contact = readContact(dis);
                resume.addContact(contactType, contact);
            });
            readCollection(dis, () -> {
                String sectionTypeName = dis.readUTF();
                SectionType sectionType = SectionType.valueOf(sectionTypeName);
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(sectionType, new TextSection(dis.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        ListSection listSection = new ListSection();
                        readCollection(dis, () -> listSection.addItem(dis.readUTF()));
                        resume.addSection(sectionType, listSection);
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        OrganizationsSection organizationsSection = new OrganizationsSection();
                        readCollection(dis, () -> {
                            Contact contact = readContact(dis);
                            Organization organization = new Organization(contact);
                            readCollection(dis, () -> {
                                String dateFromString = dis.readUTF();
                                String dateToString = dis.readUTF();
                                String title = dis.readUTF();
                                String text = dis.readUTF();
                                Experience experience = new Experience(
                                        LocalDate.parse(dateFromString),
                                        LocalDate.parse(dateToString),
                                        title,
                                        processStringBeforeRead(text)
                                );
                                organization.addExperience(experience);
                            });
                            organizationsSection.addOrganization(organization);
                        });
                        resume.addSection(sectionType, organizationsSection);
                        break;
                }
            });
            return resume;
        }
    }

    private void writeContact(DataOutputStream dos, Contact contact, ContactType contactType) throws IOException {
        String url = contact.getUrl();
        String name = contact.getName();
        if (contactType != null) {
            dos.writeUTF(contactType.name());
        }
        dos.writeUTF(processStringBeforeWrite(url));
        dos.writeUTF(processStringBeforeWrite(name));
    }

    private Contact readContact(DataInputStream dis) throws IOException {
        String url = dis.readUTF();
        String name = dis.readUTF();
        return new Contact(
                processStringBeforeRead(url),
                processStringBeforeRead(name)
        );
    }

    private String processStringBeforeWrite(String string) {
        return string != null ? string : "";
    }

    private String processStringBeforeRead(String string) {
        return string.length() > 0 ? string : null;
    }

    private interface DataWriter<T> {
        void write(T t) throws IOException;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, DataWriter<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T item : collection) {
            writer.write(item);
        }
    }

    private interface DataReader {
        void read() throws IOException;
    }

    private void readCollection(DataInputStream dis, DataReader reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }

}
