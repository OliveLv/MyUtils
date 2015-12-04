/**   
* @Title: MysqlUtil.java 
* @author lxh  
* @date Nov 15, 2015 8:30:33 PM  
* 
*/
package com.olive.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlUtil {
	private static final String mysqlDriver="com.mysql.jdbc.Driver";
	private static final String url="jdbc:mysql://localhost:3306/mysql";
	private static final String newUrl="jdbc:mysql://localhost:3306/";
	private static final String username="root";
	private static final String password="";
	private static Connection conn=null;
	private static Statement stmt=null;
	private static final String set="?useUnicode=true&characterEncoding=gbk";
	private static final String database="Matches";
	static{
		try {
			Class.forName(mysqlDriver);
			Connection oldConn = DriverManager.getConnection(url+set, username, password);
			String sql="create database if not exists "+database+" CHARACTER SET 'utf8' COLLATE 'utf8_general_ci'";
			stmt=oldConn.createStatement();
			stmt.executeUpdate(sql);
			oldConn.close();
			conn=DriverManager.getConnection(newUrl+database+set, username, password);
			stmt=conn.createStatement();
			if(conn!=null){
				System.out.println("success");
			}else{
				System.out.println("eroor");
			}
			// create table
			/**
			 * id Match.journel+Match.session+1/2/3
			 * rid Match.id
			 * name Match.elements[].name
			 * bifaIndex Match.elements[].bifaIndex
			 * portion Match.elements[].portion
			 * guapaiIndex Match.elements[].guapaiIndex
			 * pageId Match.pageId
			 */
			sql="create table if not exists matches("
					+"id VARCHAR(50),"
					+"rid VARCHAR(50),"
					+"name VARCHAR(255),"
					+"bifaIndex Integer,"
					+"portion VARCHAR(15),"
					+"bifaPeiRate VARCHAR(10),"
					+"guapaiIndex VARCHAR(10),"
					+"europAvg VARCHAR(10),"
					+"kellyVar VARCHAR(10),"
					+"pageId INTEGER,"
					+"PRIMARY KEY(id))ENGINE=InnoDB DEFAULT CHARSET=utf8";
			stmt.executeUpdate(sql);
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void init(){
		try {
			Class.forName(mysqlDriver);
			Connection oldConn = DriverManager.getConnection(url, username, password);
			String sql="create database if not exists "+database;
		    stmt=oldConn.createStatement();
			stmt.executeUpdate(sql);
			oldConn.close();
			conn=DriverManager.getConnection(newUrl+database, username, password);
			stmt=conn.createStatement();
			if(conn!=null){
				System.out.println("success");
			}else{
				System.out.println("eroor");
			}
			sql="create table if not exists matches("
					+"ID VARCHAR(50),"
					+"rid VARCHAR(50)"
					+"name VARCHAR(255),"
					+"bifaIndex Integer,"
					+"portion VARCHAR(15),"
					+"bifaPeiRate VARCHAR(10),"
					+"guapaiIndex VARCHAR(10),"
					+"europAvg VARCHAR(10),"
					+"kellyVar VARCHAR(10),"
					+"pageId INTEGER"
					+"PRIMARY KEY(id))";
			
			stmt.executeUpdate(sql);
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){
		if(conn==null){
			init();
		}
		return conn;
	}
	public static Statement getStatement(){
		Connection conn=getConnection();
		if(stmt!=null)return stmt;
		Statement stmt=null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stmt;
	}
	public static ResultSet getResultSet(String sql){
		Statement stmt=getStatement();
		ResultSet rs=getResultSet(stmt,sql);
		return rs;
	}
	public static ResultSet getResultSet(Statement stmt,String sql){
		ResultSet rs=null;
		try {
			rs=stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	public static void update(String sql){
		Statement stmt=getStatement();
		update(stmt,sql);
	}
	public static void update(Statement stmt,String sql){
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void close(){
		if(stmt!=null)
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void main(String []args){
		MysqlUtil.getConnection();
	}
}
