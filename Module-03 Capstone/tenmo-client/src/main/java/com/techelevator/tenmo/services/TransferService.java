package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Accounts;
import com.techelevator.tenmo.models.Transfer;

import com.techelevator.tenmo.models.User;


@RequestMapping("transfer")

public class TransferService {
	
	
	// attributes
	public static String AUTH_TOKEN = "";
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	
	User user;
	Accounts account;
	AccountService accountService;
	Transfer transfer;
	
	public TransferService(String url) {
		BASE_URL = url + "transfer/";
		accountService = new AccountService(url);
	}
	

	// do the transfer
	// 		make sure the FROM account exists
	//		does the FROM account have money
	//		does the TO account exist
	// 		subtract dollar amount in the FROM account - use an account method decreaseBalance
	//		add dollar amount in the TO account - use an account method increaseBalance
	
	public Transfer doTransfer(Transfer transfer, String token) {
		
		//Transfer aTransfer = makeTransfer(transfer);
			// VALIDATION - once we get the 'happy path' complete: check if the FROM account exists & amount to transfer is less than account balance
		
		// these add/subtract should pass in (token, transfer.getamountfrom, transfer.getamount)
			// subtract dollar amount 
			// Don't think this will work - it takes an account as a parameter, but in App, we only have the int accountIdFrom..
			
			accountService.subtractBalance(token, transfer.getAccountFrom(), transfer.getAmount());	
			
			//alternative way. Passes in a token, int accountIdTo, and BigDecimal amount
			
			accountService.addBalance(token, transfer.getAccountTo(), transfer.getAmount());
			
			try {
				// this will actually POST a transfer, and return a Transfer object (just the body of the entity)
				restTemplate.exchange(BASE_URL, HttpMethod.POST, makeTransferEntity(transfer, token), Transfer.class).getBody();	
			} catch(Exception exception) {
				System.out.println(exception.getMessage());
			}
			
			
			
			return transfer;
	}
	
	
	// view transfer history (number 5 on use cases).
	
	public Transfer[] listTransfers(String token) {
		Transfer[] transfersFrom = null;
		
		try {
			transfersFrom = restTemplate.exchange(BASE_URL, HttpMethod.GET, makeAuthEntity(token), Transfer[].class).getBody();
		} catch (Exception exception) {
			System.out.println("Please try again");
		}
		
		
		return transfersFrom;
	}
	
	
	public Transfer getTransferByTransferId(long transferId, String token) {
		Transfer transfer = null;
		
		transfer = restTemplate.exchange(BASE_URL + "?transferId=" + transferId, HttpMethod.GET, makeAuthEntity(token), Transfer.class).getBody();
		
		return transfer;
	}
	
	 /**
     * Returns an {HttpEntity} with the `Authorization: Bearer:` header
     *
     * @return {HttpEntity}
     */
    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
	
 private HttpEntity<?> makeTransferEntity(Transfer transfer, String token) {
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.setBearerAuth(token);
    	HttpEntity<?> entity = new HttpEntity<>(transfer, headers);
    	return entity;
    }
 
 
// tried using makeTransfer helper method to make sure transferId was assigned a random int
// 
// private Transfer makeTransfer(Transfer transfer) {
//	 
//	 transfer.setTransferId(new Random().nextInt(1000));
//	 transfer.setTransferTypeId(2);
//	 transfer.setTransferStatusId(2);
//	 transfer.setAccountFrom(transfer.getAccountFrom());
//	 transfer.setAccountTo(transfer.getAccountTo());
//	 transfer.setAmount(transfer.getAmount());
//	 
//	 return transfer;
// }

}
