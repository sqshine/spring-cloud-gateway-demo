package com.sqshine.customerservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@SpringBootApplication
public class CustomerServiceApplication {

    @Bean
    ApplicationRunner init(CustomerRepository repository) {
        return args -> repository.deleteAll()
                .thenMany(Flux.just("A", "B", "C").map(l -> new Customer(null, l)).flatMap(repository::save))
                .thenMany(repository.findAll())
                .subscribe(System.out::println);
    }

    @Bean
    RouterFunction<?> routes(CustomerRepository repository) {
        return
                route(GET("/customers"), r -> ok().body(repository.findAll(), Customer.class))
                        .andRoute(GET("/customers/{id}"), r -> ok().body(repository.findById(r.pathVariable("id")), Customer.class))
                        .andRoute(GET("/delay"), r -> ok().body(Flux.just("Hello,world!").delayElements(Duration.ofSeconds(10)), String.class));
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}

interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {
    @Id
    private String id;
    private String name;
}
