package com.example.cream_jpa.cream.controller;

import com.example.cream_jpa.cream.dto.ProductDTO;
import com.example.cream_jpa.cream.dto.Purchase_bidDTO;
import com.example.cream_jpa.cream.service.product.ProductService;
import com.example.cream_jpa.cream.service.purchaseBid.PurchaseBidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/purchase")
@RequiredArgsConstructor
@Log4j2
public class PurchaseController {

    private final ProductService productService;
    private final PurchaseBidService purchaseBidService;

    @GetMapping("")
    public String Sales(Long pno, Model model, String tab){
        Optional<ProductDTO> productDTO = productService.getOne(pno);

        if(tab == null){
            tab = "bid";
        }

        model.addAttribute("tab", tab);
        model.addAttribute("productDTO", productDTO.get());

        return "kream/purchase";
    }

    @PostMapping("/add")
    public String add(Purchase_bidDTO purchase_bidDTO){
        purchaseBidService.addPurchase(purchase_bidDTO);

        return "redirect:/kream/view?pno="+purchase_bidDTO.getPno();
    }

    @PostMapping("/now")
    public String now(Purchase_bidDTO purchase_bidDTO){

        purchaseBidService.purChaseNow(purchase_bidDTO);

        return "redirect:/kream/view?pno="+purchase_bidDTO.getPno();
    }
}
