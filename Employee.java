package com.srccodes.examples;

public class Employee {
	private int EmployeeID;
	private String First_Name;
	private String Last_Name;
	private String Country;
	private String City;
	private int Salary_in_USD;
	
	public Employee(String EmpDetail) {
		String[] details = EmpDetail.split(" ");
		this.EmployeeID = Integer.valueOf(details[0]);
		this.First_Name = details[1];
		this.Last_Name = details[2];
		this.Country = details[3];
		this.City = details[4];
		this.Salary_in_USD = Integer.valueOf(details[5]);
	}

	public Employee() {
		// TODO Auto-generated constructor stub
	}
	public String primaryKey() {
		return String.valueOf(EmployeeID);
	}
	public Integer Salary_in_USD() {
		return Salary_in_USD;
	}
	public String Details() {
		StringBuilder s = new StringBuilder();
		s.append(EmployeeID + ",'" + First_Name + "','" + Last_Name + "','" +
					Country + "','" + City + "'," + Salary_in_USD);
		
		return s.toString();
	}
	

}
