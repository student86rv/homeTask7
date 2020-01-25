package ua.epam.homeTask7.service;

import ua.epam.homeTask7.model.Account;
import ua.epam.homeTask7.repository.AccountReposirory;
import ua.epam.homeTask7.repository.jdbc.JdbcAccountRepo;

import java.util.List;

public class AccountService implements AccountReposirory {

    private AccountReposirory jdbcRepo = new JdbcAccountRepo();

    @Override
    public void add(Account entity) {
        jdbcRepo.add(entity);
    }

    @Override
    public Account get(Long id) {
        return jdbcRepo.get(id);
    }

    @Override
    public List<Account> getAll() {
        return jdbcRepo.getAll();
    }

    @Override
    public boolean update(Account entity) {
        return jdbcRepo.update(entity);
    }

    @Override
    public Account remove(Long id) {
        return jdbcRepo.remove(id);
    }
}
