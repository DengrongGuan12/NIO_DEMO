package edu.nju.nio_demo.client;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("hello world!");
		IClient nioClient = new NIOClient();
		nioClient.start();

	}

}
