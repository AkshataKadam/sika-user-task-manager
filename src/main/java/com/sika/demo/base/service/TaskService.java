package com.sika.demo.base.service;

import com.sika.demo.base.entity.TaskEntity;

import java.util.List;

public interface TaskService {

    TaskEntity createTask(TaskEntity task, String authenticatedEmail);

    TaskEntity getTask(Long id);

    TaskEntity updateTask(Long id, TaskEntity updatedTask);

    void deleteTask(Long id);

    List<TaskEntity> getAllTasks();
}

