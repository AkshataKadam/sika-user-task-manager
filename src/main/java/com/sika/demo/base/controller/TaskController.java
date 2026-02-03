package com.sika.demo.base.controller;

import com.sika.demo.base.entity.TaskEntity;
import com.sika.demo.base.model.ApiResponseDTO;
import com.sika.demo.base.service.TaskService;
import com.sika.demo.base.service.impl.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    protected final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    @PostMapping("/{userId}")
    public ApiResponseDTO<Map<String, String>> createTask(@RequestBody TaskEntity task, @PathVariable Long userId) {
        LOGGER.info("Executing TaskController: createTask()");
        return taskService.createTask(userId, task);
    }

    @GetMapping
    public List<TaskEntity> getAllTasks() {
        LOGGER.info("Executing TaskController: getAllTasks()");
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskEntity getTaskById(@PathVariable Long id) {
        LOGGER.info("Executing TaskController: getTaskById()");
        return taskService.getTask(id);
    }

    @PutMapping("/{id}")
    public TaskEntity updateTask(@PathVariable Long id, @RequestBody TaskEntity task) {
        LOGGER.info("Executing TaskController: updateTask()");
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        LOGGER.info("Executing TaskController: deleteTask()");
        taskService.deleteTask(id);
    }
}

