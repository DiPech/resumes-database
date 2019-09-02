package ru.dipech.resumes.web;

import ru.dipech.resumes.exception.ItemNotExistsStorageException;
import ru.dipech.resumes.model.*;
import ru.dipech.resumes.storage.Storage;
import ru.dipech.resumes.util.Config;
import ru.dipech.resumes.util.json.EnumWithTitleJsonAdapter;
import ru.dipech.resumes.util.json.JsonParser;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class ResumesServlet extends HttpServlet {

    private Storage storage;
    private final JsonParser jsonParser = new JsonParser(builder -> {
        builder.registerTypeAdapter(ContactType.class, new EnumWithTitleJsonAdapter());
        builder.registerTypeAdapter(SectionType.class, new EnumWithTitleJsonAdapter());
    });

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.getInstance().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        boolean isNew = !checkValue(uuid);
        String fullName = request.getParameter("full_name");
        Resume resume;
        if (isNew) {
            resume = new Resume(fullName);
        } else {
            try {
                resume = storage.get(uuid);
            } catch (ItemNotExistsStorageException e) {
                response.sendRedirect("resumes");
                return;
            }
            resume.setFullName(fullName);
        }
        String[] contactTypes = request.getParameterValues("contact[type]");
        String[] contactUrls = request.getParameterValues("contact[url]");
        String[] contactNames = request.getParameterValues("contact[name]");
        for (int i = 0; i < contactTypes.length; i++) {
            String type = contactTypes[i];
            String url = contactUrls[i];
            String name = contactNames[i];
            ContactType contactType;
            try {
                contactType = ContactType.valueOf(type);
            } catch (IllegalStateException e) {
                continue;
            }
            if (checkValue(url)) {
                resume.addContact(contactType, new Contact(url, name));
            } else {
                resume.getContacts().remove(contactType);
            }
        }
        SectionType[] sectionTypes = SectionType.values();
        for (SectionType sectionType : sectionTypes) {
            String sectionTypeName = sectionType.name();
            String sectionKey = "section[" + sectionTypeName + "]";
            switch (sectionType) {
                case OBJECTIVE:
                case PERSONAL:
                    String text = request.getParameter(sectionKey);
                    if (text != null) {
                        resume.addSection(sectionType, new TextSection(text));
                    } else {
                        resume.getSections().remove(sectionType);
                    }
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    String[] list = request.getParameterValues(sectionKey);
                    if (list != null) {
                        resume.addSection(sectionType, new ListSection(list));
                    } else {
                        resume.getSections().remove(sectionType);
                    }
                    break;
                case EDUCATION:
                case EXPERIENCE:
                    int orgIndex = 0;
                    OrganizationsSection orgSection = new OrganizationsSection();
                    while (request.getParameter(sectionKey + "[" + orgIndex + "]name") != null) {
                        String orgKey = sectionKey + "[" + orgIndex + "]";
                        String orgName = request.getParameter(orgKey + "name");
                        String orgUrl = request.getParameter(orgKey + "url");
                        Organization organization = new Organization(new Contact(orgUrl, orgName));
                        int expIndex = 0;
                        while (request.getParameter(orgKey + "[" + expIndex + "]title") != null) {
                            String expKey = orgKey + "[" + expIndex + "]";
                            String expTitle = request.getParameter(expKey + "title");
                            String expText = request.getParameter(expKey + "text");
                            Long expDateFromTs = Long.parseLong(request.getParameter(expKey + "date_from"));
                            Long expDateToTs = Long.parseLong(request.getParameter(expKey + "date_to"));
                            organization.addExperience(new Experience(
                                    getLocalDateFromTimestamp(expDateFromTs),
                                    getLocalDateFromTimestamp(expDateToTs),
                                    expTitle,
                                    expText
                            ));
                            expIndex++;
                        }
                        orgIndex++;
                        orgSection.addOrganization(organization);
                    }
                    resume.addSection(sectionType, orgSection);
                    break;
            }
        }
        if (isNew) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        response.sendRedirect("resumes");
    }

    private LocalDate getLocalDateFromTimestamp(Long expDateFromTs) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(expDateFromTs),
                TimeZone.getDefault().toZoneId()).toLocalDate();
    }

    private boolean checkValue(String url) {
        return url != null && url.trim().length() != 0;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        String uuid = request.getParameter("uuid");
        if (action.equals("delete")) {
            storage.delete(uuid);
            response.sendRedirect("resumes");
            return;
        }
        if (!action.equals("create") && !action.equals("view") && !action.equals("edit")) {
            response.sendRedirect("resumes");
            return;
        }
        String view = action.equals("view") ? "view" : "edit";
        Resume resume = action.equals("create") ? new Resume() : storage.get(uuid);
        List<ContactType> contactTypes = new ArrayList<>(Arrays.asList(ContactType.values()));
        request.setAttribute("contactTypes", jsonParser.write(contactTypes));
        List<SectionType> sectionTypes = new ArrayList<>(Arrays.asList(SectionType.values()));
        request.setAttribute("sectionTypes", jsonParser.write(sectionTypes));
        if (action.equals("edit") || action.equals("create")) {
            fixResumeEmptyData(resume);
            request.setAttribute("resume", jsonParser.write(resume));
        } else {
            request.setAttribute("resume", resume);
        }
        request.getRequestDispatcher("/WEB-INF/jsp/" + view + ".jsp").forward(request, response);
    }

    private void fixResumeEmptyData(Resume resume) {
        if (resume.getFullName() == null) {
            resume.setFullName("");
        }
        ContactType[] contactTypes = ContactType.values();
        for (ContactType contactType : contactTypes) {
            resume.getContacts().putIfAbsent(contactType, new Contact("", ""));
        }
        SectionType[] sectionTypes = SectionType.values();
        for (SectionType sectionType : sectionTypes) {
            Section section;
            switch (sectionType) {
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    section = new ListSection("");
                    break;
                case EDUCATION:
                case EXPERIENCE:
                    section = new OrganizationsSection(new Organization(new Contact("", ""), new Experience(LocalDate.of(2000, 1, 1), LocalDate.now(), "")));
                    break;
                default:
                    section = new TextSection("");
            }
            resume.getSections().putIfAbsent(sectionType, section);
        }
    }

}
