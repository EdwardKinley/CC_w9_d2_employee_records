package controllers;

import db.DBHelper;
import db.Seeds;
import models.Department;
import models.Employee;
import models.Manager;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

public class EmployeesController {

    public static void main(String[] args) {
        staticFileLocation("/public");

        Seeds.seedData();

        ManagersController managersController = new ManagersController();
        EngineersController engineersController = new EngineersController();

        get("/employees", (req, res) -> {
            Map<String, Object> model = new HashMap();
            model.put("template", "templates/employees/index.vtl");

            List<Employee> employees = DBHelper.getAll(Employee.class);
            model.put("employees", employees);

            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/employees/:id", (req, res) -> {
            Map<String, Object> model = new HashMap();
            model.put("template", "templates/employees/show.vtl");
            int employeeId = Integer.parseInt(req.params(":id"));
            Employee employee = DBHelper.find(employeeId, Employee.class);
            model.put("employee", employee);
            Department department = new Department("a");
            Manager manager = new Manager("a", "b", 1, department, 1.0);
            model.put("manager", manager);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/employees/:id/delete", (req, res) -> {
            int employeeId = Integer.parseInt(req.params(":id"));
            Employee employee = DBHelper.find(employeeId, Employee.class);
            DBHelper.delete(employee);
            res.redirect("/employees");
            return null;
        }, new VelocityTemplateEngine());

    }

}
