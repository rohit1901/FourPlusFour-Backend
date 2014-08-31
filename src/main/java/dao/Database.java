package dao;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class Database {
	public Connection getConnection() throws Exception {
		try {
//			String connectionURL = "jdbc:mysql://localhost:3306/test";
			String connectionURL = "mysql://b394f448a31f35:b2a617ae@us-cdbr-east-06.cleardb.net/heroku_87af94efd632cd6";
//			Connection connection = null;
//			Class.forName("com.mysql.jdbc.Driver").newInstance();
//			connection = DriverManager.getConnection(connectionURL, "root",
//					"");
//			return connection;
			
			URI dbUri = new URI(connectionURL);

		    String username = dbUri.getUserInfo().split(":")[0];
		    String password = dbUri.getUserInfo().split(":")[1];
		    String dbUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath();
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(dbUrl, username,
					password);
		    return connection;
		} catch (Exception e) {
			throw e;
		}

	}

	public MongoClient getMongoConnection() throws Exception {
		try {
//			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			String dbURI = "mongodb://heroku_app27027503:hphgqrbi9hruimt6jbguht91ab@ds027409.mongolab.com:27409/heroku_app27027503";
			MongoClient mongoClient = new MongoClient(new MongoClientURI(dbURI));
//			
			return mongoClient;
		} catch (Exception e) {
			throw e;
		}

	}
}
