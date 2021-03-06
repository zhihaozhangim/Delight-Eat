package com.zhihao.sell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

// Entry Point, Auto configuration
// When starting, get the value determined by EnableAutoConfiguration from
// META-INF/spring.factories, and inject them into container. Then, auto-configuration
// started
@SpringBootApplication
@EnableCaching  // enabling annotation based caching
public class SellApplication {

  public static void main(String[] args) {
    SpringApplication.run(SellApplication.class, args);
  }
}
