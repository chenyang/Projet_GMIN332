package general;

import org.openjena.atlas.io.IndentedWriter;

import myd2rq.MyReadD2RQModel;
import mymongodb.MyReadMongoModel;
import mytdb.MyReadTDBModel;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

import de.fuberlin.wiwiss.d2rq.jena.ModelD2RQ;

public class Program {
	private void requetteD2RQ(Model d2rqModel, int num){
		String queryString = QueryStringFactory.createQueryString(num);
		Query query = QueryFactory.create(queryString) ;
		// afficher la requete
		query.serialize(new IndentedWriter(System.out,true)) ;
		System.out.println() ;
		QueryExecution qexec = QueryExecutionFactory.create(query, d2rqModel) ;
		System.out.println("Les elements du modele : ") ;
		System.out.println("Vueillez patientez..");
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
		Model tdbModel = MyReadTDBModel.getTDBModel();
		System.out.println("nombre de triplets dans TDB: "+tdbModel.size());
		
		//Load D2RQ Model
		Model d2rqModel = MyReadD2RQModel.getD2RQModel();
		//System.out.println("nombre de triplets dans TDB: "+d2rqModel.size());
		
		//Load MongoDB Model
		Model mongoModel = new MyReadMongoModel().getModelWithDatabaseData();
		mongoModel.write(System.out, "RDF/XML-ABBREV");
		
		//Combination des models
		Model modelAll = tdbModel.union(d2rqModel).union(mongoModel);
		
		
		/**
		 * Les Requettes
		 */
		
		//Requette D2RQ
		//prog.requetteD2RQ(modelAll, 1);
		
		
	}

}
