package com.huyhieu.postservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(
    scanBasePackages = {"com.huyhieu.postservice", "com.huyhieu.common"},
    exclude = { // <-- 2. Thêm khối 'exclude' này
      DataSourceAutoConfiguration.class,
      JpaRepositoriesAutoConfiguration.class,
      HibernateJpaAutoConfiguration.class
    })
@EnableMongoAuditing
public class PostServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PostServiceApplication.class, args);
  }
}
