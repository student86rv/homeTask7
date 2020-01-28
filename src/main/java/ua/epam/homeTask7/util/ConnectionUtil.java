package ua.epam.homeTask7.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionUtil {

    private static Logger logger = Logger.getLogger(ConnectionUtil.class.getName());
    private static final String DB_PROPERTIES_PATH = "src/main/resources/testDb.properties";

    private static BasicDataSource dataSource = new BasicDataSource();
    private static Properties properties;

    static {
        properties = readProperties(DB_PROPERTIES_PATH);
        dataSource.setUrl(properties.getProperty("db.url") +
                "?serverTimezone=" + TimeZone.getDefault().getID());
        dataSource.setUsername(properties.getProperty("db.username"));
        dataSource.setPassword(properties.getProperty("db.password"));
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    private static Properties readProperties(String path) {
        Properties properties = null;
        try (FileInputStream fis = new FileInputStream(path)) {
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Database config reading failed");
        }
        return properties;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
