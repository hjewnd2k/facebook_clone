package com.huyhieu.socialservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(
    scanBasePackages = {"com.huyhieu.socialservice", "com.huyhieu.common"},
    exclude = { // <-- 2. Thêm khối 'exclude' này
      DataSourceAutoConfiguration.class,
      JpaRepositoriesAutoConfiguration.class,
      HibernateJpaAutoConfiguration.class
    })
public class SocialServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SocialServiceApplication.class, args);
  }
}
