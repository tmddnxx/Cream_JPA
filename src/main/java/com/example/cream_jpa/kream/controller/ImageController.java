package com.example.cream_jpa.kream.controller;

import com.example.cream_jpa.kream.service.product.ProductService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @Autowired
    private ProductService productService;


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
    public List<String> uploadFile(@RequestPart("imageFile") List<MultipartFile> files) throws IOException {

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

    @GetMapping("/tempView/{fileName}") // <img src>로 get맵핑 잡힘 // 등록할때 썸네일 보기
    public Resource tempView(@PathVariable String fileName) throws MalformedURLException {
        log.info("들어옴"+fileName);

        return new UrlResource("file:" + tempPath+"\\s_"+fileName); // 썸네일을 반환
    }
    @GetMapping("/productImg/{fileName}") // list, view에서 썸네일 표시
    public Resource productView(@PathVariable String fileName) throws MalformedURLException {
        log.info("productView 들어옴");

        return new UrlResource("file:" + productPath+"\\s_"+fileName);
    }

    // temp에서 이미지 삭제
    @DeleteMapping("/removeTemp/{fileName}")
    public ResponseEntity<String> deleteTemp(@PathVariable String fileName){
        File imageFile;
        File thumbFile;
        log.info("삭제 들어옴");
        try{
            imageFile = new File(tempPath, fileName);
            thumbFile = new File(tempPath, "s_"+fileName);
            log.info("이미지파일"+imageFile);
            log.info("썸네일파일"+thumbFile);
            boolean thumbDeleted = thumbFile.delete();
            boolean imgDeleted = imageFile.delete();
            if(imgDeleted && thumbDeleted){
                log.info("삭제완료");
                return ResponseEntity.ok("이미지 삭제 완료");
            }else {
                log.info("삭제실페");
                return ResponseEntity.status(500).body("이미지 삭제 실패");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("이미지 삭제 에러");
        }
    }

    // product에서 이미지 삭제
    @DeleteMapping("/removeProduct/{fileName}/{pno}")
    public ResponseEntity<String> deleteProduct(@PathVariable String fileName, @PathVariable Long pno){
        File productImgFile;
        File thumbFile;
        log.info("삭제 들어옴");
        try{
            productImgFile = new File(productPath, fileName);
            thumbFile = new File(productPath, "s_"+fileName);
            log.info("이미지파일"+productImgFile);
            log.info("썸네일파일"+thumbFile);
            boolean thumbDeleted = thumbFile.delete();
            boolean imgDeleted = productImgFile.delete();
            productService.updateProductImg(fileName, pno);
            if(imgDeleted && thumbDeleted){
                log.info("삭제완료");
                return ResponseEntity.ok("이미지 삭제 완료");
            }else {
                log.info("삭제실페");
                return ResponseEntity.status(500).body("이미지 삭제 실패");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("이미지 삭제 에러");
        }
    }


}
