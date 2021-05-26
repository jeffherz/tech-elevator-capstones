package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**************************************************************************************************************************
*  This is your Vending Machine Command Line Interface (CLI) class
*
*  It is the main process for the Vending Machine
*
*  THIS is where most, if not all, of your Vending Machine interactions should be coded
*  
*  It is instantiated and invoked from the VendingMachineApp (main() application)
*  
*  Your code should be placed in here
***************************************************************************************************************************/
import com.techelevator.view.Menu; // Gain access to Menu class provided for the Capstone

public class VendingMachineCLI {

	Scanner customerIn = new Scanner(System.in);
	int selection;
	String userSelect;

	// Main menu options defined as constants

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_SALES_REPORT="Sales Report";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT,MAIN_MENU_OPTION_SALES_REPORT };

	private Menu vendingMenu; // Menu object to be used by an instance of this class
	private VendingMachine vm = new VendingMachine();

	public VendingMachineCLI(Menu menu) { // Constructor - user will pas a menu for this class to use
		this.vendingMenu = menu; // Make the Menu the user object passed, our Menu
	}

	/**************************************************************************************************************************
	 * VendingMachineCLI main processing loop
	 * 
	 * Display the main menu and process option chosen
	 *
	 * It is invoked from the VendingMachineApp program
	 *
	 * THIS is where most, if not all, of your Vending Machine objects and
	 * interactions should be coded
	 *
	 * Methods should be defined following run() method and invoked from it
	 * 
	 * @throws FileNotFoundException
	 *
	 ***************************************************************************************************************************/

	public void run() {
		// Need to change eventually
		try {
			vm.loadInventory();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean shouldProcess = true; // Loop control variable

		while (shouldProcess) { // Loop until user indicates they want to exit

			String choice = (String) vendingMenu.getChoiceFromOptions(MAIN_MENU_OPTIONS); // Display menu and get choice

			switch (choice) { // Process based on user menu choice

			case MAIN_MENU_OPTION_DISPLAY_ITEMS:
				displayItems(); // invoke method to display items in Vending Machine
				break; // Exit switch statement

			case MAIN_MENU_OPTION_PURCHASE:
				purchaseItems(); // invoke method to purchase items from Vending Machine
				break; // Exit switch statement

			case MAIN_MENU_OPTION_EXIT:
				endMethodProcessing(); // Invoke method to perform end of method processing
				shouldProcess = false; // Set variable to end loop
				break; // Exit switch statement
				
			case MAIN_MENU_OPTION_SALES_REPORT:
				try {
					vm.generateReport();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		return; // End method and return to caller
	}

	/********************************************************************************************************
	 * Methods used to perform processing
	 * 
	 * @throws FileNotFoundException
	 ********************************************************************************************************/

	public void displayItems() { // static attribute used as method is not associated with specific object
									// instance
		System.out.println();
		for (String theString : vm.inventory.keySet()) {
			System.out.println(theString + " " + vm.inventory.get(theString));
		}
		

	}

	public void purchaseItems() {// static attribute used as method is not associated with specific object
									// instance

		while (true) {
			System.out.println("*************************************************");
			System.out.println("Current Balance: "+vm.amt.format(vm.getBalance()));
			System.out.println("\n1) Feed Money\n2) Select Product\n3) Finish Transaction");

			userSelect = customerIn.nextLine();
			selection = Integer.parseInt(userSelect);

			if (selection == 3) {
				System.out.println(vm.makeChange());
				try {
					vm.logChange();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}

			else if (selection == 1) {
				vm.addMoney();
//				vm.log();

			} else if (selection == 2) {
				displayItems();
				System.out.println("\nEnter item number: \n");
				System.out.println(vm.buyItem());

			}
			else {
				System.out.println("Invalid entry");
			}
		}

	}

	public void endMethodProcessing() { // static attribute used as method is not associated with specific object
										// instance
		System.out.println("Thank you, come again!");
	}
}
