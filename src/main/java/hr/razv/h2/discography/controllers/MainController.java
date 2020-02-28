package hr.razv.h2.discography.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import hr.razv.h2.discography.client.RestClient;
import hr.razv.h2.discography.model.AlbumDTO;
import hr.razv.h2.discography.model.FilteringCriteria;
import hr.razv.h2.discography.service.AlbumService;
import hr.razv.h2.discography.service.BusinessService;

@Controller
public class MainController {

	@Autowired
	AlbumService albumService;
	@Autowired
	RestClient restClient;
	@Autowired
	BusinessService businessService;

	private static final String MAIN_VIEW = "main";
	private static final String ALBUM_VIEW = "album";
	private static final String WELCOME_VIEW = "welcome";
	private static final String ADD_NEW_ALBUM_VIEW = "add_new_album";
	private static final String EDIT_ALBUM_VIEW = "edit_album";

	private static final Logger logger = Logger.getLogger(MainController.class);

	// WELCOME PAGE
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView welcome() {

		ModelAndView mv = new ModelAndView(WELCOME_VIEW);

		return mv;
	}

	// FILL IN WITH MOCK DATA
	@RequestMapping(value = "/fillInWithMockData", method = RequestMethod.POST)
	public ModelAndView fillInWithMockData() {

		albumService.fillMockData();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/album");

		return mv;
	}

	// LIST ALBUMS
	@RequestMapping(value = "/album", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request, @Param("page") Long page, @Param("orderBy") String orderBy,
			@Param("sortAsc") String sortAsc, @Param("title") String title, @Param("artist") String artist,
			@Param("year") Integer year, @Param("track") String track) {

		if ( page == null ) {
			page = (long) 1;
		}
		
		if ( orderBy == null || orderBy == "" || orderBy.isEmpty() || sortAsc == null || sortAsc == "" || sortAsc.isEmpty() ) {
			orderBy = null;
			sortAsc = null;
		}

		FilteringCriteria filteringCriteria = new FilteringCriteria(orderBy, sortAsc, title, artist, year, track);
		ModelAndView mv = new ModelAndView(MAIN_VIEW);

		int maxNumberOfPages = businessService
				.getMaxNumberOfPages(albumService.countAlbumsWithFilter(filteringCriteria));
		mv.addObject("maxNumberOfPages", maxNumberOfPages);
		if (page > maxNumberOfPages) {
			page = (long) maxNumberOfPages;
		}
		request.getSession().setAttribute("currentPage", page);

		List<AlbumDTO> albumList = new ArrayList<AlbumDTO>();
		albumList = restClient.listAllAlbums(page, filteringCriteria);

		mv.addObject("album_list", albumList);
		mv.addObject("albumDbEntriesCount", albumService.tableEntryCount());
		mv.addObject("filteringCriteria", filteringCriteria);

		return mv;
	}

