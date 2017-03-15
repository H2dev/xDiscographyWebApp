package hr.razv.h2.discography.model;

public class FilteringCriteria {
	
	private String orderBy;
	
	private String sortAsc;

	private String title;
	
	private String artist;

	private Integer year;
	
	private String track;
	
	public FilteringCriteria() {
	}

	public FilteringCriteria(String orderBy, String sortAsc, String title, String artist, Integer year, String track) {
		this.orderBy = orderBy;
		this.sortAsc = sortAsc;
		this.title = title;
		this.artist = artist;
		this.year = year;
		this.track = track;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getSortAsc() {
		return sortAsc;
	}

	public void setSortAsc(String sortAsc) {
		this.sortAsc = sortAsc;
	}

	
	
}
