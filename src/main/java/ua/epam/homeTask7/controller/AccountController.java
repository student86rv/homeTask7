package ua.epam.homeTask7.controller;

import ua.epam.homeTask7.model.Account;
import ua.epam.homeTask7.model.AccountStatus;
import ua.epam.homeTask7.repository.AccountReposirory;
import ua.epam.homeTask7.service.AccountService;

import java.util.ArrayList;
import java.util.List;

public class AccountController {

    private AccountReposirory repository = new AccountService();

    private final String ACCOUNT_FORMAT = "id: %d, name: %s, status: %s";
    private final String NOT_FOUND_MSG = "Skill not found!";

    public long addAccount(String email, int statusCode) {
        Account account = new Account();
        account.setEmail(email);
        switch(statusCode) {
            case 1:
                account.setStatus(AccountStatus.ACTIVE);
                break;
            case 2:
                account.setStatus(AccountStatus.BANNED);
                break;
            case 3:
                account.setStatus(AccountStatus.DELETED);
                break;
        }

        repository.add(account);
        return account.getId();
    }

    public String getAccount(long id) {
        Account account = repository.get(id);
        if (account == null) {
            return NOT_FOUND_MSG;
        }
        return String.format(ACCOUNT_FORMAT, account.getId(), account.getEmail(),
                account.getStatus().toString());
    }

    public List<String> getAllAccounts() {
        List<String> list = new ArrayList<>();
        for (Account account: repository.getAll()) {
            list.add(String.format(ACCOUNT_FORMAT, account.getId(), account.getEmail(),
                    account.getStatus().toString()));
        }
        return list;
    }

    public boolean updateAccount(long id, String email, int statusCode) {
        Account account = new Account(id, email);
        switch(statusCode) {
            case 1:
                account.setStatus(AccountStatus.ACTIVE);
                break;
            case 2:
                account.setStatus(AccountStatus.BANNED);
                break;
            case 3:
                account.setStatus(AccountStatus.DELETED);
                break;
        }
        return repository.update(account);
    }

    public boolean removeAccount(long id) {
        return repository.remove(id) != null;
    }
}
