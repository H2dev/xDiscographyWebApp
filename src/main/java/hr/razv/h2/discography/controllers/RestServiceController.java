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

import hr.razv.h2.discography.model.Album;
import hr.razv.h2.discography.service.AlbumService;

@RestController
public class RestServiceController {

	private static final Logger logger = Logger.getLogger(RestServiceController.class);

	@Autowired
	AlbumService albumService;

	// GET ALL ALBUMS
	@RequestMapping(value = "/rest_service/album/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Album>> restServiceGetAllAlbums(@Param(value = "page") Integer page) {

		logger.info("Getting all albums.");

		if (page == null) {
			// index for getting the full list
			page = -1;
		}
		List<Album> listAlbums = albumService.findAllAlbums((long) page);
		if (listAlbums.isEmpty()) {
			return new ResponseEntity<List<Album>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Album>>(listAlbums, HttpStatus.OK);
	}

	// GET ALBUM
	@RequestMapping(value = "/rest_service/album/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Album> restServiceGetAlbum(@PathVariable("id") int id) {

		logger.info("Getting album with id: " + id);

		Album album = albumService.findById(id);
		if (album == null) {
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Album>(album, HttpStatus.OK);
	}

	// CREATE ALBUM
	@RequestMapping(value = "/rest_service/album/", method = RequestMethod.POST)
	public ResponseEntity<Void> restServiceCreateAlbum(@RequestBody Album album, UriComponentsBuilder ucBuilder) {

		logger.info("Creating an album.");

		if (albumService.isAlbumExist(album)) {
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
	public ResponseEntity<Album> restServiceDeleteAlbum(@PathVariable("id") int id) {

		logger.info("Deleting the album with id: " + id);

		Album album = albumService.findById(id);
		if (album == null) {
			logger.info("Can't delete album with id: " + id);
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		}
		albumService.deleteAlbumById(id);
		return new ResponseEntity<Album>(HttpStatus.NO_CONTENT);
	}

	// DELETE ALL ALBUMS
	@RequestMapping(value = "/rest_service/album/", method = RequestMethod.DELETE)
	public ResponseEntity<Album> restServiceDeleteAllAlbums() {

		logger.info("Deleting all albums.");

		albumService.deleteAllAlbums();
		return new ResponseEntity<Album>(HttpStatus.NO_CONTENT);
	}

	// UPDATE ALBUM
	@RequestMapping(value = "/rest_service/album/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Album> restServiceUpdateAlbum(@PathVariable("id") int id, @RequestBody Album album) {

		logger.info("Updating the album with id: " + id);

		Album albumToUpdate = albumService.findById(id);
		if (albumToUpdate == null) {
			logger.error("Album can't be found. Id: " + id);
			return new ResponseEntity<Album>(HttpStatus.NOT_FOUND);
		}
		album.setId(albumToUpdate.getId());
		albumService.updateAlbum(album);

		return new ResponseEntity<Album>(albumToUpdate, HttpStatus.OK);
	}

}
