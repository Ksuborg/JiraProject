package test.jira.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import test.jira.service.TaskService;

import java.io.IOException;

@Controller
@RequestMapping
public class TaskController {
    private final TaskService taskService;

    private TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create/tasks")
    public ResponseEntity<?> createTask(@RequestParam("file") MultipartFile file) {
        System.out.println("in method");
        try {
            return taskService.createIssues(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}