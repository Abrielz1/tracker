package com.example.tracker.dto;

import com.example.tracker.enums.TaskStatus;
import com.example.tracker.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TaskDto {

    @NotBlank
    private String id;

   // @NotBlank
    private String name;

   // @NotBlank
    private String description;

   // @NotNull
    private Instant createdAt;

  //  @NotNull
    private Instant updatedAt;

    private TaskStatus status;

 //   @NotBlank
    private String authorId;

 //   @NotBlank
    private String assigneeId;

    private Set<String> observerIds = new HashSet<>();

    @ReadOnlyProperty
    private UserDto author;

    @ReadOnlyProperty
    private UserDto assignee;

    @ReadOnlyProperty
    private Set<User> observers = new HashSet<>();
}
