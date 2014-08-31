package dto;

import java.util.ArrayList;
import java.util.List;

public class Advertiser 
{
	String name;
	String company;
	String email;
	List<String> products = new ArrayList<>();
	String plan;
	String bio;
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public String getCompany() 
	{
		return company;
	}
	public void setCompany(String company) 
	{
		this.company = company;
	}
	public String getEmail() 
	{
		return email;
	}
	public void setEmail(String email) 
	{
		this.email = email;
	}
	public List<String> getProducts() 
	{
		return products;
	}
	public void setProducts(List<String> products) 
	{
		this.products = products;
	}
	public String getPlan() 
	{
		return plan;
	}
	public void setPlan(String plan) 
	{
		this.plan = plan;
	}
	public String getBio() 
	{
		return bio;
	}
	public void setBio(String bio) 
	{
		this.bio = bio;
	}

}
