package com.example.tracker.configuration;

import com.example.tracker.handler.TaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Slf4j
@Configuration
public class TaskRouter {

    @Bean
    public RouterFunction<ServerResponse> itemResponse(TaskHandler taskHandler) {

        return RouterFunctions.route()
                .GET("/api/v1/functional/item", taskHandler::getAllItem)
                .GET("/api/v1/functional/item/{id}", taskHandler::getById)
                .POST("/api/v1/functional/item", taskHandler::create)
                .GET("/api/v1/functional/error", taskHandler::errorRequest)
                .build();

    }
}
