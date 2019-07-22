package com.purchaseordersequence;

public class InvalidOrderSequenceException extends RuntimeException{

	private static final long serialVersionUID = -4561412812321160320L;
	
	public InvalidOrderSequenceException(String errorMessage){
		super(errorMessage);
	}

}
