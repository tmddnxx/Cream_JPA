package com.example.cream_jpa.kream.controller;

import com.example.cream_jpa.kream.dto.ProductDTO;
import com.example.cream_jpa.kream.dto.Purchase_bidDTO;
import com.example.cream_jpa.kream.dto.Sales_bidDTO;
import com.example.cream_jpa.kream.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping({"/kream", "/"})
public class KreamController {

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

    @PostMapping("/register")
    public String registerPost(ProductDTO productDTO){
        productService.register(productDTO);

        return "redirect:/kream";
    }

    @GetMapping("/view")
    public void view(Long pno, Model model){
        Optional<ProductDTO> productDTO = productService.getOne(pno); // Optional에 담김
        List<ProductDTO> quoteList = productService.getQuote(pno); // 시세보기
        List<Purchase_bidDTO> purPriceList = productService.recentPurchasePrices(pno);
        List<Sales_bidDTO> salesPriceList = productService.recentSalesPrices(pno);

        model.addAttribute("salesPriceList", salesPriceList);
        model.addAttribute("purPriceList", purPriceList);
        model.addAttribute("quoteList", quoteList);
        model.addAttribute("productDTO", productDTO.get()); // 실제 dto을 전달
    }

    @GetMapping("/modify")
    public void modify(Long pno, Model model){
        Optional<ProductDTO> productDTO = productService.getOne(pno); // Optional에 담김

        model.addAttribute("productDTO", productDTO.get()); // 실제 dto을 전달
    }
    @PostMapping("/modify")
    public String modifyPost(ProductDTO productDTO){
        log.info("ProductDTO" + productDTO);
        productService.modifyOne(productDTO);

        return "redirect:/kream";
    }
    @GetMapping("/remove")
    public String remove(Long pno){
        productService.removeOne(pno);

        return "redirect:/kream";
    }
}
