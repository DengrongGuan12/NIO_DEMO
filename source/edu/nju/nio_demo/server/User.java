package edu.nju.nio_demo.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import edu.nju.nio_demo.common.ErrorCode;
import edu.nju.nio_demo.common.SucCode;


public class User {
	private String id;
	private String passwd;
	private SocketChannel socketChannel;
	private String seqId;
	private static DBManager dbManager = new DBManager();
	public User(String seqId,SocketChannel socketChannel){
		this.seqId = seqId;
		this.socketChannel = socketChannel;
		this.sendMessge("seqId:"+this.seqId);
	}
	public void sendMessge(String message){
		try {
			socketChannel.write(ByteBuffer.wrap(message.getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void register(String id,String passwd){
		String[] columns = {"id","passwd"};
		String[] records = {id,passwd};
		if(dbManager.insertData("user", columns, records)){
			this.id = id;
			this.passwd = passwd;
			this.sendMessge("suc:"+SucCode.regSuc);
		}else{
			this.sendMessge("error:"+ErrorCode.uniqueId);
		}
		
	}
	public void login(String id, String passwd){
		String passwd_db = dbManager.selectPasswdById(id);
		if(passwd.equals(passwd_db)){
			this.id = id;
			this.passwd = passwd;
			this.sendMessge("suc:"+SucCode.logSuc);
		}else{
			this.sendMessge("error:"+ErrorCode.logErr);
		}
	}
	public SocketChannel getChannel(){
		return this.socketChannel;
	}
	public void showMyFriends(){
		this.sendMessge("friends:"+dbManager.getRelatedIdsById(this.id));
		
	}
	public void addFriend(String id){
		if(id.equals(this.id)){
			this.sendMessge("error:"+ErrorCode.addSelfAsFriend);
			return;
		}
		String[] columns = {"id1","id2"};
		String[] records = {this.id,id};
		if(dbManager.selectPasswdById(id).equals("")){
			this.sendMessge("error:"+ErrorCode.noUserId);
		}else{
			if(dbManager.insertData("relation", columns, records)){
				this.sendMessge("suc:"+SucCode.addFriendSuc);
			}else{
				this.sendMessge("error:"+ErrorCode.alreadFriends);
			}
		}
		
	}
	

}
