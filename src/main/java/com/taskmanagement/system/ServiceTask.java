package com.taskmanagement.system;

import java.util.List;

public interface ServiceTask {
    void saveTask(Taskmodel task);
    void updateTask(Taskmodel task);
    void deleteTask(Long taskId);
    Taskmodel getTaskById(Long taskId);

    List<Taskmodel> getAllTasks();
}