	// URI BUILDER FOR ALBUMS LIST
	@RequestMapping(value = "/albumsUriBuilder", method = RequestMethod.GET)
	public ModelAndView albumsUriBuilder(HttpServletRequest request,
			@ModelAttribute("filteringCriteria") FilteringCriteria filteringCriteria,
			@RequestParam(value = "page", required = false) Long page) {

		if ( page == null ) {
			page = (Long) request.getSession().getAttribute("currentPage");
		}

		if ( filteringCriteria.getOrderBy() == null || filteringCriteria.getOrderBy() == "" || filteringCriteria.getOrderBy().isEmpty() || filteringCriteria.getSortAsc() == null || filteringCriteria.getSortAsc() == "" || filteringCriteria.getSortAsc().isEmpty() ) {
			filteringCriteria.setOrderBy(null);
			filteringCriteria.setSortAsc(null);
		}
		
		if (businessService.areFilteringCriteriaEmpty(filteringCriteria)) {
			if (request.getSession().getAttribute("filteringCriteria") != null) {
				filteringCriteria = (FilteringCriteria) request.getSession().getAttribute("filteringCriteria");
			}
		} else {
			request.getSession().setAttribute("filteringCriteria", filteringCriteria);
		}

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("album");

		if (page != null && page != (long)0)
			builder.queryParam("page", page);

		if (filteringCriteria.getTitle() != null && filteringCriteria.getTitle() != ""
				&& !filteringCriteria.getTitle().isEmpty())
			builder.queryParam("title", filteringCriteria.getTitle());

		if (filteringCriteria.getArtist() != null && filteringCriteria.getArtist() != ""
				&& !filteringCriteria.getArtist().isEmpty())
			builder.queryParam("artist", filteringCriteria.getArtist());

		if (filteringCriteria.getTrack() != null && filteringCriteria.getTrack() != ""
				&& !filteringCriteria.getTrack().isEmpty())
			builder.queryParam("track", filteringCriteria.getTrack());

		if (filteringCriteria.getYear() != null)
			builder.queryParam("year", filteringCriteria.getYear());

		if (filteringCriteria.getOrderBy() != null && filteringCriteria.getOrderBy() != ""
				&& !filteringCriteria.getOrderBy().isEmpty())
			builder.queryParam("orderBy", filteringCriteria.getOrderBy());

		if (filteringCriteria.getSortAsc() != null && filteringCriteria.getSortAsc() != ""
				&& !filteringCriteria.getSortAsc().isEmpty())
			builder.queryParam("sortAsc", filteringCriteria.getSortAsc());

		String uriString = builder.toUriString();

		ModelAndView mv = new ModelAndView();
		mv.addObject("filteringCriteria", filteringCriteria);
		mv.setViewName("redirect:/" + uriString);

		return mv;
	}

	// FORM FOR ADDING AN ALBUM
	@RequestMapping(value = "/addNewAlbum", method = RequestMethod.GET)
	public ModelAndView addNewAlbum() {

		AlbumDTO newAlbum = new AlbumDTO();
		ModelAndView mv = new ModelAndView(ADD_NEW_ALBUM_VIEW);
		mv.addObject("album", newAlbum);

		return mv;
	}

	// ALBUM TO ADD
	@RequestMapping(value = "/album", method = RequestMethod.POST)
	public ModelAndView albumToAdd(@ModelAttribute("album") AlbumDTO album) {

		restClient.createAlbum(album);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/albumsUriBuilder");

		return mv;
	}

	// ALBUM VIEW
	@RequestMapping(value = "/album/{id}", method = RequestMethod.GET)
	public ModelAndView albumView(@PathVariable("id") int id) {

		ModelAndView mv = new ModelAndView(ALBUM_VIEW);
		AlbumDTO album = restClient.getAlbum(id);
		mv.addObject("album", album);

		return mv;
	}

	// DELETE ALBUM
	@RequestMapping(value = "/albumDelete/{id}", method = RequestMethod.POST)
	public ModelAndView albumDelete(@PathVariable("id") int id) {

		restClient.deleteAlbum(id);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/albumsUriBuilder");

		return mv;
	}

	// FORM FOR EDITING AN ALBUM
	@RequestMapping(value = "/editAlbum/{id}", method = RequestMethod.GET)
	public ModelAndView editAlbum(@PathVariable("id") int id) {

		ModelAndView mv = new ModelAndView(EDIT_ALBUM_VIEW);
		AlbumDTO album = restClient.getAlbum(id);
		mv.addObject("album", album);

		return mv;
	}

	// ALBUM TO UPDATE
	@RequestMapping(value = "/updateAlbum", method = RequestMethod.POST)
	public ModelAndView updateAlbum(@ModelAttribute("album") AlbumDTO album) {

		restClient.updateAlbum(album.getId(), album);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/album/" + album.getId());

		return mv;
	}

	// DELETE ALL ALBUMS
	@RequestMapping(value = "/deleteAllAlbums", method = RequestMethod.POST)
	public ModelAndView deleteAllAlbums() {

		restClient.deleteAllAlbums();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/album");

		return mv;
	}

	// JSON DOWNLOAD
	@RequestMapping(value = "/albumAsJson/{id}", method = RequestMethod.GET)
	public void jsonDwn(HttpServletResponse response, @PathVariable("id") int id) throws IOException {

		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = restClient.getJson(id);
			Object objectJson = mapper.readValue(json, Object.class);
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectJson);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.APPLICATION_JSON);
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment;filename=Json_Album_#" + id + ".txt");
			ServletOutputStream out = response.getOutputStream();
			out.println(json);
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
