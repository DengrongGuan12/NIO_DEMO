package edu.nju.nio_demo.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient implements IClient{

	private String ip = "localhost";
	private int port = 8000;
	private Selector selector;
	private CommandReader commandReader;
	private MsgParser msgParser;
	public static String seqId;
	public NIOClient(){
		try {
			SocketChannel channel = SocketChannel.open();
			channel.configureBlocking(false);
			this.selector = Selector.open();
			channel.connect(new InetSocketAddress(this.ip, this.port));
			channel.register(selector, SelectionKey.OP_CONNECT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void start() {
		// TODO Auto-generated method stub
		boolean error = false;
		while(true){
			try {
				selector.select();
				@SuppressWarnings("rawtypes")
				Iterator iterator = selector.selectedKeys().iterator();
				
				while(iterator.hasNext()){
					SelectionKey key = (SelectionKey)iterator.next();
					iterator.remove();
					if(key.isConnectable()){
						SocketChannel socketChannel = (SocketChannel)key.channel();
						if(socketChannel.isConnectionPending()){
							socketChannel.finishConnect();
						}
						socketChannel.configureBlocking(false);
						//在这里可以给服务端发送信息哦  
//	                    socketChannel.write(ByteBuffer.wrap(new String("向服务端发送了一条信息").getBytes()));  
	                    //在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。  
	                    socketChannel.register(this.selector, SelectionKey.OP_READ);
	                    this.commandReader = new CommandReader(socketChannel);
//	                    this.commandReader.run();
	                    this.msgParser = new MsgParser();
	                    Thread thread = new Thread(this.commandReader);
	                    thread.start();
					}else if(key.isReadable()){
						if(!this.read(key)){
							error = true;
							this.commandReader.setError(true);
							break;
						}
					}
				}
				if(error){
					break;
				}
 			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			
		}
		
	}
	public boolean read(SelectionKey key){
		SocketChannel socketChannel = (SocketChannel)key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(256);
		try {
			socketChannel.read(buffer);
			byte[] data = buffer.array();
			String msg = new String(data).trim();
			System.out.println("get message from server: "+ msg);
			this.msgParser.setMessage(msg);
			this.msgParser.parser();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("-------------------------------------");
			e.printStackTrace();
			return false;
		}
		return true;
		
		
	}

}
