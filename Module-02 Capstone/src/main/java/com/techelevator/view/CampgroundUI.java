package com.techelevator.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.techelevator.model.campground.Campground;
import com.techelevator.model.park.Park;
import com.techelevator.model.site.Site;

public class CampgroundUI {

	/*******************************************************************************************************
	 * This is the CampgroundUI class
	 * 
	 * All user interactions should be coded here
	 * 
	 * The application program will instantiate an object of this class and use the object to interact
	 * with the user.
	 * 
	 * And data required from the user for the application will be acquired by methods of this class
	 * and sent back to the user either as the return value from the method or in an object returned
	 * from the method.
	 * 
	 * Any messages the application programmer wishes to display to the user may be sent to methods of
	 * this class as variables or objects.  Any messaging method may also have "canned" messages the
	 * user may request by a message id.
	 * 
	 *******************************************************************************************************/


	/*******************************************************************************************************
	 * Menu class object
	 * 
	 * Use this Menu object for ALL Menu choices presented to the user
	 * 
	 * This is the same Menu class discussed in module 1 and made available in the module 1 capstone
	 * 
	 * 
	 ******************************************************************************************************/

	Menu CampMenu = new Menu(System.in, System.out);  // Define menu for keyboard input and screen output

	/*******************************************************************************************************
	 * Class member variables, objects, constants and methods should be coded here. 
	 * @return 
	 ******************************************************************************************************/

	private final String QUIT_PROGRAM_OPTION = "Quit";

	private final String CAMPGROUND_MENU_OPTION_1 = "View Campgrounds";
	private final String CAMPGROUND_MENU_OPTION_2 = "Search for Reservation";
	private final String CAMPGROUND_MENU_OPTION_EXIT = "Return to Previous Screen";

	private final String[] CAMPGROUND_MENU_OPTIONS = {CAMPGROUND_MENU_OPTION_1,
			CAMPGROUND_MENU_OPTION_2,
			CAMPGROUND_MENU_OPTION_EXIT};

	private final String RESERVATION_MENU_OPTION_1 = "Search for Available Reservation";
	private final String RESERVATION_MENU_OPTION_EXIT = "Return to Previous Screen";

	private final String[] RESERVATION_MENU_OPTIONS = {RESERVATION_MENU_OPTION_1,
			RESERVATION_MENU_OPTION_EXIT};


	public void openingGreeting() {
		System.out.println("**********************************************");		// Opening welcome to the user
		System.out.println("Welcome to our National Parks Reservation App!");
		System.out.println("**********************************************" + "\n");
	}
	
	public void closingMessage() {
		System.out.println("\n" + "*******************************************************");		// Opening welcome to the user
		System.out.println("Thank you for using our National Parks Reservation App!");
		System.out.println("                                                       ");
		System.out.println("            Go outside and have a great day!           ");
		System.out.println("*******************************************************" + "\n");
	}

	public String viewParkNamesInterface(List<Park> parksList) {
		System.out.print("\n" + "Select a park for further details.");	// Display this message to the user

		List<String> parkNamesList = new ArrayList<String>();	// Instantiate a new ArrayList of Strings to store our park names
		for(int i = 0; i < parksList.size(); i ++) {		// Iterate through our ArrayList of Park objects
			String parkName = parksList.get(i).getName();	// Use the getName() method on each Park object
			parkNamesList.add(parkName);						// Add the name to our parkNames ArrayList
		}
		parkNamesList.add(QUIT_PROGRAM_OPTION);

		Object[] parkNamesArray = parkNamesList.toArray();	// Change our ArrayList of park names to an Object Array to pass into our getChoiceFromOptions menu method

		String choice = (String)CampMenu.getChoiceFromOptions(parkNamesArray);	// Displays park names to user, asks for input on which park to select from user, returns the park name in the form of a String to choice

		return choice;	// Returning the park name to the Campground App
	}

	public void displayParkDetails(Park selectedPark) {
		System.out.println("\n" + "**********************************************");
		System.out.println("Here are some details from your selected park.");
		System.out.println("**********************************************" + "\n");

		String name = selectedPark.getName();								// Gather all of the individual data from the Park object passed in that we want to display
		String location = selectedPark.getLocation();
		String establishDate = selectedPark.getEstablishDate().toString();
		int area = selectedPark.getArea();
		int annualVisitors = selectedPark.getVisitors();
		String description = selectedPark.getDescription();

		System.out.println(name + " National Park");						// Format the display being output to the user and print it
		System.out.println("Location:            " + location);
		System.out.println("Established:         " + establishDate);
		System.out.println("Area:                " + area + " acres");
		System.out.println("Annual Visitors:     " + annualVisitors);
		System.out.println("\n" + description + "\n");
	}

	public String campgroundOptionsInterface() {
		System.out.print("Select a Command");
		String choice = (String) CampMenu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
		return choice;
	}

