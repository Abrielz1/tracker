package com.example.tracker.configuration;

import com.example.tracker.handler.UserHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Slf4j
@Configuration
public class UserRouter {

//    @Bean
//    public RouterFunction<ServerResponse> itemResponse(UserHandler userHandler) {
//
//        return RouterFunctions.route()
//                .GET("/api/v1/functional/item", userHandler::getAllUsers)
//                .GET("/api/v1/functional/item/{id}", userHandler::getById)
//                .POST("/api/v1/functional/item", userHandler::create)
//                .GET("/api/v1/functional/error", userHandler::errorRequest)
//                .build();
//
//    }
}
