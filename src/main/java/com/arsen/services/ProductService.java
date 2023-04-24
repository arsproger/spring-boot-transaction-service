package com.arsen.services;

import com.arsen.models.Company;
import com.arsen.models.Product;
import com.arsen.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CompanyService companyService;

    public ProductService(ProductRepository productRepository, CompanyService companyService) {
        this.productRepository = productRepository;
        this.companyService = companyService;
    }

    public Product createProduct(Long companyId, Product product) {
        Company company = companyService.getCompanyById(companyId);
        product.setCompany(company);
        return productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> getProductsByCompanyId(Long companyId) {
        return productRepository.findByCompanyId(companyId);
    }

    public List<Product> getProductsByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
