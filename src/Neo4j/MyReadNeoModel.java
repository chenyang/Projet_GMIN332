package Neo4j;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import general.Outil;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class MyReadNeoModel {

	PrintWriter wr_nettoye;
	ArrayList<Artist> listOfArtist;

	public Model getNeoModelWithData(){
		NeoDBManagement neomgt = new NeoDBManagement();
		Model m = ModelFactory.createDefaultModel();	
		String ns = "http://www.findevent.fr/neoartist#";
		m.setNsPrefix("neoartist", ns);
		Resource Artist = m.createResource(ns+"artist");
		Resource Wife = m.createResource(ns+"wife");
		Property property_name= m.createProperty(ns+"name");
		Property property_instrument = m.createProperty(ns+"instrument");
		Property property_genre = m.createProperty(ns+"genre");
		Property property_knows = m.createProperty(ns+"knows");
		Property property_marries = m.createProperty(ns+"marries");
		Property property_nationality = m.createProperty(ns+"nationality");//For wife's nationality
		Property property_knows_weight = m.createProperty(ns+"knows_weight");


		GraphDatabaseService graphDatabaseService = neomgt.getGraphDatabaseService();
		Transaction transaction = graphDatabaseService.beginTx();
		try{
			for(Node n : graphDatabaseService.getAllNodes()){
				//System.out.println(n.getProperty("name")+", "+n.getProperty("genre")+", "+n.getProperty("instrument"));

				//For all Node Artist	
				if(n.getProperty("genre")!=null){
					String name_underscore = (n.getProperty("name").toString()).replace(" ", "_");
					Resource artist = m.createResource(ns+name_underscore);
					m.add(artist, RDF.type, Artist);
					m.add(artist, property_name, n.getProperty("name").toString());
					m.add(artist, property_genre, n.getProperty("genre").toString());
					m.add(artist, property_instrument, n.getProperty("instrument").toString());

					if(n.hasRelationship()){
						//For all outgoing relationships
						for(Relationship rel:n.getRelationships(Direction.OUTGOING)){
							if(rel.getType().name().equals(NeoDBManagement.RelTypes.KNOWS.toString())){
								//System.out.println("OUTGOING:"+n.getProperty("name")+" knows: "+rel.getEndNode().getProperty("name").toString()+"/ "+ rel.getProperty("knows-weight"));
								String another_name_underscore = (rel.getEndNode().getProperty("name").toString()).replace(" ", "_");
								Resource anotherArtist = m.createResource(ns+another_name_underscore);
								//property_knows.addProperty(property_knows_weight, rel.getProperty("knows-weight").toString());
								m.add(artist, property_knows, anotherArtist);
								//m.add(anotherArtist, property_knows_weight, rel.getProperty("knows-weight").toString());
							}else if(rel.getType().name().equals(NeoDBManagement.RelTypes.MARRYTO.toString())){
								Resource wife = m.createResource(ns+rel.getEndNode().getProperty("name"));
								m.add(wife, RDF.type, Wife);
								m.add(wife, property_name, rel.getEndNode().getProperty("name").toString());
								m.add(wife, property_nationality,rel.getEndNode().getProperty("nationality").toString());
								m.add(artist, property_marries, wife);
							}
						}				
					}
				}
			}
			transaction.success();
		}catch(Exception e){
			//do nothing, e.printStackTrace();
			System.out.println("some node just dont have properties, we will jump over them");
		}finally{
			transaction.finish();
			neomgt.shutdown();
		}
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
		sWhere=sWhere + "?artiste a dbpedia-owl:Artist. ";
		sWhere=sWhere + "?artiste foaf:name ?name. ";
		sWhere=sWhere + "?artiste dbpedia-owl:instrument ?instrument. ";
		sWhere=sWhere + "?artiste dbpedia-owl:genre ?genre. "; 
		sQueries = sQueries+ "WHERE {"+sWhere+"} ";
		//System.out.println(sQueries);
		try {
			wr_nettoye = new PrintWriter("assets/info_artistes_dbpedia.txt", "UTF-8");
			/*
			qexec = QueryExecutionFactory.sparqlService(service_dbpedia, sQueries);
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
			 */			

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
			neomgt.createDatabase(this.readDataArtiste());
			//neomgt.removeData();
			neomgt.shutdown();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
