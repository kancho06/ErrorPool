package com.sparta.errorpool.util;

import com.sparta.errorpool.exception.FileStorageException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {
    @Value("${image.upload.directory}")
    private String uploadDir;

    public Path saveFile(MultipartFile multipartFile) {
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String fileName =
                UUID.randomUUID().toString().substring(19) +
                        System.currentTimeMillis() +
                        "." +
                        extension;
        try {
            byte[] data = multipartFile.getBytes();
            FileOutputStream fos = new FileOutputStream(uploadDir + File.separator + StringUtils.cleanPath(fileName));
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("파일을 저장할 수 없습니다. : " + fileName);
        }

        return Paths.get(uploadDir + File.separator + StringUtils.cleanPath(fileName));
    }
}
