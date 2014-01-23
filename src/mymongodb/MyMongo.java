package mymongodb;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;

public class MyMongo{

	public static void main(String args[]){

		/*// To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
		// if it's a member of a replica set:
		MongoClient mongoClient = new MongoClient();
		// or
		MongoClient mongoClient = new MongoClient( "localhost" );
		// or
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
		MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
		                                      new ServerAddress("localhost", 27018),
		                                      new ServerAddress("localhost", 27019)));*/

		try{

			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB( "mydb" );

			Set<String> colls = db.getCollectionNames();
			for (String s : colls) {
				System.out.println(s);
			}

			DBCollection coll = db.getCollection("testData");

			BasicDBObject doc = new BasicDBObject
					("Country", "France").
					append("Continent", "Europe").
					append("Language", "FR");

			BasicDBObject doc1 = new BasicDBObject
					("Country", "Tunisie").
					append("Continent", "Afrique").
					append("Language", "AR");
			
			BasicDBObject doc2 = new BasicDBObject
					("Country", "Maroc").
					append("Continent", "Afrique").
					append("Language", "AR");

			BasicDBObject doc3 = new BasicDBObject
					("Country", "Chine").
					append("Continent", "Asie").
					append("Language", "CH");
			
			BasicDBObject doc4 = new BasicDBObject
					("Country", "Singapour").
					append("Continent", "Asie").
					append("Language", "EN");

			coll.insert(doc,doc1,doc2, doc3, doc4);
			

			DBCursor cursor = coll.find();
			try {
				while(cursor.hasNext()) {
					System.out.println(cursor.next());
				}
			} finally {
				cursor.close();
			}



		}catch(UnknownHostException e){
			e.printStackTrace();

		}
	}




}