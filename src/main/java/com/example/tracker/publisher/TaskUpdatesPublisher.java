package com.example.tracker.publisher;

import com.example.tracker.dto.TaskDto;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class TaskUpdatesPublisher {

    private final Sinks.Many<TaskDto> taskDtoUpdatesSink;

    public TaskUpdatesPublisher() {
        this.taskDtoUpdatesSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(TaskDto model) {
        taskDtoUpdatesSink.tryEmitNext(model);
    }

    public Sinks.Many<TaskDto> getUpdateSink() {
        return taskDtoUpdatesSink;
    }
}
