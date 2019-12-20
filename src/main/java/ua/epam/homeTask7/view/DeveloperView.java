package ua.epam.homeTask7.view;

import ua.epam.homeTask7.controller.DeveloperController;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class DeveloperView {

    private DeveloperController controller = new DeveloperController();

    private final String MENU_HEADER = "Developers menu:";
    private final String MENU_SEPARATOR = "____________________________";
    private final String MENU_ENTRIES = "1: Add new developer" + "\n" +
            "2: Get developer by ID" + "\n" +
            "3: View all developers" + "\n" +
            "4: Update existing developer" + "\n" +
            "5: Delete developer" + "\n" +
            "0: Quit (to Main menu)";

    private final String ENTER_NAME_MSG = "Enter developer's name:";
    private final String SKILLS_ADD_MSG = "Add developer's skills" + "\n" +
            "(Enter IDs of existing skills, 0 - Exit)";

    private final String ACCOUNT_ADD_MSG = "Enter account's ID:";

    private final String ENTER_ID_MSG = "Enter developer's ID:";

    private final String SUCCESS_MSG = "Successful!";
    private final String NOT_FOUND_MSG = "Developer not found!";

    private final String NEW_DEVELOPER_MSG = "New developer added with id: %d";

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int menuChoice;
        do {
            System.out.println(MENU_HEADER);
            System.out.println(MENU_SEPARATOR);
            System.out.println(MENU_ENTRIES);
            menuChoice = scanner.nextInt();

            switch (menuChoice) {
                case 1:
                    addDeveloper();
                    break;
                case 2:
                    printDeveloper();
                    break;
                case 3:
                    printAll();
                    break;
                case 4:
                    update();
                    break;
                case 5:
                    delete();
                    break;
                default:
                    System.out.println("Select entry 1...5!");
            }
        }
        while (menuChoice != 0);
    }

    private void addDeveloper() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_NAME_MSG);
        String name = scanner.nextLine();

        System.out.println(SKILLS_ADD_MSG);
        long skillId;
        Set<Long> skillIdSet = new HashSet<>();
        while ((skillId = scanner.nextInt()) != 0) {
            skillIdSet.add(skillId);
        }

        System.out.println(ACCOUNT_ADD_MSG);
        long accountId = scanner.nextInt();

        long id = controller.addDeveloper(name, skillIdSet, accountId);
        System.out.println(String.format(NEW_DEVELOPER_MSG, id));
    }

    private void printDeveloper() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_ID_MSG);
        long id = scanner.nextInt();
        System.out.println(controller.getDeveloper(id));
    }

    private void printAll() {
        List<String> list = controller.getAllDevelopers();
        for (String s: list) {
            System.out.println(s);
        }
    }

    private void update() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_ID_MSG);
        long newId = scanner.nextInt();

        System.out.println(ENTER_NAME_MSG);
        String newName = scanner.nextLine();

        System.out.println(SKILLS_ADD_MSG);
        long skillId;
        Set<Long> skillIdSet = new HashSet<>();
        while ((skillId = scanner.nextInt()) != 0) {
            skillIdSet.add(skillId);
        }

        System.out.println(ACCOUNT_ADD_MSG);
        long accountId = scanner.nextInt();

        if (controller.updateDeveloper(newId, newName, skillIdSet, accountId)) {
            System.out.println(SUCCESS_MSG);
        } else {
            System.out.println(NOT_FOUND_MSG);
        }
    }

    private void delete() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_ID_MSG);
        long id = scanner.nextInt();
        if (controller.removeDeveloper(id)) {
            System.out.println(SUCCESS_MSG);
        } else {
            System.out.println(NOT_FOUND_MSG);
        }
    }
}
