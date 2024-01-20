package com.example.tracker.mapper;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskMapper TASK_MAPPER = Mappers.getMapper(TaskMapper.class);

    Task toTask(TaskDto taskDto);

    TaskDto toTaskDto(Task task);

    //todo: прикрутить default методы для запихивания вложенных списков в объекты
}
