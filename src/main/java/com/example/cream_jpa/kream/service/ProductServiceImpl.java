package com.example.cream_jpa.kream.service;

import com.example.cream_jpa.kream.dto.ProductDTO;
import com.example.cream_jpa.kream.entity.Product;
import com.example.cream_jpa.kream.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;


    @Override
    public void register(ProductDTO productDTO) { // 상품등록
        Product product = productDTO.toEntity();
        
        productRepository.save(product);
    }

    @Override
    public Page<ProductDTO> getAllProduct(Pageable pageable) {
        // 정렬 기준을 'pno' 기준으로 내림차순으로 설정합니다.
        Sort sort = Sort.by("pno").descending();

        // 페이지 요청 정보(pageable)에 정렬을 적용하여 새로운 페이지 요청 정보를 생성합니다.
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // 실제로 데이터베이스에서 페이징 및 정렬된 데이터를 조회합니다.
        Page<Product> productPage = productRepository.findAll(pageable);

        // 조회된 엔터티 목록을 ProductDTO로 변환합니다.
        // getContent - Page에 포함된 엔티티목록가져오는메서드
        // stream - 순차 및 병렬처리
        // map - ProductDTO의 toDTO메서드를 참조해서 각각의 엔티티를 DTO로 변환함
        // collect - 스트림의 각 요소를 리스트로 수집
        //Collectors.toList - 리스트로 변환
        List<ProductDTO> productDTOList = productPage.getContent().stream()
                .map(ProductDTO::toDTO)
                .collect(Collectors.toList());

        // ProductDTO 목록과 페이지 정보를 사용하여 새로운 Page<ProductDTO>를 생성합니다.
        // PageImpl은 Spring Data에서 제공하는 Page 구현체 중 하나입니다.
        // 생성자의 인자로는 변환된 목록, 페이지 요청 정보, 총 엔터티 개수가 필요합니다.
        return new PageImpl<>(productDTOList, pageable, productPage.getTotalElements());
    }

    @Override
    public Optional<ProductDTO> getOne(Long pno) { // 상품상세
        Optional<Product> product = productRepository.findById(pno);

        return product.map(ProductDTO::toDTO);
    }

    @Override
    public void modifyOne(ProductDTO productDTO) {
        // 주어진 ID로 엔터티 조회
        Optional<Product> optionalProduct = productRepository.findById(productDTO.getPno());

        optionalProduct.ifPresent(product ->{
            product.setProductName(productDTO.getProductName());
            product.setPrice(productDTO.getPrice());
            productRepository.save(product);
        });
    }

    @Override
    public void removeOne(Long pno) {
        productRepository.deleteById(pno);
    }
}
