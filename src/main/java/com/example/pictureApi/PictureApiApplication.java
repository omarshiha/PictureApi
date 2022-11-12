package com.example.pictureApi;

import com.example.pictureApi.Service.PicturesStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class PictureApiApplication implements CommandLineRunner {

	@Resource
	PicturesStorageService picturesStorageService;

	public static void main(String[] args) {
		SpringApplication.run(PictureApiApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		picturesStorageService.deleteAll();
		picturesStorageService.init();
	}

}
