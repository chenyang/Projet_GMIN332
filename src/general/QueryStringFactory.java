package general;

public class QueryStringFactory {
	public static final String NL = System.getProperty("line.separator") ;
	//Pour Mongo
	public static String createMongoString(int num){
		String result = "";	
		if(num==0){
			result="";
		}
		if(num==1){
			result+=Outil.createPrefixe();
			//Select countries who speak Ar
			result+="SELECT  ?country_name"+NL+
					"WHERE{"+NL+ 
					"?country rdf:type mgo:country."+NL+
					"?lang rdf:type mgo:language."+NL+
					"?lang mgo:nameOfLanguage "+Outil.toMyString("AR")+"."+NL+
					"?country mgo:hasLang ?lang."+NL+
					"?country mgo:nameOfCountry ?country_name"+NL+
					//"FILTER(?lang='French')"+NL+
					"}";
		}
		return result;
	}
	
	
	//Pour D2RQ
	public static String createQueryString(int num){
		String result = "";
		if(num==0){
			result+=Outil.createPrefixe();
			result+="SELECT  ?nom_com"+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?reg rdf:type vocab:region."+NL+
					"?com rdf:type vocab:cog_r."+NL+
					"?com vocab:cog_r_codeReg ?reg."+NL+
					"?reg vocab:region_ncc"+Outil.toMyString("LANGUEDOC-ROUSSILLON")+"."+NL+
					"?com vocab:cog_r_ncc ?nom_com."+NL+
					"}";
		}
		else if(num==1){
			// Les noms des users dans la base
			result+=Outil.createPrefixe();
			result+="SELECT  ?nom_user"+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?user rdf:type vocab:user."+NL+
					"?user vocab:user_username ?nom_user."+NL+
					"}";
			
		}else{
			System.out.println("Veuillez selectionner un bon numero");
		}
		return result;
	}

}
