package ru.dipech.resumes.util;

import ru.dipech.resumes.storage.SqlStorage;
import ru.dipech.resumes.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private final Properties properties = new Properties();
    private final String appPath = getAppPath();

    private String getAppPath() {
        String appDir = System.getProperty("app.dir");
        return appDir != null ? appDir : ".";
    }

    private String storagePath;
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    public static Config getInstance() {
        return INSTANCE;
    }

    private Config() {
        String env = System.getenv("app.env");
        if (env == null) {
            env = "dev";
        }
        String configFileName = env + ".properties";
        switch (env) {
            case "dev":
                String configPath = appPath + "/config/" + configFileName;
                File configFile = new File(configPath);
                if (!configFile.exists() || configFile.isDirectory()) {
                    throw new IllegalStateException("Config file path is wrong: " + configPath);
                }
                try (InputStream is = new FileInputStream(configFile)) {
                    loadFromInputStream(is);
                } catch (IOException e) {
                    throw new IllegalStateException("Invalid config file: " + configPath);
                }
                break;
            case "prod":
                try (InputStream is = Config.class.getResourceAsStream("/" + configFileName)) {
                    loadFromInputStream(is);
                } catch (IOException e) {
                    throw new IllegalStateException("Invalid config file: " + configFileName);
                }
                break;
            default:
                throw new IllegalStateException("Wrong env: " + env);
        }
    }

    public Storage getStorage() {
        return new SqlStorage(dbUrl, dbUser, dbPass);
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    private void loadFromInputStream(InputStream is) throws IOException {
        properties.load(is);
        init();
    }

    private void init() {
        storagePath = appPath + "/storage";
        dbUrl = properties.getProperty("db.url");
        dbUser = properties.getProperty("db.user");
        dbPass = properties.getProperty("db.pass");
    }

}
