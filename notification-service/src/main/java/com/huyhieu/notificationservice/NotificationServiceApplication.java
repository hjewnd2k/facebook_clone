package com.huyhieu.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(
    scanBasePackages = {"com.huyhieu.notificationservice", "com.huyhieu.common"},
    exclude = {
      DataSourceAutoConfiguration.class,
      JpaRepositoriesAutoConfiguration.class,
      HibernateJpaAutoConfiguration.class
    })
@EnableMongoAuditing
@EnableFeignClients
public class NotificationServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(NotificationServiceApplication.class, args);
  }
}
