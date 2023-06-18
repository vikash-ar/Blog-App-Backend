package com.blogapp.services.impl;

import com.blogapp.services.FileService;
import com.cloudinary.Cloudinary;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;


@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private Cloudinary cloudinary;

    @SneakyThrows
    @Override
    public String uploadImageToCloud(MultipartFile file) {
        log.info("--------FileServiceImpl:uploadImageToCloud::- uploading image to cloud-----");
        return cloudinary.uploader()
                .upload(file.getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }
}
