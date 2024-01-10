package com.example.cream_jpa.kream.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sales")
public class SalesController {

    @GetMapping("")
    public String Sales(){

        return "kream/sales";
    }
}
