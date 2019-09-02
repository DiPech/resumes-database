package ru.dipech.resumes.storage.serialization;

import ru.dipech.resumes.exception.StorageException;
import ru.dipech.resumes.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlSerializationStrategy implements SerializationStrategy {

    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    public XmlSerializationStrategy() {
        try {
            JAXBContext context = JAXBContext.newInstance(
                    Resume.class, Contact.class, Experience.class, ListSection.class,
                    Organization.class, OrganizationsSection.class, TextSection.class
            );
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
            unmarshaller = context.createUnmarshaller();
        } catch (Exception e) {
            throw new StorageException("Can't create JAXB context", e);
        }
    }

    @Override
    public void serialize(OutputStream os, Resume resume) throws IOException {
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            marshaller.marshal(resume, w);
        } catch (JAXBException e) {
            throw new StorageException("Can't marshal resume", e);
        }
    }

    @Override
    public Resume deserialize(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return (Resume) unmarshaller.unmarshal(r);
        } catch (JAXBException e) {
            throw new StorageException("Can't unmarshal resume", e);
        }
    }

}
