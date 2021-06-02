package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Accounts;
import com.techelevator.tenmo.models.User;

@RequestMapping("accounts")
public class AccountService {
	
	// attributes
	public static String AUTH_TOKEN = "";
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	
	// 
	public AccountService(String url) {
		BASE_URL = url + "accounts/" ;
	}
	
	// for the getCurrentBalance interaction
	public BigDecimal getCurrentBalance(String token)  {
		BigDecimal balance = null;
		
		balance = restTemplate.exchange(BASE_URL + "balance", HttpMethod.GET, makeAuthEntity(token), BigDecimal.class).getBody();
		
		return balance;
	}

	public long getAccountWithUserId(int sendTo, String token) {
		Accounts sendToAccount = null;
		sendToAccount = restTemplate.exchange(BASE_URL + sendTo, HttpMethod.GET, makeAuthEntity(token), Accounts.class).getBody();
		return sendToAccount.getAccountId();
	}
	
	
	
	
	//  add to the balance, takes in the accountId as a parameter
	public Accounts addBalance(String token, int accountId, BigDecimal dollarAmountAdd) {
		
		// get the account by account id		
		Accounts account = getAccountByAccountId(accountId, token);
		
		// get the current account balance and add to it by the amount that was sent
		BigDecimal accountBalance = account.getBalance().add(dollarAmountAdd);
		
		// set the balance to the original balance + added money
		account.setBalance(accountBalance);
		
		try {
			restTemplate.exchange(BASE_URL + accountId,  HttpMethod.PUT, makeAccountEntity(account, token), Accounts.class).getBody();
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		
		
		return account;
	}
	
	// subtract to the balance, takes in the accountId as a parameter
		public Accounts subtractBalance(String token, int accountId, BigDecimal dollarAmountAdd) {
			
			// get the account by account id
			
			
			Accounts account = getAccountByAccountId(accountId, token);
			
			if(account.getBalance().compareTo(dollarAmountAdd) == -1) {
				System.out.println("Insufficient funds to complete transaction.");
			} else if(dollarAmountAdd.compareTo(BigDecimal.ZERO) == -1) {
				System.out.println("Please enter a postive number."); 
			} else if (dollarAmountAdd.compareTo(BigDecimal.ZERO) == 0) {
				System.out.println("Please enter an amount greater than 0 (We would add a way to go back if we had more time)");
			} else {
			BigDecimal accountBalance = account.getBalance().subtract(dollarAmountAdd);
			
			account.setBalance(accountBalance);
			
			try {
				restTemplate.exchange(BASE_URL + accountId,  HttpMethod.PUT, makeAccountEntity(account, token), Accounts.class).getBody();
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
			}
			
			System.out.println("Your request has been approved.");
		
			}
			return account;
		}
	


	
	
	//Helper methods
	
	// find a single account by the account id
	public Accounts getAccountByAccountId(int accountId, String token) {
		Accounts account;
		
		account = restTemplate.exchange(BASE_URL + accountId, HttpMethod.GET, makeAuthEntity(token), Accounts.class).getBody();
	
		return account;
	}
	
	
	 /**
     * Returns an {HttpEntity} with the `Authorization: Bearer: header
     *
     * @return {HttpEntity}
     */
    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
    
    private HttpEntity<?> makeAccountEntity(Accounts account, String token) {
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.setBearerAuth(token);
    	HttpEntity<?> entity = new HttpEntity<>(account, headers);
    	return entity;
    }
}
