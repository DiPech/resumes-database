package ru.dipech.resumes.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.dipech.resumes.exception.ItemExistsStorageException;
import ru.dipech.resumes.exception.ItemNotExistsStorageException;
import ru.dipech.resumes.model.Resume;
import ru.dipech.resumes.util.Config;
import ru.dipech.resumes.util.ResumeTestDataUtil;

import java.util.Arrays;
import java.util.List;
import java.util.logging.LogManager;

public abstract class AbstractStorageTest {

    protected static final String STORAGE_PATH = Config.getInstance().getStoragePath();

    private static final String UUID1 = "uuid1";
    private static final String UUID2 = "uuid2";
    private static final String UUID3 = "uuid3";
    private static final String UUID4 = "uuid4";
    private static final Resume RESUME1 = ResumeTestDataUtil.createWithTestData("fullname1", UUID1, ResumeTestDataUtil.FillOption.NONE);
    private static final Resume RESUME2 = ResumeTestDataUtil.createWithTestData("fullname2", UUID2, ResumeTestDataUtil.FillOption.LOW);
    private static final Resume RESUME3 = ResumeTestDataUtil.createWithTestData("fullname3", UUID3, ResumeTestDataUtil.FillOption.HIGH);
    private static final Resume RESUME4 = ResumeTestDataUtil.createWithTestData("fullname4", UUID4, ResumeTestDataUtil.FillOption.MEDIUM);
    protected final Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        LogManager.getLogManager().reset();
        storage.clear();
        storage.save(RESUME1);
        storage.save(RESUME2);
        storage.save(RESUME3);
    }

    @Test
    public void testGet() {
        Assert.assertEquals(RESUME1, storage.get(UUID1));
    }

    @Test(expected = ItemNotExistsStorageException.class)
    public void testGetNotExists() {
        storage.get(UUID4);
    }

    @Test
    public void testSave() {
        int currentSize = storage.size();
        storage.save(RESUME4);
        Assert.assertEquals(currentSize + 1, storage.size());
        Assert.assertEquals(RESUME4, storage.get(UUID4));
    }

    @Test(expected = ItemExistsStorageException.class)
    public void testSaveExists() {
        storage.save(RESUME1);
    }

    @Test
    public void testUpdate() {
        Resume newResume1 = ResumeTestDataUtil.createWithTestData("an another fullname1", UUID1, ResumeTestDataUtil.FillOption.HIGH);
        storage.update(newResume1);
        Assert.assertEquals(storage.get(UUID1), newResume1);
    }

    @Test(expected = ItemNotExistsStorageException.class)
    public void testUpdateNotExists() {
        storage.update(RESUME4);
    }

    @Test(expected = ItemNotExistsStorageException.class)
    public void testDelete() {
        int currentSize = storage.size();
        storage.delete(UUID1);
        Assert.assertEquals(currentSize - 1, storage.size());
        storage.get(UUID1);
    }

    @Test(expected = ItemNotExistsStorageException.class)
    public void testDeleteNotExists() {
        storage.delete(UUID4);
    }

    @Test
    public void testGetAll() {
        List<Resume> expected = Arrays.asList(RESUME1, RESUME2, RESUME3);
        List<Resume> actual = storage.getAllSorted();
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(3, actual.size());
    }

    @Test
    public void testClear() {
        storage.clear();
        Assert.assertEquals(storage.size(), 0);
    }

    @Test
    public void testSize() {
        Assert.assertEquals(3, storage.size());
    }

}
