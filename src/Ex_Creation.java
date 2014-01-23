
import com.hp.hpl.jena.query.Dataset ;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;

public class Ex_Creation
{
	public static final String rdf_file = "departement.rdf";
    public static void main(String[] args)
    {
        // Direct way: Make a TDB-back Jena model in the named directory.
        String directory = "/home/cgao/Travail/xx/MyBase1" ;
        Dataset ds = TDBFactory.createDataset(directory) ;
        Model model = ds.getDefaultModel() ;
        FileManager.get().readModel( model, rdf_file );
        System.out.println("nombre de triplets : "+model.size());
        model.write(System.out,"N3");
        ds.close();
        
    }
}