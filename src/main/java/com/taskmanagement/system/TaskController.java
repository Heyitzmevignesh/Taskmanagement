package com.taskmanagement.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final ServiceTask taskService;

    @Autowired
    public TaskController(ServiceTask taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String showTasks(Model model) {
        List<Taskmodel> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "tasks/list";  // Assuming you have a "list.html" Thymeleaf template
    }

    @GetMapping("/create")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Taskmodel());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Taskmodel task) {
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{taskId}")
    public String showEditTaskForm(@PathVariable Long taskId, Model model) {
        Taskmodel task = taskService.getTaskById(taskId);
        model.addAttribute("task", task);
        return "tasks/edit";
    }

    @PostMapping("/edit/{taskId}")
    public String editTask(@PathVariable Long taskId, @ModelAttribute Taskmodel task) {
        task.setId(taskId);
        taskService.updateTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/tasks";
    }
}
