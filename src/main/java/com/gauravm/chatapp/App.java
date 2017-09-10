package com.gauravm.chatapp;

import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		ObjectMapper objectMapper = new ObjectMapper();
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Redis server name/IP :");
			String redisHostName = scanner.next();

			try (final JedisPool jedisPool = new JedisPool(poolConfig, redisHostName, 6379)) {
				System.out.println("User Name :");
				String userName = scanner.next();
				System.out.println("Enter chatroom name");
				String chatRoomName = scanner.next();
				scanner.nextLine();

				System.out.println("**********  Messages **********");

				final Jedis subscriberJedis = jedisPool.getResource();
				Thread messagePrinterThread = new Thread(
						new MessagePriner(userName, chatRoomName, subscriberJedis, objectMapper));
				messagePrinterThread.setDaemon(true);
				messagePrinterThread.start();
				final Jedis publisherJedis = jedisPool.getResource();
				String messageToSend = null;
				while ((messageToSend = scanner.nextLine()) != null) {
					Message message = new Message(userName, messageToSend);
					try {
						publisherJedis.publish(chatRoomName, objectMapper.writeValueAsString(message));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
