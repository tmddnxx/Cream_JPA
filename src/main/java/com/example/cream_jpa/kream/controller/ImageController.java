package com.example.cream_jpa.kream.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    // temp에 저장하기
    @PostMapping(value = "/upload" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<String> uploadFile(@RequestPart("productImg") List<MultipartFile> files) throws IOException {

        List<String> fileNameList = new ArrayList<>();
        for (MultipartFile multipartFile : files) {

            String originName = multipartFile.getOriginalFilename(); // 실제 파일 이름
            log.info("originName ? " + originName);
            String fileName = UUID.randomUUID() + "_" + originName; // uuid_파일이름

            Path savePath = Paths.get(tempPath, fileName); // temp안에 파일이름 - 이미지 경로 저장
            String contentType = Files.probeContentType(savePath); // 넘어온 파일의 타입 확인
            log.info("contentType? " +contentType);
            if(contentType.startsWith("image")){ // 이미지 파일이면 ?
                multipartFile.transferTo(savePath); // 실제 파일 저장
                File thumbNail = new File(tempPath, "s_"+fileName); // tempPath폴더에 s_로 시작하는 파일
                Thumbnailator.createThumbnail(savePath.toFile(), thumbNail, 100, 100); // Path 객체를 파일로 변경(이미지파일),
                // ,thumbNail = 파일의 경로
                fileNameList.add(fileName);
            }else{
                fileNameList.add("이미지만 등록 할 수 있습니다");
                return fileNameList;
            }
        }
        return fileNameList;
    }
}
