package com.huyhieu.newsfeedservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
    scanBasePackages = {"com.huyhieu.newsfeedservice", "com.huyhieu.common"},
    exclude = {
      DataSourceAutoConfiguration.class,
      JpaRepositoriesAutoConfiguration.class,
      HibernateJpaAutoConfiguration.class
    })
@EnableFeignClients
public class NewsFeedServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(NewsFeedServiceApplication.class, args);
  }
}
