package com.arsen.services;

import com.arsen.exceptions.ApiException;
import com.arsen.models.Company;
import com.arsen.models.Product;
import com.arsen.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CompanyService companyService;

    public ProductService(ProductRepository productRepository, CompanyService companyService) {
        this.productRepository = productRepository;
        this.companyService = companyService;
    }

    public Long createProduct(Long companyId, Product product) {
        Company company = companyService.getCompanyById(companyId);
        product.setCompany(company);
        if (product.getPrice() == null) product.setPrice(BigDecimal.ZERO);
        return productRepository.save(product).getId();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new ApiException("Product with this id was not found!"));
    }

    public List<Product> getProductsByCompanyId(Long companyId) {
        return productRepository.findByCompanyId(companyId);
    }

    public Long updateProduct(Long id, Product updatedProduct) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ApiException("Product with this id was not found!"));

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setCompany(updatedProduct.getCompany());

        return productRepository.save(updatedProduct).getId();
    }

    public Long deleteProduct(Long productId) {
        productRepository.deleteById(productId);
        return productId;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

}
