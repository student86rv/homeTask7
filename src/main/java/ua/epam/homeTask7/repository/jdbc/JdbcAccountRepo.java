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
        try (Statement statement = connection.createStatement()) {
            String createQuery = "CREATE TABLE IF NOT EXISTS accounts (\n" +
                    "id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "email VARCHAR(255) NOT NULL,\n" +
                    "status VARCHAR(255) NOT NULL\n" +
                    ");";
            statement.execute(createQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Account entity) {
        try (Statement statement = connection.createStatement()) {
            String insertQuery = String.format("INSERT INTO accounts (email, status) VALUES ('%s', '%s');",
                    entity.getEmail(), entity.getStatus().toString());
            statement.execute(insertQuery);

            String getIdQuery = String.format("SELECT id FROM skills WHERE email = '%s';",
                    entity.getEmail());
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
        Account account = new Account();
        try (Statement statement = connection.createStatement()) {
            String getAccountQuery = String.format("SELECT * FROM accounts WHERE id = '%d';", id);
            ResultSet rs = statement.executeQuery(getAccountQuery);
            while (rs.next()) {
                account.setId(rs.getInt("id"));
                account.setEmail(rs.getString("email"));
                account.setStatus(AccountStatus.valueOf(rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String getAllQuery = "SELECT * FROM accounts ORDER BY id;";
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
        int updatedRows = 0;
        try (Statement statement = connection.createStatement()) {
            String updateQuery = String.format("UPDATE accounts SET email = '%s'," +
                            "status = '%s' WHERE id = '%d';",
                    entity.getEmail(), entity.getStatus().toString(), entity.getId());
            updatedRows = statement.executeUpdate(updateQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    @Override
    public Account remove(Long id) {
        Account account = get(id);
        try (Statement statement = connection.createStatement()) {
            String removeQuery = String.format("DELETE FROM accounts WHERE id = '%d';", id);
            statement.execute(removeQuery);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
}
