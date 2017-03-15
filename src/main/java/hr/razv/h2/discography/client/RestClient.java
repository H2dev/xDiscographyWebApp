package hr.razv.h2.discography.client;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import hr.razv.h2.discography.model.AlbumDTO;
import hr.razv.h2.discography.model.FilteringCriteria;

@Component("restClient")
public class RestClient {

//	public static final String REST_SERVICE_URI = "http://xdiscography-razvhr.rhcloud.com/rest_service";
	public static final String REST_SERVICE_URI = "http://localhost:8080/xDiscographyWebApp/rest_service";
	
	private static final Logger logger = Logger.getLogger(RestClient.class);

	RestTemplate restTemplate = new RestTemplate();

	// GET
	@SuppressWarnings("unchecked")
	public List<AlbumDTO> listAllAlbums(Long page, FilteringCriteria filteringCriteria) {
				
		logger.info("I'm in listAllAlbums ()");
		
		String orderBy = filteringCriteria.getOrderBy() != null ? filteringCriteria.getOrderBy() : null;
		String sortAsc = filteringCriteria.getSortAsc() != null ? filteringCriteria.getSortAsc() : null;
		String title = filteringCriteria.getTitle() != null ? filteringCriteria.getTitle() : null;
		String artist = filteringCriteria.getArtist() != null ? filteringCriteria.getArtist() : null;
		Integer year = filteringCriteria.getYear() != null ? filteringCriteria.getYear() : null;
		String track = filteringCriteria.getTrack() != null ? filteringCriteria.getTrack() : null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(REST_SERVICE_URI + "/album/");

		if (page != null)
			builder.queryParam("page", page);

		if (title != null && title != ""
				&& !title.isEmpty())
			builder.queryParam("title", title);

		if (artist != null && artist != ""
				&& !artist.isEmpty())
			builder.queryParam("artist", artist);

		if (track != null && track != ""
				&& !track.isEmpty())
			builder.queryParam("track", track);

		if (year != null)
			builder.queryParam("year", year);

		if (orderBy != null && orderBy != "" && !orderBy.isEmpty())
			builder.queryParam("orderBy", orderBy);

		if (sortAsc != null && sortAsc != "" && !sortAsc.isEmpty())
			builder.queryParam("sortAsc", sortAsc);

		List<AlbumDTO> listAlbum = restTemplate.getForObject(builder.build().encode().toUri(), List.class);

		return listAlbum;
	}

	// GET
	public AlbumDTO getAlbum(int id) {
		logger.info("I'm in getAlbum ()");
		AlbumDTO album = restTemplate.getForObject(REST_SERVICE_URI + "/album/" + id, AlbumDTO.class);
		return album;
	}

	// POST
	public void createAlbum(AlbumDTO album) {
		logger.info("I'm in createAlbum ()");
		restTemplate.postForLocation(REST_SERVICE_URI + "/album/", album, AlbumDTO.class);
	}

	// DELETE
	public void deleteAlbum(int id) {
		logger.info("I'm in deleteAlbum ()");
		restTemplate.delete(REST_SERVICE_URI + "/album/" + id);
	}

	// PUT
	public void updateAlbum(int id, AlbumDTO album) {
		logger.info("I'm in updateAlbum ()");
		restTemplate.put(REST_SERVICE_URI + "/album/" + id, album, AlbumDTO.class);
	}

	// DELETE
	public void deleteAllAlbums() {
		logger.info("I'm in deleteAllAlbums ()");
		restTemplate.delete(REST_SERVICE_URI + "/album/");
	}

	// GET JSON FOR DWNL
	public String getJson(int id) {
		logger.info("I'm in getJson ()");
		String json = restTemplate.getForObject(REST_SERVICE_URI + "/album/" + id, String.class);
		return json;
	}

}
