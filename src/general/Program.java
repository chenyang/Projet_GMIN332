package general;

import myd2rq.MyReadD2RQModel;
import mymongodb.MyReadMongoModel;
import mytdb.MyReadTDBModel;

import org.openjena.atlas.io.IndentedWriter;

import Neo4j.MyReadNeoModel;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;

public class Program {
	
	private Model getMappingModel(String file_path){
		Model m = ModelFactory.createDefaultModel();
		FileManager.get().readModel( m, file_path);
		return m;
	}
	
	
	private void executeRequette(Model m, int num){
		String queryString = QueryStringGenerator.createQueryString(num);
		Query query = QueryFactory.create(queryString) ;
		//Show requette
		query.serialize(new IndentedWriter(System.out,true));
		System.out.println() ;
		QueryExecution qexec = QueryExecutionFactory.create(query, m) ;
		System.out.println("Elements of the model.. ") ;
		System.out.println("Please be patient..");
		try {
			ResultSet rs = qexec.execSelect() ;
			ResultSetFormatter.out(System.out, rs, query);
		}
		finally
		{
			qexec.close() ;
		}
	}
	
	
	public static void main(String[] args) {
		Program prog = new Program();

		//Load TDB Model
		MyReadTDBModel mytdb = new MyReadTDBModel();
		Model tdbModel = mytdb.getTDBModel();
		Outil.persistModel(tdbModel, "assets/outTDB.rdf");
		
		
		//Load D2RQ Model
		MyReadD2RQModel myd2rq = new MyReadD2RQModel();
		Model d2rqModel = myd2rq.getD2RQModel();
		Outil.persistModel(d2rqModel, "assets/outAnnotation.rdf");
		
		//Load MongoDB Model
		MyReadMongoModel mymongo = new MyReadMongoModel();
		Model mongoModel = mymongo.getModelWithDatabaseData();
		Outil.persistModel(mongoModel, "assets/outMongoEvent.rdf");
		
		//Load Neo4j
		MyReadNeoModel myneo = new MyReadNeoModel();
		//myneo.createDB();
		Model neomodel = myneo.getNeoModelWithData();
		Outil.persistModel(neomodel, "assets/outNeo.rdf");
		
		//Combination des models
		Model model_mapping = prog.getMappingModel("assets/mapping_d2rq_mongo.rdf");
		Model modelAllOrigin = tdbModel.union(d2rqModel).union(mongoModel).union(neomodel);
		
		Model modelAll = tdbModel.union(d2rqModel).union(mongoModel).union(neomodel).union(model_mapping);
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		InfModel im = ModelFactory.createInfModel(reasoner, modelAll);
		Outil.persistModel(im, "assets/All.rdf");
		
		
		/**
		 * Les Requettes
		 */
		
		/**D2rq et Mongodb**/
		//prog.executeRequette(im, 1);

		/**Neo4j et Mongodb**/
		//prog.executeRequette(modelAllOrigin,2);
		
		/**Neo4j**/
		//prog.executeRequette(modelAllOrigin,3);
		
		/**"D2rq et Mongo et TDB**/
		//prog.executeRequette(modelAllOrigin, 4);
		
		/**TDB et Mongo**/
		prog.executeRequette(modelAllOrigin, 5);
		
	}

}
