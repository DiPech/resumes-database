package ru.dipech.resumes.exception;

public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(Exception e) {
        super(e.getMessage(), e);
    }

    public StorageException(String message, Exception exception) {
        super(message, exception);
    }

}
