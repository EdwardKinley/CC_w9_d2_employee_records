package controllers;

import db.DBHelper;
import models.Department;
import models.Employee;
import models.Manager;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class ManagersController {

    public ManagersController() {
        this.setupEndpoints();
    }

    private void setupEndpoints() {

        get("/managers", (req, res) -> {
            Map<String, Object> model = new HashMap();
            model.put("template", "templates/managers/index.vtl");
            List<Employee> managers = DBHelper.getAll(Manager.class);
            model.put("managers", managers);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/managers/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/managers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("managers/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "templates/managers/show.vtl");
            int managerId = Integer.parseInt(req.params(":id"));
            Manager manager = DBHelper.find(managerId, Manager.class);
            model.put("manager", manager);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/managers", (req, res) -> {
//            Map<String, Object> model = new HashMap<>();
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.valueOf(req.queryParams("salary"));
            double budget = Double.valueOf(req.queryParams("budget"));
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);
            Manager manager = new Manager(firstName, lastName, salary, department, budget);
            DBHelper.save(manager);
            res.redirect("/managers");
            return null;
        }, new VelocityTemplateEngine());

        post("/managers/:id/delete", (req, res) -> {
//            Map<String, Object> model = new HashMap<>();
            int managerId = Integer.parseInt(req.params(":id"));
            Manager manager = DBHelper.find(managerId, Manager.class);
            DBHelper.delete(manager);
            res.redirect("/managers");
            return null;
        }, new VelocityTemplateEngine());



    }

}

