package model;

import java.sql.Connection;
import java.util.ArrayList;

import dao.Access;
import dao.Database;
import dto.Advertisements;
import dto.Credentials;

public class AccessManager 
{
	public ArrayList<Credentials> getCredential() throws Exception 
	{
		ArrayList<Credentials> credentials = new ArrayList<Credentials>();
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		credentials = access.getCredential(con);
		return credentials;
	}
	
	public int getSponsorAmount(String username) throws Exception
	{
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		int amount = access.getSponsorAmount(username, con);
		return amount;
	}
	
	public int countName(String email, String type) throws Exception
	{
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		int countName = access.countName(email, type, con);
		return countName;
	}
	
	public ArrayList<Advertisements> getAdvertisements(String email) throws Exception
	{
		ArrayList<Advertisements> advertisements = new ArrayList<Advertisements>();
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		advertisements = access.getAdvertisements(email, con);
		return advertisements;
	}
}