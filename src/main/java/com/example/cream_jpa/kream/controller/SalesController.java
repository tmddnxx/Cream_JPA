package com.example.cream_jpa.kream.controller;

import com.example.cream_jpa.kream.dto.ProductDTO;
import com.example.cream_jpa.kream.dto.Sales_bidDTO;
import com.example.cream_jpa.kream.service.product.ProductService;
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
@RequestMapping("/sales")
@RequiredArgsConstructor
@Log4j2
public class SalesController {

    private final ProductService productService;
    private final SalesBidService salesBidService;

    @GetMapping("")
    public String Sales(Long pno, Model model){
        Optional<ProductDTO> productDTO = productService.getOne(pno);

        model.addAttribute("productDTO", productDTO.get());

        return "kream/sales";
    }

    @PostMapping("/add")
    public String add(Sales_bidDTO sales_bidDTO){

        salesBidService.addSales(sales_bidDTO);

        return "redirect:/kream/view?pno="+sales_bidDTO.getPno();
    }

}
