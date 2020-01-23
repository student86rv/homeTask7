package ua.epam.homeTask7.repository.jdbc;

import ua.epam.homeTask7.model.Account;
import ua.epam.homeTask7.model.AccountStatus;
import ua.epam.homeTask7.repository.AccountReposirory;
import ua.epam.homeTask7.util.ConfigReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountRepo implements AccountReposirory {

    private ConfigReader configReader = ConfigReader.getInstance();
    private Connection connection;

    public JdbcAccountRepo() {
        try {
            this.connection = DriverManager.getConnection(configReader.getUrl(),
                    configReader.getUser(), configReader.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createAccountTable();
    }

    private void createAccountTable() {
        String createQuery = "CREATE TABLE IF NOT EXISTS accounts (\n" +
                "id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "email VARCHAR(255) NOT NULL,\n" +
                "status VARCHAR(255) NOT NULL\n" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Account entity) {
        String insertQuery = String.format("INSERT INTO accounts (email, status) VALUES ('%s', '%s');",
                entity.getEmail(), entity.getStatus().toString());
        String getIdQuery = "SELECT MAX(id) id FROM accounts;";

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
    public Account get(Long id) {
        String getAccountQuery = String.format("SELECT * FROM accounts WHERE id = '%d';", id);
        Account account = null;
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(getAccountQuery);
            while (rs.next()) {
                account = new Account(
                        rs.getInt("id"),
                        rs.getString("email"),
                        AccountStatus.valueOf(rs.getString("status"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public List<Account> getAll() {
        String getAllQuery = "SELECT * FROM accounts ORDER BY id;";
        List<Account> accounts = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(getAllQuery);
            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                AccountStatus status = AccountStatus.valueOf(rs.getString("status"));
                accounts.add(new Account(id, email, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public boolean update(Account entity) {
        String updateQuery = String.format("UPDATE accounts SET email = '%s'," +
                        "status = '%s' WHERE id = '%d';",
                entity.getEmail(), entity.getStatus().toString(), entity.getId());
        int updatedRows = 0;
        try (Statement statement = connection.createStatement()) {
            updatedRows = statement.executeUpdate(updateQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    @Override
    public Account remove(Long id) {
        Account account = get(id);
        String removeQuery = String.format("DELETE FROM accounts WHERE id = '%d';", id);
        try (Statement statement = connection.createStatement()) {
            statement.execute(removeQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
}
