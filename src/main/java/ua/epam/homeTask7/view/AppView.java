package ua.epam.homeTask7.view;

import java.util.Scanner;

public class AppView {
    private final String MENU_HEADER = "Main menu:";
    private final String MENU_SEPARATOR = "______________";
    private final String MENU_ENTRIES = "1: Developers" + "\n" +
            "2: Accounts" + "\n" +
            "3: Skills" + "\n" +
            "0: Quit";

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
//                    DeveloperView developerView = new DeveloperView();
//                    developerView.run();
                    break;
                case 2:
                    AccountView accountView = new AccountView();
                    accountView.run();
                    break;
                case 3:
                    SkillView skillView = new SkillView();
                    skillView.run();
                    break;
            }
        }
        while (menuChoice != 0);
    }
}
