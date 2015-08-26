package edu.nju.nio_demo.client;

import edu.nju.nio_demo.common.ErrorCode;
import edu.nju.nio_demo.common.SucCode;

public class MsgParser {
	
	private String messge;
	public void setMessage(String msg){
		this.messge = msg;
	}
	public void parser(){
		String[] msgStrings = this.messge.split(":");
		if(msgStrings[0].equals("seqId")){
			NIOClient.seqId = msgStrings[1];
		}else if(msgStrings[0].equals("error")){
			int errorCode = Integer.parseInt(msgStrings[1]);
			switch (errorCode) {
			case ErrorCode.uniqueId:
				System.out.println("this id has been registered!");
				CommandReader.getRegRes = true;
				break;
			case ErrorCode.logErr:
				System.out.println("error id or passwd!");
				CommandReader.getLoginRes = true;
				break;
			case ErrorCode.noUserId:
				System.out.println("This user doesnot exist!");
				CommandReader.addFriendRes = true;
				break;
			case ErrorCode.alreadFriends:
				System.out.println("You are already friends!");
				CommandReader.addFriendRes = true;
				break;
			default:
				break;
			}
		}else if(msgStrings[0].equals("suc")){
			int sucCode = Integer.parseInt(msgStrings[1]);
			switch (sucCode) {
			case SucCode.regSuc:
				System.out.println("Register success!Please login!");
				CommandReader.getRegRes = true;
				break;
			case SucCode.logSuc:
				System.out.println("Login sucess!");
				CommandReader.hasLogin = true;
				CommandReader.getLoginRes = true;
				break;
			case SucCode.addFriendSuc:
				System.out.println("Add Friend Success!");
				CommandReader.addFriendRes = true;
				break;
			default:
				break;
			}
		}else if(msgStrings[0].equals("friends")){
			System.out.println(msgStrings[1]);
			CommandReader.getFriendsRes = true;
		}
	}

}
