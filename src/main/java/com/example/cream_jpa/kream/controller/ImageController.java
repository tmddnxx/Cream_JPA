package com.example.cream_jpa.kream.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/img")
@Log4j2
public class ImageController {

    @Value("${com.example.tempUpload.path}")
    private String tempPath;

    @Value("${com.example.productUpload.path}")
    private String productPath;


    @PostConstruct
    public void init(){ // 폴더 생성
        File tempFolder = new File(tempPath);
        File productFolder = new File(productPath);

        if(!tempFolder.exists()){
            tempFolder.mkdirs();
        }
        if(!productFolder.exists()){
            productFolder.mkdirs();
        }
        
        // 생성된 폴더의 경로 저장
        tempPath = tempFolder.getAbsolutePath();
        productPath = productFolder.getAbsolutePath();
    }



}
