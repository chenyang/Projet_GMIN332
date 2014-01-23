package mymongodb;

import general.QueryStringFactory;

import java.io.FileOutputStream;
import java.net.UnknownHostException;

import org.openjena.atlas.io.IndentedWriter;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

//Sert a construire Jena Model pour Mongo + remplisage dans de la base de mongo
//
public class MongoModelGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try{
			Model m = ModelFactory.createDefaultModel();
			String ns = "http://www.mymongo.fr#";
			m.setNsPrefix("mgo", ns);


			//Creation de ressource
			Resource Country = m.createResource(ns+"country");
			Resource Continent = m.createResource(ns+"continent");
			Resource Language = m.createResource(ns+"language");


			//Creatation de Predicat
			Property nameOfCountry = m.createProperty(ns+"nameOfCountry");
			Property nameOfContinent = m.createProperty(ns+"nameOfContinent");
			Property nameOfLanguage = m.createProperty(ns+"nameOfLanguage");

			Property hasLang = m.createProperty(ns+"hasLang");
			Property belongsTo = m.createProperty(ns+"belongsTo");



			//Partie pour lire mongo et remplir le modle
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB( "mydb" );

			DBCollection coll = db.getCollection("testData");
			DBCursor cursor = coll.find();
			try {
				while(cursor.hasNext()) {
					BasicDBObject obj = (BasicDBObject) cursor.next();
					System.out.println(obj);
					
					String tempCountryName = obj.getString("Country");
					String tempCont = obj.getString("Continent");
					String tempLang = obj.getString("Language");


					Resource country = m.createResource(ns+tempCountryName);
					m.add(country, nameOfCountry, tempCountryName);
					m.add(country, RDF.type, Country);
					
					Resource lang = m.createResource(ns+tempLang);
					m.add(lang, nameOfLanguage, tempLang);
					m.add(lang, RDF.type, Language);
					
					Resource continent = m.createResource(ns+tempCont);
					m.add(continent, nameOfContinent, tempCont);
					m.add(continent, RDF.type, Continent);
					
					m.add(country, hasLang, lang);
					m.add(country, belongsTo, continent);
				}
				
				
				//Output the model with data to an RDF file
				FileOutputStream ost = new FileOutputStream("outMongo.rdf");
				m.write(ost, "RDF/XML-ABBREV" ); 
				m.write(System.out, "RDF/XML-ABBREV");
				
				
				//Test SPARQL Here
				Query query = QueryFactory.create(QueryStringFactory.createMongoString(1)) ;
				
				query.serialize(new IndentedWriter(System.out,true)) ;
				System.out.println() ;
				
				QueryExecution qexec = QueryExecutionFactory.create(query, m) ;
				ResultSet rs = qexec.execSelect() ;
				ResultSetFormatter.out(System.out, rs, query);

			} catch(Exception e){
				e.printStackTrace();
			}
			finally {
				cursor.close();
			}


		}
		catch (Exception e){
			e.printStackTrace();
		}


	}

}
