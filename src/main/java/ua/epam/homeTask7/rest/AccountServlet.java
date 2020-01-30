package ua.epam.homeTask7.rest;

import com.google.gson.Gson;
import ua.epam.homeTask7.model.Account;
import ua.epam.homeTask7.service.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AccountServlet", urlPatterns = "/api/v1/accounts")
public class AccountServlet extends HttpServlet {

    private AccountService accountService = new AccountService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Account account = new Account(1, "test@test", AccountStatus.ACTIVE);
        List<Account> accounts = accountService.getAll();
        String jsonString = gson.toJson(accounts);

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }
}
