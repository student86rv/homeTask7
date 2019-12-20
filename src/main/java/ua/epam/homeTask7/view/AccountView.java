package ua.epam.homeTask7.view;

import ua.epam.homeTask7.controller.AccountController;

import java.util.List;
import java.util.Scanner;

public class AccountView {

    private AccountController controller = new AccountController();

    private final String MENU_HEADER = "Accounts menu:";
    private final String MENU_SEPARATOR = "__________________________";
    private final String MENU_ENTRIES = "1: Add new account" + "\n" +
            "2: Get account by ID" + "\n" +
            "3: View all accounts" + "\n" +
            "4: Update existing account" + "\n" +
            "5: Delete account" + "\n" +
            "0: Quit (to Main menu)";

    private final String ENTER_EMAIL_MSG = "Enter account's email:";
    private final String STATUS_SELECT_MSG = "Select account's status" + "\n" +
            "(1 - Active, 2 - Banned, 3 - Deleted)";

    private final String ENTER_ID_MSG = "Enter account's ID:";

    private final String SUCCESS_MSG = "Successful!";
    private final String NOT_FOUND_MSG = "Account not found!";

    private final String NEW_ACCOUNT_MSG = "New account added with id: %d";

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
                    addAccount();
                    break;
                case 2:
                    printAccount();
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

    private void addAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_EMAIL_MSG);
        String email = scanner.nextLine();
        System.out.println(STATUS_SELECT_MSG);
        int statusCode = scanner.nextInt();

        long id = controller.addAccount(email, statusCode);
        System.out.println(String.format(NEW_ACCOUNT_MSG, id));
    }

    private void printAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_ID_MSG);
        long id = scanner.nextInt();
        System.out.println(controller.getAccount(id));
    }

    private void printAll() {
        List<String> list = controller.getAllAccounts();
        for (String s: list) {
            System.out.println(s);
        }
    }

    private void update() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_ID_MSG);
        long newId = scanner.nextInt();

        System.out.println(ENTER_EMAIL_MSG);
        String newEmail = scanner.nextLine();

        System.out.println(STATUS_SELECT_MSG);
        int newStatusCode = scanner.nextInt();

        if (controller.updateAccount(newId, newEmail, newStatusCode)) {
            System.out.println(SUCCESS_MSG);
        } else {
            System.out.println(NOT_FOUND_MSG);
        }
    }

    private void delete() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_ID_MSG);
        long id = scanner.nextInt();
        if (controller.removeAccount(id)) {
            System.out.println(SUCCESS_MSG);
        } else {
            System.out.println(NOT_FOUND_MSG);
        }
    }
}
