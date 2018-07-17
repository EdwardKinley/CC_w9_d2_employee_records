package controllers;

        import db.DBHelper;
        import models.*;
        import models.Engineer;
        import spark.ModelAndView;
        import spark.template.velocity.VelocityTemplateEngine;

        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import static spark.Spark.get;
        import static spark.Spark.post;

public class EngineersController {

    public EngineersController() {
        this.setupEndpoints();
    }

    private void setupEndpoints() {

        get("engineers/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "templates/engineers/show.vtl");
            int engineerId = Integer.parseInt(req.params(":id"));
            Engineer engineer = DBHelper.find(engineerId, Engineer.class);
            model.put("engineer", engineer);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/engineers", (req, res) -> {
            Map<String, Object> model = new HashMap();
            model.put("template", "templates/engineers/index.vtl");

            List<Employee> engineers = DBHelper.getAll(Engineer.class);
            model.put("engineers", engineers);

            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/engineers/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/engineers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/engineers", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.valueOf(req.queryParams("salary"));
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);
            Engineer newEngineer = new Engineer(firstName, lastName, salary, department);
            DBHelper.save(newEngineer);
            res.redirect("/engineers");
            return null;
        }, new VelocityTemplateEngine());



    }
}
