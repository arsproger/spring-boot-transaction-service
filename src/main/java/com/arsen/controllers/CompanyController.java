package com.arsen.controllers;

import com.arsen.dtos.CompanyDto;
import com.arsen.mappers.CompanyMapper;
import com.arsen.models.Company;
import com.arsen.services.CompanyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @GetMapping
    public List<CompanyDto> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return companies.stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CompanyDto getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        return companyMapper.toDto(company);
    }

    @PostMapping
    public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
        Company company = companyMapper.toEntity(companyDto);
        company = companyService.saveCompany(company);
        return companyMapper.toDto(company);
    }

    @PutMapping("/{id}")
    public CompanyDto updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDto) {
        Company company = companyMapper.toEntity(companyDto);
        company.setId(id);
        company = companyService.saveCompany(company);
        return companyMapper.toDto(company);
    }

    @DeleteMapping("/{id}")
    public void deleteCompanyById(@PathVariable Long id) {
        companyService.deleteCompany(id);
    }

}