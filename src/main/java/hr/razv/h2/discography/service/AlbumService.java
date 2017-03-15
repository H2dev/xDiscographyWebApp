package hr.razv.h2.discography.service;

import java.util.List;

import hr.razv.h2.discography.model.AlbumDTO;
import hr.razv.h2.discography.model.FilteringCriteria;

public interface AlbumService {

	public void initDB();

	public void fillMockData();

	public AlbumDTO findById(int id);

	public List<AlbumDTO> findByTitle(String title);

	public void addAlbum(AlbumDTO albumDto);

	public void updateAlbum(AlbumDTO albumDto);

	public void deleteAlbumById(int id);

	public List<AlbumDTO> findAllAlbums(Long page, FilteringCriteria filteringCriteria);
	
	public void deleteAllAlbums();

	public boolean isAlbumExist(int id);
	
	public long tableEntryCount();

	Integer countAlbumsWithFilter(FilteringCriteria filteringCriteria);

}
