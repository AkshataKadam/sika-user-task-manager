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

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    protected final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);


    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


//    public ApiResponseDTO<Map<String, String>> updateUser(Long id, UserEntity updatedUser) {
//        if (!id.equals(updatedUser.getUserId())) {
//            LOGGER.error("You cannot update this user as path ID and body userId must be the same");
//            return ResponseBuilder.error(
//                    "USER_UPDATE_FAILURE",
//                    "You cannot update this user.");
//        } else {
//            UserEntity existing = getUserById(id);
//
//            existing.setFirstName(updatedUser.getFirstName());
//            existing.setLastName(updatedUser.getLastName());
//            existing.setDateOfBirth(updatedUser.getDateOfBirth());
//            userRepository.save(existing);
//
//            return ResponseBuilder.success(
//                    "USER_UPDATE_SUCCESS",
//                    "User Updated successfully for " + existing.getEmail(),
//                    Map.of("email", existing.getEmail())
//            );
//        }
//    }



//    @Override
//    public TaskEntity createTask(TaskEntity task, String authenticatedEmail) {
//        UserEntity user = userRepository.findByEmail(authenticatedEmail)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        task.setUserId(user.getUserId());
//        task.setStatus(TaskStatusEnum.TODO);
//
////        return taskRepository.save(task);
//
//
//        if (!task.getId().equals(updatedUser.getUserId())) {
//            LOGGER.error("You cannot update this user as path ID and body userId must be the same");
//            return ResponseBuilder.error(
//                    "USER_UPDATE_FAILURE",
//                    "You cannot update this user.");
//        } else {
//            UserEntity existing = getUserById(id);
//
//            existing.setFirstName(updatedUser.getFirstName());
//            existing.setLastName(updatedUser.getLastName());
//            existing.setDateOfBirth(updatedUser.getDateOfBirth());
//            taskRepository.save(task);
//
//            return ResponseBuilder.success(
//                    "USER_UPDATE_SUCCESS",
//                    "User Updated successfully for " + existing.getEmail(),
//                    Map.of("email", existing.getEmail())
//            );
//        }
//    }



    @Override
    public TaskEntity createTask(TaskEntity task, String authenticatedEmail) {
        UserEntity user = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setUserId(user.getUserId());
        task.setStatus(TaskStatusEnum.TODO);

        return taskRepository.save(task);
    }

    @Override
    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public TaskEntity getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public TaskEntity updateTask(Long id, TaskEntity updatedTask) {
        TaskEntity existing = getTask(id);

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setDueDate(updatedTask.getDueDate());
        existing.setStatus(updatedTask.getStatus());

        return taskRepository.save(existing);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}

