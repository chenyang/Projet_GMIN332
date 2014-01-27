package mytdb;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;

public class MyReadTDBModel {
	public static final String rdf_file_1 = "assets/departement.rdf";
	public static final String rdf_file_2 = "assets/region.rdf";
	Dataset ds;
	public Model getTDBModel()
	{
		// Direct way: Make a TDB-back Jena model in the named directory.
		String directory = "databases/MyTDB_Base" ;
		ds = TDBFactory.createDataset(directory) ;
		Model model = ds.getDefaultModel();
		FileManager.get().readModel( model, rdf_file_1 );
		FileManager.get().readModel( model, rdf_file_2 );
		return model;
	}
	
}
