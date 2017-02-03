package hr.razv.h2.discography.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.razv.h2.discography.business.BusinessService;
import hr.razv.h2.discography.dao.AlbumDAO;
import hr.razv.h2.discography.model.Album;

@Service("albumService")
@Transactional
public class AlbumServiceImpl implements AlbumService {

	@SuppressWarnings("rawtypes")
	@Autowired
	AlbumDAO albumDao;
	@Autowired
	BusinessService businessService;
		
	@Override
	public void initDB() {
		albumDao.initDB();
	}

	@Override
	public void fillMockData() {
		albumDao.fillMockData();
	}
	
	@Override
	public Album findById(int id) {
		Album album = albumDao.findById(id);
		List<String> tracklistJavaList = businessService.parseTracklistString(album.getTracklist());
		album.setTracklistJavaList(tracklistJavaList);
		return album;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Album> findByTitle(String title) {
		return albumDao.findByTitle(title);
	}

	@Override
	public void addAlbum(Album album) {
		String tracklistRawString = businessService.parseTracklistJavaList(album.getTracklistJavaList());
		album.setTracklist(tracklistRawString);
		albumDao.addAlbum(album);
	}

	@Override
	public void updateAlbum(Album album) {
		String tracklistRawString = businessService.parseTracklistJavaList(album.getTracklistJavaList());
		album.setTracklist(tracklistRawString);
		albumDao.updateAlbum(album);
	}

	@Override
	public void deleteAlbumById(int id) {
		albumDao.deleteAlbumById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Album> findAllAlbums(Long page) {
		return albumDao.findAllAlbums(page);
	}

	@Override
	public void deleteAllAlbums() {
		albumDao.deleteAllAlbums();
	}

	@Override
	public boolean isAlbumExist(Album album) {
		return albumDao.isAlbumExist(album);
	}

	@Override
	public long tableEntryCount() {
		return albumDao.tableEntryCount();
	}
	
}
