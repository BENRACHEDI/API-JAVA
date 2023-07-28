package com.gidoc.pdf;
import jakarta.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gidoc.pdf.service.FilesStorageService;

@SpringBootApplication
public class MyApplication implements CommandLineRunner {
  @Resource
  FilesStorageService storageService;

  public static void main(String[] args) {
    SpringApplication.run(MyApplication.class, args);
  }

  @Override
  public void run(String... arg) throws Exception {
//    storageService.deleteAll();
    storageService.init();
  }
}
