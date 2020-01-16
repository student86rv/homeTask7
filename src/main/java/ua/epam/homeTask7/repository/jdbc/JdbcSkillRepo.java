package ua.epam.homeTask7.repository.jdbc;

import ua.epam.homeTask7.model.Skill;
import ua.epam.homeTask7.repository.SkillRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

public class JdbcSkillRepo implements SkillRepository {

    private String databaseUrl;
    private String user;
    private String password;

    private Connection connection;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public JdbcSkillRepo() {
        setDbConfig();
        try {
            this.connection = DriverManager.getConnection(databaseUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setDbConfig() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/local.properties")) {
            properties.load(fis);
            this.databaseUrl = properties.getProperty("db.url") +
                    "?serverTimezone=" +
                    TimeZone.getDefault().getID();
            this.user = properties.getProperty("db.username");
            this.password = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSkillsTable() {
        try (Statement statement = connection.createStatement()) {
            String sqlQuery = "CREATE TABLE IF NOT EXISTS skills (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) NOT NULL" +
                    ");";
            statement.execute(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Skill entity) {
        try (Statement statement = connection.createStatement()) {
            String insertQuery = String.format("INSERT INTO skills (name)" +
                    "VALUES ('%s');", entity.getName());
            statement.execute(insertQuery);

            String getIdQuery = String.format("SELECT id FROM skills" +
                    "WHERE name = '%s';", entity.getName());
            ResultSet rs = statement.executeQuery(getIdQuery);
            int id = 0;
            while (rs.next()) {
                id = rs.getInt("id");
            }
            entity.setId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Skill get(Long id) {
        Skill skill = new Skill();
        try (Statement statement = connection.createStatement()) {
            String getSkillQuery = String.format("SELECT * FROM skills" +
                    "WHERE id = '%d';", id);
            ResultSet rs = statement.executeQuery(getSkillQuery);
            while (rs.next()) {
                skill.setId(rs.getInt("id"));
                skill.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skill;
    }

    @Override
    public List<Skill> getAll() {
        return null;
    }

    @Override
    public boolean update(Skill entity) {
        return false;
    }

    @Override
    public Skill remove(Long aLong) {
        return null;
    }
}
