package edu.nju.nio_demo.server;

public class CommandParser {
	private String msg;
	public void setMsg(String msg){
		this.msg = msg;
	}
	public void parse(){
		String[] msgStrings = this.msg.split(":");
		String seqId = msgStrings[0];
		User user = UserManager.getUserBySeqId(seqId);
		String cmd = msgStrings[1];
		if(cmd.equals("reg")){
			String[] regMsgs = msgStrings[2].split(";");
			String id = regMsgs[0];
			String passwd = regMsgs[1];
			
			user.register(id, passwd);
			
			
		}else if(cmd.equals("login")){
			String[] loginMsg = msgStrings[2].split(";");
			String id = loginMsg[0];
			String passwd = loginMsg[1];
			user.login(id, passwd);
		}else if(cmd.equals("showAllFriends")){
			user.showMyFriends();
		}else if(cmd.equals("addFriend")){
			user.addFriend(msgStrings[2]);
		}
		
	}

}
