package model;

import java.sql.Connection;
import java.util.ArrayList;

import dao.Access;
import dao.Database;
import dto.tagsInfo;
import dto.userAllInfo;
import dto.userAllInvitations;
import dto.user_live_location;
import dto.usercredential;
import dto.userinformation;

public class AccessManager {
	public ArrayList<usercredential> getUserCredential() throws Exception {
		ArrayList<usercredential> credentials = new ArrayList<usercredential>();
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		credentials = access.getUserCredential(con);
		return credentials;
	}

	public ArrayList<userinformation> getUserInformation() throws Exception {
		ArrayList<userinformation> userinformation = new ArrayList<userinformation>();
		Database db1 = new Database();
		Connection con1 = db1.getConnection();
		Access access1 = new Access();
		userinformation = access1.getUserInformation(con1);
		return userinformation;
	}

	public ArrayList<user_live_location> getUserLocation() throws Exception {
		ArrayList<user_live_location> userinformation = new ArrayList<user_live_location>();
		Database db1 = new Database();
		Connection con1 = db1.getConnection();
		Access access1 = new Access();
		userinformation = access1.getUserLocation(con1);
		return userinformation;
	}

	public ArrayList<userAllInfo> getUserAllInfo(String latitude, String longitude, String searchTags, String radius) throws Exception {
		ArrayList<userAllInfo> userinformation = new ArrayList<userAllInfo>();
		Database db1 = new Database();
		Connection con1 = db1.getConnection();
		Access access1 = new Access();
		userinformation = access1.getUserAllInfo(con1, latitude, longitude, searchTags, radius);
		return userinformation;
	}

	public ArrayList<userAllInvitations> getUserAllInvitations(String userId)
			throws Exception {
		ArrayList<userAllInvitations> userinvitations = new ArrayList<userAllInvitations>();
		Database db1 = new Database();
		Connection con1 = db1.getConnection();
		Access access1 = new Access();
		userinvitations = access1.getUserAllInvitations(con1, userId);
		return userinvitations;
	}

	public ArrayList<dto.tagsInfo> getTagsInfo() throws Exception {
		ArrayList<tagsInfo> tagsInfo = new ArrayList<tagsInfo>();
		Database db1 = new Database();
		Connection con1 = db1.getConnection();
		Access access1 = new Access();
		tagsInfo = access1.getTagsInfo(con1);
		return tagsInfo;
	}

}