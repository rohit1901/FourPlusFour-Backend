package de.vogella.jersey.first;

import java.sql.Connection;

import dao.Database;

public class test {

	public static void main(String args[]) {
		Database db = new Database();
		try {
			Connection con = db.getConnection();
			if (null != con || con.isValid(1)) {
				System.out.println("connection succeeded!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR connecting!" + e);
		}
	}
}
