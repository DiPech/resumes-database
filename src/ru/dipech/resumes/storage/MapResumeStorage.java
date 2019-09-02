package ru.dipech.resumes.storage;

import ru.dipech.resumes.model.Resume;

public class MapResumeStorage extends AbstractMapStorage<Resume> {

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean checkBySearchKey(Resume searchKey) {
        return searchKey != null;
    }

    @Override
    protected Resume getBySearchKey(Resume searchKey) {
        return searchKey;
    }

    @Override
    protected void saveBySearchKey(Resume searchKey, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void updateBySearchKey(Resume searchKey, Resume resume) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected void deleteBySearchKey(Resume searchKey) {
        storage.remove(searchKey.getUuid());
    }

}
