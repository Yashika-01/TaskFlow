package com.taskflow.taskflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @GetMapping
    public List<String> getTasks() {
        // Example: Replace with real task fetching logic
        return List.of("Task 1", "Task 2", "Task 3");
    }
}
