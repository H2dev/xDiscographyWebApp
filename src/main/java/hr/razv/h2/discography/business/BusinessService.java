package hr.razv.h2.discography.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service("businessService")
public class BusinessService {

	public List<String> parseTracklistString(String tracklistRaw) {

		String[] stringArray = tracklistRaw.split("\\|");
		List<String> trackListJavaList = new ArrayList<String>();
		for (String string : stringArray) {
			if (!(string.equals(null) || string.replaceAll("\\s","").isEmpty())) {
				trackListJavaList.add(string.trim());
			}
		}

		return trackListJavaList;
	}

	public String parseTracklistJavaList(List<String> tracklistJavaList) {

		String tracklistRaw = "";
		for (String track : tracklistJavaList) {
			tracklistRaw = tracklistRaw.concat("|").concat(track);
		}
		tracklistRaw = tracklistRaw.substring(1);

		return tracklistRaw;
	}
	
	public int getMaxNumberOfPages( int listSize ) {
		
		int maxNumberOfPages = listSize / (int)ConstantsDiscography.ITEMS_PER_PAGE;
		if ( listSize % ConstantsDiscography.ITEMS_PER_PAGE > 0 ) {
			++maxNumberOfPages;
		}
		return maxNumberOfPages;
	}

}
