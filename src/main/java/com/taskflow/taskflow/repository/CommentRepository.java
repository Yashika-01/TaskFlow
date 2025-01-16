package com.taskflow.taskflow.repository;

import com.taskflow.taskflow.model.Comment;
import com.taskflow.taskflow.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask(Task task);
}
