package ru.dipech.resumes.storage;

import ru.dipech.resumes.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void insertBySearchKey(final int searchKey, Resume resume) {
        storage[size] = resume;
    }

    @Override
    protected void cutBySearchKey(int searchKey) {
        if (searchKey < size) {
            storage[searchKey] = storage[size - 1];
        }
    }

}
