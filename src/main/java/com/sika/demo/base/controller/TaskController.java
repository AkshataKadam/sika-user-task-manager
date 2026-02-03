package com.sika.demo.base.controller;

import com.sika.demo.base.entity.TaskEntity;
import com.sika.demo.base.model.ApiResponseDTO;
import com.sika.demo.base.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/{userId}")
    public ApiResponseDTO<Map<String, String>> create(@RequestBody TaskEntity task, @PathVariable Long userId) {
        return taskService.createTask(userId, task);
    }

    @GetMapping
    public List<TaskEntity> getAll() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskEntity get(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PutMapping("/{id}")
    public TaskEntity update(@PathVariable Long id, @RequestBody TaskEntity task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}

