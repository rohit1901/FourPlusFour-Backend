package model;

import java.sql.Connection;
import java.util.ArrayList;

import dao.Access;
import dao.Database;
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
}