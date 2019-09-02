package ru.dipech.resumes.sql;

import org.postgresql.util.PSQLException;
import ru.dipech.resumes.exception.ItemExistsStorageException;
import ru.dipech.resumes.exception.StorageException;

import java.sql.SQLException;

public class SqlExceptionConverter {
    // http://www.postgresql.org/docs/9.3/static/errcodes-appendix.html
    public static StorageException convert(SQLException e) {
        if (e instanceof PSQLException) {
            if (e.getSQLState().equals("23505")) {
                return new ItemExistsStorageException(null);
            }
        }
        return new StorageException(e);
    }
}
