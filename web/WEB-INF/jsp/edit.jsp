<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.dipech.resumes.model.Experience" %>
<%@ page import="ru.dipech.resumes.model.SectionType" %>
<jsp:include page="partials/header.jsp"/>
<section id="section-edit">
    <h1>
        <span v-if="resume.uuid">Редактирование резюме {{ resume.uuid }}</span>
        <span v-else>Создание нового резюме</span>
    </h1>
    <form method="post" action="resumes" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" v-bind:value="resume.uuid">
        <h2>Основные данные</h2>
        <div class="form-group">
            <label>Имя</label>
            <input type="text" name="full_name" class="form-control" placeholder="Иванов Иван"
                   v-model:value="resume.fullName" v-bind:class="{ 'is-invalid': isFullNameInvalid() }">
            <div class="invalid-feedback" v-if="isFullNameInvalid()">
                Имя не может быть короче {{ fullNameMinLength }} символов.
            </div>
        </div>
        <h2>Контакты</h2>
        <table class="table table-bordered">
            <tr class="cell-primary">
                <th class="w1p">Контакт</th>
                <th>URL</th>
                <th>Название</th>
            </tr>
            <tr v-for="(contact, contactTypeName) in resume.contacts">
                <th class="cell-secondary">
                    {{ getContactTitle(contactTypeName) }}
                    <input type="hidden" name="contact[type]"
                           v-bind:value="contactTypeName">
                </th>
                <td>
                    <input type="text" size=30
                           name="contact[url]"
                           class="form-control"
                           placeholder="URL, номер телефона, email или id профиля"
                           v-model="contact.url">
                </td>
                <td>
                    <input type="text" size=30
                           name="contact[name]"
                           class="form-control"
                           placeholder="Своё название контакта (необязательно)"
                           v-model="contact.name">
                </td>
            </tr>
        </table>
        <div class="mt-3" v-for="(section, sectionTypeName) in resume.sections">
            <h2>{{ getSectionTitle(sectionTypeName) }}</h2>
            <template v-if="isTextSection(sectionTypeName)">
                <textarea class="form-control" rows="10"
                          v-bind:name="'section[' + sectionTypeName + ']'"
                          v-model="section.INSTANCE.text"
                          v-bind:class="{ 'is-invalid': isSectionTextInvalid(section.INSTANCE.text) }"
                ></textarea>
                <div class="invalid-feedback" v-if="isSectionTextInvalid(section.INSTANCE.text)">
                    Текст секции слишком короткий
                </div>
            </template>
            <template v-if="isListSection(sectionTypeName)">
                <table class="table table-bordered">
                    <tr v-for="(item, itemIndex) in section.INSTANCE.items">
                        <td>
                            <input type="text" size=30
                                   name=""
                                   class="form-control"
                                   v-bind:name="'section[' + sectionTypeName + ']'"
                                   v-model="section.INSTANCE.items[itemIndex]"
                                   v-bind:class="{ 'is-invalid': isSectionListItemInvalid(item) }"
                                   required>
                            <div class="invalid-feedback" v-if="isSectionListItemInvalid(item)">
                                Текст элемента списка слишком короткий
                            </div>
                        </td>
                        <td style="width: 1%">
                            <button type="button" class="btn btn-outline-danger btn-sm"
                                    v-on:click="removeTextSectionItem(section, itemIndex)">
                                <img src="img/delete.svg">
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <th colspan="2" style="text-align: right">
                            <button type="button" class="btn btn-outline-info btn-sm"
                                    v-on:click="addNewTextSectionItem(section)">
                                <img src="img/create.svg">
                            </button>
                        </th>
                    </tr>
                </table>
            </template>
            <template v-if="isOrganizationSection(sectionTypeName)">
                <table class="table table-bordered mt-3">
                    <tr>
                        <th colspan="2" class="cell-primary">
                            Список организаций
                        </th>
                    </tr>
                    <template v-for="(org, orgIndex) in section.INSTANCE.organizations">
                        <tr>
                            <td>
                                <div class="row">
                                    <div class="col-md-8">
                                        <label>Название организации</label>
                                        <input type="text" class="form-control"
                                               placeholder="Например: Google Corp"
                                               v-bind:name="'section[' + sectionTypeName + '][' + orgIndex + ']name'"
                                               v-bind:class="{ 'is-invalid': isOrganizationNameInvalid(org) }"
                                               v-model="section.INSTANCE.organizations[orgIndex].contact.name"
                                               required>
                                        <div class="invalid-feedback" v-if="isOrganizationNameInvalid(org)">
                                            Название организации слишком короткое
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <label>URL организации</label>
                                        <input type="text" class="form-control"
                                               v-bind:name="'section[' + sectionTypeName + '][' + orgIndex + ']url'"
                                               placeholder="Например: http://developers.google.com"
                                               v-model="section.INSTANCE.organizations[orgIndex].contact.url">
                                    </div>
                                    <div class="col-md-12">
                                        <table class="table table-bordered mt-3">
                                            <tr>
                                                <th class="cell-secondary" colspan="2">
                                                    Опыт в организации
                                                </th>
                                            </tr>
                                            <template v-for="(exp, expIndex) in org.experiences">
                                                <tr>
                                                    <td>
                                                        <div class="row">
                                                            <div class="col-md-3">
                                                                <div class="form-group">
                                                                    <label>Дата начала</label>
                                                                    <input type="hidden"
                                                                           v-bind:name="'section[' + sectionTypeName + '][' + orgIndex + '][' + expIndex + ']date_from'"
                                                                           v-bind:value="exp.dateFrom.getTime()">
                                                                    <vuejs-datepicker
                                                                            input-class="form-control vue-date-picker"
                                                                            v-model="resume.sections[sectionTypeName].INSTANCE.organizations[orgIndex].experiences[expIndex].dateFrom"
                                                                            required
                                                                    ></vuejs-datepicker>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label>Дата окончания</label>
                                                                    <input type="hidden"
                                                                           v-bind:name="'section[' + sectionTypeName + '][' + orgIndex + '][' + expIndex + ']date_to'"
                                                                           v-bind:value="exp.dateTo.getTime()">
                                                                    <template v-if="!isDateNow(exp.dateTo)">
                                                                        <vuejs-datepicker
                                                                                input-class="form-control vue-date-picker"
                                                                                v-model="resume.sections[sectionTypeName].INSTANCE.organizations[orgIndex].experiences[expIndex].dateTo"
                                                                        ></vuejs-datepicker>
                                                                        <br>
                                                                    </template>
                                                                    <label style="margin-top: -5px;">
                                                                        <input type="checkbox"
                                                                               v-bind:checked="isDateNow(exp.dateTo)"
                                                                               v-on:click="toggleLastDateWithNow(sectionTypeName, orgIndex, expIndex)">
                                                                        По текущий момент
                                                                    </label>
                                                                </div>
                                                            </div>
                                                            <div class="col-md-9">
                                                                <div class="form-group">
                                                                    <label>Должность</label>
                                                                    <input type="text" class="form-control"
                                                                           placeholder="Например: Руководитель IT-отдела"
                                                                           v-bind:name="'section[' + sectionTypeName + '][' + orgIndex + '][' + expIndex + ']title'"
                                                                           v-bind:class="{ 'is-invalid': isOrganizationTitleInvalid(exp.title) }"
                                                                           v-model="resume.sections[sectionTypeName].INSTANCE.organizations[orgIndex].experiences[expIndex].title"
                                                                           required>
                                                                    <div class="invalid-feedback"
                                                                         v-if="isOrganizationTitleInvalid(exp.title)">
                                                                        Слишком короткое значение
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label>Описание</label>
                                                                    <textarea class="form-control" rows="4"
                                                                              placeholder="Опишите чем вы занимались в данной организации на указанный период"
                                                                              v-bind:name="'section[' + sectionTypeName + '][' + orgIndex + '][' + expIndex + ']text'"
                                                                              v-model="resume.sections[sectionTypeName].INSTANCE.organizations[orgIndex].experiences[expIndex].text"
                                                                    ></textarea>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td class="cell-secondary"
                                                        style="width: 1%; vertical-align: middle;">
                                                        <button type="button" class="btn btn-outline-danger btn-sm"
                                                                v-on:click="removeOrganizationExperience(sectionTypeName, orgIndex, expIndex)">
                                                            <img src="img/delete.svg">
                                                        </button>
                                                    </td>
                                                </tr>
                                                <tr v-if="expIndex < org.experiences.length - 1">
                                                    <th class="cell-secondary" colspan="2"></th>
                                                </tr>
                                            </template>
                                            <tr>
                                                <th class="cell-secondary" colspan="2" style="text-align: right">
                                                    <button type="button" class="btn btn-outline-info btn-sm"
                                                            v-on:click="addNewOrganizationExperience(sectionTypeName, orgIndex)">
                                                        <img src="img/create.svg">
                                                    </button>
                                                </th>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </td>
                            <td class="cell-primary" style="width: 1%; vertical-align: middle;">
                                <button type="button" class="btn btn-outline-danger btn-sm"
                                        v-on:click="removeOrganization(sectionTypeName, orgIndex)">
                                    <img src="img/delete.svg">
                                </button>
                            </td>
                        </tr>
                        <tr v-if="orgIndex < section.INSTANCE.organizations.length - 1">
                            <th class="cell-primary" colspan="2"></th>
                        </tr>
                    </template>
                    <tr>
                        <th class="cell-primary" colspan="2" style="text-align: right">
                            <button type="button" class="btn btn-outline-info btn-sm"
                                    v-on:click="addNewOrganization(sectionTypeName)">
                                <img src="img/create.svg">
                            </button>
                        </th>
                    </tr>
                </table>
            </template>
        </div>
        <button type="submit" class="btn btn-outline-primary">
            Сохранить
        </button>
    </form>
</section>
<script>
    let resume = ${resume};
    let contactTypes = ${contactTypes};
    let sectionTypes = ${sectionTypes};
    let sectionContentTypes = {
        text: ['<%=SectionType.OBJECTIVE.name()%>', '<%=SectionType.PERSONAL.name()%>'],
        list: ['<%=SectionType.ACHIEVEMENT.name()%>', '<%=SectionType.QUALIFICATIONS.name()%>'],
        organizations: ['<%=SectionType.EDUCATION.name()%>', '<%=SectionType.EXPERIENCE.name()%>']
    };
    let dateCurrent = new Date(<%=Experience.NOW.getYear()%>, <%=Experience.NOW.getMonthValue()-1%>, <%=Experience.NOW.getDayOfMonth()%>);
</script>
<script type="text/javascript" src="js/edit.js"></script>
<jsp:include page="partials/footer.jsp"/>
