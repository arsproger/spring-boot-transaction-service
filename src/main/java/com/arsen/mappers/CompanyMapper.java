package com.arsen.mappers;

import com.arsen.dtos.CompanyDto;
import com.arsen.models.Company;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyDto toDto(Company company);

    Company toEntity(CompanyDto companyDto);
}