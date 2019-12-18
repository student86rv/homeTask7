package ua.epam.homeTask7.repository.javaIO;

import ua.epam.homeTask7.model.Skill;
import ua.epam.homeTask7.repository.SkillRepository;

import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JavaIOSkillRepo implements SkillRepository {

    private final String PATH = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "resources";

    private final String FILE_NAME = "skillsFile.txt";

    private List<Skill> skillsList = new ArrayList<>();
    private File skillsFile;
    private long count;

    public JavaIOSkillRepo() {
        this.skillsFile = new File(PATH, FILE_NAME);
        if (skillsFile.exists()) {
            this.skillsList = readFromFile(skillsFile);
            count = getMaxId();
        } else {
            try {
                skillsFile.createNewFile();
                count = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void add(Skill entity) {
        skillsList = readFromFile(skillsFile);
        entity.setId(count);
        skillsList.add(entity);
        count++;
        writeToFile(skillsList, skillsFile);
    }

    public Skill get(Long id) {
        skillsList = readFromFile(skillsFile);
        for (Skill skill : skillsList) {
            if (skill.getId() == id) {
                return skill;
            }
        }
        return null;
    }

    public List<Skill> getAll() {
        skillsList = readFromFile(skillsFile);
        return skillsList;
    }

    public boolean update(Skill entity) {
        boolean result = false;
        skillsList = readFromFile(skillsFile);
        for (Skill skill : skillsList) {
            if (skill.getId() == entity.getId()) {
                skillsList.set(skillsList.indexOf(skill), entity);
                result = true;
            }
        }
        writeToFile(skillsList, skillsFile);
        return result;
    }

    public Skill remove(Long id) {
        Skill removedSkill = null;
        skillsList = readFromFile(skillsFile);
        for (int i = 0; i < skillsList.size(); i++) {
            if (skillsList.get(i).getId() == id) {
                removedSkill = skillsList.remove(i);
            }
        }
        writeToFile(skillsList, skillsFile);
        return removedSkill;
    }

    private long getMaxId() {
        skillsList = readFromFile(skillsFile);
        long[] iDs = new long[skillsList.size()];
        for (int i = 0; i < skillsList.size(); i++) {
            iDs[i] = skillsList.get(i).getId();
        }
        return findMax(iDs);
    }

    private long findMax(long[] arr) {
        long max = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    private List<Skill> readFromFile(File file) {
        List<Skill> readList = new ArrayList<>();
        Gson gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                Skill skill = gson.fromJson(currentLine, Skill.class);
                readList.add(skill);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readList;
    }

    private void writeToFile(List<Skill> skills, File file) {
        Gson gson = new Gson();
        try {
           BufferedWriter bw = new BufferedWriter(new FileWriter(file));
           for (Skill skill: skills) {
               String jsonString = gson.toJson(skill);
               bw.write(jsonString + "\n");
           }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
