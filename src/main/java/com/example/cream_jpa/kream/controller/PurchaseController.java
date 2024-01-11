package com.example.cream_jpa.kream.controller;

import com.example.cream_jpa.kream.dto.ProductDTO;
import com.example.cream_jpa.kream.dto.Purchase_bidDTO;
import com.example.cream_jpa.kream.dto.Sales_bidDTO;
import com.example.cream_jpa.kream.service.product.ProductService;
import com.example.cream_jpa.kream.service.purchaseBid.PurchaseBidService;
import com.example.cream_jpa.kream.service.salesBid.SalesBidService;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
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
    public String Sales(Long pno, Model model){
        Optional<ProductDTO> productDTO = productService.getOne(pno);

        model.addAttribute("productDTO", productDTO.get());

        return "kream/purchase";
    }

    @PostMapping("/add")
    public String add(Purchase_bidDTO purchase_bidDTO){

        purchaseBidService.addPurchase(purchase_bidDTO);

        return "redirect:/kream/view?pno="+purchase_bidDTO.getPno();
    }

}
