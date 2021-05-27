package com.techelevator.model.site;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.campground.Campground;

public class JdbcSiteDAO implements SiteDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Site> getAvailableSites(Campground selectedCampground, LocalDate arrival, LocalDate departure) {
		List<Site> availableSites = new ArrayList<Site>();
		String getAvailableSitesSQL = "SELECT * "
									+ "FROM site "
									+ "WHERE campground_id = ? "
									+ "AND site_id NOT IN (SELECT site_id "
														+ "FROM reservation "
														+ "WHERE (? <= to_date) "
														+ "AND (? >= from_date)) "
									+ "LIMIT 5"
									;
	
		SqlRowSet resultFromSQL = jdbcTemplate.queryForRowSet(getAvailableSitesSQL
															 , selectedCampground.getCampgroundId()
															 , arrival
															 , departure
															 );
		
		while(resultFromSQL.next()) {
			Site currentSite = mapRowToSite(resultFromSQL);
			availableSites.add(currentSite);
		}
		return availableSites;
	}
	
	public Site getSiteFromList(List<Site> availableSites, int siteOptionNumber) {
		Site selectedSite = availableSites.get(siteOptionNumber - 1);
		return selectedSite;
	}
	
	private Site mapRowToSite(SqlRowSet aRow) {
		Site currentSite = new Site();
		
		currentSite.setSiteId(aRow.getLong("site_id"));
		currentSite.setCampgroundId(aRow.getInt("campground_id"));
		currentSite.setSiteNumber(aRow.getInt("site_number"));
		currentSite.setMaxOccupancy(aRow.getInt("max_occupancy"));
		currentSite.setAccessible(aRow.getBoolean("accessible"));
		currentSite.setMaxRvLength(aRow.getInt("max_rv_length"));
		currentSite.setHasUtilities(aRow.getBoolean("utilities"));
		
		return currentSite;
	}

}
