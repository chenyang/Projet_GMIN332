package mytdb;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;

public class MyReadTDBModel {
	public static final String rdf_file_1 = "departement.rdf";
	public static final String rdf_file_2 = "region.rdf";
	public static Model getTDBModel()
	{
		// Direct way: Make a TDB-back Jena model in the named directory.
		String directory = "/home/cgao/Travail/Projet_GMIN332/MyTDB_Base" ;
		Dataset ds = TDBFactory.createDataset(directory) ;
		Model model = ds.getNamedModel( "geo+region" ); 
		FileManager.get().readModel( model, rdf_file_1 );
		FileManager.get().readModel( model, rdf_file_2 );
		//ds.close();
		return model;
	}
}
