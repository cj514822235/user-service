package com.tw.userservice.service;


import com.tw.userservice.exception.TaskNotFoundException;
import com.tw.userservice.exception.TaskRepetitionException;
import com.tw.userservice.model.Level;
import com.tw.userservice.model.ModifyTaskInfo;
import com.tw.userservice.model.Criteria;
import com.tw.userservice.model.ShareTask;
import com.tw.userservice.model.Task;
import com.tw.userservice.repository.TaskRepository;
import com.tw.userservice.utils.TaskSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Service
public class TaskService {


    @Autowired
    private TaskRepository taskRepository;


    public String createTasks(String userId, List<Task> tasks) {
        if(checkTasks(userId, tasks)){
            tasks.forEach(task -> {task.setStatus(true);task.setUserId(userId);});
            taskRepository.saveAll(tasks);
            return userId;
        }
        throw new TaskRepetitionException("Task Repetition"); //todo
    }

    private boolean checkTasks(String userId, List<Task> tasks) {
     List<Task> list = taskRepository.findTasksByUserId(userId);
     AtomicReference<Boolean> repeatTask = new AtomicReference<>(true);

     tasks.forEach(task -> list.forEach(task1 -> {
         if(task1.getDescription().equals(task.getDescription())){
             repeatTask.set(false);
         }
     }));
     return repeatTask.get();

    }


    public Long deleteTask(Long id) {
        Task task = Optional.ofNullable(taskRepository.
                        findTaskById(id)).
                filter(task1 -> task1.getStatus().equals(true))
                .orElseThrow(() -> new TaskNotFoundException("Task " + id + " Not Found"));
        task.setStatus(false);
        taskRepository.save(task);
        return id;

    }


    public List<Task> getTasks(String userId, String level) {

        Criteria criteria = getCriteria(userId, level);

        TaskSpecification taskSpecification = new TaskSpecification(criteria);

        return taskRepository.findAll(taskSpecification);
    }

    public Criteria getCriteria(String userId, String level) {//todo

        if (level == null) {
            return Criteria.builder()
                    .UserId(userId)
                    .build();
        }
        return Criteria.builder()
                .UserId(userId)
                .level(Level.valueOf(level))
                .build();
    }

    public Task getTask(Long id) {
        return Optional.ofNullable(taskRepository.findTaskById(id))
                .filter(task1 -> task1.getStatus().equals(true))
                .orElseThrow(() -> new TaskNotFoundException("Task Not Found"));
    }


    public Long shareTask(ShareTask shareTask) {
        Task task = Optional.ofNullable(taskRepository.findTaskById(shareTask.getTaskId()))
                .filter(task1 -> task1.getStatus().equals(true))
                .orElseThrow(() -> new TaskNotFoundException("Task Not Found"));

        taskRepository.findTasksByUserId(shareTask.getUserId()).forEach(task1 -> {
            if(task1.getLevel().equals(task.getLevel())&&task1.getDescription().equals(task.getDescription())){
                throw new TaskRepetitionException("Task already exist");
            }
        });

        Task newTask = Task.builder()
                .status(true)
                .userId(shareTask.getUserId())
                .description(task.getDescription())
                .level(task.getLevel())
                .build();
        taskRepository.save(newTask);
        return newTask.getId();
    }

    public Long modifyTask(Long id, ModifyTaskInfo modifyTaskInfo) {
        Task task = Optional.ofNullable(taskRepository.findTaskById(id))
                .filter(task1 -> task1.getStatus().equals(true)).orElseThrow(()->new TaskNotFoundException("Task Not Found"));
        if(task.getDescription().equals(modifyTaskInfo.getDescription())){
            throw new TaskRepetitionException("The modifications are the same.");
        }
        task.setDescription(modifyTaskInfo.getDescription());
        taskRepository.save(task);
        return task.getId();
    }
}
