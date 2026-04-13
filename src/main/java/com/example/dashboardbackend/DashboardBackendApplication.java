package com.example.dashboardbackend;

import com.example.dashboardbackend.config.CloudflareProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CloudflareProperties.class)
public class DashboardBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(DashboardBackendApplication.class, args);
  }

}
