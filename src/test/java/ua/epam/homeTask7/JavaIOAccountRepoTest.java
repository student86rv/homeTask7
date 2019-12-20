package ua.epam.homeTask7;

import org.junit.Test;

import static org.junit.Assert.*;

import ua.epam.homeTask7.model.Account;
import ua.epam.homeTask7.model.AccountStatus;
import ua.epam.homeTask7.repository.javaIO.JavaIOAccountRepo;

public class JavaIOAccountRepoTest {

    @Test
    public void testAddAndGet() {
        JavaIOAccountRepo repo = new JavaIOAccountRepo();
        Account account = new Account("test.email@gmail.com", AccountStatus.ACTIVE);
        repo.add(account);
        long id = account.getId();

        Account account2 = repo.get(id);
        assertEquals(account, account2);
        repo.remove(id);
    }

    @Test
    public void testRemove() {
        JavaIOAccountRepo repo = new JavaIOAccountRepo();
        Account account = new Account("test.email@gmail.com", AccountStatus.ACTIVE);
        repo.add(account);
        long id = account.getId();

        repo.remove(id);
        assertNull(repo.get(id));
    }

    @Test
    public void testUpdate() {
        JavaIOAccountRepo repo = new JavaIOAccountRepo();
        Account account = new Account("test.email@gmail.com", AccountStatus.ACTIVE);
        repo.add(account);
        long id = account.getId();

        Account newAccount = new Account(id, "some.new@ukr.net", AccountStatus.BANNED);
        repo.update(newAccount);

        assertEquals(repo.get(id), newAccount);
        repo.remove(id);
    }
}