	public void displayParkCampgrounds(List<Campground> campgroundList, Park selectedPark) {
		String selectedParkName = selectedPark.getName();

		System.out.println("\n" + "**********************************************");
		System.out.println(selectedParkName + " National Park Campgrounds.");
		System.out.println("**********************************************" + "\n");

		System.out.println("    Name                            |   Month Open |   Month Close   |   Daily Fee");
		for(int i=0; i < campgroundList.size(); i++) {
			Campground currentCampground = campgroundList.get(i);
			String campgroundNumber = "#" + (i+1);
			System.out.print(campgroundNumber + "  "); 
			System.out.print(String.format("%-40s", currentCampground.getName()));	
			System.out.print(String.format("%-15s", currentCampground.getOpenFromMm()));		
			System.out.print(String.format("%-15s", currentCampground.getOpenToMm()));	//	+ currentCampground.getOpenToMm() + " / "
			System.out.println(String.format("%-25s", "$" + currentCampground.getDailyFee()));	//	+ "$" + currentCampground.getDailyFee());
		}
	}

	public String reservationOptionsInterface() {
		System.out.print("\n" + "Select a Command");
		String choice = (String) CampMenu.getChoiceFromOptions(RESERVATION_MENU_OPTIONS);
		return choice;
	}

	public int userCampgroundNumber(List<Campground> campgroundList) {
		boolean shouldLoop = true;
		int campgroundNumber = 0;
		while(shouldLoop) {
			@SuppressWarnings("resource")
			Scanner userInput = new Scanner(System.in);
			System.out.print("\n" + "Which campground do you want (or enter 0 to cancel and return to park details)? ");
			campgroundNumber = userInput.nextInt();
			if (campgroundNumber >= 0 && campgroundNumber <= campgroundList.size()) {
				shouldLoop = false;
			} else {
				System.out.println("\n" + "Invalid campground input, please try again.");
			}
		}
		return campgroundNumber;
	}

	public LocalDate userArrivalDate() {
		boolean shouldLoop = true;
		String arrivalDateInput = "";
		LocalDate arrivalDate = LocalDate.now();
		while(shouldLoop) {
			try {
				@SuppressWarnings("resource")
				Scanner userInput = new Scanner(System.in);
				System.out.print("What is the arrival date (yyyy-mm-dd)? ");
				arrivalDateInput = userInput.nextLine();
				arrivalDate = LocalDate.parse(arrivalDateInput);
				if (arrivalDate.isAfter(LocalDate.now())) {
					shouldLoop = false;
				} else {
					System.out.println("Oops! Your arrival date cannot be earlier than today! Please try again." + "\n");
					shouldLoop = true;
				}
			} catch (DateTimeParseException e) {
				System.out.println("Oops! You entered an invalid arrival date! Please try again (only digits and dashes please)." + "\n");
				shouldLoop = true;
			}
		}
		return arrivalDate;
	}

	public LocalDate userDepartureDate(LocalDate arrivalDate) {
		boolean shouldLoop = true;
		String departureDateInput = "";
		LocalDate departureDate = LocalDate.now();
		while(shouldLoop) {
			try {
				@SuppressWarnings("resource")
				Scanner userInput = new Scanner(System.in);
				System.out.print("What is the departure date (yyyy-mm-dd)? ");
				departureDateInput = userInput.nextLine();
				departureDate = LocalDate.parse(departureDateInput);
				if (departureDate.isAfter(arrivalDate)) {
					shouldLoop = false;
				} else {
					System.out.println("Oops! Your departure date must be later than your arrival date! Please try again." + "\n");
					shouldLoop = true;
				}

			} catch (DateTimeParseException e) {
				System.out.println("Oops! You entered an invalid arrival date! Please try again (only digits and dashes please).");
				shouldLoop = true;
			}
		}
		return departureDate;
	}

	public int displayAvailableSites(List<Site> availableSites, BigDecimal totalStayPrice) {
		System.out.println("\n" + "**********************************************");
		System.out.println("Here is your list of available sites!");
		System.out.println("**********************************************" + "\n");

		System.out.println("Option Number / Site Number / Max Occupancy / Accessible / Max RV Length / Utilities / Cost.");
		for(int i=0; i < availableSites.size(); i++) {
			Site availableSite = availableSites.get(i);
			int optionNumber = i + 1;
			System.out.println("#" + optionNumber + "            / "
					+ availableSite.getSiteNumber() + "           / " 
					+ availableSite.getMaxOccupancy() + "             / "
					+ availableSite.isAccessible() + "      / "
					+ availableSite.getMaxRvLength() + "             / "
					+ availableSite.isHasUtilities() + "     / "
					+ "$" + totalStayPrice);
		}
		boolean shouldLoop = true;
		int selectedOptionInput = 0;
		while(shouldLoop) {
			@SuppressWarnings("resource")
			Scanner userInput = new Scanner(System.in);
			System.out.print("\n" + "Which site should be reserved (enter 0 to cancel or select option number)? ");
			selectedOptionInput = userInput.nextInt();
			if (selectedOptionInput >= 0 && selectedOptionInput <= availableSites.size()) {
				shouldLoop = false;
			} else {
				System.out.println("\n" + "Oops! You entered an invalid option number. Please enter a different number");
			}
		}
		return selectedOptionInput;
	}
	
	public String nameForReservation() {
		@SuppressWarnings("resource")
		Scanner userInput = new Scanner(System.in);
		System.out.print("What name should the reservations be made under? ");
		String reservationName = userInput.nextLine();
		return reservationName;
	}
	
}
