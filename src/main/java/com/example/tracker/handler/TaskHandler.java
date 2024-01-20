package com.example.tracker.handler;

import com.example.tracker.dto.TaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.net.URI;

@Slf4j
@Component
public class TaskHandler {

    public Mono<ServerResponse> getAllItem(ServerRequest serverRequest) { // todo: прикрутить список тасков из бд

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Flux.just(),
                        TaskDto.class);
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) { // todo: прикрутить таск из бд по id

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(null),
                        TaskDto.class);
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) { // todo: прикрутить создание таски в бд

        return serverRequest.bodyToMono(TaskDto.class)
                .flatMap(item -> {
                    log.info("Item for create {}", item);
                    return  Mono.just(item);
                })
                .flatMap(item -> ServerResponse.created(URI.create("/api/v1/functional/item/" + item.getId())).build());

    }

    // todo: создать обновление таски в бд
    // todo: создать удаление таски из бд

    public Mono<ServerResponse> errorRequest(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.error(new RuntimeException("Exception in errorRequest!")), String.class)
                .onErrorResume(e -> {
                    log.error("Error for errorRequest", e);

                    return ServerResponse.badRequest().body(Mono.error(e), String.class);
                });
    }
}
