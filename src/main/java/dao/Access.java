package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;


import dto.tagsInfo;
import dto.userAllInfo;
import dto.userAllInvitations;
import dto.userTagsInfo;
import dto.user_live_location;
import dto.usercredential;
import dto.userinformation;

public class Access {
	public ArrayList<usercredential> getUserCredential(Connection con)
			throws SQLException {
		ArrayList<usercredential> credentialList = new ArrayList<usercredential>();
		PreparedStatement stmt = con
				.prepareStatement("SELECT email,password FROM registration");
		ResultSet rs = stmt.executeQuery();
		try {
			while (rs.next()) {
				usercredential userCredentialObj = new usercredential();
				userCredentialObj.setUserid(rs.getString("email"));
				userCredentialObj.setPassword(rs.getString("password"));
				credentialList.add(userCredentialObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			con.close();
		}
		return credentialList;

	}

	public ArrayList<userinformation> getUserInformation(Connection con)
			throws SQLException {
		ArrayList<userinformation> allInformationList = new ArrayList<userinformation>();
		PreparedStatement stmt = con
				.prepareStatement("SELECT * FROM userinformation");
		ResultSet rs = stmt.executeQuery();
		try {
			while (rs.next()) {
				userinformation userinformationObj = new userinformation();
				userinformationObj.setUserid(rs.getString("userid"));
				userinformationObj.setAge(rs.getInt("age"));
				userinformationObj.setGeneral_interests(rs
						.getString("general_interests"));
				userinformationObj.setInformation(rs.getString("information"));
				userinformationObj.setName(rs.getString("name"));
				userinformationObj.setPicture(rs.getBlob("picture"));
				userinformationObj.setProfessional_interests(rs
						.getString("professional_interests"));
				userinformationObj.setUserCredential(rs
						.getBlob("userCredential"));
				allInformationList.add(userinformationObj);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			con.close();
		}
		return allInformationList;

	}

	public ArrayList<user_live_location> getUserLocation(Connection con)
			throws SQLException {
		ArrayList<user_live_location> locationList = new ArrayList<user_live_location>();
		PreparedStatement stmt = con
				.prepareStatement("SELECT * FROM user_live_location");
		ResultSet rs = stmt.executeQuery();
		try {
			while (rs.next()) {
				user_live_location userLocationObj = new user_live_location();
				userLocationObj.setUserid(rs.getString("userid"));
				userLocationObj.setLatitudce(rs.getString("latitudce"));
				userLocationObj.setLongitude(rs.getString("longitude"));
				locationList.add(userLocationObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			con.close();
		}
		return locationList;
	}

	public ArrayList<userAllInfo> getUserAllInfo(Connection con, String latitude, String longitude, String searchTags, String radius)
			throws SQLException {
		System.out.println("latitude in access.java getUserAllInfo: " + latitude);

		ArrayList<userAllInfo> locationList = new ArrayList<userAllInfo>();
		Database db1 = new Database();
		try {
			MongoClient con1 = db1.getMongoConnection();
			ArrayList<user_live_location> nearbyUsers = getNearbyUsers (con1, latitude, longitude, radius);
			ArrayList<user_live_location> nearbyTagMatchingUsers = getTagsMatchingUsers (con, nearbyUsers, searchTags);

			try {
				for (int i = 0; i < nearbyTagMatchingUsers.size(); i++) {
//			    	System.out.println("Query in getUserAllInfo: " + "select userinformation.userid, userinformation.age, userinformation.general_interests, userinformation.information, userinformation.name, userinformation.professional_interests from userinformation where userinformation.userid = \"" + nearbyTagMatchingUsers.get(i).getUserid() + "\"");
//			    	PreparedStatement stmt = con
//							.prepareStatement("select userinformation.userid, userinformation.age, userinformation.general_interests, userinformation.information, userinformation.name, userinformation.professional_interests from userinformation where userinformation.userid = \"" + nearbyTagMatchingUsers.get(i).getUserid() + "\"");
			    	System.out.println("Query in getUserAllInfo: " + "select registration.email, registration.firstName, registration.lastName, registration.homeTown, registration.dateOfBirth, registration.bio from registration where registration.email = \"" + nearbyTagMatchingUsers.get(i).getUserid() + "\"");
			    	PreparedStatement stmt = con
							.prepareStatement("select registration.email, registration.firstName, registration.lastName, registration.homeTown, registration.dateOfBirth, registration.bio from registration where registration.email = \"" + nearbyTagMatchingUsers.get(i).getUserid() + "\"");
					
					ResultSet rs = stmt.executeQuery();
					
						while (rs.next()) {
							userAllInfo userAllInfoObj = new userAllInfo();
							userAllInfoObj.setUserid(rs.getString("email"));
							userAllInfoObj.setFirstname(rs.getString("firstName"));
							userAllInfoObj.setLastname(rs.getString("lastName"));
							userAllInfoObj.setHometown(rs.getString("homeTown"));
							userAllInfoObj.setDate_of_birth(rs.getDate("dateOfBirth").toString());
							userAllInfoObj.setBio(rs.getString("bio"));
	//						userAllInfoObj.setLatitudce(rs.getString("latitudce"));
	//						userAllInfoObj.setLongitude(rs.getString("longitude"));
							locationList.add(userAllInfoObj);
						}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally {
			con.close();
		}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		return locationList;
	}

	private ArrayList<user_live_location> getTagsMatchingUsers(Connection con,
			ArrayList<user_live_location> nearbyUsers, String searchTags) throws SQLException {
		ArrayList<user_live_location> tagsMatchingUsersList = new ArrayList<user_live_location>();
		List<String> searchTagsList = Arrays.asList(searchTags.split("\\s*,\\s*"));
		try {
			for (int i = 0; i < nearbyUsers.size(); i++) {
				boolean hasMatch = false;

//		    	System.out.println("Query in getTagsMatchingUsers: " + "select userinformation.userid, userinformation.tags from userinformation where userinformation.userid = \"" + nearbyUsers.get(i).getUserid() + "\"");
//		    	PreparedStatement stmt = con
//						.prepareStatement("select userinformation.userid, userinformation.tags from userinformation where userinformation.userid = \"" + nearbyUsers.get(i).getUserid() + "\"");
		    	
		    	System.out.println("Query in getTagsMatchingUsers: " + "select usertags.userid, usertags.tagid from usertags where usertags.userid = \"" + nearbyUsers.get(i).getUserid() + "\"");
		    	PreparedStatement stmt = con
						.prepareStatement("select usertags.userid, usertags.tagid from usertags where usertags.userid = \"" + nearbyUsers.get(i).getUserid() + "\"");

				ResultSet rs = stmt.executeQuery();
				
					while (rs.next()) {
						userTagsInfo userTagsInfoObj = new userTagsInfo();
						userTagsInfoObj.setUserid((rs.getString("userid")));
						userTagsInfoObj.setTags((rs.getString("tagid")));
				    	System.out.println("userTagsInfoObj: " + userTagsInfoObj.getTags());
//
//						if(!userTagsInfoObj.getTags().isEmpty()) {
//							List<String> userTagsList = Arrays.asList(userTagsInfoObj.getTags().split("\\s*,\\s*"));
//							for (String tag : userTagsList) {
//								if (searchTagsList.contains(tag)) {
//									hasMatch = true;
//									break;
//								}
//							}
//							if (hasMatch) {
//								System.out.println("hasMatch true for: " + nearbyUsers.get(i).getUserid()); 
//								tagsMatchingUsersList.add(nearbyUsers.get(i));
//							}
//						}
				    	
						if (searchTagsList.contains(userTagsInfoObj.getTags())) {
									hasMatch = true;
									break;
							}
						}
						if (hasMatch) {
						System.out.println("hasMatch true for: " + nearbyUsers.get(i).getUserid()); 
						tagsMatchingUsersList.add(nearbyUsers.get(i));
						}
						
					}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
//			con.close();
		}	
		return tagsMatchingUsersList;
	}

	public ArrayList<user_live_location> getNearbyUsers(MongoClient mc, String lat, String lon, String radius)
			throws Exception {
		
		System.out.println("latitude in getNearbyUsers access.java: " + lat + " lng: "+ lon + " radius: " + radius);
		ArrayList<user_live_location> locationList = new ArrayList<user_live_location>();

		DB db = mc.getDB( "heroku_app27027503" );
		DBCollection coll = db.getCollection("TTUserLiveLocationCollection");
		BasicDBList myLocation = new BasicDBList();
		myLocation.put(0, Double.parseDouble(lon));
		myLocation.put(1, Double.parseDouble(lat));
		DBCursor cursor = coll.find(
	            new BasicDBObject("loc",
		                new BasicDBObject("$near",
		                        new BasicDBObject("$geometry",
		                                new BasicDBObject("type", "Point")
		                                    .append("coordinates", myLocation))
		                             .append("$maxDistance",  Double.parseDouble(radius))
		                        )
		                )
		            );
		System.out.println("cursor count: " + cursor.count());
		try {
		   while(cursor.hasNext()) {
				user_live_location userLiveLocObj = new user_live_location();
		        BasicDBObject userObj = (BasicDBObject) cursor.next();
		        System.out.println(userObj);
		        String userId = userObj.getString("_id");

				userLiveLocObj.setUserid(userId);
				locationList.add(userLiveLocObj);
		   }
		} finally {
		   cursor.close();
		}
		

		return locationList;
	}
	
	public ArrayList<userAllInvitations> getUserAllInvitations(Connection con, String userId)
			throws SQLException {
		ArrayList<userAllInvitations> invitationList = new ArrayList<userAllInvitations>();
		System.out.println("select * from invitation where username_to='" + userId + "' and isnull(accepted);");
		PreparedStatement stmt = con
				.prepareStatement("select * from invitation where username_to='" + userId + "' and isnull(accepted);");
		ResultSet rs = stmt.executeQuery();
		try {
			while (rs.next()) {
				userAllInvitations userAllInvitationsObj = new userAllInvitations();
				userAllInvitationsObj.setUsername_from(rs
						.getString("username_from"));
				userAllInvitationsObj.setGeneral_interests(rs
						.getString("general_interests"));
				userAllInvitationsObj.setLatitude(rs.getString("latitude"));
				userAllInvitationsObj.setLongitude(rs.getString("longitude"));
				userAllInvitationsObj.setProfessional_interests(rs
						.getString("professional_interests"));
				userAllInvitationsObj.setTime(rs.getString("time"));
				userAllInvitationsObj.setUsername_to(rs
						.getString("username_to"));
				userAllInvitationsObj.setAccepted(rs.getString("accepted"));
				userAllInvitationsObj.setActivity(rs.getString("activity"));
				invitationList.add(userAllInvitationsObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			con.close();
		}
		return invitationList;
	}

	public ArrayList<dto.tagsInfo> getTagsInfo(Connection con1) throws SQLException {
		ArrayList<tagsInfo> tagsList = new ArrayList<tagsInfo>();
		PreparedStatement stmt = con1
				.prepareStatement("select * from tags;");
		ResultSet rs = stmt.executeQuery();
		try {
			while (rs.next()) {
				tagsInfo tag = new tagsInfo();
				tag.setTagId(rs
						.getString("tag_id"));
				tag.setTagName(rs
						.getString("tag_name"));
				tag.setSortId(rs.getString("tag_sort_id"));
				
				tagsList.add(tag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			con1.close();
		}
		return tagsList;
	}
}