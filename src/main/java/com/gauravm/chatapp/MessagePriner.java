/**
 * 
 */
package com.gauravm.chatapp;

import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @author Gaurav Mishra
 *
 */
public class MessagePriner implements Runnable {

	private String chatRoomName;
	private Jedis jedis;
	private String userName;
	private ObjectMapper messageFormatter;

	public MessagePriner(String userName, String chatRoomName, Jedis jedis, ObjectMapper messageFormatter) {
		this.chatRoomName = chatRoomName;
		this.jedis = jedis;
		this.userName = userName;
		this.messageFormatter = messageFormatter;
	}

	@Override
	public void run() {
		JedisPubSub jedisPubSub = new JedisPubSub() {
			@Override
			public void onMessage(String channel, String message) {
				try {
					Message readValue = messageFormatter.readValue(message, Message.class);
					if (!userName.equalsIgnoreCase(readValue.getUserName())) {
						System.out.println(readValue.getUserName() + " : " + readValue.getMessage());
					}
				} catch (Exception e) {
					System.err.println("Could not parse message : " + message);
				}
			}
		};
		jedis.subscribe(jedisPubSub, chatRoomName);
	}

}
