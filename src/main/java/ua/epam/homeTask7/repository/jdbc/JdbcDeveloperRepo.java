package ua.epam.homeTask7.repository.jdbc;

import ua.epam.homeTask7.model.Account;
import ua.epam.homeTask7.model.AccountStatus;
import ua.epam.homeTask7.model.Developer;
import ua.epam.homeTask7.model.Skill;
import ua.epam.homeTask7.repository.DeveloperRepository;
import ua.epam.homeTask7.util.ConfigReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcDeveloperRepo implements DeveloperRepository {

    private static Logger logger = Logger.getLogger(JdbcDeveloperRepo.class.getName());

    private final String GET_SKILL_QUERY = "SELECT s.id id, s.name name FROM skills s INNER JOIN\n" +
            "developer_skills d_s ON s.id = d_s.skill_id WHERE d_s.developer_id = '%d' ORDER BY name;";
    private final String INSERT_DEV_SKILL_QUERY = "INSERT INTO developer_skills (developer_id, skill_id)\n" +
            "VALUES ('%d', '%d');";
    private final String DELETE_DEV_SKILL_QUERY = "DELETE FROM developer_skills WHERE developer_id = '%d';";

    private ConfigReader configReader = ConfigReader.getInstance();
    private Connection connection;

    public JdbcDeveloperRepo() {
        try {
            this.connection = DriverManager.getConnection(configReader.getUrl(),
                    configReader.getUser(), configReader.getPassword());
            logger.log(Level.INFO, "Repository connected to database");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Connection to database failed");
        }
        createTables();
    }

    private void createTables() {
        String createDevQuery = "CREATE TABLE IF NOT EXISTS developers (\n" +
                "id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "name VARCHAR(255) NOT NULL,\n" +
                "account_id INT NOT NULL,\n" +
                "FOREIGN KEY (account_id) REFERENCES accounts (id)\n" +
                ");";
        String createDSQuery = "CREATE TABLE IF NOT EXISTS developer_skills (\n" +
                "developer_id INT NOT NULL,\n" +
                "skill_id INT NOT NULL,\n" +
                "UNIQUE (developer_id, skill_id),\n" +
                "FOREIGN KEY (developer_id) REFERENCES developers (id),\n" +
                "FOREIGN KEY (skill_id) REFERENCES skills (id)\n" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.addBatch(createDevQuery);
            statement.addBatch(createDSQuery);
            statement.executeBatch();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Tables creating failed");
        }
    }

    @Override
    public void add(Developer entity) {
        String insertDevQuery = String.format("INSERT INTO developers (name, account_id)\n" +
                "VALUES ('%s', '%d');", entity.getName(), entity.getAccount().getId());
        String getIdQuery = "SELECT MAX(id) id FROM developers;";
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute(insertDevQuery);

            ResultSet rs = statement.executeQuery(getIdQuery);
            int id = 0;
            while (rs.next()) {
                id = rs.getInt("id");
            }
            entity.setId(id);

            for (Skill skill : entity.getSkills()) {
                statement.addBatch(String.format(INSERT_DEV_SKILL_QUERY, id, skill.getId()));
            }
            statement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Writing failed. Trying to cancel transaction...");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.log(Level.SEVERE, "Transaction rollback failed");
            }
        }
    }

    @Override
    public Developer get(Long id) {
        String getDevQuery = String.format("SELECT d.id id, d.name name, d.account_id account_id,\n" +
                "a.email email, a.status status FROM developers d INNER JOIN accounts a\n" +
                "ON d.account_id = a.id WHERE d.id = '%d'", id);
        Developer developer = null;
        Set<Skill> skills = new HashSet<>();
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);

            ResultSet devRs = statement.executeQuery(getDevQuery);
            while (devRs.next()) {
                developer = new Developer(
                        devRs.getInt("id"),
                        devRs.getString("name"),
                        null,
                        new Account(
                                devRs.getInt("account_id"),
                                devRs.getString("email"),
                                AccountStatus.valueOf(devRs.getString("status"))
                        )
                );
            }
            ResultSet skillRs = statement.executeQuery(String.format(GET_SKILL_QUERY, id));
            while (skillRs.next()) {
                skills.add(new Skill(
                        skillRs.getInt("id"),
                        skillRs.getString("name")
                ));
            }
            developer.setSkills(skills);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Reading failed. Trying to cancel transaction...");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.log(Level.SEVERE, "Transaction rollback failed");
            }
        }
        return developer;
    }

    @Override
    public List<Developer> getAll() {
        String getAllQuery = "SELECT d.id id, d.name name, d.account_id account_id,\n" +
                "a.email email, a.status status FROM developers d INNER JOIN accounts a\n" +
                "ON account_id = a.id ORDER BY id";
        List<Developer> developers = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);

            ResultSet devRs = statement.executeQuery(getAllQuery);
            while (devRs.next()) {
                developers.add(new Developer(
                        devRs.getInt("id"),
                        devRs.getString("name"),
                        null,
                        new Account(
                                devRs.getInt("account_id"),
                                devRs.getString("email"),
                                AccountStatus.valueOf(devRs.getString("status"))
                        )
                ));
            }
            for (Developer developer : developers) {
                Set<Skill> skills = new HashSet<>();
                ResultSet skillRs = statement.executeQuery(String.format(GET_SKILL_QUERY, developer.getId()));

                while (skillRs.next()) {
                    skills.add(new Skill(
                            skillRs.getInt("id"),
                            skillRs.getString("name")
                    ));
                }
                developer.setSkills(skills);
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Reading failed. Trying to cancel transaction...");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.log(Level.SEVERE, "Transaction rollback failed");
            }
        }
        return developers;
    }

    @Override
    public boolean update(Developer entity) {
        String updateDevQuery = String.format("UPDATE developers SET name = '%s'," +
                        "account_id = '%d' WHERE id = '%d';",
                entity.getName(), entity.getAccount().getId(), entity.getId());
        int updatedRows = 0;
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            updatedRows = statement.executeUpdate(updateDevQuery);

            statement.execute(String.format(DELETE_DEV_SKILL_QUERY, entity.getId()));

            for (Skill skill : entity.getSkills()) {
                statement.addBatch(String.format(INSERT_DEV_SKILL_QUERY, entity.getId(), skill.getId()));
            }
            statement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Updating failed. Trying to cancel transaction...");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.log(Level.SEVERE, "Transaction rollback failed");
            }
        }
        return updatedRows > 0;
    }

    @Override
    public Developer remove(Long id) {
        Developer developer = get(id);
        String deleteDevQuery = String.format("DELETE FROM developers WHERE id = '%d';", id);
        try (Statement statement = connection.createStatement()) {
            statement.addBatch(deleteDevQuery);
            statement.addBatch(DELETE_DEV_SKILL_QUERY);
            statement.executeBatch();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Deleting failed. Trying to cancel transaction...");
        }
        return developer;
    }
}
