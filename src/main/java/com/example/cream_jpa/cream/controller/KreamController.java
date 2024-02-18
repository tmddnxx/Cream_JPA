package com.example.cream_jpa.cream.controller;

import com.example.cream_jpa.cream.dto.ProductDTO;
import com.example.cream_jpa.cream.dto.Purchase_bidDTO;
import com.example.cream_jpa.cream.dto.Sales_bidDTO;
import com.example.cream_jpa.cream.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping({"/kream", "/"})
public class KreamController {

    @Value("${com.example.tempUpload.path}")
    private String tempPath;

    @Value("${com.example.productUpload.path}")
    private String productPath;

    private final ProductService productService;

    @GetMapping("")
    public String kream(String keyword, Pageable pageable, Model model){
        pageable = PageRequest.of(Math.max(0, pageable.getPageNumber() -1), 21);
        Page<ProductDTO> productList = productService.getAllProduct(keyword, pageable);

        model.addAttribute("keyword", keyword);
        model.addAttribute("productList", productList);
        return "kream/kream";
    }

    @GetMapping("/register")
    public String register(){

        return "kream/register";
    }
    @Transactional
    @PostMapping("/register")
    public String registerPost(ProductDTO productDTO) throws IOException {

        for (int i=0; i<productDTO.getProductImg().size(); i++){
            String productImg = productDTO.getProductImg().get(i);
            moveToProductImg(productImg);
        }
        productService.register(productDTO);

        return "redirect:/kream";
    }

    @GetMapping("/view")
    public void view(Long pno, Model model){
        Optional<ProductDTO> productDTO = productService.getOne(pno); // Optional에 담김
        List<ProductDTO> quoteList = productService.getQuote(pno); // 시세보기
        List<Purchase_bidDTO> purPriceList = productService.recentPurchasePrices(pno);
        List<Sales_bidDTO> salesPriceList = productService.recentSalesPrices(pno);


        model.addAttribute("imgList", productDTO.get().getProductImg());
        model.addAttribute("salesPriceList", salesPriceList);
        model.addAttribute("purPriceList", purPriceList);
        model.addAttribute("quoteList", quoteList);
        model.addAttribute("productDTO", productDTO.get()); // 실제 dto을 전달
    }

    @GetMapping("/modify")
    public void modify(Long pno, Model model){
        Optional<ProductDTO> productDTO = productService.getOne(pno); // Optional에 담김

        model.addAttribute("imgList", productDTO.get().getProductImg());
        model.addAttribute("productDTO", productDTO.get()); // 실제 dto을 전달
    }
    @Transactional
    @PostMapping("/modify")
    public String modifyPost(ProductDTO productDTO) throws IOException {
        log.info("ProductDTO" + productDTO);

        for (int i=0; i<productDTO.getProductImg().size(); i++){
            String productImg = productDTO.getProductImg().get(i);
            moveToProductImg(productImg);
        }

        productService.modifyOne(productDTO);

        return "redirect:/kream";
    }
    @GetMapping("/remove")
    public String remove(Long pno){
        productService.removeOne(pno);

        return "redirect:/kream";
    }


    // 상품 등록 시 temp에 있는 이미지 product로 옮기기
    private void moveToProductImg(String fileName) throws IOException {
        Path tempImgPath = Paths.get(tempPath, fileName);
        Path sTempImgPath = Paths.get(tempPath, "s_"+fileName);
        Path productImgPath = Paths.get(productPath, fileName);
        Path sProductImgPath = Paths.get(productPath, "s_"+fileName);

        // tempPath에 파일이 존재하는지 확인
        if (Files.exists(tempImgPath) && Files.exists(sTempImgPath)) {
            // tempPath에 파일이 존재하면 이동
            Files.move(tempImgPath, productImgPath, StandardCopyOption.REPLACE_EXISTING); // => 동일한 이름있으면 덮어쓰기
            Files.move(sTempImgPath, sProductImgPath, StandardCopyOption.REPLACE_EXISTING);
        } else {
            // tempPath에 파일이 존재하지 않으면 처리할 로직 추가
            log.info("해당 파일이 tempPath에 존재하지 않습니다.");
        }

    }
}
