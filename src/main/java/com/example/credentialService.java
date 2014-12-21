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
import javax.mail.Address;
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
import dto.Advertisements;
import dto.Credentials;

@Path("/credentialService")
public class credentialService 
{

	public static final String CHILD = "child";
	public static final String ADVERTISER = "advertiser";
	public static final String SPONSOR = "sponsor";
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String SPACE = " ";
	
	public static final String ENGLISH = "english";
	public static final String MATHS = "maths";
	public static final String SCIENCE = "science";
	public static final String GK = "gk";
	public static final String GEOGRAPHY = "geography";
	public static final String MUSIC = "music";
	public static final String COMPUTERS = "computers";
	
	public static final String[] SUBJECTS = {ENGLISH,MATHS,SCIENCE,GK,GEOGRAPHY,MUSIC,COMPUTERS};
	
	public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static int ZERO = 0;
	public static int ONE = 1;
	public static int TWO = 2;
	public static int THREE = 3;
	public static int FOUR = 4;
	public static int FIVE = 5;
	
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
		ArrayList<Credentials> credentialList = new ArrayList<Credentials>();
		try 
		{
			credentialList = new AccessManager().getCredential();
			
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
	 * Gets the level entered by a child.
	 * 
	 * @param username
	 * @return
	 */
	@GET
	@Path("/getLevel")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String getLevel(@QueryParam("username") String username) 
	{
		int level = -1;
		
		try 
		{
			level = new AccessManager().getLevel(username);
			return ("" + level + "");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ("" + level + "");
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
	 * Gets the count of names from child/advertiser/sponsor
	 * db for a particular email (username) and returns true if count
	 * is equal to 0 otherwise returns false.
	 * 
	 * @param email
	 * @param type
	 * @return
	 */
	@GET
	@Path("/duplicateUserExists")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String duplicateUserExists(@QueryParam("email") String email, @QueryParam("type") String type) 
	{
		int countEmail = 0;
		try 
		{
			countEmail = new AccessManager().countEmail(email, type);
			
			if(countEmail == 0)
			{
				return FALSE;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return TRUE;
	}
	
	/**
	 * Gets all advertisements for an advertiser.
	 * 
	 * @param email
	 * @return
	 */
	@GET
	@Path("/getAllAdvertisements")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllAdvertisements(@QueryParam("email") String email) 
	{
		String advertisements = null;
		ArrayList<Advertisements> advertisementList = new ArrayList<Advertisements>();
		try 
		{
			advertisementList = new AccessManager().getAdvertisements(email);
			Gson gson = new Gson();
			advertisements = gson.toJson(advertisementList);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		System.out.println("advertisementList" + advertisementList);
		System.out.println("advertisements" + advertisements);
		return advertisements;
	}


    /**
	 * Checks the existance of a record in level table with the passed email and subject.
	 * If such a tuple exists, updates the learnLevel otherwise creates a new tuple.
	 *
	 * @param email
	 * @param level
	 * @param subject
	 * @return
	 */
	@GET
	@Path("/updateLearnLevel")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateLearnLevel(@QueryParam("email") String email, @QueryParam("level") String level, @QueryParam("subject") String subject) throws SQLException 
	{

		int learnLevel = Integer.parseInt(level);
		int countDuplicateSubjectLevelForMember = 0;
		
		PreparedStatement ps = null;
		Connection con = null;
		Database db = new Database();
		
		
		try 
		{
			countDuplicateSubjectLevelForMember = new AccessManager().countLearnLevel(email, subject);
			
			if(countDuplicateSubjectLevelForMember == 0)
			{
				con = db.getConnection();
				ps = con.prepareStatement(
						"insert into level (email,learnLevel,testLevel,subject) values (?,?," + ZERO + ",?)");
				
				ps.setString(1, email);
				ps.setInt(2, learnLevel);
				ps.setString(3, subject);
				
				int result = ps.executeUpdate();
				
				if(result > 0)
				{
					System.out.println("SQL Query Executed successfully. Records inserted in level----"  + result);
					return TRUE;
				}

			}	
			else if(countDuplicateSubjectLevelForMember > 0)
			{
				con = db.getConnection();
				ps = con.prepareStatement(
						"UPDATE level SET learnLevel = " + learnLevel + " where email = '"  + email + "' AND subject = '" + subject + "'");
				
				int result = ps.executeUpdate();
				
				if(result > 0)
				{
					System.out.println("SQL update Executed successfully. Records updated in level----"  + result);
					return TRUE;
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			con.close();
		}
		return FALSE;
	}
	
	
    /**
	 * Checks the existance of a record in level table with the passed email and subject.
	 * If such a tuple exists, updates the testLevel otherwise creates a new tuple.
	 *
	 * @param email
	 * @param level
	 * @param subject
	 * @return
	 */
	@GET
	@Path("/updateTestLevel")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateTestLevel(@QueryParam("email") String email, @QueryParam("level") String level, @QueryParam("subject") String subject) throws SQLException 
	{

		int testLevel = Integer.parseInt(level);
		int countDuplicateSubjectLevelForMember = 0;
		
		PreparedStatement ps = null;
		Connection con = null;
		Database db = new Database();
		
		
		try 
		{
			countDuplicateSubjectLevelForMember = new AccessManager().countTestLevel(email, subject);
			
			if(countDuplicateSubjectLevelForMember == 0)
			{
				con = db.getConnection();
				ps = con.prepareStatement(
						"insert into level (email,learnLevel,testLevel,subject) values (?," + ZERO + ",?,?)");
				
				ps.setString(1, email);
				ps.setInt(2, testLevel);
				ps.setString(3, subject);
				
				int result = ps.executeUpdate();
				
				if(result > 0)
				{
					System.out.println("SQL Query Executed successfully. Records inserted in level----"  + result);
					return TRUE;
				}

			}	
			else if(countDuplicateSubjectLevelForMember > 0)
			{
				con = db.getConnection();
				ps = con.prepareStatement(
						"UPDATE level SET testLevel = " + testLevel + " where email = '"  + email + "' AND subject = '" + subject + "'");
				
				int result = ps.executeUpdate();
				
				if(result > 0)
				{
					System.out.println("SQL update Executed successfully. Records updated in level----"  + result);
					return TRUE;
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			con.close();
		}
		return FALSE;
	}
	
	
	/**
	 * creates an advertisement for today's date.
	 * 
	 * @param email
	 * @param plan
	 * @param product
	 * @param usedAt
	 * @throws SQLException
	 */
	@POST
	@Path("/createAdvertisement")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void createAdvertisement(@FormParam("q1") String email,
			@FormParam("q2") String plan,
			@FormParam("q3") String product,
			@FormParam("q4") String usedAt)
			throws SQLException 
			{
				Calendar advertisementDateTime = Calendar.getInstance();
				String advertisementDateTimeString = dateFormat.format(advertisementDateTime.getTime());
				String[] advertisementDateTimeStringArray = advertisementDateTimeString.split(SPACE);
				String date = advertisementDateTimeStringArray[0];
				
				PreparedStatement ps = null;
				Connection con = null;
				Database db = new Database();
				
				try 
				{
		
					con = db.getConnection();
					ps = con.prepareStatement(
							"insert into advertisements (email,date,plan,product,usedAt) values (?,?,?,?,?)");
		
					ps.setString(1, email);
					ps.setString(2, date);
					ps.setString(3, plan);
					ps.setString(4, product);
					ps.setString(5, usedAt);
					
					int result = ps.executeUpdate();
					
					if(result > 0)
					{
						System.out.println("SQL Query Executed successfully. Records inserted in advertisement----"  + result);
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
	 * Submits values to the student registration table
	 * and simultaneously creates a new entry in credentials
	 * table for the user. In addition to that, creates an entry per
	 * subject in the level table.
	 * 
	 * @param name
	 * @param age
	 * @param school
	 * @param address
	 * @param email
	 * @param bio
	 * @param password
	 * @param testLevel
	 * @param father
	 * @param teacher
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
			@FormParam("q6") String bio,
			@FormParam("q7") String password,
			@FormParam("q8") String testLevel,
			@FormParam("q9") String father,
			@FormParam("q10") String teacher)
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
							"insert into child (name,age,school,address,email,bio,type,testLevel,password) values (?,?,?,?,?,?,'" + CHILD + "',?,?)");
		
					ps.setString(1, name);
					ps.setInt(2, age);
					ps.setString(3, school);
					ps.setString(4, address);
					ps.setString(5, email);
					ps.setString(6, bio);
					ps.setString(7, testLevel);
					ps.setString(8, password);
					
					int result = ps.executeUpdate();
					
					if(result > 0)
					{
						System.out.println("SQL Query Executed successfully. Records inserted----"  + result);
						//sendRegistrationLinkStatus = sendEmail(email, hashValue);
						ps = con.prepareStatement(
								"insert into credentials (username,password,type) values (?,?,'" + CHILD + "')");
						ps.setString(1, email);
						ps.setString(2, password);
						
						result = ps.executeUpdate();
						if(result > 0)
						{
							System.out.println("SQL Query Executed successfully. Records inserted in credentials----"  + result);
							
							for(int i=0; i<6; i++)
							{
								ps = con.prepareStatement(
										"insert into level (email,testLevel,learnLevel,subject) values (?,?,?,'" + SUBJECTS[i] + "')");
								ps.setString(1, email);
								ps.setString(2, testLevel);
								ps.setString(3, testLevel);
								
								result = ps.executeUpdate();
								if(result > 0)
								{
									System.out.println("SQL Query Executed successfully. Records inserted in level----"  + result);
								}
							}
						}
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
	 * Submits values to the sponsor registration table
	 * and simultaneously creates a new entry in credentials
	 * table for the user.
	 * 
	 * @param name
	 * @param email
	 * @param bio
	 * @param password
	 * @throws SQLException
	 */
	@POST
	@Path("/registerSponsor")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void registerSponsor(@FormParam("q1") String name,
			@FormParam("q2") String email,
			@FormParam("q3") String bio,
			@FormParam("q4") String password)
			throws SQLException 
			{
				PreparedStatement ps = null;
				Connection con = null;
				Database db = new Database();
				try 
				{
		
					con = db.getConnection();
					ps = con.prepareStatement(
							"insert into sponsor (name,email,bio,amount,password) values (?,?,?," + ZERO + ",?)");
		
					ps.setString(1, name);
					ps.setString(2, email);
					ps.setString(3, bio);
					ps.setString(4, password);
					
					int result = ps.executeUpdate();
					
					if(result > 0)
					{
						System.out.println("SQL Query Executed successfully. Records inserted----"  + result);
						ps = con.prepareStatement(
								"insert into credentials (username,password,type) values (?,?,'" + SPONSOR + "')");
						ps.setString(1, email);
						ps.setString(2, password);
						
						result = ps.executeUpdate();
						if(result > 0)
						{
							System.out.println("SQL Query Executed successfully. Records inserted in credentials----"  + result);
						}
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
	 * Submits values to the advertiser registration table
	 * and simultaneously creates a new entry in credentials
	 * table for the user.
	 * 
	 * @param name
	 * @param company
	 * @param email
	 * @param products
	 * @param plan
	 * @param bio
	 * @param password
	 * @throws SQLException
	 */
	@POST
	@Path("/registerAdvertiser")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void registerAdvertiser(@FormParam("q1") String name,
			@FormParam("q2") String company,
			@FormParam("q3") String email,@FormParam("q4") String products,
			@FormParam("q5") String plan,
			@FormParam("q6") String bio,
			@FormParam("q7") String password)
			throws SQLException 
			{
				PreparedStatement ps = null;
				Connection con = null;
				Database db = new Database();
				try 
				{
		
					con = db.getConnection();
					ps = con.prepareStatement(
							"insert into advertiser (name,company,email,products,plan,bio,password) values (?,?,?,?,?,?,?)");
		
					ps.setString(1, name);
					ps.setString(2, company);
					ps.setString(3, email);
					ps.setString(4, products);
					ps.setString(5, plan);
					ps.setString(6, bio);
					ps.setString(7, password);
					
					int result = ps.executeUpdate();
					
					if(result > 0)
					{
						System.out.println("SQL Query Executed successfully. Records inserted----"  + result);
						ps = con.prepareStatement(
								"insert into credentials (username,password,type) values (?,?,'" + ADVERTISER + "')");
						ps.setString(1, email);
						ps.setString(2, password);
						
						result = ps.executeUpdate();
						if(result > 0)
						{
							System.out.println("SQL Query Executed successfully. Records inserted in credentials----"  + result);
						}
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
	 * Sends email from info@tabletribes.com to user's email address for
	 * registration
	 * 
	 * @param email
	 * @param hashValue
	 * @return
	 */
	@POST
	@Path("/sendEmail")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	private boolean sendEmail(@FormParam("title") String title,
			@FormParam("fullname") String fullname,
			@FormParam("email_address") String email_address,
			@FormParam("birthdate") Date birthdate,
			@FormParam("country") String country,
			@FormParam("phonenumber") String phonenumber,
			@FormParam("input-textArea") String text) {
		final String username = "noreply@tabletribes.com";
		final String password = "TT1s@wesome";
		final String destinationEmailAddress = "rohit.khanduri@hotmail.com";
		
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.host", "box260.bluehost.com");
		prop.put("mail.smtp.port", "25");
		prop.put("mail.smtp.starttls.enable", "true");
		Session session = Session.getDefaultInstance(prop,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			String body = "Mail Body";
			String htmlBody = "<strong>Name: " + fullname + ", email: "
					+ email_address + ", country: " + country + ", message: "
					+ text + "</strong>";
			String textBody = "Name: " + fullname + ", email: " + email_address
					+ ", country: " + country + ", message: " + text;
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("info@tabletribes.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(destinationEmailAddress));
			message.setSubject("New Enquiry");
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

		} catch (MessagingException e) {
			System.out.println("Error while sending new enquiry email due to: "
					+ e);
		}
		return false;
	}
	
	/**
	 * Sends email from noreply@rohitkhanduri.com to user's email address for
	 * registration
	 * 
	 * @param email
	 * @param hashValue
	 * @return
	 */
	@POST
	@Path("/sendEmailFromGodaddy")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public void sendEmailFromGodaddy(@FormParam("title") String title,
			@FormParam("fullname") String fullname,
			@FormParam("email_address") String email_address,
			@FormParam("birthdate") Date birthdate,
			@FormParam("country") String country,
			@FormParam("phonenumber") String phonenumber,
			@FormParam("input-textArea") String text) 
	{
		Properties mailServerProperties;
		Session getMailSession;
		MimeMessage generateMailMessage;
		
		try
		{
		System.out.println("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "25");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");
 
//Step2		
		System.out.println("\n\n 2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.setFrom(new InternetAddress("noreply@rohitkhanduri.com"));
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("rohit.khanduri@hotmail.com"));
		generateMailMessage.setSubject("Greetings from Crunchify..");
		String emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
		generateMailMessage.setContent(emailBody, "text/html");
		System.out.println("Mail Session has been created successfully..");
 
//Step3		
		System.out.println("\n\n 3rd ===> Get Session and Send mail");
		Transport transport = getMailSession.getTransport("smtp");
		
		// Enter your correct gmail UserID and Password (XXXarpitshah@gmail.com)
		transport.connect("mail.rohitkhanduri.com", "_mainaccount@rohitkhanduri.com", "Rohit1901!");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
		System.out.println("---------------------Email sent---------------------");
		
		} 
		catch (Exception e)
		{
			System.out.println("Error while sending new enquiry email due to: "
					+ e);
		}
		System.out.println("---------------------Email not sent---------------------");
	}

}
