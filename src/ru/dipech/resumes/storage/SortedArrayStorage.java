package ru.dipech.resumes.storage;

import ru.dipech.resumes.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        return Arrays.binarySearch(storage, 0, size, new Resume(uuid, "dummy"), Comparator.comparing(Resume::getUuid));
    }

    @Override
    protected void insertBySearchKey(final int searchKey, Resume resume) {
        int index = -searchKey - 1;
        if (size > 0 && index < size) {
            System.arraycopy(storage, index, storage, index + 1, size - index);
        }
        storage[index] = resume;
    }

    @Override
    protected void cutBySearchKey(int searchKey) {
        System.arraycopy(storage, searchKey + 1, storage, searchKey, size - searchKey - 1);
    }

}
