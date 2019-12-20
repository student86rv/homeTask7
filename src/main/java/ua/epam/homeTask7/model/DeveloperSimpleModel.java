package ua.epam.homeTask7.model;

 /*
 Simple representation of Developer class

 Developer(long id, String name, Set<Skill> skills, Account account)

 is converted to

 DeveloperSimpleModel(long id, String name, long[] skillsID, long accountID)

 before writing to file
 */

import java.util.Set;

public class DeveloperSimpleModel {

    private long id;
    private String name;
    private long[] skillsId;
    private long accountId;

    public DeveloperSimpleModel(Developer developer) {
        this.id = developer.getId();
        this.name = developer.getName();
        this.skillsId = getSkillsAsArray(developer.getSkills());
        this.accountId = developer.getAccount().getId();
    }

    private long[] getSkillsAsArray(Set<Skill> skills) {
        long[] skillsId = new long[skills.size()];
        for (int i = 0; i < skills.size(); i++) {
            skillsId[i] = ((Skill) skills.toArray()[i]).getId();
        }
        return skillsId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long[] getSkillsId() {
        return skillsId;
    }

    public void setSkillsId(long[] skillsId) {
        this.skillsId = skillsId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
