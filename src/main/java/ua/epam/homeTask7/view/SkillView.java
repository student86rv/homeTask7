package ua.epam.homeTask7.view;

import ua.epam.homeTask7.controller.SkillController;

import java.util.List;
import java.util.Scanner;

public class SkillView {

    private SkillController controller = new SkillController();

    private final String MENU_HEADER = "Skills menu:";
    private final String MENU_SEPARATOR = "________________________";
    private final String MENU_ENTRIES = "1: Add new skill" + "\n" +
            "2: Get skill by ID" + "\n" +
            "3: View all skills" + "\n" +
            "4: Update existing skill" + "\n" +
            "5: Delete skill" + "\n" +
            "0: Quit (to Main menu)";

    private final String ENTER_NAME_MSG = "Enter skill's name:";
    private final String ENTER_ID_MSG = "Enter skill's ID:";

    private final String SUCCESS_MSG = "Successful!";
    private final String NOT_FOUND_MSG = "Skill not found!";

    private final String NEW_SKILL_MSG = "New skill added with id: %d";

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
                    addSkill();
                    break;
                case 2:
                    printSkill();
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

    private void addSkill() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_NAME_MSG);
        String name = scanner.nextLine();
        long id = controller.addSkill(name);
        System.out.println(String.format(NEW_SKILL_MSG, id));
    }

    private void printSkill() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_ID_MSG);
        long id = scanner.nextInt();
        System.out.println(controller.getSkill(id));
    }

    private void printAll() {
        List<String> list = controller.getAllSkills();
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

        if (controller.updateSkill(newId, newName)) {
            System.out.println(SUCCESS_MSG);
        } else {
            System.out.println(NOT_FOUND_MSG);
        }
    }

    private void delete() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_ID_MSG);
        long id = scanner.nextInt();
        if (controller.removeSkill(id)) {
            System.out.println(SUCCESS_MSG);
        } else {
            System.out.println(NOT_FOUND_MSG);
        }
    }
}
