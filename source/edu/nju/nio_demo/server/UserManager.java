package edu.nju.nio_demo.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UserManager {
	public static HashMap<String, User> userMap = new HashMap<String,User>();
	public static void addUser(String seqId, User user){
		userMap.put(seqId, user);
	}
	public static User getUserBySeqId(String seqId){
		return userMap.get(seqId);
	}
	public static void removeUserByChannel(SocketChannel channel){
		Iterator iter = userMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String key = (String)entry.getKey();
			User val = (User)entry.getValue();
			if(val.getChannel().equals(channel)){
				try {
					channel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userMap.remove(key);
			}
		}
	}

}
