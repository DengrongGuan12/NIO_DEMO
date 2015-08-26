package edu.nju.nio_demo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class CommandReader implements Runnable{
	private SocketChannel socketChannel;
	private BufferedReader reader;
	private boolean error;
	public static boolean hasLogin = false;
	public static boolean getLoginRes = false;
	public static boolean getRegRes = false;
	public static boolean getFriendsRes = false;
	public static boolean addFriendRes = false;
	public CommandReader(SocketChannel socketChannel){
		this.socketChannel = socketChannel;
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.error = false;
	}
	
	private void sendCommandMsg(String msg){
		msg = NIOClient.seqId +":"+msg;
		try {
			socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void run() {
		// TODO Auto-generated method stub
		while(!error && !hasLogin){
			System.out.println("please input : 1.login 2.register");
			String line = "";
			try {
				line = this.reader.readLine();
				if("1".equals(line)){
					this.login();
				}else if("2".equals(line)){
					this.register();
				}else{
					System.out.println("error input !");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(!error && hasLogin){
			System.out.println("please input :1.show all friends list 2.send message to online friend 3.add friend 4.logout");
			String line = "";
			try {
				line = this.reader.readLine();
				if("4".equals(line)){
					
				}else if("1".equals(line)){
					this.showAllFriends();
				}else if("2".equals(line)){
					this.sendMsgToOnlineFriend();
				}else if("3".equals(line)){
					this.addFriend();
				}else{
					System.out.println("error input !");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}
	private void login(){
		String id = "";
		String passwd = "";
		boolean suc;
		getLoginRes = false;
		while(!error && !hasLogin){
			suc = true;
			System.out.println("please input the id:");
			try {
				id = this.reader.readLine();
				if(id.equals("")){
					System.out.println("ID cannot be null!Please input again!");
					suc = false;
					continue;
				}
				System.out.println("please input the passwd:");
				passwd = this.reader.readLine();
				if("".equals(passwd)){
					System.out.println("Passwd cannot be null!Please input again!");
					suc = false;
					continue;
				}
				if(suc){
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		this.sendCommandMsg("login:"+id+";"+passwd);
		while(!getLoginRes){
			
		}
	}
	private void register(){
		String id = "";
		String passwd1 = "";
		String passwd2 = "";
		boolean suc;
		getRegRes = false;
		while(!error){
			suc = true;
			System.out.println("please input the ID:");
			try {
				id = this.reader.readLine();
				if(id.equals("")){
					System.out.println("ID cannot be null ! Please input again!");
					suc = false;
					continue;
				}
				System.out.println("Please input the passwd:");
				passwd1 = this.reader.readLine();
				if("".equals(passwd1)){
					System.out.println("Passwd cannot be null! Please input again!");
					suc = false;
					continue;
				}
				System.out.println("Please input the passwd again : ");
				passwd2 = this.reader.readLine();
				if(!passwd1.equals(passwd2)){
					System.out.println("Two passwd must be same!");
					suc = false;
					continue;
				}
				if(suc){
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String message = "reg:"+id+";"+passwd1;
		this.sendCommandMsg(message);
		while(!getRegRes){
			
		}
		
	}
	public void setError(boolean error){
		this.error = error;
	}
	private void showAllFriends(){
		getFriendsRes = false;
		this.sendCommandMsg("showAllFriends");
		while(!getFriendsRes){
			
		}
	}
	private void addFriend(){
		addFriendRes = false;
		System.out.println("Please input the friend id:");
		String line = "";
		try {
			line = this.reader.readLine();
			if("".equals(line)){
				System.out.println("friend's id cannot be null!");
			}else{
				this.sendCommandMsg("addFriend:"+line);
				while(!addFriendRes){
					
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void sendMsgToOnlineFriend(){
		String id = "";
		String msg = "";
		System.out.println("Please input the friend id:");
		try {
			id = this.reader.readLine();
			if("".equals(id)){
				System.out.println("Friend's id cannot be null!");
			}else{
				System.out.println("Please input the message:");
				msg = this.reader.readLine();
				if("".equals(msg)){
					System.out.println("Message cannot be null!");
				}else{
					this.sendCommandMsg("sendMessage:"+id+":"+msg);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
