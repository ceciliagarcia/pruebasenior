package com.pruebavermont.app.service;


import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pruebavermont.app.entity.User;
import com.pruebavermont.app.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	private static final String COPY_DIR = "copies";
	
	@Override
	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}



	@Override
	@Transactional(readOnly = true)
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}



	@Override
	/* Metodo que realiza la copia de nuestro archivo
	 * @param uploadfile el archivo recibido en la entrada
	 */
	public void copyFile(MultipartFile uploadfile) {
		File uploadDirectory = new File(COPY_DIR);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        
        String fileName = uploadfile.getOriginalFilename();
        File localFile = new File(COPY_DIR, fileName);
        
        uploadFileAsync(uploadfile, localFile);
	}



	private void uploadFileAsync(MultipartFile sourceFile, File destinationFile) {
	     ExecutorService executorService = Executors.newCachedThreadPool();
	        CompletableFuture.runAsync(() -> {
	            try {
	                Files.copy(sourceFile.getInputStream(), destinationFile.toPath());
	            } catch (IOException e) {
	                e.printStackTrace();
	                throw new RuntimeException("Error al copiar el archivo: " + e.getMessage());
	            }
	        }, executorService);
		
	}


	@Override
	/*
	 * Metodo donde se inicializa los datos de nuestra base de datos y a su vez nos devuelve 
	 * los datos obtenidos en un archivo txt
	 */
	public File getFileInfoUsers() {
		
	     List<User> users = Stream.of(
	             new User("Thomson, Elias", "555-8596", "Diamond St. 4G3 NY"),
	             new User("Simond, Ella", "415-9687", "Fleed St. 45B, 56 BR-NY"),
	             new User("Clifford, Thomas", "416-69883", "Meet town, 45-FL")
	         )
	         .collect(Collectors.toList());
	     
	     for (User user : users) {	    	
	            save(user); 
	     }
	          	 
		Iterable<User> infoUsers = findAll();
		String filePath = "pruebaVermont.txt";
		
		File file = new File(filePath);	
		try {
		   
		        BufferedWriter writerTxt = new BufferedWriter(new FileWriter(file));

		        for (User info : infoUsers) {
		            String userText = userToText(info);
		            writerTxt.write(userText);
		            writerTxt.newLine();
		        }
		        writerTxt.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return file;
	}

	 private static String userToText(User user) {

	  return "Fullname: " + user.getFullName() + ", Phone: " + user.getPhone() +  ", Address: " + user.getAddress(); 
	 }
	

}
