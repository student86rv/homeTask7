package ua.epam.homeTask7.repository.javaIO;

import ua.epam.homeTask7.model.Account;
import ua.epam.homeTask7.repository.AccountReposirory;

import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JavaIOAccountRepo implements AccountReposirory {

    private final String PATH = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "resources";

    private final String FILE_NAME = "accountsFile.txt";

    private List<Account> accountsList = new ArrayList<>();
    private File accountsFile;
    private long count;

    public JavaIOAccountRepo() {
        this.accountsFile = new File(PATH, FILE_NAME);

        if (accountsFile.exists()) {
            this.accountsList = readFromFile(accountsFile);
            count = getMaxId();
        } else {
            try {
                accountsFile.createNewFile();
                count = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void add(Account entity) {
        accountsList = readFromFile(accountsFile);
        entity.setId(count);
        accountsList.add(entity);
        count++;
        writeToFile(accountsList, accountsFile);
    }

    @Override
    public Account get(Long id) {
        accountsList = readFromFile(accountsFile);
        for (Account account : accountsList) {
            if (account.getId() == id) {
                return account;
            }
        }
        return null;
    }

    @Override
    public List<Account> getAll() {
        accountsList = readFromFile(accountsFile);
        return accountsList;
    }

    @Override
    public boolean update(Account entity) {
        boolean result = false;
        accountsList = readFromFile(accountsFile);
        for (Account account : accountsList) {
            if (account.getId() == entity.getId()) {
                accountsList.set(accountsList.indexOf(account), entity);
                result = true;
            }
        }
        writeToFile(accountsList, accountsFile);
        return result;
    }

    @Override
    public Account remove(Long id) {
        Account removedAccount = null;
        accountsList = readFromFile(accountsFile);
        for (int i = 0; i < accountsList.size(); i++) {
            if (accountsList.get(i).getId() == id) {
                removedAccount = accountsList.remove(i);
            }
        }
        writeToFile(accountsList, accountsFile);
        return removedAccount;
    }

    private long getMaxId() {
        accountsList = readFromFile(accountsFile);
        long[] iDs = new long[accountsList.size()];
        for (int i = 0; i < accountsList.size(); i++) {
            iDs[i] = accountsList.get(i).getId();
        }
        return findMax(iDs);
    }

    private long findMax(long[] arr) {
        if (arr.length > 0) {
            long max = arr[0];
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] > max) {
                    max = arr[i];
                }
            }
            return max;
        }
        return 1;
    }

    private List<Account> readFromFile(File file) {
        List<Account> readList = new ArrayList<>();
        Gson gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                Account account = gson.fromJson(currentLine, Account.class);
                readList.add(account);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readList;
    }

    private void writeToFile(List<Account> accounts, File file) {
        Gson gson = new Gson();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (Account account: accounts) {
                String jsonString = gson.toJson(account);
                bw.write(jsonString + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
