package edu.nju.nio_demo.server;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.nju.nio_demo.server.DBConnectionPool.PooledConnection;



public class DBManager {
	public static final String url = "jdbc:mysql://127.0.0.1/nio_demo";  
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "root";  
    public static final String password = "12345";  
    
	private static DBConnectionPool connectionPool;

	public void close() {
		try {
			connectionPool.closeConnectionPool();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DBManager() {
		// TODO Auto-generated constructor stub
		connectionPool = new DBConnectionPool(name, url, user, password);
		try {
			connectionPool.createPool();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public boolean insertData(String tableName,String[] columns,String[] records){
		boolean suc = true;
		String sql = "insert into "+tableName;
		String columnString = "(";
		String recordString = " values(";
		for(int i = 0;i<columns.length;i++){
			if(i == columns.length-1){
				columnString += columns[i]+")";
				recordString += records[i]+")";
			}else{
				columnString += columns[i]+",";
				recordString += records[i]+",";
			}
			
		}
		sql += columnString + recordString;
		PooledConnection con = null;
		try {
			 con = connectionPool.getConnection();
			con.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			suc = false;
		}finally{
			con.close();
		}
		
		
		return suc;
	}
	public String selectPasswdById(String id){
		String passwd = "";
		String sql = "select passwd from user where id = "+id;
		PooledConnection connection = null;
		try {
			connection = connectionPool.getConnection();
			ResultSet rs = connection.executeQuery(sql);
			if(rs.next()){
				passwd = rs.getString(1);
			}
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			connection.close();
		}
		
		return passwd;
	}
	public String getRelatedIdsById(String id){
		String ids = "";
		String sql = "select * from relation";
		PooledConnection con = null;
		try {
			con = connectionPool.getConnection();
			ResultSet rs = con.executeQuery(sql);
			while(rs.next()){
				String id1 = rs.getString(1);
				String id2 = rs.getString(2);
				if(id1.equals(id)){
					ids += id2+";";
				}else if(id2.equals(id)){
					ids += id1+";";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ids;
	}
	
	

}
