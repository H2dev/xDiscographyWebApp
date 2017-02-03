package hr.razv.h2.discography.dao;

import java.util.List;

import hr.razv.h2.discography.model.Album;

public interface AlbumDAO<T> {
	
	public void initDB ();
	
	public void fillMockData();
	
	public void addAlbum ( Album album );
	
	public Album findById(int id);
	
	public List<Album> findAllAlbums( Long page );
	
    public List<Album> findByTitle(String title);

    public void updateAlbum(Album album);
    
    public void deleteAlbumById(int id);
     
    public void deleteAllAlbums();
     
    public boolean isAlbumExist(Album album);

	public long tableEntryCount();
    
}
