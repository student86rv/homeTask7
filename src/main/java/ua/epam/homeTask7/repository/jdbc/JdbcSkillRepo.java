package ua.epam.homeTask7.repository.jdbc;

import ua.epam.homeTask7.model.Skill;
import ua.epam.homeTask7.repository.SkillRepository;
import ua.epam.homeTask7.util.ConfigReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcSkillRepo implements SkillRepository {

    private ConfigReader configReader = ConfigReader.getInstance();
    private Connection connection;

    public JdbcSkillRepo() {
        try {
            this.connection = DriverManager.getConnection(configReader.getUrl(),
                    configReader.getUser(), configReader.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createSkillsTable();
    }

    private void createSkillsTable() {
        String createQuery = "CREATE TABLE IF NOT EXISTS skills (\n" +
                "id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "name VARCHAR(255) NOT NULL\n" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Skill entity) {
        String insertQuery = String.format("INSERT INTO skills (name) VALUES ('%s');",
                entity.getName());
        String getIdQuery = String.format("SELECT id FROM skills WHERE name = '%s';",
                entity.getName());

        try (Statement statement = connection.createStatement()) {
            statement.execute(insertQuery);

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
        String getSkillQuery = String.format("SELECT * FROM skills WHERE id = '%d';", id);
        Skill skill = new Skill();
        try (Statement statement = connection.createStatement()) {
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
        String getAllQuery = "SELECT * FROM skills ORDER BY id;";
        List<Skill> skills = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(getAllQuery);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name  = rs.getString("name");
                skills.add(new Skill(id, name));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return skills;
    }

    @Override
    public boolean update(Skill entity) {
        String updateQuery = String.format("UPDATE skills SET name = ('%s') WHERE id = '%d';",
                entity.getName(), entity.getId());
        int updatedRows = 0;
        try (Statement statement = connection.createStatement()) {
            updatedRows = statement.executeUpdate(updateQuery);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    @Override
    public Skill remove(Long id) {
        Skill skill = get(id);
        String removeQuery = String.format("DELETE FROM skills WHERE id = '%d';", id);
        try (Statement statement = connection.createStatement()) {
            statement.execute(removeQuery);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return skill;
    }
}
