package com.pruebavermont.app.service;


import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.pruebavermont.app.entity.User;

public interface UserService {
	
	public Iterable<User> findAll();	
	public User save(User user);
	public void copyFile(MultipartFile uploadfile);
	public File getFileInfoUsers();

}
