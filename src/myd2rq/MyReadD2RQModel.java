package myd2rq;

import com.hp.hpl.jena.rdf.model.Model;

import de.fuberlin.wiwiss.d2rq.jena.ModelD2RQ;

public class MyReadD2RQModel {
	public static Model getD2RQModel(){
		Model d2rqModel = new ModelD2RQ("file:mapping_annotation.n3");
		//Model d2rqModel = new ModelD2RQ("file:mapping_gdc.n3");
		return d2rqModel;
	}
}
