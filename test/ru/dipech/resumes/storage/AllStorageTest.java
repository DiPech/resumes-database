package ru.dipech.resumes.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class,
        MapUuidStorageTest.class,
        MapResumeStorageTest.class,
        ObjectPathStorageTest.class,
        ObjectFileStorageTest.class,
        XmlFileStorageTest.class,
        JsonFileStorageTest.class,
        DataFileStorageTest.class,
        SqlStorageTest.class
})
public class AllStorageTest {
}
