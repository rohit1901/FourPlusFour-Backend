package com.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.AccessManager;

import org.glassfish.jersey.internal.util.Base64;

import com.google.gson.Gson;

import dao.Database;
import dto.Credentials;

@Path("/credentialService")
public class credentialService 
{

	public static String CHILD = "child"; 
	public static int ZERO = 0; 
	
	private boolean updateFlag = false;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String credentials() 
	{
		String credentials = null;
		ArrayList<Credentials> credentialList = new ArrayList<Credentials>();
		try 
		{
			credentialList = new AccessManager().getCredential();
			Gson gson = new Gson();
			credentials = gson.toJson(credentialList);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return credentials;
	}
	
	/**
	 * Matches credentials with those in the database.
	 * Returns true if it's a successful match otherwise returns
	 * false.
	 * 
	 * @param username
	 * @param password
	 * @param type
	 * @return
	 */
	@GET
	@Path("/matchCredentials")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String matchCredentials(@QueryParam("username") String username,
	        @QueryParam("password") String password,
	        @QueryParam("type") String type) 
	{
		//String credentials = null;
		
		final String TRUE = "true";
		final String FALSE = "false";
		
		//System.out.println(credentials);
		
		ArrayList<Credentials> credentialList = new ArrayList<Credentials>();
		try 
		{
			credentialList = new AccessManager().getCredential();
			//Gson gson = new Gson();
			
			for(Credentials credential : credentialList)
			{
				if((credential.getUsername().equals(username)) && (credential.getPassword().equals(password)) && (credential.getType().equals(type)))
				{
					return TRUE;
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return FALSE;
	}
	
	
	/**
	 * Gets the amount sponsored by a sponsor.
	 * 
	 * @param username
	 * @return
	 */
	@GET
	@Path("/getSponsorAmount")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String getSponsorAmount(@QueryParam("username") String username) 
	{
		int amount = 0;
		
		try 
		{
			amount = new AccessManager().getSponsorAmount(username);
			return ("" + amount + "");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ("" + amount + "");
	}


	/**
	 * Submits values to the student registration table. 
	 * 
	 * @param name
	 * @param age
	 * @param school
	 * @param address
	 * @param email
	 * @param bio
	 * @throws SQLException
	 */
	@POST
	@Path("/registerStudent")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void registerStudent(@FormParam("q1") String name,
			@FormParam("q2") int age,
			@FormParam("q3") String school,
			@FormParam("q4") String address,
			@FormParam("q5") String email,
			@FormParam("q6") String bio)
			throws SQLException 
			{
				PreparedStatement ps = null;
				Connection con = null;
				Database db = new Database();
				/*String hashValue;
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar registrationDateTime = Calendar.getInstance();
												
				Boolean sendRegistrationLinkStatus = false;
				
				hashValue = new String(Base64.encode(email.getBytes()));	
				*/
				
				try 
				{
		
					con = db.getConnection();
					ps = con.prepareStatement(
							"insert into child (name,age,school,address,email,bio,type,testLevel) values (?,?,?,?,?,?,'" + CHILD + "'," + ZERO + ")");
		
					ps.setString(1, name);
					ps.setInt(2, age);
					ps.setString(3, school);
					ps.setString(4, address);
					ps.setString(5, email);
					ps.setString(6, bio);
					
					int result = ps.executeUpdate();
					
					if(result > 0)
					{
						System.out.println("SQL Query Executed successfully. Records inserted----"  + result);
						//sendRegistrationLinkStatus = sendEmail(email, hashValue);
					}

				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					throw new RuntimeException(e);
				} 
				finally 
				{
					con.close();
				}
			}
		
	/**
	 * @param key
	 * @throws SQLException
	 */
	@POST
	@Path("/registerUser")
	@Consumes(MediaType.TEXT_PLAIN)
	public void registerUser(@QueryParam("key") String key) throws SQLException
	{
		Statement ps = null;
		Connection con = null;
		
		PreparedStatement psReg = null;
		Connection conReg = null;
		
		String hashValue = new String(Base64.decode(key.getBytes()));
		
		Database db = new Database();
		try 
		{
			con = db.getConnection();
			ps = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = ps.executeQuery("select * from tempregistration where tempregistration.email = '" + hashValue + "'");

			if (rs.next()) 
			{

				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String homeTown = rs.getString("homeTown");
				Date dateOfBirth = rs.getDate("dateOfBirth");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String bio = rs.getString("bio");
//				System.out.println(rs.toString());

				rs.deleteRow();
				System.out.println("Successfully removed tempRegistration entry from Database.");
				
				conReg = db.getConnection();
				psReg = conReg.prepareStatement(
						"insert into registration (firstName,lastName,homeTown,dateOfBirth,email,password,bio) values (?,?,?,?,?,?,?)");

				psReg.setString(1, firstName);
				psReg.setString(2, lastName);
				psReg.setString(3, homeTown);
				psReg.setDate(4, dateOfBirth);
				psReg.setString(5, email);
				psReg.setString(6, password);
				psReg.setString(7, bio);
				System.out.println(psReg.toString());

				int result = psReg.executeUpdate();
				
				if(result > 0)
				{
					System.out.println("Successfully registered user. \nrecords affected --------------------- "  + result);
				}
			}
			
			
		} 
		catch (Exception e) 
		{
			System.out.println("Failure registering user due to: " + e);
		}
		finally 
		{
			if (con != null && conReg !=null) 
			{
				con.close();
				conReg.close();
			}
			
		}
	}
	
	@POST
	@Path("/tempRegisterUser")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void tempRegisterUser(@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("homeTown") String homeTown,
			@FormParam("dateOfBirth") Date dateOfBirth,
			@FormParam("email") String email,
			@FormParam("password") String password,
			@FormParam("bio") String bio)
			throws SQLException 
			{
				PreparedStatement ps = null;
				Connection con = null;
				
				String hashValue;
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar registrationDateTime = Calendar.getInstance();
												
				Boolean sendRegistrationLinkStatus = false;
				
				hashValue = new String(Base64.encode(email.getBytes()));	
				
				Database db = new Database();
				try 
				{
		
					con = db.getConnection();
					ps = con.prepareStatement(
							"insert into tempRegistration (firstName,lastName,homeTown,dateOfBirth,email,password,bio,hashValue,registrationDateTime) values (?,?,?,?,?,?,?,?,?)");
		
					ps.setString(1, firstName);
					ps.setString(2, lastName);
					ps.setString(3, homeTown);
					ps.setDate(4, dateOfBirth);
					ps.setString(5, email);
					ps.setString(6, password);
					ps.setString(7, bio);
					ps.setString(8, hashValue);
					ps.setString(9, dateFormat.format(registrationDateTime.getTime()));
					int result = ps.executeUpdate();
					
					if(result > 0)
					{
						System.out.println("SQL Query Executed records inserted--------------------- "  + result);
						sendRegistrationLinkStatus = sendEmail(email, hashValue);
					}

				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					throw new RuntimeException(e);
				} 
				finally 
				{
					con.close();
				}
			}

	/**
	 * A simple method to generate random numbers 
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	private int randInt(int min, int max) 
	{

		Random rand = new Random();

	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	
	/**
	 * Sends email from info@tabletribes.com to user's email address for registration
	 * 
	 * @param email
	 * @param hashValue
	 * @return
	 */
	private boolean sendEmail(String email, String hashValue)
	{
		final String username = "noreply@tabletribes.com";
		final String password = "TT1s@wesome";
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.host", "box260.bluehost.com");
		prop.put("mail.smtp.port", "25");
		prop.put("mail.smtp.starttls.enable", "true");
		Session session = Session.getDefaultInstance(prop,
				new javax.mail.Authenticator()
				{
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(username, password);
					}
				});
 
		try 
		{
 
			String link = "http://vast-scrubland-7419.herokuapp.com/credentialService/registerUser?key=" + hashValue;
			
			String body = "Mail Body";
			String htmlBody = "<strong>Hi. Please click on the following link <a href=" + link +">" + link + "</a> to successfully register with TableTribes.</strong>";
			String textBody = "Hi. Please click on the following link <a href=" + link +">" + link + "</a> to successfully register with TableTribes.";
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("info@tabletribes.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject("Verify your Email address and register with TABLETRIBES");
			MailcapCommandMap mc = (MailcapCommandMap) CommandMap
					.getDefaultCommandMap();
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(mc);
			message.setText(htmlBody);
			message.setContent(textBody, "text/html");
			Transport.send(message);
			System.out.println("Done");
			
			return true;
 
		} 
		catch (MessagingException e) 
		{
			System.out.println("Error while sending registration email due to: " + e);
		}
		return false;
	}

}