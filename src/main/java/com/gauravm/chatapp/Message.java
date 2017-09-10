/**
 * 
 */
package com.gauravm.chatapp;

import java.io.Serializable;

/**
 * @author Gaurav Mishra
 *
 */
public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String message;

	public Message() {
		super();
	}
	
	public Message(String userName, String message) {
		super();
		this.userName = userName;
		this.message = message;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
}
