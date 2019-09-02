package ru.dipech.resumes.util;

import ru.dipech.resumes.model.*;

import java.time.LocalDate;

public class ResumeTestDataUtil {

    public enum FillOption {
        NONE,
        LOW,
        MEDIUM,
        HIGH
    }

    public static Resume createWithTestData(String fullName, String uuid, FillOption fillOption) {
        Resume resume;
        if (uuid == null) {
            resume = new Resume(fullName);
        } else {
            resume = new Resume(uuid, fullName);
        }
        if (fillOption != FillOption.NONE) {
            fillWithTestData(resume, fillOption);
        }
        return resume;
    }

    private static void fillWithTestData(Resume resume, FillOption fillOption) {
        fillResumeContacts(resume, fillOption);
        fillResumeSections(resume, fillOption);
    }

    private static void fillResumeContacts(Resume resume, FillOption fillOption) {
        Contact contactPhone = new Contact("+7 (903) 375 13 87", "+7 (903) 375 13 87");
        resume.addContact(ContactType.PHONE, contactPhone);
        Contact contactSkype = new Contact("dipech_prog", "dipech_prog");
        resume.addContact(ContactType.SKYPE, contactSkype);
        if (fillOption == FillOption.MEDIUM || fillOption == FillOption.HIGH) {
            Contact contactEmail = new Contact("dmitry.pechkovsky@gmail.com", "dmitry.pechkovsky@gmail.com");
            resume.addContact(ContactType.EMAIL, contactEmail);
            if (fillOption == FillOption.HIGH) {
                Contact contactGithub = new Contact("https://github.com/dipech", null);
                resume.addContact(ContactType.GITHUB, contactGithub);
                Contact contactHomepage = new Contact("https://dipech.github.io", null);
                resume.addContact(ContactType.HOMEPAGE, contactHomepage);
            }
        }
    }

    private static void fillResumeSections(Resume resume, FillOption fillOption) {
        resume.addSection(SectionType.OBJECTIVE, new TextSection(
                "Sed feugiat eu est vel imperdiet."
        ));
        resume.addSection(SectionType.PERSONAL, new TextSection(
                "Aliquam vel eros ac lorem malesuada aliquet vel eu diam."
        ));
        if (fillOption == FillOption.MEDIUM || fillOption == FillOption.HIGH) {
            resume.addSection(SectionType.ACHIEVEMENT, new ListSection(
                    "Mauris in sapien pharetra, congue felis quis.",
                    "Nullam odio felis, congue sit amet nisi a, scelerisque iaculis lorem.",
                    "In at enim est. Duis sed felis nisi.",
                    "Morbi bibendum faucibus sem ut rutrum.",
                    "Pellentesque sed varius ipsum, eu rhoncus felis.",
                    "Mauris eu nulla gravida, eleifend leo sit amet, convallis erat."
            ));
            resume.addSection(SectionType.QUALIFICATIONS, new ListSection(
                    "Mauris accumsan commodo ex.",
                    "Etiam ultricies sem vel neque vulputate, vitae maximus dolor malesuada.",
                    "Donec faucibus velit et congue blandit."
            ));
            if (fillOption == FillOption.HIGH) {
                resume.addSection(SectionType.EXPERIENCE, new OrganizationsSection(
                        new Organization(
                                new Contact("http://curabitur.net", "Morbi massa ex"),
                                new Experience(
                                        LocalDate.of(2015, 5, 1),
                                        Experience.NOW,
                                        "Fliquet diam eget",
                                        "Nullam malesuada orci"
                                )
                        ),
                        new Organization(
                                new Contact("https://quisque-olestie .com", "Tortor eget"),
                                new Experience(
                                        LocalDate.of(2015, 7, 1),
                                        LocalDate.of(2016, 11, 1),
                                        "Pellentesque tempus",
                                        "Aenean cursus nibh in nisi suscipit"
                                )
                        )
                ));
                resume.addSection(SectionType.EDUCATION, new OrganizationsSection(
                        new Organization(
                                new Contact("http://eget-scelerisque.ru", "Eget scelerisque"),
                                new Experience(
                                        LocalDate.of(2005, 10, 1),
                                        LocalDate.of(2006, 5, 1),
                                        "Sed vitae eros ut nisl pellentesque"
                                )
                        ),
                        new Organization(
                                new Contact("http://www.ifmo.ru/", "Sed vitae eros ut nisl pellentesque rutrum in et purus"),
                                new Experience(
                                        LocalDate.of(2010, 6, 1),
                                        LocalDate.of(2011, 11, 1),
                                        "Eget sagittis lectus dictum"
                                ),
                                new Experience(
                                        LocalDate.of(2011, 2, 1),
                                        LocalDate.of(2013, 4, 1),
                                        "Morbi risus augue"
                                )
                        ),
                        new Organization(
                                new Contact("http://www.school.mipt.ru", "Nulla volutpat mi in eros ultricies"),
                                new Experience(
                                        LocalDate.of(2007, 1, 1),
                                        LocalDate.of(2009, 3, 1),
                                        "Fringilla at porttitor"
                                )
                        )
                ));
            }
        }
    }

}
