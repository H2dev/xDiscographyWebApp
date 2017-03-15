package hr.razv.h2.discography.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import hr.razv.h2.discography.model.AlbumDTO;
import hr.razv.h2.discography.model.FilteringCriteria;
import hr.razv.h2.discography.service.AlbumService;

@RestController
public class RestServiceController {

	private static final Logger logger = Logger.getLogger(RestServiceController.class);

	@Autowired
	AlbumService albumService;

	// GET ALL ALBUMS
	@RequestMapping(value = "/rest_service/album/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AlbumDTO>> restServiceGetAllAlbums(
																@Param("page") Long page, 
																@Param("orderBy") String orderBy, 
																@Param("sortAsc") String sortAsc,
																@Param("title") String title, 
																@Param("artist") String artist, 
																@Param("year") Integer year, 
																@Param("track") String track) {

		logger.info("Getting all albums.");

		if (page == null) {
			// index for getting the full list
			page = (long) -1;
		}
		
		FilteringCriteria filteringCriteria = new FilteringCriteria(orderBy, sortAsc, title, artist, year, track);
		
		List<AlbumDTO> listAlbums = albumService.findAllAlbums( page, filteringCriteria);
		if (listAlbums.isEmpty()) {
			return new ResponseEntity<List<AlbumDTO>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<AlbumDTO>>(listAlbums, HttpStatus.OK);
	}

	// GET ALBUM
	@RequestMapping(value = "/rest_service/album/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AlbumDTO> restServiceGetAlbum(@PathVariable("id") int id) {

		logger.info("Getting album with id: " + id);

		AlbumDTO album = albumService.findById(id);
		if (album == null) {
			return new ResponseEntity<AlbumDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<AlbumDTO>(album, HttpStatus.OK);
	}

	// CREATE ALBUM
	@RequestMapping(value = "/rest_service/album/", method = RequestMethod.POST)
	public ResponseEntity<Void> restServiceCreateAlbum(@RequestBody AlbumDTO album, UriComponentsBuilder ucBuilder) {

		logger.info("Creating an album.");

		if (albumService.isAlbumExist(album.getId())) {
			logger.error("Album already exists. Id: " + album.getId());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		albumService.addAlbum(album);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/album/{id}").buildAndExpand(album.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	// DELETE ALBUM
	@RequestMapping(value = "/rest_service/album/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<AlbumDTO> restServiceDeleteAlbum(@PathVariable("id") int id) {

		logger.info("Deleting the album with id: " + id);

		AlbumDTO album = albumService.findById(id);
		if (album == null) {
			logger.info("Can't delete album with id: " + id);
			return new ResponseEntity<AlbumDTO>(HttpStatus.NOT_FOUND);
		}
		albumService.deleteAlbumById(id);
		return new ResponseEntity<AlbumDTO>(HttpStatus.NO_CONTENT);
	}

	// DELETE ALL ALBUMS
	@RequestMapping(value = "/rest_service/album/", method = RequestMethod.DELETE)
	public ResponseEntity<AlbumDTO> restServiceDeleteAllAlbums() {

		logger.info("Deleting all albums.");

		albumService.deleteAllAlbums();
		return new ResponseEntity<AlbumDTO>(HttpStatus.NO_CONTENT);
	}

	// UPDATE ALBUM
	@RequestMapping(value = "/rest_service/album/{id}", method = RequestMethod.PUT)
	public ResponseEntity<AlbumDTO> restServiceUpdateAlbum(@PathVariable("id") int id, @RequestBody AlbumDTO album) {

		logger.info("Updating the album with id: " + id);

		AlbumDTO albumToUpdate = albumService.findById(id);
		if (albumToUpdate == null) {
			logger.error("Album can't be found. Id: " + id);
			return new ResponseEntity<AlbumDTO>(HttpStatus.NOT_FOUND);
		}
		album.setId(albumToUpdate.getId());
		albumService.updateAlbum(album);

		return new ResponseEntity<AlbumDTO>(albumToUpdate, HttpStatus.OK);
	}

}
