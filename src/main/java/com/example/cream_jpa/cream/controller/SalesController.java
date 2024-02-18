package com.example.cream_jpa.cream.controller;

import com.example.cream_jpa.cream.dto.ProductDTO;
import com.example.cream_jpa.cream.dto.Sales_bidDTO;
import com.example.cream_jpa.cream.service.product.ProductService;
import com.example.cream_jpa.cream.service.salesBid.SalesBidService;
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
    public String Sales(Long pno, Model model, String tab){
        Optional<ProductDTO> productDTO = productService.getOne(pno);
        if(tab == null){
            tab = "bid";
        }
        model.addAttribute("productDTO", productDTO.get());
        model.addAttribute("tab", tab);
        return "kream/sales";
    }

    @PostMapping("/add") // 판매입찰
    public String add(Sales_bidDTO sales_bidDTO){

        salesBidService.addSales(sales_bidDTO);

        return "redirect:/kream/view?pno="+sales_bidDTO.getPno();
    }

    @PostMapping("/now") // 즉시구매
    public String now(Sales_bidDTO sales_bidDTO){
        salesBidService.saleNow(sales_bidDTO);

        return "redirect:/kream/view?pno="+sales_bidDTO.getPno();
    }

}
