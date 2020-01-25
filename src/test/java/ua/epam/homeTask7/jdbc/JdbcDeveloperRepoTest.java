package ua.epam.homeTask7.jdbc;

import org.junit.*;
import ua.epam.homeTask7.model.Account;
import ua.epam.homeTask7.model.AccountStatus;
import ua.epam.homeTask7.model.Developer;
import ua.epam.homeTask7.model.Skill;
import ua.epam.homeTask7.repository.jdbc.JdbcAccountRepo;
import ua.epam.homeTask7.repository.jdbc.JdbcDeveloperRepo;
import ua.epam.homeTask7.repository.jdbc.JdbcSkillRepo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JdbcDeveloperRepoTest {

    private static JdbcAccountRepo accountRepo;
    private static JdbcSkillRepo skillRepo;
    private static JdbcDeveloperRepo developerRepo;

    private static Account account;
    private static Set<Skill> skills;

    @BeforeClass
    public static void initRepo() {
        accountRepo = new JdbcAccountRepo();
        skillRepo = new JdbcSkillRepo();
        developerRepo = new JdbcDeveloperRepo();
    }

    @BeforeClass
    public static void initEntities() {
        account = new Account("vasya.pupkin@gmail.com", AccountStatus.ACTIVE);
        accountRepo.add(account);

        skills = new HashSet<>();
        skills.add(new Skill("Java"));
        skills.add(new Skill("C++"));
        skills.add(new Skill("PHP"));
        skills.add(new Skill("Front-end"));
        skills.add(new Skill("Some new skill"));

        for (Skill skill : skills) {
            skillRepo.add(skill);
        }
    }



    @Test
    public void addAndGetTest() {

        Developer developer = new Developer("Ivan Ivanov", skills, account);
        developerRepo.add(developer);

        long id = developer.getId();
        Developer developer1 = developerRepo.get(id);

        assertEquals(developer, developer1);
        assertEquals(account, developer1.getAccount());
        assertEquals(skills, developer1.getSkills());
//        developerRepo.remove(id);
    }

//    @Test
//    public void removeTest() {
//        Developer developer = new Developer("Petr Petrov", skills, account);
//        developerRepo.add(developer);
//        long id = developer.getId();
//        Developer developer1 = developerRepo.remove(id);
//
//        assertEquals(developer, developer1);
//        assertNull(developerRepo.get(id));
//    }

//    @Test
//    public void updateTest(){
//        Account account = new Account("petr.petrov@ukr.net", AccountStatus.BANNED);
//        repo.add(account);
//        long id = account.getId();
//
//        Account newAccount = new Account(id,"sidor.sidorov@gmail.com", AccountStatus.ACTIVE);
//        repo.update(newAccount);
//
//        assertEquals(repo.get(id), newAccount);
//        repo.remove(id);
//    }

//    @AfterClass
//    public static void removeFromDb() {
//        accountRepo.remove(account.getId());
//
//        for (Skill skill : skills) {
//            skillRepo.remove(skill.getId());
//        }
//    }
}
