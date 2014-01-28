package general;

public class QueryStringFactory {

	public static final String NL = System.getProperty("line.separator") ;

	//For D2RQ
	public static String createQueryString(int num){
		String result = "";
		if(num==0){

		}else if(num==1){
			//user qui ont annote un evenement avec datetime inferieur 2014-01-24, city
			result+=Outil.createPrefixe();
			result+="SELECT ?nom_user ?commentaire ?status ?event_id ?datetime ?city"+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?user rdf:type d2rqevent:user."+NL+
					"?event rdf:type d2rqevent:event."+NL+
					"?annotation rdf:type d2rqevent:annotation."+NL+
					"?user d2rqevent:user_username ?nom_user."+NL+
					"?annotation d2rqevent:annotation_ref_user ?user."+NL+
					"?annotation d2rqevent:annotation_ref_event ?event."+NL+
					"?annotation d2rqevent:annotation_comment ?commentaire."+NL+
					"?event d2rqevent:event_name ?event_name."+NL+

					"?event_mongo rdf:type mgoevent:event."+NL+
					"?event_mongo mgoevent:city ?city."+NL+
					"?event_mongo mgoevent:ticketStatus ?status."+NL+
					"?event_mongo mgoevent:eventId ?event_id."+NL+
					"?event_mongo mgoevent:datetime ?datetime."+NL+
					"FILTER(?event_id=?event_name&&?datetime<='2014-01-24')"+NL+
					"}";


		}else if(num==2){
			//Les artists qui connaissent 'Florian Gauthier' et l'evenement qu'il va participer
			result+=Outil.createPrefixe();
			result+="SELECT  ?artist_name ?artist_genre ?artist_instrument ?other_artist_name ?event_id ?city ?datetime"+NL+
					"WHERE"+NL+
					"{"+NL+ 

					"?artist rdf:type neoartist:artist."+NL+
					"?artist neoartist:knows ?other_artist."+NL+
					"?artist neoartist:name ?artist_name."+NL+
					"?artist neoartist:instrument ?artist_instrument."+NL+
					"?artist neoartist:genre ?artist_genre."+NL+
					"?other_artist neoartist:name ?other_artist_name."+NL+

					"?event rdf:type mgoevent:event."+NL+
					"?event mgoevent:participant ?artist_name."+NL+
					"?event mgoevent:eventId ?event_id."+NL+
					"?event mgoevent:city ?city."+NL+
					"?event mgoevent:datetime ?datetime."+NL+

					"FILTER(?other_artist_name='Florian Gauthier')"+NL+
					"}";

		}else if(num==3){
			//Les artists marriees et leurs femme, leurs amietes, et leurs nombre des amis
			result+=Outil.createPrefixe();
			result+="SELECT  ?artist_name ?wife_name ?wife_nationality (COUNT(?friend_artist_name) AS ?numOfFriends)"+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?artist rdf:type neoartist:artist."+NL+
					"?artist neoartist:knows ?other_artist."+NL+
					"?artist neoartist:name ?artist_name."+NL+
					"?other_artist neoartist:name ?friend_artist_name."+NL+
					"?artist neoartist:marries ?wife."+NL+
					"?wife neoartist:name ?wife_name."+NL+
					"?wife neoartist:nationality ?wife_nationality."+NL+
					"}"+NL+
					"GROUP BY ?artist_name ?wife_name ?wife_nationality";

		}else if(num==4){
			//Les information sur les evenements qui sont deja passe mais avec les tickets available, voir les annotations
			//associees a ces evenements

			result+=Outil.createPrefixe();
			result+="SELECT ?nom_user ?user_location ?commentaire ?status ?event_id ?city ?datetime"+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?user rdf:type d2rqevent:user."+NL+
					"?user d2rqevent:user_location ?user_location."+NL+
					"?event rdf:type d2rqevent:event."+NL+
					"?annotation rdf:type d2rqevent:annotation."+NL+
					"?user d2rqevent:user_username ?nom_user."+NL+
					"?annotation d2rqevent:annotation_ref_user ?user."+NL+
					"?annotation d2rqevent:annotation_ref_event ?event."+NL+
					"?annotation d2rqevent:annotation_comment ?commentaire."+NL+
					"?event d2rqevent:event_name ?event_name."+NL+

					"?event_mongo rdf:type mgoevent:event."+NL+
					"?event_mongo mgoevent:ticketStatus ?status."+NL+
					"?event_mongo mgoevent:eventId ?event_id."+NL+
					"?event_mongo mgoevent:city ?city."+NL+
					"?event_mongo mgoevent:datetime ?datetime."+NL+
					"FILTER(?event_id=?event_name&&?status='available'&&?datetime<='2014-01-27')"+NL+
					"}";



		}else if(num==5){
			//Les evenemts qui sont vont/sont deja passee entre 2014-01-01 et 2014-01-28, regroupee par nombre des participant >=3 
			result+=Outil.createPrefixe();
			result+="SELECT ?event_id ?datetime ?city ?status (COUNT(?participant) AS ?numOfArtists)"+NL+
					"WHERE"+NL+
					"{"+NL+ 

					"?event_mongo rdf:type mgoevent:event."+NL+
					"?event_mongo mgoevent:ticketStatus ?status."+NL+
					"?event_mongo mgoevent:eventId ?event_id."+NL+
					"?event_mongo mgoevent:datetime ?datetime."+NL+
					"?event_mongo mgoevent:city ?city."+NL+
					"?event_mongo mgoevent:participant ?participant."+NL+
					
					"?dep geo:nom ?nom_dep."+NL+
					"?dep geo:chef-lieu ?chef_lieu."+NL+
					"?chef_lieu geo:nom ?city."+NL+
					
					
					"FILTER(?datetime<='2014-01-28' && ?datetime>='2014-01-01')"+NL+
					"}"+NL+
					"GROUP BY ?event_id ?datetime ?city ?status HAVING(COUNT(?participant) >=3)"+NL+
					"ORDER BY DESC(?city)"
					;
		}
		
		//Test purpose..
		else if(num==21){
			// Find usernames in MySQL
			result+=Outil.createPrefixe();
			result+="SELECT  ?nom_user"+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?user rdf:type d2rqevent:user."+NL+
					"?user d2rqevent:user_username ?nom_user."+NL+
					"}";

		}else if(num==22){
			//Find artists who knows others
			result+=Outil.createPrefixe();
			result+="SELECT  ?artist ?other_artist"+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?artist rdf:type neoartist:artist."+NL+
					"?artist neoartist:knows ?other_artist."+NL+
					"}";

		}else if (num==23){
			result+=Outil.createPrefixe();
			result+="SELECT DISTINCT ?city_name ?event_id"+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?event mgoevent:city ?city_name."+NL+
					"?event mgoevent:eventId ?event_id."+NL+
					//"FILTER(?city_name='Paris')"+NL+
					"}"+NL+
					"ORDER BY DESC(?city_name)"+NL
					;
		}else if(num==24){
			//Find events info and related annotation info
			result+=Outil.createPrefixe();
			result+="SELECT ?nom_user ?user_location ?commentaire ?status ?event_id "+NL+
					"WHERE"+NL+
					"{"+NL+ 
					"?user rdf:type d2rqevent:user."+NL+
					"?user d2rqevent:user_location ?user_location."+NL+
					"?event rdf:type d2rqevent:event."+NL+
					"?annotation rdf:type d2rqevent:annotation."+NL+
					"?user d2rqevent:user_username ?nom_user."+NL+
					"?annotation d2rqevent:annotation_ref_user ?user."+NL+
					"?annotation d2rqevent:annotation_ref_event ?event."+NL+
					"?annotation d2rqevent:annotation_comment ?commentaire."+NL+
					"?event d2rqevent:event_name ?event_name."+NL+

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
