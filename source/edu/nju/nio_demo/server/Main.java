package edu.nju.nio_demo.server;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("hello!");
		IServer nioServer = new NIOServer();
		nioServer.start();

	}

}
