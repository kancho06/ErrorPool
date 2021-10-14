package com.sparta.errorpool.util;

import com.sparta.errorpool.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {
    @Value("${image.upload.directory}")
    private String uploadDir;

    public Path saveFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        byte[] data = multipartFile.getBytes();
        FileOutputStream fos = new FileOutputStream("/home/ubuntu/server/image" + File.separator + StringUtils.cleanPath(fileName));
        fos.write(data);
        fos.close();
        Path copyOfLocation = Paths.get("/home/ubuntu/server/image" + File.separator + StringUtils.cleanPath(fileName));
//        try {
//            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new FileStorageException("파일을 저장할 수 없습니다. : " + fileName);
//        }
        return copyOfLocation;
    }
}
