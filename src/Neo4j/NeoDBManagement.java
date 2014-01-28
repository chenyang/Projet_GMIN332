package Neo4j;

import java.util.ArrayList;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.core.NodeManager;

public class NeoDBManagement {

	//Neo4j db path
	private static final String Neo4j_PATH = "databases/graph.db";

	private GraphDatabaseService graphDatabaseService = new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_PATH);
	private ArrayList<Relationship> listOfRelations = new ArrayList<Relationship>();
	private ArrayList<Node> listOfArtistNode = new ArrayList<Node>();
	private Node node_maxime, node_florian, node_didier, node_vioda;
	private ArrayList<Node> listOfWifes = new ArrayList<Node>();

	//list of relationships
	public static enum RelTypes implements RelationshipType{
		KNOWS, 
		MARRYTO
	}

	GraphDatabaseService getGraphDatabaseService(){
		return graphDatabaseService;
	}
	

	@SuppressWarnings("deprecation")
	void createDatabase(ArrayList<Artist> listOfArtists){

		System.out.println("num of list of Artists: "+listOfArtists.size());
		
		//Begin Transaction
		Transaction transaction = graphDatabaseService.beginTx();
		try{
			//For Each Artist transferred from dbpedia
			for(Artist artist:listOfArtists){
				//Create Nodes and set Properties
				Node temp = graphDatabaseService.createNode();
				temp.setProperty("name", artist.getName());
				temp.setProperty("instrument", artist.getInstrument());
				temp.setProperty("genre", artist.getGenre());
				listOfArtistNode.add(temp);
			}
			
			for(Node n :listOfArtistNode){
				if(n.getProperty("name").equals("Maxime Jullia")){
					node_maxime = n;
				}else if(n.getProperty("name").equals("Florian Gauthier")){
					node_florian = n;
				}else if(n.getProperty("name").equals("Didier Barbelivien")){
					node_didier = n;
				}else if(n.getProperty("name").equals("Vioda")){
					node_vioda = n;
				}
			}
			
			//Relationship manually
			if(node_maxime!=null&&node_florian!=null&&node_didier!=null&&node_vioda!=null){
				System.out.println("will add relationships");
				
				Relationship r_m_f = (node_maxime).createRelationshipTo(node_florian, RelTypes.KNOWS);
				r_m_f.setProperty("knows-weight", 10);
				Relationship r_m_d = (node_maxime).createRelationshipTo(node_didier, RelTypes.KNOWS);
				r_m_d.setProperty("knows-weight", 4);
				
				Relationship r_f_m = (node_florian).createRelationshipTo(node_maxime, RelTypes.KNOWS);
				r_f_m.setProperty("knows-weight", 8);
				Relationship r_f_d = (node_florian).createRelationshipTo(node_didier, RelTypes.KNOWS);
				r_f_d.setProperty("knows-weight", 7);
				Relationship r_f_v = (node_florian).createRelationshipTo(node_vioda, RelTypes.KNOWS);
				r_f_v.setProperty("knows-weight", 3);
				
				Relationship r_d_v = (node_didier).createRelationshipTo(node_vioda, RelTypes.KNOWS);
				r_d_v.setProperty("knows-weight", 6);
				
				Relationship r_v_d = node_vioda.createRelationshipTo(node_didier, RelTypes.KNOWS);
				r_v_d.setProperty("knows-weight", 3);
				Relationship r_v_f = node_vioda.createRelationshipTo(node_florian, RelTypes.KNOWS);
				r_v_f.setProperty("knows-weight", 9);
				
				
				
				//add relationships of Wifes
				Node wife1 = graphDatabaseService.createNode();
				wife1.setProperty("name", "Florence");
				wife1.setProperty("nationality", "French");
				
				Node wife2 = graphDatabaseService.createNode();
				wife2.setProperty("name", "Alice");
				wife2.setProperty("nationality", "English");
			
				Relationship r_wf_1 = node_maxime.createRelationshipTo(wife1, RelTypes.MARRYTO);
				Relationship r_wf_2 = node_florian.createRelationshipTo(wife2, RelTypes.MARRYTO);
			}
			
			//success transaction
			transaction.success();

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//close transaction
			transaction.finish();
		}
	}

	void shutdown(){
		graphDatabaseService.shutdown();
		System.out.print("shut down");
	}

	void removeData(){
		Transaction transaction = graphDatabaseService.beginTx();
		try{
			//delete all "first" Outgoing relations
			/*first.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();

			first.delete();
			second.delete();*/
			transaction.success();
		}finally{
			transaction.finish();
		}
	}
}
