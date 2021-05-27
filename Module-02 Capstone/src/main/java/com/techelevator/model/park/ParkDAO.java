package com.techelevator.model.park;

import java.util.List;

public interface ParkDAO {
	
	public List<Park> viewAllParks();	// Returns an ArrayList of Park objects
	
	public Park getParkByName(String parkName);	// Returns a Park object associated with the park name passed in

}
