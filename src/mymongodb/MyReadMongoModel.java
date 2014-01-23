package mymongodb;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MyReadMongoModel {

	public Model getModelWithDatabaseData(){

		Model m = this.createEventModel();
		String ns = m.getNsPrefixURI("mgoevent");

		//Alimentation with database data
		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB( "mydb" );
			DBCollection coll = db.getCollection("music_event");
			DBCursor cursor = coll.find();

			while(cursor.hasNext()) {
				BasicDBObject obj = (BasicDBObject) cursor.next();

				String event_id = obj.getString("id");
				String ticket_status = obj.getString("ticket_status");
				String datetime = obj.getString("datetime");
				BasicDBObject venue =(BasicDBObject) obj.get("venue");
				String city = venue.getString("city");
				BasicDBList artists = (BasicDBList) obj.get("artists");

				Resource musicEvent = m.createResource(ns+event_id);
				m.add(musicEvent, RDF.type, m.getResource(ns+"event"));
				m.add(musicEvent, m.getProperty(ns+"ticketStatus"), ticket_status);
				m.add(musicEvent, m.getProperty(ns+"datetime"), datetime);
				m.add(musicEvent, m.getProperty(ns+"city"), city);
				for(Object artist : artists){
					BasicDBObject my = (BasicDBObject)artist;
					m.add(musicEvent, m.getProperty(ns+"participant"), my.getString("name"));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return m;
	}

	public Model createEventModel(){
		Model m = ModelFactory.createDefaultModel();
		String ns = "http://www.findevent.fr#";
		m.setNsPrefix("mgoevent", ns);

		Resource Event = m.createResource(ns+"event");
		Property ticketStatus = m.createProperty(ns+"ticketStatus");
		Property datetime = m.createProperty(ns+"datetime");
		Property city = m.createProperty(ns+"city");
		Property participant = m.createProperty(ns+"participant");

		return m;
	}
	
	public void persistModel(){
		Model m = this.getModelWithDatabaseData();
		FileOutputStream ost;
		try {
			ost = new FileOutputStream("assets/outMongoEvent.rdf");
			//m.write(System.out, "RDF/XML-ABBREV");
			m.write(ost, "RDF/XML-ABBREV" ); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
