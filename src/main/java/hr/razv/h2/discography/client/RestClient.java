package hr.razv.h2.discography.client;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import hr.razv.h2.discography.model.Album;

@Component("restClient")
public class RestClient {

	public static final String REST_SERVICE_URI = "http://localhost:8080/xDiscographyWebApp/rest_service";

	private static final Logger logger = Logger.getLogger(RestClient.class);
	
	RestTemplate restTemplate = new RestTemplate();
	
	// GET 
	@SuppressWarnings("unchecked")
	public List<Album> listAllAlbums( Long page ) {
		logger.info("I'm in listAllAlbums ()");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(REST_SERVICE_URI + "/album/")
												.queryParam("page", page);
		List<Album> listAlbum =  restTemplate.getForObject( builder.build().encode().toUri(), List.class );
		
		return listAlbum;
	}
	
	// GET 
	public Album getAlbum( int id ) {
		logger.info("I'm in getAlbum ()");
        Album album = restTemplate.getForObject( REST_SERVICE_URI + "/album/" + id, Album.class);
        return album;
    }
	
	// POST
    public void createAlbum( Album album ) {
    	logger.info("I'm in createAlbum ()");
        restTemplate.postForLocation( REST_SERVICE_URI + "/album/", album, Album.class);
    }
	
    // DELETE 
    public void deleteAlbum( int id ) {
    	logger.info("I'm in deleteAlbum ()");
        restTemplate.delete( REST_SERVICE_URI + "/album/" + id );
    }
    
    // PUT
    public void updateAlbum( int id, Album album ) {
    	logger.info("I'm in updateAlbum ()");
    	restTemplate.put( REST_SERVICE_URI + "/album/" + id, album, Album.class );
    }
    
    // DELETE 
    public void deleteAllAlbums() {
    	logger.info("I'm in deleteAllAlbums ()");
        restTemplate.delete(REST_SERVICE_URI + "/album/" );
    }

    // GET JSON FOR DWNL
	public String getJson( int id ) {
		logger.info("I'm in getJson ()");
        String json = restTemplate.getForObject( REST_SERVICE_URI + "/album/" + id, String.class);
        return json;
    }

}
