package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dto.Advertisements;
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
				.prepareStatement("SELECT * FROM heroku_4265740aecd0c5d.credentials");
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
	
	
	/**
	 * Gets all advertisements for an advertiser from the database.
	 * 
	 * @param email
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Advertisements> getAdvertisements(String email, Connection con)
			throws SQLException {
		ArrayList<Advertisements> advertisementsList = new ArrayList<Advertisements>();
		Advertisements advertisementObj = new Advertisements();
		
		System.out.println("before executing count query. value of email is----" + email);
		PreparedStatement stmt = con.prepareStatement("SELECT count(*) FROM heroku_4265740aecd0c5d.advertisements where email='" + email + "'");
		/*PreparedStatement stmt = con
				.prepareStatement("SELECT * FROM heroku_4265740aecd0c5d.advertisements where email='" + email + "'");*/
		ResultSet rs = stmt.executeQuery();
		try 
		{
			while(rs.next())
			{
				if(rs.getInt("count(*)") != 0)
				{
					System.out.println("count is not zero.");
					PreparedStatement stmtNew = con.prepareStatement("SELECT * FROM heroku_4265740aecd0c5d.advertisements where email='" + email + "'");
					ResultSet rsNew = stmtNew.executeQuery();
					while (rsNew.next()) 
					{
						advertisementObj.setEmail(rsNew.getString("email"));
						advertisementObj.setDate(rsNew.getString("date"));
						advertisementObj.setPlan(rsNew.getString("plan"));
						advertisementObj.setProduct(rsNew.getString("product"));
						advertisementObj.setUsedAt(rsNew.getString("usedAt"));
						advertisementsList.add(advertisementObj);
					}
				}
			
				else
				{
					advertisementObj.setEmail("empty");
					advertisementObj.setDate("empty");
					advertisementObj.setPlan(rs.getString("empty"));
					advertisementObj.setProduct(rs.getString("empty"));
					advertisementObj.setUsedAt(rs.getString("empty"));
					advertisementsList.add(advertisementObj);
				}
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
		return advertisementsList;

	}
	
	public int getSponsorAmount(String username, Connection con)
	{
		int amount = 0;
		PreparedStatement stmt;
		try 
		{
			stmt = con.prepareStatement("SELECT amount FROM heroku_4265740aecd0c5d.sponsor where email='" + username + "'");
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				amount = rs.getInt("amount");
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return amount;
	}
	
	public int countName(String email, String type, Connection con)
	{
		int countName = 0;
		PreparedStatement stmt;
		try 
		{
			stmt = con.prepareStatement("SELECT count(name) FROM heroku_4265740aecd0c5d." + type + "where email='" + email + "'");
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				countName = rs.getInt("count(name)");
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return countName;
	}
}