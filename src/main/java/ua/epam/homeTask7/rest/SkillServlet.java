package ua.epam.homeTask7.rest;

import com.google.gson.Gson;
import ua.epam.homeTask7.model.Skill;
import ua.epam.homeTask7.service.SkillService;

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

@WebServlet(name = "SkillServlet", urlPatterns = "/api/v1/skills")
public class SkillServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(SkillServlet.class.getName());

    private SkillService skillService = new SkillService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        if (req.getParameter("id").equalsIgnoreCase("all")) {
            List<Skill> skills = skillService.getAll();
            String jsonSkill = gson.toJson(skills);
            out.print(jsonSkill);
            out.flush();
        } else if (req.getParameter("id").matches("\\d+")) {
            long id = Long.parseLong(req.getParameter("id"));
            Skill skill = skillService.get(id);
            String jsonSkill = gson.toJson(skill);
            out.print(jsonSkill);
            out.flush();
        } else {
            resp.sendError(400, "Id is not valid");
            logger.log(Level.WARNING, "Request with invalid Id");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Skill skill = gson.fromJson(req.getReader(), Skill.class);
            skillService.add(skill);
        } catch (Exception e) {
            resp.sendError(400, "Request body is not a valid skill");
            logger.log(Level.WARNING, "Request with invalid json");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Skill skill = gson.fromJson(req.getReader(), Skill.class);
            skillService.update(skill);
        } catch (Exception e) {
            resp.sendError(400, "Request body is not a valid account");
            logger.log(Level.WARNING, "Request with invalid json");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id").matches("\\d+")) {
            long id = Long.parseLong(req.getParameter("id"));
            skillService.remove(id);
        } else {
            resp.sendError(400, "Id is not valid");
            logger.log(Level.WARNING, "Request with invalid Id");
        }
    }
}
