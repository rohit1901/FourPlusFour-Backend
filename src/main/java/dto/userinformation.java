package dto;

import java.sql.Blob;

public class userinformation {

	private String userid;
	private int age;
	private String general_interests;
	private String information;
	private String name;
	private Blob picture;
	private String professional_interests;
	private Blob userCredential;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGeneral_interests() {
		return general_interests;
	}

	public void setGeneral_interests(String general_interests) {
		this.general_interests = general_interests;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Blob getPicture() {
		return picture;
	}

	public void setPicture(Blob picture) {
		this.picture = picture;
	}

	public String getProfessional_interests() {
		return professional_interests;
	}

	public void setProfessional_interests(String professional_interests) {
		this.professional_interests = professional_interests;
	}

	public Blob getUserCredential() {
		return userCredential;
	}

	public void setUserCredential(Blob userCredential) {
		this.userCredential = userCredential;
	}
}
