package com.sika.demo.base.service;

import com.sika.demo.base.entity.TaskEntity;
import com.sika.demo.base.model.ApiResponseDTO;

import java.util.List;
import java.util.Map;

public interface TaskService {

    ApiResponseDTO<Map<String, String>> createTask(Long userId, TaskEntity task);

    TaskEntity getTask(Long id);

    TaskEntity updateTask(Long id, TaskEntity updatedTask);

    void deleteTask(Long id);

    List<TaskEntity> getAllTasks();
}

