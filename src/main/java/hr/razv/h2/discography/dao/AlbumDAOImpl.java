package hr.razv.h2.discography.dao;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import hr.razv.h2.discography.model.Album;
import hr.razv.h2.discography.model.FilteringCriteria;
import hr.razv.h2.discography.util.Constants;

@Repository("albumDao")
public class AlbumDAOImpl implements AlbumDAO<Album> {

	private static final Logger logger = Logger.getLogger(AlbumDAOImpl.class);

	ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring-beans.xml" });
	ConnectionSource cs = (ConnectionSource) context.getBean("connectionSource");

	@SuppressWarnings("unchecked")
	public Dao<Album, Integer> albumDaoORMLite = (Dao<Album, Integer>) context.getBean("albumDaoORMLite");

	@Override
	public void initDB() {
		try {
			TableUtils.createTableIfNotExists(cs, Album.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fillMockData() {
		try {
			albumDaoORMLite.updateRaw("DELETE FROM album");
			Resource resource = new ClassPathResource("sql/mock_data.sql");
			InputStream resourceInputStream = resource.getInputStream();
			DataInputStream in = new DataInputStream(resourceInputStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				albumDaoORMLite.updateRaw(strLine);
			}
			in.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public Album findById(int id) {

		Album album = new Album();
		try {
			album = albumDaoORMLite.queryForId(id);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return album;
	}

	@Override
	public void addAlbum(Album album) {
		try {
			albumDaoORMLite.create(album);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public List<Album> findAllAlbums(Long page, FilteringCriteria filteringCriteria) {

		List<Album> listAlbums = new ArrayList<Album>();
		try {
			if (page >= (long) 0) {
				Boolean booleanAsc = true;
				String orderBy = filteringCriteria.getOrderBy() != null ? filteringCriteria.getOrderBy() : null;
				String sortAsc = filteringCriteria.getSortAsc() != null ? filteringCriteria.getSortAsc() : null;

				QueryBuilder<Album, Integer> queryBuilder = albumDaoORMLite.queryBuilder();

				queryBuilder.offset((page - (long) 1) * Constants.ITEMS_PER_PAGE)
						.limit(Constants.ITEMS_PER_PAGE);

				buildQueryFromFilteringParams(queryBuilder, filteringCriteria);

				if (orderBy != null && orderBy != "" && !orderBy.isEmpty() && sortAsc != null && sortAsc != ""
						&& !sortAsc.isEmpty()) {
					booleanAsc = Boolean.parseBoolean(sortAsc);
					queryBuilder.orderBy(orderBy, booleanAsc);
				}

				PreparedQuery<Album> preparedQuery = queryBuilder.prepare();
				listAlbums = albumDaoORMLite.query(preparedQuery);
			} else {
				listAlbums = albumDaoORMLite.queryForAll();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return listAlbums;
	}

	private void buildQueryFromFilteringParams(QueryBuilder<Album, Integer> queryBuilder,
			FilteringCriteria filteringCriteria) {

		String title = filteringCriteria.getTitle() != null ? filteringCriteria.getTitle() : null;
		String artist = filteringCriteria.getArtist() != null ? filteringCriteria.getArtist() : null;
		Integer year = filteringCriteria.getYear() != null ? filteringCriteria.getYear() : null;
		String track = filteringCriteria.getTrack() != null ? filteringCriteria.getTrack() : null;

		Where<Album, Integer> where = null;
		try {
			if (title != null && title != "" && !title.isEmpty()) {
				where = queryBuilder.where();
				where.like(Album.TITLE_FIELD, "%" + title + "%");
			}
			if (artist != null && artist != "" && !artist.isEmpty()) {
				if (where != null) {
					where.and();
				} else {
					where = queryBuilder.where();
				}
				where.like(Album.ARTIST_FIELD, "%" + artist + "%");
			}
			if (year != null) {
				if (where != null) {
					where.and();
				} else {
					where = queryBuilder.where();
				}
				where.eq(Album.YEAR_FIELD, year);
			}
			if (track != null && track != "" && !track.isEmpty()) {
				if (where != null) {
					where.and();
				} else {
					where = queryBuilder.where();
				}
				where.like(Album.TRACKLIST_FIELD, "%" + track + "%");
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public Integer countAlbumsWithFilter(FilteringCriteria filteringCriteria) {

		List<Album> listAlbums = new ArrayList<Album>();
		try {
			QueryBuilder<Album, Integer> queryBuilder = albumDaoORMLite.queryBuilder();
			buildQueryFromFilteringParams(queryBuilder, filteringCriteria);
			PreparedQuery<Album> preparedQuery = queryBuilder.prepare();
			listAlbums = albumDaoORMLite.query(preparedQuery);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return (listAlbums != null ? listAlbums.size() : 0);
	}

	@Override
	public List<Album> findByTitle(String title) {

		List<Album> listAlbums = new ArrayList<Album>();
		try {
			QueryBuilder<Album, Integer> queryBuilder = albumDaoORMLite.queryBuilder();
			queryBuilder.where().eq(Album.TITLE_FIELD, title);
			PreparedQuery<Album> preparedQuery = queryBuilder.prepare();
			listAlbums = albumDaoORMLite.query(preparedQuery);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return listAlbums;
	}

	@Override
	public void updateAlbum(Album album) {
		try {
			albumDaoORMLite.update(album);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void deleteAlbumById(int id) {
		try {
			albumDaoORMLite.delete(findById(id));
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void deleteAllAlbums() {
		try {
			albumDaoORMLite.delete(findAllAlbums((long) -1, null));
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public boolean isAlbumExist(int id) {

		boolean exists = false;
		try {
			exists = albumDaoORMLite.idExists(id);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return exists;
	}

	@Override
	public long tableEntryCount() {
		long fullListSize = (long) 0;
		try {
			fullListSize = albumDaoORMLite.countOf();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return fullListSize;
	}

}
