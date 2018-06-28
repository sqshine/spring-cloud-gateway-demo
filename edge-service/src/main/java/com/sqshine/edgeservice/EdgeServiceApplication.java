package com.sqshine.edgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @author sqshine
 */
@SpringCloudApplication
public class EdgeServiceApplication {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("start", p -> p.path("/start")
                        .uri("http://www.baidu.com"))

                .build();
    }


    public static void main(String[] args) {
        SpringApplication.run(EdgeServiceApplication.class, args);
    }
}
