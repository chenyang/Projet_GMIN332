package Neo4j;

import java.util.ArrayList;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.core.NodeManager;

public class NeoDBManagement {

	//Neo4j db path
	private static final String Neo4j_PATH = "/home/cgao/SERVERS/neo4j-community-2.0.0/data/graph.db";

	GraphDatabaseService graphDatabaseService = new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_PATH);
	ArrayList<Relationship> listOfRelations = new ArrayList<Relationship>();
	ArrayList<Node> listOfArtistNode = new ArrayList<Node>();
	Node node_maxime, node_florian, node_didier, node_vioda;
	ArrayList<Node> listOfWifes = new ArrayList<Node>();

	//list of relationships
	private static enum RelTypes implements RelationshipType{
		KNOWS, 
		MARRYTO
	}

	void showAllNodes(){

		Transaction transaction = graphDatabaseService.beginTx();
		try{
			for(Node n : graphDatabaseService.getAllNodes()){
				if(n.hasRelationship()){
					System.out.println(n.getProperty("name"));
					
					//Pour les outgoing
					for(Relationship rel:n.getRelationships(Direction.OUTGOING)){
						if(rel.isType(RelTypes.KNOWS)){
							System.out.println("out "+rel.getEndNode().getProperty("name").toString()+", "+ rel.getProperty("knows-weight"));
						}
					}				
					
				}
			}
			transaction.success();
		}finally{

		}
	}
	
	
	@SuppressWarnings("deprecation")
	void createDatabase(ArrayList<Artist> listOfArtists){

		System.out.println(listOfArtists.size());

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
				
				Relationship r_v_d = (node_vioda).createRelationshipTo(node_didier, RelTypes.KNOWS);
				r_v_d.setProperty("knows-weight", 3);
				Relationship r_v_f = (node_vioda).createRelationshipTo(node_florian, RelTypes.KNOWS);
				r_v_f.setProperty("knows-weight", 9);
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
