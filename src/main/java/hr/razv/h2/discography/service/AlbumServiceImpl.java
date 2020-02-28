package hr.razv.h2.discography.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.razv.h2.discography.dao.AlbumDAO;
import hr.razv.h2.discography.model.Album;
import hr.razv.h2.discography.model.AlbumDTO;
import hr.razv.h2.discography.model.FilteringCriteria;

@Service("albumService")
@Transactional
public class AlbumServiceImpl implements AlbumService {

	@SuppressWarnings("rawtypes")
	@Autowired
	AlbumDAO albumDao;
	@Autowired
	BusinessService businessService;
		
	@Override
	@PostConstruct
	public void initDB() {
		albumDao.initDB();
	}

	@Override
	public void fillMockData() {
		albumDao.fillMockData();
	}
	
	@Override
	public AlbumDTO findById(int id) {
		Album album = albumDao.findById(id);
		return businessService.convertAlbumtoAlbumDTO(album);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AlbumDTO> findByTitle(String title) {
		List<Album> albumList = albumDao.findByTitle(title);
		return businessService.convertAlbumListToAlbumDTOList(albumList);
	}

	@Override
	public void addAlbum(AlbumDTO albumDTO) {
		albumDao.addAlbum(businessService.convertAlbumDTOtoAlbum(albumDTO));
	}

	@Override
	public void updateAlbum(AlbumDTO albumDTO) {
		albumDao.updateAlbum(businessService.convertAlbumDTOtoAlbum(albumDTO));
	}

	@Override
	public void deleteAlbumById(int id) {
		albumDao.deleteAlbumById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AlbumDTO> findAllAlbums(Long page, FilteringCriteria filteringCriteria) {
		List<Album> albumList = albumDao.findAllAlbums(page, filteringCriteria);
		return businessService.convertAlbumListToAlbumDTOList(albumList);
	}

	@Override
	public void deleteAllAlbums() {
		albumDao.deleteAllAlbums();
	}

	@Override
	public boolean isAlbumExist(int id) {
		return albumDao.isAlbumExist(id);
	}

	@Override
	public long tableEntryCount() {
		return albumDao.tableEntryCount();
	}
	
	@Override
	public Integer countAlbumsWithFilter(FilteringCriteria filteringCriteria) {
		return albumDao.countAlbumsWithFilter(filteringCriteria);
	}
	
}
