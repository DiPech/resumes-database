package ru.dipech.resumes.web;

import ru.dipech.resumes.model.Contact;
import ru.dipech.resumes.model.ContactType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.dipech.resumes.model.Experience.NOW;

public class HtmlHelper {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

    public static String printContact(Contact contact, ContactType contactType) {
        if (contact == null) {
            return "";
        }
        String name = (contact.getName() != null && contact.getName().length() > 0) ? contact.getName() : contactType.getTitle();
        String url = contact.getUrl();
        switch (contactType) {
            case EMAIL:
                url = "mailto:" + url;
                break;
            case SKYPE:
                url = "skype:" + url;
                break;
            case PHONE:
                url = "tel:+" + url.replaceAll("[^\\d]", "");
                break;
        }
        return (url != null ? "<a href='" + url + "'>" : "") + name + (url != null ? "</a>" : "");
    }

    public static String printExperienceDate(LocalDate localDate) {
        if (localDate.equals(NOW)) {
            return "Сейчас";
        }
        return localDate.format(DATE_TIME_FORMATTER);
    }

}
