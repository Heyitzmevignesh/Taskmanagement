package com.taskmanagement.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class Taskservice implements ServiceTask {

    private final DataSource dataSource;

    @Autowired
    public Taskservice(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveTask(Taskmodel task) {
        String sql = "INSERT INTO taskmodel (title, description, due_date, completed) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertTaskStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            insertTaskStmt.setString(1, task.getTitle());
            insertTaskStmt.setString(2, task.getDescription());
            insertTaskStmt.setObject(3, task.getDueDate());
            insertTaskStmt.setBoolean(4, task.isCompleted());

            // Execute the query
            int rowsAffected = insertTaskStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Task created successfully!");

                // Retrieve the generated ID
                ResultSet generatedKeys = insertTaskStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    task.setId(generatedKeys.getLong(1));
                }
            } else {
                System.out.println("Failed to create task.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException according to your application's error-handling strategy.
        }
    }

    @Override
    public void updateTask(Taskmodel task) {
        String sql = "UPDATE taskmodel SET title=?, description=?, due_date=?, completed=? WHERE id=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement updateTaskStmt = connection.prepareStatement(sql)) {

            updateTaskStmt.setString(1, task.getTitle());
            updateTaskStmt.setString(2, task.getDescription());
            updateTaskStmt.setObject(3, task.getDueDate());
            updateTaskStmt.setBoolean(4, task.isCompleted());
            updateTaskStmt.setLong(5, task.getId());

            // Execute the query
            int rowsAffected = updateTaskStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Task updated successfully!");
            } else {
                System.out.println("Failed to update task. Task not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException according to your application's error-handling strategy.
        }
    }

    @Override
    public void deleteTask(Long taskId) {
        String sql = "DELETE FROM taskmodel WHERE id=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement deleteTaskStmt = connection.prepareStatement(sql)) {

            deleteTaskStmt.setLong(1, taskId);

            // Execute the query
            int rowsAffected = deleteTaskStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Task deleted successfully!");
            } else {
                System.out.println("Failed to delete task. Task not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException according to your application's error-handling strategy.
        }
    }

    @Override
    public Taskmodel getTaskById(Long taskId) {
        String sql = "SELECT * FROM taskmodel WHERE id = ?";
        Taskmodel task = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getTaskStmt = connection.prepareStatement(sql)) {

            getTaskStmt.setLong(1, taskId);

            // Execute the query
            ResultSet resultSet = getTaskStmt.executeQuery();

            // Check if a task was found
            if (resultSet.next()) {
                task = mapResultSetToTask(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException according to your application's error-handling strategy.
        }

        return task;
    }

    // Helper method to map ResultSet to a Taskmodel object
    private Taskmodel mapResultSetToTask(ResultSet resultSet) throws SQLException {
        Taskmodel task = new Taskmodel();
        task.setId(resultSet.getLong("id"));
        task.setTitle(resultSet.getString("title"));
        task.setDescription(resultSet.getString("description"));
        task.setDueDate(resultSet.getObject("due_date", LocalDate.class));
        task.setCompleted(resultSet.getBoolean("completed"));
        return task;
    }

    @Override
    public List<Taskmodel> getAllTasks() {
        String sql = "SELECT * FROM taskmodel";
        List<Taskmodel> tasks = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getAllTasksStmt = connection.prepareStatement(sql)) {

            // Execute the query
            ResultSet resultSet = getAllTasksStmt.executeQuery();

            // Iterate through the result set and add tasks to the list
            while (resultSet.next()) {
                Taskmodel task = mapResultSetToTask(resultSet);
                tasks.add(task);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException according to your application's error-handling strategy.
        }

        return tasks;
    }
}
