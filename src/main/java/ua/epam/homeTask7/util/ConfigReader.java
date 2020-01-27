package ua.epam.homeTask7.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.TimeZone;

public class ConfigReader {

    private static ConfigReader configReader;

    private static final String DB_PROPERTIES_PATH = "src/main/resources/testDb.properties";

    private String url;
    private String user;
    private String password;

    private ConfigReader() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(DB_PROPERTIES_PATH)) {
            properties.load(fis);
            this.url = properties.getProperty("db.url") +
                    "?serverTimezone=" +
                    TimeZone.getDefault().getID();
            this.user = properties.getProperty("db.username");
            this.password = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (configReader == null) {
            configReader = new ConfigReader();
        }
        return configReader;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
