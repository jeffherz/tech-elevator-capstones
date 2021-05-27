package com.techelevator.model.park;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcParkDAO implements ParkDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> viewAllParks() {
		List<Park> allParks = new ArrayList<Park>();	// Instantiate an ArrayList of Park objects to return
		String getAllParksSQL = "SELECT * "				// Select all column data from the park table from all rows ordered alphabetically by name
							  + "FROM park "
							  + "ORDER BY name ";
		
		SqlRowSet resultFromSQL = jdbcTemplate.queryForRowSet(getAllParksSQL);
		
		while(resultFromSQL.next()) {
			Park currentPark = mapRowToPark(resultFromSQL);		// Take column data from each row and store it inside a Park object
			allParks.add(currentPark);
		}
		
		return allParks;	// Return the ArrayList of Park objects retrieved from our SQL command.
	}
	
	public Park getParkByName(String parkName) {
		String getParkSQL = "SELECT * "			// Select all column information where the row name = the park name passed in as a parameter
						  + "FROM park "
						  + "WHERE name = ? ";
		
		SqlRowSet resultFromSQL = jdbcTemplate.queryForRowSet(getParkSQL, parkName);
		
		Park selectedPark = null;
		while(resultFromSQL.next()) {
			selectedPark = mapRowToPark(resultFromSQL);	// Take the column information for the selected park and store that information in a Park object
		}
		
		return selectedPark;	// Return the Park object with the stored information from the selected park
	}

	private Park mapRowToPark(SqlRowSet aRow) {
		Park currentPark = new Park();
		
		currentPark.setParkId(aRow.getLong("park_id"));
		currentPark.setName(aRow.getString("name"));
		currentPark.setLocation(aRow.getString("location"));
		currentPark.setEstablishDate(aRow.getDate("establish_date").toLocalDate());
		currentPark.setArea(aRow.getInt("area"));
		currentPark.setVisitors(aRow.getInt("visitors"));
		currentPark.setDescription(aRow.getString("description"));
		
		return currentPark;
	}

}
