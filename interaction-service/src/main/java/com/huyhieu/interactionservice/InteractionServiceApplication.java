package com.huyhieu.interactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(
    scanBasePackages = {"com.huyhieu.interactionservice", "com.huyhieu.common"},
    exclude = {
      DataSourceAutoConfiguration.class,
      JpaRepositoriesAutoConfiguration.class,
      HibernateJpaAutoConfiguration.class
    })
@EnableMongoAuditing
public class InteractionServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(InteractionServiceApplication.class, args);
  }
}
