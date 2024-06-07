# Cream_JPA
처음 진행한 개인 사이드 프로젝트입니다.

쇼핑몰 사이트인 'Kream'을 모방한 공부용 프로젝트입니다.

'Kream'의 경매시스템과 비슷한 입찰 시스템에 흥미를 느껴 진행하게되었습니다.

JPA 공부와 SpringSecurity 공부를 위해 만들었습니다.


## 개발기간
2024/01/04 ~ 2024/02/20


## 개발환경
+ Java
+ JDK17
+ Thymeleaf
+ __DB__ : MariaDB 10.1
+ __IDE__ : InteliJ
+ __Framework__ : Springboot 3.2
+ __ORM__ : Spring Data JPA
+ Open API 및 라이브러리
  + 카카오 로그인 API
  + QueryDSL
  + Thumbnail
  + Spring mail
  + SpringSecurity
  + coolSMS


## 구현기능
+ SpringSecurity 회원가입(비밀번호 암호화) 및 로그인
+ 카카오 로그인
+ SMS 본인인증
+ 동일상품에 대한 구매입찰 및 판매입찰 -> 가격이 상응하면 자동 체결(@Scheduled)
+ 상품 이미지 업로드 및 썸네일 표시
+ 최근 거래가 표시 및 시세 차트화
+ 헤더 모달 검색창
+ 마이페이지 구매/판매내역 확인 및 기간별 조회
