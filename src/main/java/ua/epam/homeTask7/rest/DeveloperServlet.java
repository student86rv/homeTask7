package ua.epam.homeTask7.rest;

import com.google.gson.Gson;
import ua.epam.homeTask7.model.Developer;
import ua.epam.homeTask7.service.DeveloperService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DeveloperServlet", urlPatterns = "/api/v1/developers")
public class DeveloperServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(DeveloperServlet.class.getName());

    private DeveloperService developerService = new DeveloperService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        if (req.getParameter("id").equalsIgnoreCase("all")) {
            List<Developer> developers = developerService.getAll();
            String jsonDev = gson.toJson(developers);
            out.print(jsonDev);
            out.flush();
        } else if (req.getParameter("id").matches("\\d+")) {
            long id = Long.parseLong(req.getParameter("id"));
            Developer developer = developerService.get(id);
            String jsonDev = gson.toJson(developer);
            out.print(jsonDev);
            out.flush();
        } else {
            resp.sendError(400, "Id is not valid");
            logger.log(Level.WARNING, "Request with invalid Id");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Developer developer = gson.fromJson(req.getReader(), Developer.class);
            developerService.add(developer);
        } catch (Exception e) {
            resp.sendError(400, "Request body is not a valid developer");
            logger.log(Level.WARNING, "Request with invalid json");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Developer developer = gson.fromJson(req.getReader(), Developer.class);
            developerService.update(developer);
        } catch (Exception e) {
            resp.sendError(400, "Request body is not a valid account");
            logger.log(Level.WARNING, "Request with invalid json");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id").matches("\\d+")) {
            long id = Long.parseLong(req.getParameter("id"));
            developerService.remove(id);
        } else {
            resp.sendError(400, "Id is not valid");
            logger.log(Level.WARNING, "Request with invalid Id");
        }
    }
}
