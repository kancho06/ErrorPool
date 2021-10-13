package com.sparta.errorpool.util;

import com.sparta.errorpool.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {
    @Value("${image.upload.directory:${user.home}}")
    private String uploadDir;

    public Path saveFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        Path copyOfLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(fileName));
        try {
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("파일을 저장할 수 없습니다. : " + multipartFile.getOriginalFilename());
        }
        return copyOfLocation;
    }
}
