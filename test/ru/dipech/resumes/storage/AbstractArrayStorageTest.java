package ru.dipech.resumes.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.dipech.resumes.exception.StorageException;
import ru.dipech.resumes.util.ResumeTestDataUtil;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void testSaveOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_SIZE; i++) {
                String fullName = "fullname" + (i + 1);
                String uuid = "uuid" + (i + 1);
                storage.save(ResumeTestDataUtil.createWithTestData(fullName, uuid, ResumeTestDataUtil.FillOption.MEDIUM));
            }
        } catch (StorageException e) {
            Assert.fail("Save overflow detected, but there is no overflow.");
        }
        Assert.assertEquals(AbstractArrayStorage.STORAGE_SIZE, storage.size());
        storage.save(ResumeTestDataUtil.createWithTestData("another fullname", null, ResumeTestDataUtil.FillOption.MEDIUM));
    }

}
