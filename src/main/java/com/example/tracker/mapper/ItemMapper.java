package com.example.tracker.mapper;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

    ItemMapper ITEM_MAPPER = Mappers.getMapper(ItemMapper.class);

    TaskDto toItem(Task item);

    Task toTaskDto(TaskDto taskDto);
}
