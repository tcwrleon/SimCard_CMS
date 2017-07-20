package com.sunyard.datasource;

import com.sunyard.pulgin.PageView;
import com.sunyard.util.PropertiesUtil;

import java.sql.*;
import java.util.*;

public class AccessManager {
	private volatile static AccessManager instance;
    private static String dburl;
    private static Properties prop;   //只要添加这几句话就可以
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;


    static {
        try {
            //{Microsoft Access Driver (*.mdb)}  本地测试用这段  服务器上用下面那段
            dburl = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ="+ PropertiesUtil.getString("access_path")+";uid=;pwd=gd2013";
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            prop = new Properties();
            prop.put("charSet", "gb2312");
            conn = DriverManager.getConnection(dburl,prop);
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	//双重校验锁提升效率
	public static AccessManager getInstance() {
		if (instance == null) {
			synchronized (AccessManager.class) {
				if (instance == null) {
					instance = new AccessManager();
				}
			}
		}
		return instance;
	}

	public static void main(String args[]) throws Exception {
		getInstance().ConnectAccessFile();

/*        String sql = "select top 8 *      from L_SMS where 1=1 and id not in (select top 8 id from L_SMS order by id DESC )        order by id DESC";
        String dataBaseName = sql.substring(sql.indexOf("from") + 4 , sql.indexOf("where")).trim();
        System.out.println(dataBaseName);
        String orderBy = sql.substring(sql.lastIndexOf("order"));
        System.out.println(orderBy);*/


    }

    public void deleteSMS(String id){
        String sql = " delete from L_SMS where id = " + id;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String,String>> querySimCardInfo(PageView pageView ,Map<String, String> map){

        String sql = " select * from L_SMS where 1=1 ";
        List<Map<String,String>> listResult = new ArrayList<Map<String, String>>();
        try {
            String countSql = "select count(1) from (" + sql + ") tmp_count"; // 记录统计
            rs = stmt.executeQuery(countSql);

            int count = 0;
            if (rs.next()) {
                count = ((Number) rs.getObject(1)).intValue();
            }
            pageView.setTotalCount(count);
            pageView.setPageSize(1000);
            /*String beginrow = String.valueOf(pageView.getStart());
            sql += " limit " + beginrow + "," + pageView.getPageSize();*/
            if(!map.isEmpty() || null != map){
                if(map.get("simnum") != null && !"".equals(map.get("simnum"))){
                    sql += "and simnum = '" + map.get("simnum") +"'";
                }
            }
            sql += " order by id DESC";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, String> row = new HashMap<String, String>();
                row.put("id", rs.getString("id"));
                row.put("simnum", rs.getString("simnum"));
                row.put("content", rs.getString("content"));
                row.put("number", rs.getString("number"));
                String time = rs.getString("time");
                /*time = "20" + time.substring(0,2) + " " + time.substring(2,4) + "-" + time.substring(4,6) + " "+
                        time.substring(6,8) + ":" + time.substring(8,10) + ":" + time.substring(10,time.length());*/
                row.put("time", time);
                listResult.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listResult;
    }

    private void ConnectAccessFile() throws Exception
    {
        //rs = stmt.executeQuery("select * from L_SMS");


        rs = stmt.executeQuery("select top 10 id,number,content,time,imsi,iccid,simnum" +
                "    from L_SMS where 1=1 and id not in (select top 10 id from L_SMS where 1=1 and simnum like '%%13560260059%%' order by id DESC )" +
                "and simnum like '%%13560260059%%'" +
                "order by id DESC ");
        while (rs.next()) {
            System.out.print("ID:" + rs.getString("id") + " ");
            System.out.print("本机号码:" + rs.getString("simnum") + " ");
            System.out.print("内容:" + rs.getString("content") + " ");
            System.out.print("对方号码:" + rs.getString("number") + " ");
            System.out.println("接收时间:" + rs.getString("time") + " ");
        }
    }

}