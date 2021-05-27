package com.techelevator.model.campground;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.techelevator.model.park.Park;

public interface CampgroundDAO {

	public List<Campground> viewAllCampgrounds(Park selectedPark);
	
	public Campground getCampgroundFromList(List<Campground> campgroundList, int campgroundNumber);
	
	public BigDecimal getTotalStayPrice(Campground selectedCampground, LocalDate arrival, LocalDate departure);
	
}
