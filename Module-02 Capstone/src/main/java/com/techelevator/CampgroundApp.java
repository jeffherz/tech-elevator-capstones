package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.model.campground.Campground;
import com.techelevator.model.campground.JdbcCampgroundDAO;
import com.techelevator.model.park.JdbcParkDAO;
import com.techelevator.model.park.Park;
import com.techelevator.model.reservation.JdbcReservationDAO;
import com.techelevator.model.site.JdbcSiteDAO;
import com.techelevator.model.site.Site;
import com.techelevator.view.*;

public class CampgroundApp {

	/****************************************************************************************************
	 * This is the Campground Reservation system application program
	 * 
	 * Any and all user interactions should be placed in the CampgroundUI class 
	 *     which is in the com.techelevator.view package.
	 *     
	 * This application program should instantiate a CampgroundUI object 
	 *      and use its methods to interact with the user.
	 *      
	 * This application program should contain no user interactions.
	 * 
	 * Any and all database accesses should be placed in the appropriate
	 *     com.techelevator.model.tablename package component
	 *     
	 * This application program should instantiate DAO objects and use the methods
	 *     in the DAO to interact with the database tables.   
	 *     
	 * There should be no SQL in this application program
	 *   
	 ***************************************************************************************************/

	public static void main(String[] args) {

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundUI userInterface = new CampgroundUI();  // Object to manage user interactions;
		// Feel free to change the name

		/****************************************************************************************************
		 * Instantiate any DAOs you will be using here
		 ***************************************************************************************************/

		JdbcParkDAO parkDAO = new JdbcParkDAO(dataSource);
		JdbcCampgroundDAO campgroundDAO = new JdbcCampgroundDAO(dataSource);
		JdbcSiteDAO siteDAO = new JdbcSiteDAO(dataSource);
		JdbcReservationDAO reservationDAO = new JdbcReservationDAO(dataSource);

		/****************************************************************************************************
		 * Your application programming logic goes here
		 ***************************************************************************************************/

		userInterface.openingGreeting();	// Opening message to the user welcoming them to the parks reservation app

		List<Park> parksList = parkDAO.viewAllParks();	// Grab a list of all the park objects from the JdbcParkDAO

		boolean shouldLoop = true;
		while (shouldLoop) {

			String usersSelectedPark = userInterface.viewParkNamesInterface(parksList);	// Display the list of park names to user, ask user to choose a park, returns the chosen park and set that choice to usersSelectedPark

			if(!usersSelectedPark.contentEquals("Quit")) {
				Park selectedPark = parkDAO.getParkByName(usersSelectedPark);	// Taking the park name selected, return the Park object associated with that park name and store that data

				boolean parkLoop = true;
				while (parkLoop) {

					userInterface.displayParkDetails(selectedPark);	// Display to the user the park details for the Park object they selected
					String selectedOption = userInterface.campgroundOptionsInterface();

					switch(selectedOption) {
					case "View Campgrounds": {
						List<Campground> campgroundList = campgroundDAO.viewAllCampgrounds(selectedPark);
						boolean campgroundsLoop = true;
						while (campgroundsLoop) {
							userInterface.displayParkCampgrounds(campgroundList, selectedPark);
							String selectedReservationOption = userInterface.reservationOptionsInterface();
							switch(selectedReservationOption) {
							case "Search for Available Reservation": {
								userInterface.displayParkCampgrounds(campgroundList, selectedPark);
								int campgroundNumber = userInterface.userCampgroundNumber(campgroundList);
								if (campgroundNumber != 0) {
									Campground selectedCampground = campgroundDAO.getCampgroundFromList(campgroundList, campgroundNumber);
									LocalDate arrivalDate = userInterface.userArrivalDate();
									LocalDate departureDate = userInterface.userDepartureDate(arrivalDate);
									List<Site> availableSites = siteDAO.getAvailableSites(selectedCampground, arrivalDate, departureDate);
									if (!availableSites.isEmpty()) {
										BigDecimal totalStayPrice = campgroundDAO.getTotalStayPrice(selectedCampground, arrivalDate, departureDate);
										int siteOptionNumber = userInterface.displayAvailableSites(availableSites, totalStayPrice); 
										if (siteOptionNumber != 0) {
											String reservationName = userInterface.nameForReservation();
											Site selectedSite = siteDAO.getSiteFromList(availableSites, siteOptionNumber);
											reservationDAO.addReservation(selectedSite, reservationName, arrivalDate, departureDate);
											long reservationId = reservationDAO.getReservationByName(reservationName);
											System.out.println("The reservation has been made for " + reservationName + " and the confirmation id is {" + reservationId + "}");
											System.out.println("\n" + "Thank you for booking with us today.  You will now be redirected to the main menu.");
											campgroundsLoop = false;
											parkLoop = false;
											//shouldLoop = false;
											break;
										} else {
											campgroundsLoop = true;
											break;
										}
									}

								} else {
									System.out.println("I'm sorry, there are no more available sites!");
								}
							}
							campgroundsLoop = false;
							break;

							case "Return to Previous Screen": {
								campgroundsLoop = false;
								break;
							}
							}	// end of reservationOptionsInterface
						}	// end of campgroundLoop
						break;
					}
					case "Search for Reservation": {
						// BONUS PARK-WIDE RESERVATION SEARCH
						System.out.println("\n" + "*** Feature not implemented yet ***");
						break;
					}
					case "Return to Previous Screen": {
						parkLoop = false;
						break;
					}
					}	// end of campgroundOptionsInterface

				}	// end of parkLoop
			} else {
				shouldLoop = false;

				userInterface.closingMessage();
			}
		}	// end of shouldLoop

	}	// end of main
}	// end of class
