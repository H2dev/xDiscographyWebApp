package hr.razv.h2.discography.model;

import java.io.Serializable;
import java.util.List;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class AlbumDTO implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 818428608983402833L;
	
	public AlbumDTO() {
	}

	private int id;
	
	private String title;

	private Integer year;

	private String artist;
	
	private List<String> tracklist;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public List<String> getTracklist() {
		return tracklist;
	}

	public void setTracklist(List<String> tracklist) {
		this.tracklist = tracklist;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
