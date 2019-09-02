package ru.dipech.resumes.storage;

import ru.dipech.resumes.model.Resume;

import java.util.List;

public interface Storage {

    Resume get(String uuid);

    void save(Resume resume);

    void update(Resume resume);

    void delete(String uuid);

    List<Resume> getAllSorted();

    void clear();

    int size();

}
