package com.techelevator.model.reservation;

import java.time.LocalDate;

import com.techelevator.model.site.Site;

public interface ReservationDAO {

	public void addReservation(Site selectedSite, String reservationName, LocalDate arrival, LocalDate departure);
	
	public long getReservationByName(String reservationName);
}
