package ru.dipech.resumes.storage;

import ru.dipech.resumes.model.Resume;

public class MapUuidStorage extends AbstractMapStorage<String> {

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean checkBySearchKey(String searchKey) {
        return storage.containsKey(searchKey) && storage.get(searchKey) != null;
    }

    @Override
    protected Resume getBySearchKey(String searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void saveBySearchKey(String searchKey, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void updateBySearchKey(String searchKey, Resume resume) {
        storage.replace(searchKey, resume);
    }

    @Override
    protected void deleteBySearchKey(String searchKey) {
        storage.remove(searchKey);
    }

}
