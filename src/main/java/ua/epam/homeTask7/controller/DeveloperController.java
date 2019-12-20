package ua.epam.homeTask7.controller;

import ua.epam.homeTask7.model.Account;
import ua.epam.homeTask7.model.Developer;
import ua.epam.homeTask7.model.Skill;
import ua.epam.homeTask7.repository.javaIO.JavaIOAccountRepo;
import ua.epam.homeTask7.repository.javaIO.JavaIODeveloperRepo;
import ua.epam.homeTask7.repository.javaIO.JavaIOSkillRepo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeveloperController {

    private JavaIODeveloperRepo repo = new JavaIODeveloperRepo();

    private final String DEVELOPER_FORMAT = "id: %d, name: %s" + "\n" +
                                            "account: %s, status: %s" + "\n" +
                                            "skills: %s";
    private final String NOT_FOUND_MSG = "Skill not found!";

    public long addDeveloper(String name, Set<Long> skillsIdSet, long accountId) {
        JavaIOSkillRepo skillRepo = new JavaIOSkillRepo();
        Set<Skill> skills = new HashSet<>();
        for (Long id: skillsIdSet) {
            Skill skill = skillRepo.get(id);
            if (skill != null) {
                skills.add(skill);
            }
        }

        JavaIOAccountRepo accountRepo = new JavaIOAccountRepo();
        Account account = accountRepo.get(accountId);

        Developer developer = new Developer(name, skills, account);

        repo.add(developer);
        return developer.getId();
    }

    public String getDeveloper(long id) {
        Developer developer = repo.get(id);
        if (developer == null) {
            return NOT_FOUND_MSG;
        }
        return String.format(DEVELOPER_FORMAT, developer.getId(), developer.getName(),
                developer.getAccount().getEmail(), developer.getAccount().getStatus().toString(),
                developer.getSkills().toString());
    }

    public List<String> getAllDevelopers() {
        List<String> list = new ArrayList<>();
        for (Developer developer: repo.getAll()) {
            list.add(String.format(DEVELOPER_FORMAT, developer.getId(), developer.getName(),
                    developer.getAccount().getEmail(), developer.getAccount().getStatus().toString(),
                    developer.getSkills().toString()));
        }
        return list;
    }

    public boolean updateDeveloper(long id, String name, Set<Long> skillsIdSet, long accountId) {
        JavaIOSkillRepo skillRepo = new JavaIOSkillRepo();
        Set<Skill> skills = new HashSet<>();
        for (Long skillsID: skillsIdSet) {
            Skill skill = skillRepo.get(skillsID);
            if (skill != null) {
                skills.add(skill);
            }
        }

        JavaIOAccountRepo accountRepo = new JavaIOAccountRepo();
        Account account = accountRepo.get(accountId);

        Developer developer = new Developer(id, name, skills, account);
        return repo.update(developer);
    }

    public boolean removeDeveloper(long id) {
        return repo.remove(id) != null;
    }
}
