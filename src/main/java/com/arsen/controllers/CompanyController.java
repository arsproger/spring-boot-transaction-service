package com.arsen.controllers;

import com.arsen.dtos.CompanyDto;
import com.arsen.mappers.CompanyMapper;
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
        return companyService.getAllCompanies().stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CompanyDto getCompanyById(@PathVariable Long id) {
        return companyMapper.toDto(companyService.getCompanyById(id));
    }

    @PostMapping
    public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
        return companyMapper.toDto(companyService.saveCompany(companyMapper.toEntity(companyDto)));
    }

    @PutMapping("/{id}")
    public CompanyDto updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDto) {
        return companyMapper.toDto(companyService.updateCompany(id, companyMapper.toEntity(companyDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteCompanyById(@PathVariable Long id) {
        companyService.deleteCompany(id);
    }

}