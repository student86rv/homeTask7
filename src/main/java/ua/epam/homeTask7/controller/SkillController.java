package ua.epam.homeTask7.controller;

import ua.epam.homeTask7.model.Skill;
import ua.epam.homeTask7.repository.SkillRepository;
import ua.epam.homeTask7.repository.javaIO.JavaIOSkillRepo;
import ua.epam.homeTask7.service.SkillService;

import java.util.ArrayList;
import java.util.List;

public class SkillController {

    private SkillRepository repository = new SkillService();

    private final String SKILL_FORMAT = "id: %d, name: %s";
    private final String NOT_FOUND_MSG = "Skill not found!";

    public long addSkill(String name) {
        Skill skill = new Skill(name);
        repository.add(skill);
        return skill.getId();
    }

    public String getSkill(long id) {
        Skill skill = repository.get(id);
        if (skill == null) {
            return NOT_FOUND_MSG;
        }
        return String.format(SKILL_FORMAT, skill.getId(), skill.getName());
    }

    public List<String> getAllSkills() {
        List<String> list = new ArrayList<>();
        for (Skill skill: repository.getAll()) {
            list.add(String.format(SKILL_FORMAT, skill.getId(), skill.getName()));
        }
        return list;
    }

    public boolean updateSkill(long id, String name) {
        Skill skill = new Skill(id, name);
        return repository.update(skill);
    }

    public boolean removeSkill(long id) {
        return repository.remove(id) != null;
    }
}

