package com.anurag.aggregator;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The driver class of this project
 */
@SpringBootApplication
//@EnableAutoConfiguration
@PropertySources(value = {@PropertySource("classpath:application.properties")})
@Import(WebFluxAutoConfiguration.WebFluxConfig.class)
public class Application {

//    @Autowired
//    public Application(Environment environment) {
//        this.env = environment;
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }



    /*

        Various bean definitions
     */

    /**
     * Reactive {@link WebClient} bean declaration, hope its good as they say
     *
     * @return a new instance of {@link WebClient}
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector())
                .build();
    }

   /* @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(String.format("mongodb://%s:%s",
                env.getProperty("spring.data.mongodb.host", "localhost"),
                env.getProperty("spring.data.mongodb.port", "27017"))
        );
    }*/

    /*@Bean
    @Autowired
    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
        return new ReactiveMongoTemplate(mongoClient, env.getProperty("spring.data.mongodb.database", "aggregator-db"));
    }*/

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }

    /*

    Various member fields declarations
     */
//    private final Environment env;
}
