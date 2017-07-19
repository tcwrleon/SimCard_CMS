package com.sunyard.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class ConnectAccess {
	/**
	 * 初学者请注意：
	 * 1:先建立一个access文件a1.mdb,并放在D:/下;
	 * 2:在<a href="http://lib.csdn.net/base/14" class='replace_word' title="MySQL知识库" target='_blank' style='color:#df3434; font-weight:bold;'>数据库</a>文件a1.mdb中建立一个表Table1；
	 * 3：为Table1添加一列，并插入至少一条记录；
	 * 4：本文是一个完整的类，直接拿去运行就可以。
	 */
	public static void main(String args[]) throws Exception {
		ConnectAccess ca=new ConnectAccess();
		ca.ConnectAccessFile();
		//ca.ConnectAccessDataSource();
	}
	public void ConnectAccessFile() throws Exception 
	{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		/**
		 * 直接连接access文件。
		 */
		String dbur1 = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=f://SMS.mdb;uid=;pwd=gd2013";
		Properties prop = new Properties();    //只要添加这几句话就可以
		prop.put("charSet", "gb2312");
		Connection conn = DriverManager.getConnection(dbur1,prop);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from L_SMS order by id DESC");
		while (rs.next()) {
			System.out.print("本机号码:" + rs.getString("simnum") + " ");
			System.out.print("内容:" + rs.getString("content") + " ");
			System.out.print("对方号码:" + rs.getString("number") + " ");
            String time = rs.getString("time");
            time = "20" + time.substring(0,2) + " " + time.substring(2,4) + "-" + time.substring(4,6) + " "+
            time.substring(6,8) + ":" + time.substring(8,10) + ":" + time.substring(10,time.length());
			System.out.println("接收时间:" + time);
		}
		rs.close();
		stmt.close();
		conn.close();
	}
	public void ConnectAccessDataSource()throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		/**
		 * 采用ODBC连接方式 如何建立ODBC连接？
		 * 答：在windows下，【开始】->【控制面板】->【性能和维护】->【管理工具】->【数据源】，在数据源这里添加一个指向a1.mdb文件的数据源。
		 * 比如创建名字为dataS1
		 */
		String dbur1 = "jdbc:odbc:dataS1";// 此为ODBC连接方式
		Connection conn = DriverManager.getConnection(dbur1, "username", "gd2013");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from L_SMS");
		while (rs.next()) {
			System.out.print("本机号码:" + rs.getString("simnum") + " ");
			System.out.print("内容:" + rs.getString("content") + " ");
			System.out.print("对方号码:" + rs.getString("number") + " ");
			System.out.println("接收时间:" + rs.getString("time") + " ");
		}
		rs.close();
		stmt.close();
		conn.close();
	}
}