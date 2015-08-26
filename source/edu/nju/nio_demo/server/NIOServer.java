package edu.nju.nio_demo.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer implements IServer{

	private int port = 8000;
	private Selector selector;
	
	private CommandParser parser;
	private int seqId = 0;
	public NIOServer(){
		parser = new CommandParser();
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			this.selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void start() {
		// TODO Auto-generated method stub
		System.out.println("NIO Server is starting....");
		while(true){
			try {
				selector.select();
				Iterator iterator = this.selector.selectedKeys().iterator();
				while(iterator.hasNext()){
					SelectionKey selectionKey = (SelectionKey)iterator.next();
					iterator.remove();
					if(selectionKey.isAcceptable()){
						ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
						SocketChannel channel = serverSocketChannel.accept();
						channel.configureBlocking(false);
						channel.register(selector, SelectionKey.OP_READ);
						this.addUser(channel);
					}else if(selectionKey.isReadable()){
						this.read(selectionKey);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	public void read(SelectionKey key){
		SocketChannel socketChannel = (SocketChannel)key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(256);
		try {
			socketChannel.read(buffer);
			byte[] data = buffer.array();
			String msg = new String(data).trim();
			System.out.println("get message from client: "+ msg);
			this.parser.setMsg(msg);
			this.parser.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			UserManager.removeUserByChannel(socketChannel);
		}
		
		
	}
	private synchronized void addUser(SocketChannel socketChannel){
		UserManager.addUser(this.seqId+"", new User(this.seqId+"",socketChannel));
		this.seqId++;
	}
	

}
