package com.techelevator.model.campground;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.park.Park;

public class JdbcCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JdbcCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Campground> viewAllCampgrounds(Park selectedPark) {
		List<Campground> allCampgrounds = new ArrayList<Campground>();	// Instantiate an ArrayList of Park objects to return
		String getAllCampgroundsSQL = "SELECT * "				// Select all column data from the park table from all rows ordered alphabetically by name
							  + "FROM campground "
							  + "INNER JOIN park "
							  + "ON park.park_id = "
							  + "campground.park_id "
							  + "WHERE campground.park_id = ? "
							  + "ORDER BY campground.name ";
		
		SqlRowSet resultFromSQL = jdbcTemplate.queryForRowSet(getAllCampgroundsSQL, selectedPark.getParkId());
		
		while(resultFromSQL.next()) {
			Campground currentCampground = mapRowToCampground(resultFromSQL);		// Take column data from each row and store it inside a Park object
			allCampgrounds.add(currentCampground);
		}
		
		return allCampgrounds;	// Return the ArrayList of Park objects retrieved from our SQL command.	
	}
	
	@Override
	public Campground getCampgroundFromList(List<Campground> campgroundList, int campgroundNumber) {
		Campground selectedCampground = campgroundList.get(campgroundNumber - 1);
		return selectedCampground;
	}

	@Override
	public BigDecimal getTotalStayPrice(Campground selectedCampground, LocalDate arrival, LocalDate departure) {
		BigDecimal totalStayPrice = new BigDecimal("0.00");
		long totalDuration = ChronoUnit.DAYS.between(arrival, departure);
		
		String getStayPriceSQL = "SELECT daily_fee "
							   + "FROM campground "
							   + "WHERE campground_id = ? ";
		
		SqlRowSet resultFromSQL = jdbcTemplate.queryForRowSet(getStayPriceSQL, selectedCampground.getCampgroundId());
		
		while(resultFromSQL.next()) {
			BigDecimal dailyFee = mapRowToStayPrice(resultFromSQL);
			totalStayPrice = totalStayPrice.add(dailyFee).multiply(new BigDecimal(totalDuration));
		}
		return totalStayPrice;
	}
	
	private BigDecimal mapRowToStayPrice(SqlRowSet aRow) {
		BigDecimal stayPrice = aRow.getBigDecimal("daily_fee");
		return stayPrice;
	}
	
	private Campground mapRowToCampground(SqlRowSet aRow) {
		Campground currentCampground = new Campground();
		
		currentCampground.setCampgroundId(aRow.getLong("campground_id"));
		currentCampground.setParkId(aRow.getInt("park_id"));
		currentCampground.setName(aRow.getString("name"));
		currentCampground.setOpenFromMm(aRow.getString("open_from_mm"));
		currentCampground.setOpenToMm(aRow.getString("open_to_mm"));
		currentCampground.setDailyFee(aRow.getBigDecimal("daily_fee"));
		return currentCampground;
	}

}
