package hr.razv.h2.discography.service;

import java.util.List;

import hr.razv.h2.discography.model.Album;

public interface AlbumService {

	public void initDB();

	public void fillMockData();

	public Album findById(int id);

	public List<Album> findByTitle(String title);

	public void addAlbum(Album album);

	public void updateAlbum(Album album);

	public void deleteAlbumById(int id);

	public List<Album> findAllAlbums(Long page);

	public void deleteAllAlbums();

	public boolean isAlbumExist(Album album);
	
	public long tableEntryCount();

}
