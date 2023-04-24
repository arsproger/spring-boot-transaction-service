package com.arsen.controllers;

import com.arsen.dtos.ProductDto;
import com.arsen.mappers.ProductMapper;
import com.arsen.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return productMapper.toDto(productService.getProductById(id));
    }

    @GetMapping("/company/{companyId}")
    public List<ProductDto> getProductsByCompanyId(@PathVariable Long companyId) {
        return productService.getProductsByCompanyId(companyId).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}")
    public List<ProductDto> getProductsByUserId(@PathVariable Long userId) {
        return productService.getProductsByUserId(userId).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}")
    public Long createProduct(@PathVariable("id") Long companyId, @RequestBody ProductDto productDto) {
        return productService.createProduct(companyId, productMapper.toEntity(productDto));
    }

    @PutMapping("/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productMapper.toEntity(productDto));
    }

    @DeleteMapping("/{id}")
    public Long deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

}
