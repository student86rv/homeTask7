package ua.epam.homeTask7.jdbc;

import org.junit.Before;
import org.junit.Test;
import ua.epam.homeTask7.model.Skill;
import ua.epam.homeTask7.repository.jdbc.JdbcSkillRepo;

import static org.junit.Assert.*;

public class JdbcSkillRepoTest {

    private JdbcSkillRepo repo;

    @Before
    public void initRepo() {
        repo = new JdbcSkillRepo();
    }

    @Test
    public void addAndGetTest() {
        Skill skill = new Skill("Some new skill");
        repo.add(skill);
        long id = skill.getId();
        Skill skill2 = repo.get(id);

        assertEquals(skill, skill2);

        repo.remove(id);
    }

    @Test
    public void removeTest() {
        Skill skill = new Skill("Remove test skill");
        repo.add(skill);
        long id = skill.getId();
        Skill skill2 = repo.remove(id);

        assertEquals(skill, skill2);
        assertNull(repo.get(id));
    }

}
