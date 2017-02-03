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

import com.fasterxml.jackson.databind.ObjectMapper;

import hr.razv.h2.discography.business.BusinessService;
import hr.razv.h2.discography.client.RestClient;
import hr.razv.h2.discography.model.Album;
import hr.razv.h2.discography.service.AlbumService;

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

		logger.info("I'm in welcome controller");

		ModelAndView mv = new ModelAndView(WELCOME_VIEW);
		albumService.initDB();

		return mv;
	}

	// FILL IN WITH MOCK DATA
	@RequestMapping(value = "/fillInWithMockData", method = RequestMethod.POST)
	public ModelAndView fillInWithMockData() {

		logger.info("I'm in fillInWithMockData controller");

		albumService.initDB();
		albumService.fillMockData();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/album");

		return mv;
	}

	// LIST ALBUMS
	@RequestMapping(value = "/album", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request,
			@RequestParam(value = "returningFromView", required = false) String returningFromView,
			@Param("page") Long page) {

		logger.info("I'm in main controller");

		long fullListSize = albumService.tableEntryCount();
		int maxNumberOfPages = businessService.getMaxNumberOfPages((int) fullListSize);

		if (returningFromView != null) {
			if (returningFromView.equals("true")) {
				page = (Long) request.getSession().getAttribute("currentPage");
			}
		} else if (page == null) {
			page = (long) 1;
		}

		List<Album> albumList = new ArrayList<Album>();
		albumList = restClient.listAllAlbums(page);

		ModelAndView mv = new ModelAndView(MAIN_VIEW);
		mv.addObject("album_list", albumList);
		request.getSession().setAttribute("currentPage", page);
		mv.addObject("maxNumberOfPages", maxNumberOfPages);

		return mv;
	}

	// FORM FOR ADDING AN ALBUM
	@RequestMapping(value = "/addNewAlbum", method = RequestMethod.GET)
	public ModelAndView addNewAlbum() {

		logger.info("I'm in addNewAlbum controller");

		Album newAlbum = new Album();
		ModelAndView mv = new ModelAndView(ADD_NEW_ALBUM_VIEW);
		mv.addObject("album", newAlbum);

		return mv;
	}

	// ALBUM TO ADD
	@RequestMapping(value = "/album", method = RequestMethod.POST)
	public ModelAndView albumToAdd(@ModelAttribute("album") Album album) {

		logger.info("I'm in albumToAdd controller");

		restClient.createAlbum(album);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/album/");

		return mv;
	}

	// ALBUM VIEW
	@RequestMapping(value = "/album/{id}", method = RequestMethod.GET)
	public ModelAndView albumView(@PathVariable("id") int id) {

		logger.info("I'm in albumView controller");

		ModelAndView mv = new ModelAndView(ALBUM_VIEW);
		Album album = restClient.getAlbum(id);
		mv.addObject("album", album);
		
		return mv;
	}

	// DELETE ALBUM
	@RequestMapping(value = "/albumDelete/{id}", method = RequestMethod.POST)
	public ModelAndView albumDelete(@PathVariable("id") int id) {

		logger.info("I'm in albumDelete controller");

		restClient.deleteAlbum(id);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/album");

		return mv;
	}

	// FORM FOR EDITING AN ALBUM
	@RequestMapping(value = "/editAlbum/{id}", method = RequestMethod.GET)
	public ModelAndView editAlbum(@PathVariable("id") int id) {

		logger.info("I'm in editAlbum controller");

		ModelAndView mv = new ModelAndView(EDIT_ALBUM_VIEW);
		Album album = restClient.getAlbum(id);
		mv.addObject("album", album);

		return mv;
	}

	// ALBUM TO UPDATE
	@RequestMapping(value = "/updateAlbum", method = RequestMethod.POST)
	public ModelAndView updateAlbum(@ModelAttribute("album") Album album) {

		logger.info("I'm in updateAlbum controller");

		restClient.updateAlbum(album.getId(), album);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/album/" + album.getId());

		return mv;
	}

	// DELETE ALL ALBUMS
	@RequestMapping(value = "/deleteAllAlbums", method = RequestMethod.POST)
	public ModelAndView deleteAllAlbums() {

		logger.info("I'm in deleteAllAlbums controller");

		restClient.deleteAllAlbums();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/album");

		return mv;
	}

	// JSON DOWNLOAD
	@RequestMapping(value = "/albumAsJson/{id}", method = RequestMethod.GET)
	public void jsonDwn(HttpServletResponse response, @PathVariable("id") int id) throws IOException {

		logger.info("I'm in jsonDwn controller");
		
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
