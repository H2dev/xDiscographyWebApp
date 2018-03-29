package hr.razv.h2.discography.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Album implements Serializable {
		
	private static final long serialVersionUID = 818428608983402833L;

	public static final String TITLE_FIELD = "title";
	public static final String YEAR_FIELD = "year";
	public static final String ARTIST_FIELD = "artist";
	public static final String TRACKLIST_FIELD = "tracklist";
	
	public Album() {
	}

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(columnName = TITLE_FIELD, canBeNull = false)
	private String title;

	@DatabaseField(columnName = YEAR_FIELD, canBeNull = true)
	private Integer year;

	@DatabaseField(columnName = ARTIST_FIELD, canBeNull = true)
	private String artist;

	@DatabaseField(columnName = TRACKLIST_FIELD, canBeNull = true, width=8000)
	private String tracklist;

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

	public String getTracklist() {
		return tracklist;
	}

	public void setTracklist(String tracklist) {
		this.tracklist = tracklist;
	}


}
