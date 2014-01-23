package general;
import com.hp.hpl.jena.vocabulary.RDF;


public class Outil {
	
	public static final String NL = System.getProperty("line.separator") ;

	public static String toMyString(String param){
		
		return "'"+param+"'";	
	}
	
	
	public static String createPrefixe(){
		
		//D2RQ
		//String prolog_vocab = "PREFIX vocab: <http://www.lirmm.fr/vocab#>";
		String prolog_vocab = "PREFIX vocab: <http://www.findevent.fr/vocab#>";
		String prolog_rdf = "PREFIX rdf: <"+RDF.getURI()+">" ;
		String prolog_db = "PREFIX db: <http://localhost:2020/resource/>";
		String prolog_rdfs = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
		String prolog_owl = "PREFIX owl: <http://www.w3.org/2002/07/owl#>";
		String prolog_map = "PREFIX map: <http://localhost:2020/resource/#>";
		String prolog_xsd = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>";
		
		//TDB

    	String prolog_gn = "PREFIX geonames: <http://www.geonames.org/ontology#>" ;
        //String prolog_rdf = "PREFIX rdf: <"+RDF.getURI()+">" ;
        String prolog_skos = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>" ;
        String prolog_geo = "PREFIX geo: <http://rdf.insee.fr/geo/>";
		
		
		//Pour mongo
		String prolog_mgo = "PREFIX mgo:<http://www.mymongo.fr#>";
		
		
		//Pour Neo4j
		
		
		return prolog_vocab+NL+prolog_rdf+NL+prolog_db+NL+prolog_rdfs+NL+
				prolog_owl+NL+prolog_map+NL+prolog_xsd+NL+prolog_gn+NL+prolog_skos+NL+
				prolog_geo+NL+prolog_mgo+NL;
	}
	
	
	
}
