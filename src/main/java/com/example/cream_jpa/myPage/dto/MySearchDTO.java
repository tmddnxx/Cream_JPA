package com.example.cream_jpa.myPage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class MySearchDTO {

    private LocalDate searchToDate = LocalDate.now().minusMonths(6); // 이전날짜
    private LocalDate searchFromDate = LocalDate.now(); // 다음날짜
    private String type = "all"; // 전체,종료,진행

}
