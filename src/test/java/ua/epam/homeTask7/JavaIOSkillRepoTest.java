package ua.epam.homeTask7;

import org.junit.Test;
import static org.junit.Assert.*;

import ua.epam.hometask7.model.Skill;
import ua.epam.hometask7.repository.javaIO.JavaIOSkillRepo;

public class JavaIOSkillRepoTest {

    @Test
    public void testAddAndGet() {
        JavaIOSkillRepo repo = new JavaIOSkillRepo();
        Skill skill = new Skill("Some new skill");
        repo.add(skill);
        long id = skill.getId();

        Skill skill2 = repo.get(id);
        assertEquals(skill, skill2);
        repo.remove(id);
    }

    @Test
    public void testRemove() {
        JavaIOSkillRepo repo = new JavaIOSkillRepo();
        Skill skill = new Skill("New skill");
        repo.add(skill);
        long id = skill.getId();

        repo.remove(id);
        assertNull(repo.get(id));
    }

    @Test
    public void testUpdate() {
        JavaIOSkillRepo repo = new JavaIOSkillRepo();
        Skill skill = new Skill("Yet another skill");
        repo.add(skill);
        long id = skill.getId();

        Skill newSkill = new Skill(id, "Updated");
        repo.update(newSkill);

        assertEquals(repo.get(id), newSkill);
        repo.remove(id);
    }
}
