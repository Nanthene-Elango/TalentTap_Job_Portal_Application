package com.talenttap.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.talenttap.DTO.CompanyProfileDTO;
import com.talenttap.entity.Company;
import com.talenttap.entity.Employer;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyProfileDTO toCompanyProfileDTO(Company company);
}

