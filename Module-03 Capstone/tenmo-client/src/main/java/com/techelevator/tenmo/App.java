package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Scanner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Accounts;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";

// declare auth token and restTemplate for HTTP request methods
public static String AUTH_TOKEN = "";
public RestTemplate restTemplate = new RestTemplate();
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks (Under Construction)";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests (Under Construction)";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
//	private static final String TRANSFER_MENU_OPTION_VIEW_ALL_USERS = "View a list of all users";
//	private static final String[] TRANSFER_MENU_OPTIONS = {TRANSFER_MENU_OPTION_VIEW_ALL_USERS};
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private UserService userService;
    private TransferService transferService; 
    Principal principal;
    Transfer transfer;
    
    Scanner keyboard;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL), new UserService(API_BASE_URL), new TransferService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService, UserService userService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.userService = userService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();  
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}
	

	// enter the HttpMethod requests in these methods below:
	
	private void viewCurrentBalance() {
		BigDecimal balance = accountService.getCurrentBalance(currentUser.getToken());
		System.out.println("Your current account balance is " + balance);
		
		
		
	}
	
	private void viewTransferHistory() {
		int accountId = (int)accountService.getAccountWithUserId(currentUser.getUser().getId(), currentUser.getToken());
		Transfer[] transfers = null;
		transfers = transferService.listTransfers(currentUser.getToken());
		
		
		if(transfers.length > 0) {
			System.out.println("TRANSFERS");
			for(Transfer t: transfers) {
			System.out.println("ID: " + t.getTransferId() + " |  Account From: " + t.getAccountFrom() + " |  Account To: " + t.getAccountTo() + " |  Amount: " + t.getAmount());
			}
		} else {
			System.out.println("No transfers to display");
		}
		
		System.out.println("--------------------------------" + "\n" + "Ran out of time to implement part 6 into the TEnmo Application" + "\n" + "--------------------------------");
		
//		long userInput = console.getUserInputInteger("If you would like to view the details of a particular transfer, please enter the transfer ID: ");
//		
//		transferService.getTransferByTransferId(userInput, currentUser.getToken());
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		viewAllUsers();   // need to add ID to this as well?
//		System.out.println("Please enter the user_id for desired transfer (example: 2)");
//		Scanner userInput = new Scanner(System.in);
//		int sendTo = Integer.parseInt(userInput.next());
		int accountIdFrom = (int)accountService.getAccountWithUserId((currentUser.getUser().getId()), currentUser.getToken());
		
		int userInput = console.getUserInputInteger("Please enter the user_id for desired transfer (example: 2)");
	
//		int accountIdTo = (int)accountService.getAccountWithUserId(sendTo, currentUser.getToken());
		//System.out.println(accountIdTo);
		
		
//		System.out.println("Please enter the dollar amount for desired transfer:");
//		BigDecimal dollar = new BigDecimal(userInput.next());
		//System.out.println(dollar);
		
		
		BigDecimal userInputDollar = BigDecimal.valueOf((long)console.getUserInputInteger("Please enter the dollar amount for desired transfer"));
		
				
		Transfer transfer = new Transfer(accountIdFrom, userInput, userInputDollar);
		
		try {	
		transferService.doTransfer(transfer, currentUser.getToken());   // this is giving us a Null value

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			//System.out.println("The transfer failed.");
		}
		
		
		
		// ORIGINAL
//		Transfer transfer = new Transfer(accountIdFrom, accountIdTo, dollar);
//		
//		transferService.doTransfer(transfer, currentUser.getToken());
//		
//		System.out.println(transfer.toString());
//		System.out.println("Your request has been approved");
		
		
		//transfer.setAccountFrom(accountFrom);
		
		// take the user input to populate a transfer object
		//transferObject.setFrom - current user principal
		// enter account id to send money to system out println
		// scanner thing
		// transferObject.sendTo
		// system out print  - enter how much money you want to send
		// scanner thing
		// transferObject.dollarAmount
		
		}
		
	

	private void requestBucks() {
		// TODO Auto-generated method stub
		
		System.out.println("-----------------------------");
		System.out.println(" Feature not implemented yet ");
		System.out.println(" Please select another option");
		System.out.println("-----------------------------");
		
	}
	
	private void exitProgram() {
		
		// add the warm send-off
		System.out.println("-----------------------------");
		System.out.println("     Thank you for using     ");
		System.out.println("            TEnmo            ");
		System.out.println("                             ");
		System.out.println("       Have a nice day!      ");
		System.out.println("-----------------------------");
		System.exit(0);
		                 
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
	
	 /**
     * Returns an {HttpEntity} with the `Authorization: Bearer:` header
     *
     * @return {HttpEntity}
     */
    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
    
	private void viewAllUsers() {
		User[] users = userService.getAllUsers(currentUser.getToken());
		System.out.println(String.format("%-15s %-15s", "USERNAME:", "ID:"));
		System.out.println("");
		for(int i=0; i < users.length; i++) {
			System.out.println(String.format("%-15s %-15s", users[i].getUsername(), users[i].getId()));
		}
	}

}
