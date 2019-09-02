<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.dipech.resumes.model.SectionType" %>
<%@ page import="ru.dipech.resumes.web.HtmlHelper" %>
<jsp:include page="partials/header.jsp"/>
<section>
    <jsp:useBean id="resume" type="ru.dipech.resumes.model.Resume" scope="request"/>
    <h1>Резюме: ${resume.fullName}</h1>
    <small>UUID: ${resume.uuid}</small>
    <c:if test="${not empty resume.contacts}">
        <h2>Контакты</h2>
    </c:if>
    <c:forEach items="${resume.contacts}" var="contactEntry">
        <jsp:useBean id="contactEntry"
                     type="java.util.Map.Entry<ru.dipech.resumes.model.ContactType, ru.dipech.resumes.model.Contact>"/>
        <c:set var="contact" value="${contactEntry.value}"/>
        <c:set var="contactType" value="${contactEntry.key}"/>
        ${HtmlHelper.printContact(contact, contactType)}
        <br/>
    </c:forEach>
    <c:forEach items="${resume.sections}" var="sectionEntry">
        <jsp:useBean id="sectionEntry"
                     type="java.util.Map.Entry<ru.dipech.resumes.model.SectionType, ru.dipech.resumes.model.Section>"/>
        <c:set var="section" value="${sectionEntry.value}"/>
        <c:set var="sectionType" value="${sectionEntry.key}"/>
        <jsp:useBean id="section" type="ru.dipech.resumes.model.Section"/>
        <jsp:useBean id="sectionType" type="ru.dipech.resumes.model.SectionType"/>
        <h2>${sectionType.title}</h2>
        <c:if test="${sectionType.name() eq SectionType.OBJECTIVE.name() or sectionType.name() eq SectionType.PERSONAL.name()}">
            ${section.text}
        </c:if>
        <c:if test="${sectionType.name() eq SectionType.ACHIEVEMENT.name() or sectionType.name() eq SectionType.QUALIFICATIONS.name()}">
            <ul>
                <c:forEach items="${section.items}" var="item">
                    <li>${item}</li>
                </c:forEach>
            </ul>
        </c:if>
        <c:if test="${sectionType.name() eq SectionType.EDUCATION.name() or sectionType.name() eq SectionType.EXPERIENCE.name()}">
            <c:forEach items="${section.organizations}" var="org">
                <jsp:useBean id="org" type="ru.dipech.resumes.model.Organization"/>
                <c:set var="contact" value="${org.contact}"/>
                <h3>
                    <c:out escapeXml="false"
                           value="${not empty contact.url ? '<a href=\"'.concat(contact.url).concat('\">').concat(contact.name).concat('</a>') : contact.name}"/>
                </h3>
                <table class="table table-bordered mt-2 mb-3">
                    <tbody>
                    <c:forEach items="${org.experiences}" var="exp">
                        <jsp:useBean id="exp" type="ru.dipech.resumes.model.Experience"/>
                        <tr>
                            <th class="period">
                                    ${HtmlHelper.printExperienceDate(exp.dateFrom)}
                                ––
                                    ${HtmlHelper.printExperienceDate(exp.dateTo)}
                            </th>
                            <td><b>${exp.title}</b><br>${exp.text}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:forEach>
        </c:if>
        <br/>
    </c:forEach>
</section>
<jsp:include page="partials/footer.jsp"/>
