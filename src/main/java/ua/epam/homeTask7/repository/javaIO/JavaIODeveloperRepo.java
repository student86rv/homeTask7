package ua.epam.homeTask7.repository.javaIO;

import com.google.gson.Gson;
import ua.epam.homeTask7.model.Account;
import ua.epam.homeTask7.model.Developer;
import ua.epam.homeTask7.model.DeveloperSimpleModel;
import ua.epam.homeTask7.model.Skill;
import ua.epam.homeTask7.repository.DeveloperRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaIODeveloperRepo implements DeveloperRepository {

    private final String PATH = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "resources";

    private final String FILE_NAME = "developersFile.txt";

    private List<DeveloperSimpleModel> developersList = new ArrayList<>();
    private File developersFile;
    private long count;

    public JavaIODeveloperRepo() {
        this.developersFile = new File(PATH, FILE_NAME);
        if (developersFile.exists()) {
            this.developersList = readFromFile(developersFile);
            count = getMaxId() + 1;
        } else {
            try {
                developersFile.createNewFile();
                count = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void add(Developer entity) {
        developersList = readFromFile(developersFile);
        entity.setId(count);
        developersList.add(toSimpleModel(entity));
        count++;
        writeToFile(developersList, developersFile);
    }

    @Override
    public Developer get(Long id) {
        developersList = readFromFile(developersFile);
        for (DeveloperSimpleModel model : developersList) {
            if (model.getId() == id) {
                return fromSimpleModel(model);
            }
        }
        return null;
    }

    @Override
    public List<Developer> getAll() {
        developersList = readFromFile(developersFile);
        List<Developer> allDevelopers = new ArrayList<>();

        for (DeveloperSimpleModel model : developersList) {
            allDevelopers.add(fromSimpleModel(model));
        }
        return allDevelopers;
    }

    @Override
    public boolean update(Developer entity) {
        boolean result = false;
        developersList = readFromFile(developersFile);
        for (DeveloperSimpleModel model : developersList) {
            if (model.getId() == entity.getId()) {
                developersList.set(developersList.indexOf(model), toSimpleModel(entity));
                result = true;
            }
        }
        writeToFile(developersList, developersFile);
        return result;
    }

    @Override
    public Developer remove(Long id) {
        Developer removed = null;
        developersList = readFromFile(developersFile);
        for (int i = 0; i < developersList.size(); i++) {
            if (developersList.get(i).getId() == id) {
                removed = fromSimpleModel(developersList.remove(i));
            }
        }
        writeToFile(developersList, developersFile);
        return removed;
    }

    private DeveloperSimpleModel toSimpleModel(Developer developer) {
        return new DeveloperSimpleModel(developer);
    }

    private Developer fromSimpleModel(DeveloperSimpleModel model) {
        Set<Skill> skillSet = getSkillsFromRepo(model.getSkillsId());
        Account account = getAccountFromRepo(model.getAccountId());
        return new Developer(model.getId(), model.getName(), skillSet, account);
    }

    private Set<Skill> getSkillsFromRepo(long[] skillsId) {
        Set<Skill> skillSet = new HashSet<>();
        JavaIOSkillRepo repo = new JavaIOSkillRepo();
        for (long id : skillsId) {
            skillSet.add(repo.get(id));
        }
        return skillSet;
    }

    private Account getAccountFromRepo(long id) {
        JavaIOAccountRepo repo = new JavaIOAccountRepo();
        return repo.get(id);
    }

    private long getMaxId() {
        developersList = readFromFile(developersFile);
        long[] iDs = new long[developersList.size()];
        for (int i = 0; i < developersList.size(); i++) {
            iDs[i] = developersList.get(i).getId();
        }
        return findMax(iDs);
    }

    private long findMax(long[] arr) {
        if (arr == null || arr.length == 0) {
            return 1;
        }
        long max = arr[0];
        for (long item : arr) {
            if (item > max) {
                max = item;
            }
        }
        return max;
    }

    private List<DeveloperSimpleModel> readFromFile(File file) {
        List<DeveloperSimpleModel> readList = new ArrayList<>();
        Gson gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                DeveloperSimpleModel model = gson.fromJson(currentLine, DeveloperSimpleModel.class);
                readList.add(model);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readList;
    }

    private void writeToFile(List<DeveloperSimpleModel> models, File file) {
        Gson gson = new Gson();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (DeveloperSimpleModel model : models) {
                String jsonString = gson.toJson(model);
                bw.write(jsonString + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
