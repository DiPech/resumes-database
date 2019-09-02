package ru.dipech.resumes.exception;

public class ItemExistsStorageException extends AbstractItemStorageException {
    public ItemExistsStorageException(String uuid) {
        super("Item exists in storage", uuid);
    }
}
