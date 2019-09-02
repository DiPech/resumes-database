package ru.dipech.resumes.storage;

import ru.dipech.resumes.exception.StorageException;
import ru.dipech.resumes.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

    protected static final int STORAGE_SIZE = 10000;
    protected int size = 0;
    protected Resume[] storage = new Resume[STORAGE_SIZE];

    @Override
    protected void saveBySearchKey(Integer searchKey, Resume resume) {
        if (size == STORAGE_SIZE) {
            throw new StorageException("Storage is full");
        }
        insertBySearchKey(searchKey, resume);
        size++;
    }

    @Override
    protected void deleteBySearchKey(Integer searchKey) {
        cutBySearchKey(searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected boolean checkBySearchKey(Integer searchKey) {
        return searchKey > -1 && storage[searchKey] != null;
    }

    @Override
    protected Resume getBySearchKey(Integer searchKey) {
        return storage[searchKey];
    }

    @Override
    protected void updateBySearchKey(Integer searchKey, Resume resume) {
        storage[searchKey] = resume;
    }

    @Override
    protected List<Resume> getAllAsList() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    protected abstract void insertBySearchKey(final int searchKey, Resume resume);

    protected abstract void cutBySearchKey(int searchKey);

}
