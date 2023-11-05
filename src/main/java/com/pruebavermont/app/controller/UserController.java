package com.pruebavermont.app.controller;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pruebavermont.app.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/copy")
	public ResponseEntity <?> copyFile (@RequestParam("file") MultipartFile file)	{
		
		if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please, select a file to copy");
        }	
		userService.copyFile(file);		
		return ResponseEntity.ok("The file is being copied asynchronously.");
	}
	@GetMapping("/users")
	public ResponseEntity <?> getFileInfoUser(){
		
		File file = userService.getFileInfoUsers();			
			 if (file.exists()) {
		            HttpHeaders headers = new HttpHeaders();
		            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pruebavermont.txt");
		            FileSystemResource resource = new FileSystemResource(file);
		            return ResponseEntity.ok()
		                .headers(headers)
		                .body(resource);
		        } else {
		            return ResponseEntity.notFound().build();
		        }
	
	    }
}
