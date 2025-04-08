package com.talenttap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class IndustryType {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="industry_type_id")
	private int industryTypeId;
	
	@Column(name="industry_type")
	private String industryType;
}
