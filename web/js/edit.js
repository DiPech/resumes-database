new Vue({
    el: '#section-edit',
    components: {
        vuejsDatepicker: vuejsDatepicker
    },
    data: {
        resume: resume,
        contactTypes: contactTypes,
        sectionTypes: sectionTypes,
        sectionContentTypes: sectionContentTypes,
        fullNameMinLength: 5,
        dateCurrent: dateCurrent
    },
    methods: {
        removeTextSectionItem: function (section, itemIndex) {
            section.INSTANCE.items.splice(itemIndex, 1);
        },
        addNewTextSectionItem: function (section) {
            section.INSTANCE.items.push("");
        },
        addNewOrganizationExperience: function (sectionTypeName, orgIndex) {
            this.resume.sections[sectionTypeName].INSTANCE.organizations[orgIndex].experiences.push({
                dateFrom: new Date(2000, 0, 1),
                dateTo: new Date(2000, 0, 1),
                title: "",
                text: ""
            });
        },
        removeOrganizationExperience: function (sectionTypeName, orgIndex, expIndex) {
            this.resume.sections[sectionTypeName].INSTANCE.organizations[orgIndex].experiences.splice(expIndex, 1);
        },
        addNewOrganization: function (sectionTypeName) {
            this.resume.sections[sectionTypeName].INSTANCE.organizations.push({
                contact: {
                    url: "",
                    name: ""
                },
                experiences: []
            });
        },
        removeOrganization: function (sectionTypeName, orgIndex) {
            this.resume.sections[sectionTypeName].INSTANCE.organizations.splice(orgIndex, 1);
        },
        toggleLastDateWithNow: function (sectionTypeName, orgIndex, expIndex) {
            let dateTo = this.resume.sections[sectionTypeName].INSTANCE.organizations[orgIndex].experiences[expIndex].dateTo;
            if (this.isDateNow(dateTo)) {
                dateTo = new Date();
            } else {
                dateTo = this.dateCurrent;
            }
            this.resume.sections[sectionTypeName].INSTANCE.organizations[orgIndex].experiences[expIndex].dateTo = dateTo;
        },
        isFullNameInvalid: function () {
            return this.resume.fullName.length < this.fullNameMinLength;
        },
        isSectionTextInvalid: function (text) {
            return text.length < 10;
        },
        isSectionListItemInvalid: function (item) {
            return item.length < 3;
        },
        isOrganizationTitleInvalid: function (title) {
            return title.length < 5;
        },
        isOrganizationNameInvalid: function (org) {
            return org.contact.name.length < 5;
        },
        detectSectionContentType: function (sectionTypeName) {
            for (let key in this.sectionContentTypes) {
                if (this.sectionContentTypes[key].indexOf(sectionTypeName) !== -1) {
                    return key;
                }
            }
            return null;
        },
        isTextSection: function (sectionTypeName) {
            return this.detectSectionContentType(sectionTypeName) === 'text';
        },
        isListSection: function (sectionTypeName) {
            return this.detectSectionContentType(sectionTypeName) === 'list';
        },
        isOrganizationSection: function (sectionTypeName) {
            return this.detectSectionContentType(sectionTypeName) === 'organizations';
        },
        getContactTitle: function (contactTypeName) {
            return this.getTypeTitleByName(this.contactTypes, contactTypeName);
        },
        getSectionTitle: function (sectionTypeName) {
            return this.getTypeTitleByName(this.sectionTypes, sectionTypeName);
        },
        getTypeTitleByName: function (array, typeName) {
            for (let i in array) {
                let type = array[i];
                if (type.name === typeName) {
                    return type.title;
                }
            }
            return null;
        },
        isDateNow: function (date) {
            return date.getTime() === this.dateCurrent.getTime();
        }
    },
    created: function () {
        // Convert dates to js Date object
        for (let sectionTypeName in this.resume.sections) {
            if (this.isOrganizationSection(sectionTypeName)) {
                let section = this.resume.sections[sectionTypeName];
                for (let orgIndex in section.INSTANCE.organizations) {
                    let org = section.INSTANCE.organizations[orgIndex];
                    for (let expIndex in org.experiences) {
                        let exp = org.experiences[expIndex];
                        let dateFrom = exp.dateFrom;
                        let dateTo = exp.dateTo;
                        this.resume.sections[sectionTypeName].INSTANCE.organizations[orgIndex].experiences[expIndex].dateFrom =
                            new Date(dateFrom.year, parseInt(dateFrom.month) - 1, dateFrom.day);
                        this.resume.sections[sectionTypeName].INSTANCE.organizations[orgIndex].experiences[expIndex].dateTo =
                            new Date(dateTo.year, parseInt(dateTo.month) - 1, dateTo.day);
                    }
                }
            }
        }
    }
});
