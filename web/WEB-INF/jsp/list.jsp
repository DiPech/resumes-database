<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.dipech.resumes.model.ContactType" %>
<%@ page import="ru.dipech.resumes.web.HtmlHelper" %>
<jsp:include page="partials/header.jsp"/>
<section style="overflow: auto">
    <table class="table table-bordered">
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th colspan="3"></th>
        </tr>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="ru.dipech.resumes.model.Resume"/>
            <tr>
                <td><a href="resumes?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                <td>${HtmlHelper.printContact(resume.getContact(ContactType.EMAIL), ContactType.EMAIL)}</td>
                <td width="1%"><a class="btn btn-outline-info btn-sm"
                                  href="resumes?uuid=${resume.uuid}&action=view"><img src="img/view.svg"></a></td>
                <td width="1%"><a class="btn btn-outline-success btn-sm" href="resumes?uuid=${resume.uuid}&action=edit"><img
                        src="img/edit.svg"></a></td>
                <td width="1%"><a class="btn btn-outline-danger btn-sm"
                                  href="resumes?uuid=${resume.uuid}&action=delete"><img src="img/delete.svg"></a></td>
            </tr>
        </c:forEach>
        <tr>
            <th colspan="2"></th>
            <th colspan="3" style="text-align: center;">
                <a class="btn btn-outline-primary btn-sm" href="resumes?action=create">
                    <img src="img/create.svg">
                </a>
            </th>
        </tr>
    </table>
</section>
<jsp:include page="partials/footer.jsp"/>
