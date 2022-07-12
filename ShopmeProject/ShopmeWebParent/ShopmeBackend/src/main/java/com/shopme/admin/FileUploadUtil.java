package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		
		//kiem tra xem duong dan den folder chua file upload co ton tai hay khong
		//Neu khong ton tai thi tao ra duong dan
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try(InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream,  filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch(IOException ex) {
			throw new IOException("Could not save file : " + fileName, ex);
		}
		
	}
	
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
	}
}
