package com.sika.demo.base.controller;

import com.sika.demo.base.entity.TaskEntity;
import com.sika.demo.base.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskEntity create(@RequestBody TaskEntity task, Authentication auth) {
        log.info(auth.getName());
        return taskService.createTask(task, auth.getName());
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

