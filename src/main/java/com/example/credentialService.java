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
import javax.mail.Authenticator;
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

import org.glassfish.jersey.internal.util.Base64;

import model.AccessManager;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.DB;

import dao.Database;
import dto.tagsInfo;
import dto.userAllInfo;
import dto.userAllInvitations;
import dto.user_live_location;
import dto.usercredential;
import dto.userinformation;

@Path("/credentialService")
public class credentialService {

	private boolean updateFlag = false;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String credentials() {
		String credentials = null;
		ArrayList<usercredential> credentialList = new ArrayList<usercredential>();
		try {
			credentialList = new AccessManager().getUserCredential();
			Gson gson = new Gson();
			credentials = gson.toJson(credentialList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return credentials;
	}
	
	@GET
	@Path("/matchCredentials")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String matchCredentials(@QueryParam("userid") String userid,
	        @QueryParam("password") String password) 
	{
		//String credentials = null;
		
		final String TRUE = "true";
		final String FALSE = "false";
		
		//System.out.println(credentials);
		
		ArrayList<usercredential> credentialList = new ArrayList<usercredential>();
		try 
		{
			credentialList = new AccessManager().getUserCredential();
			//Gson gson = new Gson();
			
			for(usercredential credential : credentialList)
			{
				if((credential.getUserid().equals(userid)) && (credential.getPassword().equals(password)))
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

	@GET
	@Path("/userinformation")
	@Produces(MediaType.APPLICATION_JSON)
	public String userInformation() {
		String userInformation = null;
		ArrayList<userinformation> userInformationList = new ArrayList<userinformation>();
		try {
			userInformationList = new AccessManager().getUserInformation();
			Gson gson = new Gson();
			userInformation = gson.toJson(userInformationList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userInformation;
	}

	@POST
	@Path("/registerUser")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void createMessage(@FormParam("userid") String userid,
			@FormParam("password") String password,
			@FormParam("age") String age,
			@FormParam("general_interests") String general,
			@FormParam("information") String information,
			@FormParam("name") String name,
			@FormParam("professional_interests") String professional)
			throws SQLException {
		PreparedStatement ps = null;
		PreparedStatement psAddCredential = null;
		Connection con = null;
		Database db = new Database();
		try {

			con = db.getConnection();
			ps = con.prepareStatement(
					"insert into userinformation (userid,age,general_interests,information,name,picture,professional_interests,userCredential)values (?,?,?,?,?,?,?,?)",
					new String[] { "ID" });

			ps.setString(1, userid);
			ps.setString(2, age);
			ps.setString(3, general);
			ps.setString(4, information);
			ps.setString(5, name);
			ps.setString(6, "");
			ps.setString(7, professional);
			ps.setString(8, "");
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			psAddCredential = con
					.prepareStatement("insert into usercredential (userid,password,source,userInfo) values ('"
							+ userid
							+ "','"
							+ password
							+ "','TABLETRIBES',null)");
			psAddCredential.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			con.close();
		}
	}

//	@POST
//	@Path("/updateLiveLocation")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public void searchPeople(@FormParam("userID") String userid,
//			@FormParam("latitude") String latitudce,
//			@FormParam("longitude") String longitude) throws SQLException {
//
//		PreparedStatement psCheckUserid = null;
//		PreparedStatement ps = null;
//		Connection con = null;
//		Database db = new Database();
//		try {
//
//			con = db.getConnection();
//			ps = con.prepareStatement("select userid from user_live_location");
//			ResultSet rs = ps.executeQuery();
//
//			while (rs.next()) {
//				if (rs.getString("userid").equals(userid)) {
//					psCheckUserid = con
//							.prepareStatement("UPDATE user_live_location SET latitudce='"
//									+ latitudce
//									+ "', longitude='"
//									+ longitude
//									+ "' where userid = '" + userid +"';");
//					psCheckUserid.executeUpdate();
//					updateFlag = true;
//
//				}
//			}
//			if (!updateFlag) {
//				psCheckUserid = con
//						.prepareStatement("insert into user_live_location (userid,latitudce,longitude)values ('"
//								+ userid
//								+ "','"
//								+ latitudce
//								+ "','"
//								+ longitude + "');");
//				psCheckUserid.executeUpdate();
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		} finally {
//			con.close();
//		}
//		// return null;
//	}
	@POST
	@Path("/updateLiveLocation")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void searchPeople(@FormParam("userID") String userid,
			@FormParam("latitude") String latitude,
			@FormParam("longitude") String longitude) throws SQLException {

		PreparedStatement psCheckUserid = null;
		PreparedStatement ps = null;
		MongoClient mongoClient = null;
		Database db = new Database();
	       System.out.println("Inside new mongoclient connection service for: "+ userid + " " + latitude + " " + longitude);

			try {
				mongoClient = db.getMongoConnection();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DB TTDatabase = mongoClient.getDB( "heroku_app27027503" );
			DBCollection coll = TTDatabase.getCollection("TTUserLiveLocationCollection");
			coll.createIndex(new BasicDBObject("loc", "2dsphere"));

		    BasicDBList coordinates = new BasicDBList();
			coordinates.put(0, Double.parseDouble(longitude));
			coordinates.put(1, Double.parseDouble(latitude));
//					coll.insert(new BasicDBObject("userid", userid)
//					                .append("loc", new BasicDBObject("type", "Point").append("coordinates", coordinates)));
//				       System.out.println("Inserted record into TTUserLiveLocationCollection for userid: " + userid);
			
			//new value for co-ordinates for this user
			BasicDBObject newLocation = new BasicDBObject();
			newLocation.append("$set", new BasicDBObject().append("loc", new BasicDBObject("type", "Point").append("coordinates", coordinates)));
			
			BasicDBObject searchQuery = new BasicDBObject().append("_id", userid).append("locationType", "current");
			DBCursor cursor = coll.find(searchQuery);
			try {
				if (cursor.length() == 0) {
					coll.insert(new BasicDBObject("_id", userid)
	                .append("loc", new BasicDBObject("type", "Point").append("coordinates", coordinates))
	                .append("locationType", "current"));
					System.out.println("Inserted record into TTUserLiveLocationCollection for userid: " + userid);
				}
				else {
					coll.update(searchQuery, newLocation);
					System.out.println("Updated record in TTUserLiveLocationCollection for userid: " + userid);
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
			   cursor.close();
			}
			
		// return null;
	}

	@GET
	@Path("/userLocation")
	@Produces(MediaType.APPLICATION_JSON)
	public String userLocation() {
		String userInformation = null;
		ArrayList<user_live_location> userLocationList = new ArrayList<user_live_location>();
		try {
			userLocationList = new AccessManager().getUserLocation();
			Gson gson = new Gson();
			userInformation = gson.toJson(userLocationList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userInformation;
	}

	@GET
	@Path("/whosAround")
	@Produces(MediaType.APPLICATION_JSON)
	public String whosAround(@QueryParam("searchLat") String latitude,
			@QueryParam("searchLng") String longitude, @QueryParam("searchTags") String searchTags, @QueryParam("radius") String radius) {
		String userAllInformation = null;
		ArrayList<userAllInfo> userAllInfoList = new ArrayList<userAllInfo>();
		try {
			System.out.println("latitude in credentialService whosAround: " + latitude);
			userAllInfoList = new AccessManager().getUserAllInfo(latitude, longitude, searchTags, radius);
			System.out.println("userAllInfoList " + userAllInfoList);
			Gson gson = new Gson();
			userAllInformation = gson.toJson(userAllInfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userAllInformation;
	}
	
	@GET
	@Path("/tags")
	@Produces(MediaType.APPLICATION_JSON)
	public String tags(@QueryParam("searchLat") String latitude,
			@QueryParam("searchLng") String longitude) {
		String tagsInfo = null;
		ArrayList<tagsInfo> tagsInfoList = new ArrayList<tagsInfo>();
		try {
			System.out.println("In tags service ");
			tagsInfoList = new AccessManager().getTagsInfo();
			System.out.println("userAllInfoList " + tagsInfoList);
			Gson gson = new Gson();
			tagsInfo = gson.toJson(tagsInfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tagsInfo;
	}
	
	@POST
	@Path("/addUserTag")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void updateInvitation(
			@FormParam("userid") String userid,
			@FormParam("tag") String tag) throws SQLException {

		// PreparedStatement psCheckUserid = null;
		PreparedStatement ps = null;
		Connection con = null;
		Database db = new Database();

		try {
			con = db.getConnection();
			ps = con.prepareStatement("insert into usertags (userid,tagid) values ('"
					+ userid
					+ "','"
					+ tag + "');");
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			con.close();
		}
		// return null;
	}

	@POST
	@Path("/updateInvitation")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void updateInvitation(
			@FormParam("username_from") String username_from,
			@FormParam("latitude_invite") String latitude,
			@FormParam("longitude_invite") String longitude,
			@FormParam("time_invite") String time,
			@FormParam("username_to") String username_to,
			@FormParam("activity") String activity) throws SQLException {

		// PreparedStatement psCheckUserid = null;
		PreparedStatement ps = null;
		Connection con = null;
		Database db = new Database();

		System.out.println("longitude: " + longitude + ", latitude: "
				+ latitude);
		try {
			con = db.getConnection();
			ps = con.prepareStatement("insert into invitation (username_from,latitude,longitude,time,username_to,activity) values ('"
					+ username_from
					+ "','"
					+ latitude
					+ "','"
					+ longitude
					+ "','"
					+ time
					+ "','"
					+ username_to + "','" + activity + "');");
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			con.close();
		}
		// return null;
	}

	@GET
	@Path("/getInvitations")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInvitations(@QueryParam("userid") String userId) {
		String userAllInvitations = null;
		ArrayList<userAllInvitations> userAllInvitationsList = new ArrayList<userAllInvitations>();
		try {
			userAllInvitationsList = new AccessManager()
					.getUserAllInvitations(userId);
			Gson gson = new Gson();
			userAllInvitations = gson.toJson(userAllInvitationsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userAllInvitations;
	}

	@POST
	@Path("/acceptInvitation")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void acceptInvitation(
			@FormParam("username_from_accept") String username_from,
			@FormParam("username_to_accept") String username_to) throws SQLException {

		// PreparedStatement psCheckUserid = null;
		PreparedStatement ps = null;
		Connection con = null;
		Database db = new Database();

		try {
			con = db.getConnection();
			ps = con.prepareStatement("update invitation set accepted = 'YES' where username_from ='"
					+ username_from
					+ "' and username_to = '"
					+ username_to
					+ "';");
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			con.close();
		}
		// return null;
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
//			ps = con.prepareStatement("select * from tempregistration where tempregistration.email = '" + hashValue + "'", ResultSet.CONCUR_UPDATABLE);
//			System.out.println(ps.toString());

			ResultSet rs = ps.executeQuery("select * from tempregistration where tempregistration.email = '" + hashValue + "'");

			if (rs.next()) {
//				System.out.println(rs.toString());

				/*DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String registrationDateTimeString = rs.getString("registrationDateTime");
				java.util.Date registrationDateTime = dateFormat.parse(registrationDateTimeString);
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR, -24);
				java.util.Date twentyFourHoursBack = cal.getTime();
				
				if(twentyFourHoursBack.equals(registrationDateTime))*/
							
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
			if (con != null && conReg !=null) {
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
				//hashValue = randInt(min, max);
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
					//ps.executeUpdate();
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
			
			/*
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("rohitthump@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email));
			message.setSubject("Welcome to TableTribes - Know Your People");
			
			message.setText("Dear Mail Crawler,"
					+ "\n\n Click on the following link to confirm your registration with us, please!"
					+ link);
 
			Transport.send(message);
 
			System.out.println("Done! Email sent.");
			return true;
			*/
			
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