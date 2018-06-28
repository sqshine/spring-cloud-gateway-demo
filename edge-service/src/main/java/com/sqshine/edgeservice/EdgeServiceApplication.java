package com.sqshine.edgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * @author sqshine
 */
@SpringCloudApplication
public class EdgeServiceApplication {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("start", p -> p.path("/start")
                        .uri("http://spring.io/"))

                .route("lb", p -> p.path("/lb/**").filters(f -> f.stripPrefix(1))
                        .uri("lb://customer-service"))

                .route("cf", p -> p.path("/cf/**").filters(f -> f.filter((exchange, chain) -> chain.filter(exchange)
                        .then(Mono.fromRunnable(() -> {
                            ServerHttpResponse httpResponse = exchange.getResponse();
                            httpResponse.setStatusCode(HttpStatus.CONFLICT);
                            httpResponse.getHeaders().setContentType(MediaType.APPLICATION_PDF);
                        }))).stripPrefix(1))
                        .uri("lb://customer-service"))

                .route("cf1", p -> p.path("/cf1/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter((exchange, chain) -> {
                                    return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                                        ServerHttpResponse httpResponse = exchange.getResponse();
                                        httpResponse.setStatusCode(HttpStatus.CONFLICT);
                                        httpResponse.getHeaders().setContentType(MediaType.APPLICATION_PDF);
                                    }));
                                }))
                        .uri("lb://customer-service"))

                .route("cf2", p -> p.path("/cf2/**")
                        .filters(f -> f.rewritePath("/cf2/(?<segment>.*)", "/customers/${segment}"))
                        .uri("lb://customer-service"))


                .build();
    }


    public static void main(String[] args) {
        SpringApplication.run(EdgeServiceApplication.class, args);
    }
}
