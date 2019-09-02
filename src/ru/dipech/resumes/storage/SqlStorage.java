package ru.dipech.resumes.storage;

import ru.dipech.resumes.exception.ItemNotExistsStorageException;
import ru.dipech.resumes.model.*;
import ru.dipech.resumes.sql.SqlHelper;
import ru.dipech.resumes.util.json.JsonParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SqlStorage implements Storage {

    private static final Logger LOG = Logger.getLogger(SqlStorage.class.getName());

    private final JsonParser jsonParser = new JsonParser();
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPass) {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPass);
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get by uuid: " + uuid);
        return sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("SELECT full_name FROM resume WHERE uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new ItemNotExistsStorageException(uuid);
                }
                Resume resume = new Resume(uuid, rs.getString("full_name"));
                selectAndProcessEach(conn, "" +
                                "SELECT type, name, url " +
                                "FROM contact " +
                                "WHERE uuid = ?",
                        psc -> psc.setString(1, uuid),
                        rsc -> addContact(resume, rsc));
                selectAndProcessEach(conn, "" +
                                "SELECT type, data " +
                                "FROM section " +
                                "WHERE uuid = ?",
                        pss -> pss.setString(1, uuid),
                        rss -> addSection(resume, rss));
                return resume;
            }
        });
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            insertResumeData(resume, conn);
            return null;
        });
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new ItemNotExistsStorageException(resume.getUuid());
                }
            }
            deleteResumeContacts(resume, conn);
            deleteResumeSections(resume, conn);
            insertResumeData(resume, conn);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Update by uuid: " + uuid);
        sqlHelper.execute("DELETE FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new ItemNotExistsStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("Get all");
        return sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("SELECT uuid, full_name FROM resume ORDER BY uuid, full_name")) {
                ResultSet rs = ps.executeQuery();
                Map<String, Resume> resumes = new LinkedHashMap<>();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
                selectAndProcessEach(conn, "SELECT uuid, type, name, url FROM contact", psc -> {}, rsc -> {
                    Resume resume = resumes.get(rsc.getString("uuid"));
                    addContact(resume, rsc);
                });
                selectAndProcessEach(conn, "SELECT uuid, type, data FROM section", pss -> {}, rss -> {
                    Resume resume = resumes.get(rss.getString("uuid"));
                    addSection(resume, rss);
                });
                return new ArrayList<>(resumes.values());
            }
        });
    }

    @Override
    public void clear() {
        LOG.info("Clear");
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public int size() {
        LOG.info("Size");
        return sqlHelper.execute("SELECT COUNT(uuid) AS count FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("count") : 0;
        });
    }

    private void insertResumeData(Resume resume, Connection conn) throws SQLException {
        insertResumeContacts(resume, conn);
        insertResumeSections(resume, conn);
    }

    private void insertResumeContacts(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (uuid, type, name, url) VALUES (?,?,?,?)")) {
            for (Map.Entry<ContactType, Contact> entry : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, entry.getValue().getName());
                ps.setString(4, entry.getValue().getUrl());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertResumeSections(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (uuid, type, data) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> entry : resume.getSections().entrySet()) {
                Section section = entry.getValue();
                ps.setString(1, resume.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, jsonParser.write(section, Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addContact(Resume resume, ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        if (type != null) {
            Contact contact = new Contact(rs.getString("url"), rs.getString("name"));
            resume.addContact(ContactType.valueOf(type), contact);
        }
    }

    private void addSection(Resume resume, ResultSet rs) throws SQLException {
        SectionType sectionType = SectionType.valueOf(rs.getString("type"));
        String data = rs.getString("data");
        resume.addSection(sectionType, jsonParser.read(data, Section.class));
    }

    private void deleteResumeContacts(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE uuid = ?")) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }

    private void deleteResumeSections(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE uuid = ?")) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }

    private void selectAndProcessEach(Connection conn, String query, SqlBinder binder, SqlProcessor processor)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            binder.bind(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                processor.process(rs);
            }
        }
    }

    private interface SqlBinder {
        void bind(PreparedStatement ps) throws SQLException;
    }

    private interface SqlProcessor {
        void process(ResultSet rs) throws SQLException;
    }

}
