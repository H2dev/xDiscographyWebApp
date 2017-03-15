package hr.razv.h2.discography.dao;

import java.util.List;

import hr.razv.h2.discography.model.Album;
import hr.razv.h2.discography.model.FilteringCriteria;

public interface AlbumDAO<T> {
	
	public void initDB ();
	
	public void fillMockData();
	
	public void addAlbum ( Album album );
	
	public Album findById(int id);
	
	public List<Album> findAllAlbums( Long page, FilteringCriteria filteringCriteria );
	
    public List<Album> findByTitle(String title);

    public void updateAlbum(Album album);
    
    public void deleteAlbumById(int id);
     
    public void deleteAllAlbums();
     
    public boolean isAlbumExist(int id);

	public long tableEntryCount();

	public Integer countAlbumsWithFilter(FilteringCriteria filteringCriteria);
    
}
