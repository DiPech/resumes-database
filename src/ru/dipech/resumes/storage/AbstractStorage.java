package ru.dipech.resumes.storage;

import ru.dipech.resumes.exception.ItemExistsStorageException;
import ru.dipech.resumes.exception.ItemNotExistsStorageException;
import ru.dipech.resumes.model.Resume;

import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    @Override
    public Resume get(String uuid) {
        LOG.info("Get by uuid: " + uuid);
        SK searchKey = getExistedSearchKey(uuid);
        return getBySearchKey(searchKey);
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        SK searchKey = getNotExistedSearchKey(resume.getUuid());
        saveBySearchKey(searchKey, resume);
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        SK searchKey = getExistedSearchKey(resume.getUuid());
        updateBySearchKey(searchKey, resume);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Update by uuid: " + uuid);
        SK searchKey = getExistedSearchKey(uuid);
        deleteBySearchKey(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("Get all");
        List<Resume> result = getAllAsList();
        result.sort(Resume::compareTo);
        return result;
    }

    private SK getExistedSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!checkBySearchKey(searchKey)) {
            LOG.warning("Resume with uuid [" + uuid + "] not exist");
            throw new ItemNotExistsStorageException(uuid);
        }
        return searchKey;
    }

    private SK getNotExistedSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (checkBySearchKey(searchKey)) {
            LOG.warning("Resume with uuid [" + uuid + "] exist");
            throw new ItemExistsStorageException(uuid);
        }
        return searchKey;
    }

    abstract protected SK getSearchKey(String uuid);

    abstract protected boolean checkBySearchKey(SK searchKey);

    abstract protected Resume getBySearchKey(SK searchKey);

    abstract protected void saveBySearchKey(SK searchKey, Resume resume);

    abstract protected void updateBySearchKey(SK searchKey, Resume resume);

    abstract protected void deleteBySearchKey(SK searchKey);

    abstract protected List<Resume> getAllAsList();

}
