package com.talenttap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class SalaryRange {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "salary_range_id")
	private int salaryRangeId;
	
	@Column(name = "min_range" , nullable = false)
	private double min_range;
	
	@Column(name = "max_range" , nullable = false)
	private double max_range;

	public int getSalaryRangeId() {
		return salaryRangeId;
	}

	public void setSalaryRangeId(int salaryRangeId) {
		this.salaryRangeId = salaryRangeId;
	}

	public double getMin_range() {
		return min_range;
	}

	public void setMin_range(double min_range) {
		this.min_range = min_range;
	}

	public double getMax_range() {
		return max_range;
	}

	public void setMax_range(double max_range) {
		this.max_range = max_range;
	}

}
