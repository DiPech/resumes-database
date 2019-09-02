package ru.dipech.resumes.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;
    private Contact contact;
    private final List<Experience> experiences;

    public Organization() {
        this(new Contact(), new ArrayList<>());
    }

    public Organization(Contact contact, Experience... experiences) {
        this(contact, new ArrayList<>(Arrays.asList(experiences)));
    }

    public Organization(Contact contact, List<Experience> experiences) {
        Objects.requireNonNull(contact, "contact must not be null");
        Objects.requireNonNull(experiences, "experiences must not be null");
        this.contact = contact;
        this.experiences = experiences;
    }

    public void addExperience(Experience experience) {
        experiences.add(experience);
    }

    public Contact getContact() {
        return contact;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization organization = (Organization) o;
        return Objects.equals(contact, organization.contact) &&
                Objects.equals(experiences, organization.experiences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contact, experiences);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(contact.getName());
        String linkUrl = contact.getUrl();
        if (linkUrl != null) {
            sb.append(" (").append(linkUrl).append(")");
        }
        sb.append("\n");
        experiences.forEach(e -> sb.append(e.toString()));
        return sb.toString();
    }

}
