package ru.dipech.resumes.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationsSection extends Section {

    private static final long serialVersionUID = 1L;
    private final List<Organization> organizations;

    public OrganizationsSection() {
        this(new ArrayList<>());
    }

    public OrganizationsSection(Organization... organizations) {
        this(new ArrayList<>(Arrays.asList(organizations)));
    }

    public OrganizationsSection(List<Organization> organizations) {
        Objects.requireNonNull(organizations, "organizations must not be null");
        this.organizations = organizations;
    }

    public void addOrganization(Organization organization) {
        organizations.add(organization);
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationsSection that = (OrganizationsSection) o;
        return Objects.equals(organizations, that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizations);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        organizations.forEach(o -> sb.append(o.toString()).append("\n"));
        return sb.toString();
    }

}
