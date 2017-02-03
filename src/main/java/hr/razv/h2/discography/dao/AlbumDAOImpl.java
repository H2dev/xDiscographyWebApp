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
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import hr.razv.h2.discography.business.ConstantsDiscography;
import hr.razv.h2.discography.model.Album;

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
	public List<Album> findAllAlbums(Long page) {

		List<Album> listAlbums = new ArrayList<Album>();
		try {
			if (page > (long) 0) {
				QueryBuilder<Album, Integer> queryBuilder = albumDaoORMLite.queryBuilder();
				queryBuilder.offset((page - (long) 1) * ConstantsDiscography.ITEMS_PER_PAGE)
						.limit(ConstantsDiscography.ITEMS_PER_PAGE);
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
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAlbumById(int id) {
		try {
			albumDaoORMLite.delete(findById(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAllAlbums() {
		try {
			albumDaoORMLite.delete(findAllAlbums((long) -1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isAlbumExist(Album album) {

		boolean exists = false;
		try {
			exists = albumDaoORMLite.idExists(album.getId());
		} catch (SQLException e) {
			e.printStackTrace();
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
