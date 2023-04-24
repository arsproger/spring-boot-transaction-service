package com.arsen.controllers;

import com.arsen.dtos.ProductDto;
import com.arsen.mappers.ProductMapper;
import com.arsen.models.Product;
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
        List<Product> products = productService.getAllProducts();
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return productMapper.toDto(product);
    }

    @GetMapping("/company/{companyId}")
    public List<ProductDto> getProductsByCompanyId(@PathVariable Long companyId) {
        List<Product> products = productService.getProductsByCompanyId(companyId);
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}")
    public List<ProductDto> getProductsByUserId(@PathVariable Long userId) {
        List<Product> products = productService.getProductsByUserId(userId);
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}")
    public ProductDto createProduct(@PathVariable("id") Long companyId, @RequestBody ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product createdProduct = productService.createProduct(companyId, product);
        return productMapper.toDto(createdProduct);
    }

    @PutMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product updatedProduct = productService.updateProduct(product);
        return productMapper.toDto(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
