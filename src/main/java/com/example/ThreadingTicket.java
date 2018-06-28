package com.example;

public class ThreadingTicket extends Thread {
	String itemid = "";

	public ThreadingTicket(String string) {
		itemid = string;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("hey its thread ! : " + itemid);
	}

}
