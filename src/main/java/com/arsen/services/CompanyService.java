package com.arsen.services;

import com.arsen.exceptions.ApiException;
import com.arsen.models.Company;
import com.arsen.repositories.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(
                () -> new ApiException("Company with this id was not found!"));
    }

    public Long saveCompany(Company company) {
        return companyRepository.save(company).getId();
    }

    public Long deleteCompany(Long id) {
        companyRepository.deleteById(id);
        return id;
    }

    public Long updateCompany(Long id, Company updatedCompany) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ApiException("Company with this id was not found!"));

        company.setName(updatedCompany.getName());
        company.setBalance(updatedCompany.getBalance());

        return companyRepository.save(company).getId();
    }

}
