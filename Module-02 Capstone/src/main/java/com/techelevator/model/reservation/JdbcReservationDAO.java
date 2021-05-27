package com.techelevator.model.reservation;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.site.Site;

public class JdbcReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JdbcReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void addReservation(Site selectedSite, String reservationName, LocalDate arrival, LocalDate departure) {
		String addReservationSQL = "Insert into reservation ("
				               + "site_id, "
				               + "name, "
				               + "from_date, "
				               + "to_date, "
				               + "create_date) "
				               + "Values (?, ?, ?, ?, ?)";
		jdbcTemplate.update(addReservationSQL, selectedSite.getSiteId(), reservationName, arrival, departure, LocalDate.now());
	}

	@Override
	public long getReservationByName(String reservationName) {
		long reservationId = 0;
		String getReservationByNameSQL = "Select reservation_id "
				                       + "from reservation "
				                       + "where name = ?";
		SqlRowSet resultFromSQL = jdbcTemplate.queryForRowSet(getReservationByNameSQL, reservationName);
		while(resultFromSQL.next()) {
			reservationId = mapRowToReservationId(resultFromSQL);
		}
				
		return reservationId;
	}

	private long mapRowToReservationId(SqlRowSet aRow) {
		long reservationId = aRow.getLong("reservation_id");
		return reservationId;
	}
	
}
