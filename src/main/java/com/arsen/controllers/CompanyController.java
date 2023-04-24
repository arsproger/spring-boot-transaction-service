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
    public Long createCompany(@RequestBody CompanyDto companyDto) {
        return companyService.saveCompany(companyMapper.toEntity(companyDto));
    }

    @PutMapping("/{id}")
    public Long updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDto) {
        return companyService.updateCompany(id, companyMapper.toEntity(companyDto));
    }

    @DeleteMapping("/{id}")
    public Long deleteCompanyById(@PathVariable Long id) {
        return companyService.deleteCompany(id);
    }

}