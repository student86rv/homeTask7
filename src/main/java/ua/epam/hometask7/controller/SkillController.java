package ua.epam.hometask7.controller;

import ua.epam.hometask7.model.Skill;
import ua.epam.hometask7.repository.javaIO.JavaIOSkillRepo;

import java.util.ArrayList;
import java.util.List;

public class SkillController {

    private JavaIOSkillRepo repo = new JavaIOSkillRepo();

    private final String SKILL_FORMAT = "id: %d, name: %s";
    private final String NOT_FOUND_MSG = "Skill not found!";

    public long addSkill(String name) {
        Skill skill = new Skill(name);
        repo.add(skill);
        return skill.getId();
    }

    public String getSkill(long id) {
        Skill skill = repo.get(id);
        if (skill == null) {
            return NOT_FOUND_MSG;
        }
        return String.format(SKILL_FORMAT, skill.getId(), skill.getName());
    }

    public List<String> getAllSkills() {
        List<String> list = new ArrayList<>();
        for (Skill skill: repo.getAll()) {
            list.add(String.format(SKILL_FORMAT, skill.getId(), skill.getName()));
        }
        return list;
    }

    public boolean updateSkill(long id, String name) {
        Skill skill = new Skill(id, name);
        return repo.update(skill);
    }

    public boolean removeSkill(long id) {
        return repo.remove(id) != null;
    }
}

