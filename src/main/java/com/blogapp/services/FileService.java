package com.blogapp.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {
    String uploadImageToCloud(MultipartFile file);
}

