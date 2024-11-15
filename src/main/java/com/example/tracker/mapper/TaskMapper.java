package com.example.tracker.mapper;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class}
)

public interface TaskMapper {

    TaskMapper TASK_MAPPER = Mappers.getMapper(TaskMapper.class);

    Task taskDtotoTask(TaskDto taskDto);

    TaskDto taskToTaskDto(Task task);
}
