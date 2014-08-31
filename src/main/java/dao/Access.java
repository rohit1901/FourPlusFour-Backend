package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dto.Credentials;

public class Access 
{
	/**
	 * Gets all user credentials from the database.
	 * 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Credentials> getCredential(Connection con)
			throws SQLException {
		ArrayList<Credentials> credentialList = new ArrayList<Credentials>();
		PreparedStatement stmt = con
				.prepareStatement("SELECT username,password FROM credentials");
		ResultSet rs = stmt.executeQuery();
		try 
		{
			while (rs.next()) 
			{
				Credentials credentialObj = new Credentials();
				credentialObj.setUsername(rs.getString("username"));
				credentialObj.setPassword(rs.getString("password"));
				credentialObj.setType(rs.getString("type"));
				credentialList.add(credentialObj);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			con.close();
		}
		return credentialList;

	}
}