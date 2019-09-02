package ru.dipech.resumes.model;

import ru.dipech.resumes.util.json.EnumWithTitleInterface;

public enum ContactType implements EnumWithTitleInterface {
    PHONE("Номер телефона"),
    SKYPE("Skype"),
    EMAIL("Почта"),
    LINKEDIN("LinkedIn"),
    GITHUB("Github"),
    STACKOVERFLOW("Stackoverflow"),
    HOMEPAGE("Домашняя страница");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
