package ru.dipech.resumes.storage;

import ru.dipech.resumes.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {

    private List<Resume> storage = new ArrayList<>();

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean checkBySearchKey(Integer searchKey) {
        return searchKey > -1 && storage.get(searchKey) != null;
    }

    @Override
    protected Resume getBySearchKey(Integer searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void saveBySearchKey(Integer searchKey, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void updateBySearchKey(Integer searchKey, Resume resume) {
        storage.set(searchKey, resume);
    }

    @Override
    protected void deleteBySearchKey(Integer searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected List<Resume> getAllAsList() {
        return storage;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

}
