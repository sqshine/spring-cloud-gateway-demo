package com.sqshine.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;

@SpringCloudApplication
public class GatewayApplication {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("hello_rewrite", p -> p.path("/service/hello/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            String name = exchange.getRequest().getQueryParams().getFirst("name");
                            String path = "/hello/" + name;
                            ServerHttpRequest request = exchange.getRequest().mutate()
                                    .path(path)
                                    .build();
                            return chain.filter(exchange.mutate().request(request).build());
                        }))
                        .uri("lb://hello"))
                //.uri("http://localhost:8081"))

                .route("ui", p -> p.path("/").or().path("/css/**").or().path("/js/**")
                        .uri("lb://ui"))

                /*.route("monolith2", p -> p.path("/service/randomfortune/**")
                        .uri("http://192.168.90.186:8081"))*/

                /*.route("monolith2", p -> p.path("/a/b/service/ra/**").filters(f -> f.stripPrefix(2))
                        .uri("http://192.168.90.186:8081"))*/

                .route("monolith", p -> p.path("/**")
                        .uri("lb://MONOLITH"))

                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
