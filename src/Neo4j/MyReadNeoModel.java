package Neo4j;

import java.io.PrintWriter;
import java.util.ArrayList;

import general.Outil;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class MyReadNeoModel {
	
	PrintWriter wr_nettoye;
	ArrayList<Artist> listOfArtist;
	
	public Model getNeoModelWithData(){
		Model m = ModelFactory.createDefaultModel();	
		String ns = "http://www.findevent.fr#";
		m.setNsPrefix("neoartist", ns);
		Resource Artist = m.createResource(ns+"artist");
		Property property_name= m.createProperty(ns+"name");
		Property property_instrument = m.createProperty(ns+"instrument");
		Property property_birthday = m.createProperty(ns+"birthday");
		Property property_genre = m.createProperty(ns+"genre");
		Property property_knows = m.createProperty(ns+"knows");
		return m;
	}
	
	//This method is used to load data from dbpedia
	@SuppressWarnings("finally")
	public ArrayList<Artist> readDataArtiste(){
		listOfArtist = new ArrayList<Artist>();
		String sQueries;
		String sSelect;
		String sWhere;
		String prefixe = Outil.createPrefixe();
		final String NL = System.getProperty("line.separator") ;
		final String service_dbpedia = "http://www.dbpedia.org/sparql";
		QueryExecution qexec;
		
		sQueries="";
		sQueries+=prefixe;
		sSelect="SELECT DISTINCT * ";
		sQueries=sQueries+sSelect;	
		sWhere = "";
		sWhere=sWhere + "?artiste a dbpedia-owl:Artist.";
		sWhere=sWhere + "?artiste foaf:name ?name.";
		sWhere=sWhere + "?artiste dbpedia-owl:genre ?genre."; 
		sWhere=sWhere + "?artiste dbpedia-owl:genre ?genre."; 
		sWhere=sWhere + "?artiste dbpedia-owl:instrument ?instrument.";
		sQueries = sQueries+ "WHERE { "+sWhere+" } ";
		System.out.println(sQueries);
		qexec = QueryExecutionFactory.sparqlService(service_dbpedia, sQueries);
		try {
			wr_nettoye = new PrintWriter("assets/info_artistes_dbpedia.txt", "UTF-8");
			ResultSet rs = qexec.execSelect() ;
			while(rs.hasNext())
			{
				QuerySolution soln = rs.nextSolution();
				Artist artist = new Artist();
				artist.setGenre(Outil.getLastItemInLink(soln.get("?genre").toString()));
				artist.setInstrument(Outil.getLastItemInLink(soln.get("?instrument").toString()));
				artist.setName(Outil.getLastItemInLink(soln.get("?name").toString()));
				listOfArtist.add(artist);
				
				//Persist data to txt file
				wr_nettoye.println(Outil.getLastItemInLink(soln.get("?name").toString())+", "
				+Outil.getLastItemInLink(soln.get("?genre").toString())
				+", "+Outil.getLastItemInLink(soln.get("?instrument").toString())
				);
			}
			
			//Manually add some artists
			Artist a1 = new Artist();
			a1.setGenre("Acid_rock");
			a1.setInstrument("Piano");
			a1.setName("Maxime Jullia");
			
			Artist a2 = new Artist();
			a2.setGenre("Acid_house");
			a2.setInstrument("Bass_guitar");
			a2.setName("Vioda");
			
			Artist a3 = new Artist();
			a3.setGenre("Acid_house");
			a3.setInstrument("Synthesizer");
			a3.setName("Florian Gauthier");
			
			Artist a4 = new Artist();
			a4.setGenre("Beat_music");
			a4.setInstrument("Guitar");
			a4.setName("Didier Barbelivien");

			listOfArtist.add(a1);
			listOfArtist.add(a2);
			listOfArtist.add(a3);
			listOfArtist.add(a4);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			wr_nettoye.close();
			return listOfArtist;
		}
	}
	
	//Create and insert data into Neo4j database
	public void createDB(){
		NeoDBManagement neomgt = new NeoDBManagement();
		try{
			//neomgt.createDatabase(this.readDataArtiste());
			neomgt.showAllNodes();
			//neomgt.removeData();
			neomgt.shutdown();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
