package hr.razv.h2.discography.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import hr.razv.h2.discography.model.Album;
import hr.razv.h2.discography.model.AlbumDTO;
import hr.razv.h2.discography.model.FilteringCriteria;

@Service("businessService")
public class BusinessService {

	public Album convertAlbumDTOtoAlbum(AlbumDTO albumDTO) {

		Album album = new Album();
		album.setId(albumDTO.getId());
		album.setTitle(albumDTO.getTitle() != null ? albumDTO.getTitle() : null);
		album.setArtist(albumDTO.getArtist() != null ? albumDTO.getArtist() : null);
		album.setYear(albumDTO.getYear() != null ? albumDTO.getYear() : null);
		album.setTracklist(parseTracklistJavaList(
				albumDTO.getTracklist() != null ? albumDTO.getTracklist() : null));

		return album;
	}

	public AlbumDTO convertAlbumtoAlbumDTO(Album album) {

		AlbumDTO albumDTO = new AlbumDTO();
		albumDTO.setId(album.getId());
		albumDTO.setTitle(album.getTitle() != null ? album.getTitle() : null);
		albumDTO.setArtist(album.getArtist() != null ? album.getArtist() : null);
		albumDTO.setYear(album.getYear() != null ? album.getYear() : null);
		albumDTO.setTracklist(parseTracklistString(album.getTracklist() != null ? album.getTracklist() : null));

		return albumDTO;
	}

	public List<AlbumDTO> convertAlbumListToAlbumDTOList(List<Album> albumList) {
		List<AlbumDTO> albumDTOList = new ArrayList<AlbumDTO>();
		if (albumList != null) {
			for (Album album : albumList) {
				albumDTOList.add(convertAlbumtoAlbumDTO(album));
			}
		}
		return albumDTOList;
	}

	public List<String> parseTracklistString(String tracklistDb) {

		List<String> trackListJavaList = new ArrayList<String>();
		if (tracklistDb != null) {
			String[] stringArray = tracklistDb.split("\\|");
			for (String string : stringArray) {
				if (!(string.equals(null) || string.replaceAll("\\s", "").isEmpty())) {
					trackListJavaList.add(string.trim());
				}
			}
		}
		return trackListJavaList;
	}

	public String parseTracklistJavaList(List<String> tracklistJavaList) {

		String tracklistDb = "";
		if (tracklistJavaList != null) {
			for (String track : tracklistJavaList) {
				tracklistDb = tracklistDb.concat("|").concat(track);
			}
		}
		tracklistDb = tracklistDb.substring(1);

		return tracklistDb;
	}

	public int getMaxNumberOfPages(int listSize) {

		int maxNumberOfPages = listSize / (int) ConstantsDiscography.ITEMS_PER_PAGE;
		if (listSize % ConstantsDiscography.ITEMS_PER_PAGE > 0) {
			++maxNumberOfPages;
		}
		return maxNumberOfPages;
	}

	public Boolean areFilteringCriteriaEmpty(FilteringCriteria filteringCriteria) {
		if (filteringCriteria.getTitle() != null)
			return false;
		else if (filteringCriteria.getArtist() != null)
			return false;
		else if (filteringCriteria.getTrack() != null)
			return false;
		else if (filteringCriteria.getYear() != null)
			return false;
		else
			return true;
	}

}
