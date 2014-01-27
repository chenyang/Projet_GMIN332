package general;

public class QueryStringFactory {
	
	public static final String NL = System.getProperty("line.separator") ;
	
	//For D2RQ
	public static String createQueryString(int num){
		String result = "";
		if(num==0){

		}
		else if(num==1){
			// Find usernames in MySQL
			result+=Outil.createPrefixe();
			result+="SELECT  ?nom_user"+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?user rdf:type findevent:user."+NL+
					"?user findevent:user_username ?nom_user."+NL+
					"}";

		}else if(num==2){
			//Find artists who knows others
			result+=Outil.createPrefixe();
			result+="SELECT  ?artist ?other_artist"+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?artist rdf:type neoartist:artist."+NL+
					"?artist neoartist:knows ?other_artist."+NL+
					"}";

		}else if (num==3){
			
		}else if(num==99){
			//Find events info and related annotation info
			result+=Outil.createPrefixe();
			result+="SELECT ?nom_user ?commentaire ?status ?event_id"+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?user rdf:type findevent:user."+NL+
					"?event rdf:type findevent:event."+NL+
					"?annotation rdf:type findevent:annotation."+NL+
					"?user findevent:user_username ?nom_user."+NL+
					"?annotation findevent:annotation_ref_user ?user."+NL+
					"?annotation findevent:annotation_ref_event ?event."+NL+
					"?annotation findevent:annotation_comment ?commentaire."+NL+
					"?event findevent:event_name ?event_name."+NL+

					"?event_mongo rdf:type mgoevent:event."+NL+
					"?event_mongo mgoevent:ticketStatus ?status."+NL+
					"?event_mongo mgoevent:eventId ?event_id."+NL+
					"FILTER(?event_id=?event_name)"+NL+
					"}";

		}else{
			System.out.println("Please select a number");
		}
		return result;
	}


}
