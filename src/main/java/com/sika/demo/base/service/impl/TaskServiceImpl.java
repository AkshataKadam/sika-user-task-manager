package com.sika.demo.base.service.impl;

import com.sika.demo.base.model.ApiResponseDTO;
import com.sika.demo.base.utils.ResponseBuilder;
import com.sika.demo.base.utils.constants.TaskStatusEnum;
import com.sika.demo.base.entity.TaskEntity;
import com.sika.demo.base.entity.UserEntity;
import com.sika.demo.base.repository.TaskRepository;
import com.sika.demo.base.repository.UserRepository;
import com.sika.demo.base.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    protected final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponseDTO<Map<String, String>> createTask(Long userId, TaskEntity task) {
        LOGGER.info("Creating task");
        if (!userId.equals(task.getUserId())) {
            LOGGER.error("You cannot create this task as logged in userId and task userId must be the same");
            return ResponseBuilder.error(
                    "TASK_CREATION_FAILURE",
                    "You cannot create this task.");
        } else {
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            task.setUserId(user.getUserId());
            task.setStatus(TaskStatusEnum.TODO);
            taskRepository.save(task);

            LOGGER.info("Task created successfully.");
            return ResponseBuilder.success(
                    "TASK_CREATION_SUCCESS",
                    "Task created successfully for " + user.getUserId(),
                    Map.of("task", task.getTitle())
            );
        }
    }

    @Override
    public List<TaskEntity> getAllTasks() {
        LOGGER.info("Fetching all tasks.");
        return taskRepository.findAll();
    }

    @Override
    public TaskEntity getTask(Long id) {
        LOGGER.info("Fetching task by id: {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public TaskEntity updateTask(Long id, TaskEntity updatedTask) {
        // TODO: User authentication needs to be implemented
        LOGGER.info("Updating task");
        TaskEntity existing = getTask(id);

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setDueDate(updatedTask.getDueDate());
        existing.setStatus(updatedTask.getStatus());

        return taskRepository.save(existing);
    }

    @Override
    public void deleteTask(Long id) {
        // TODO: User authentication needs to be implemented
        LOGGER.info("Deleting task by id: {}", id);
        taskRepository.deleteById(id);
    }
}

