package com.example.tracker.dto;

import com.example.tracker.enums.TaskStatus;
import com.example.tracker.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;
    private TaskStatus status;

    private String authorId;

    private String assigneeId;

    private Set<String> observerIds = new HashSet<>();

    @ReadOnlyProperty
    private User author;

    @ReadOnlyProperty
    private User assignee;

    @ReadOnlyProperty
    private Set<User> observers = new HashSet<>();
}
