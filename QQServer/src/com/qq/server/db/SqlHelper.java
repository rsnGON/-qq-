package com.qq.server.db;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class SqlHelper {  
    // ����Ҫʹ�õı���  
    private static Connection conn = null;  
    private static PreparedStatement ps = null;  
    
    private static String driver = "com.mysql.jdbc.Driver";  
    private static String url = "jdbc:mysql://localhost:3306/qq?characterEncoding=utf-8&useSSL=true";  
    private static String userName = "root";  
    private static String password = "123456";  
  
    
    public static Connection getConn()
	{
		return conn;
	}



	public static PreparedStatement getPs()
	{
		return ps;
	}

	
    
    // �õ�����  
    public static Connection getConnection()
    {  
        try
        {  
            conn = DriverManager.getConnection(url, userName, password);  
        } catch (SQLException e) 
        {  
            e.printStackTrace();  
        }  
        return conn;  
    }  
  
    // ������update/delete/insert  
//    public static void executeUpdateMultiParams(String[] sql,  
//            String[][] parameters)
//    {  
//        try {  
//            // �������  
//            conn = getConnection();  
//            // ���ܴ�����sql���  
//            conn.setAutoCommit(false);  
//            for (int i = 0; i < sql.length; i++) 
//            {  
//                if (parameters[i] != null)
//                {  
//                    ps = conn.prepareStatement(sql[i]);  
//                    for (int j = 0; j < parameters[i].length; j++)  
//                        ps.setString(j + 1, parameters[i][j]);  
//                }  
//                ps.executeUpdate();  
//            }  
//            conn.commit();  
//        } catch (Exception e)
//        {  
//            e.printStackTrace();  
//            try 
//            {  
//                conn.rollback();  
//            } catch (SQLException e1)
//            {  
//                e1.printStackTrace();  
//            }  
//            throw new RuntimeException(e.getMessage());  
//        } finally 
//        {  
//            // �ر���Դ  
//            close(ps, conn);  
//        }  
//    }  
  
    // update/delete/insert  
    // sql��ʽ:UPDATE tablename SET columnn = ? WHERE column = ?  
    public static void executeUpdate(String sql, String[] parameters) {  
        try {  
            // 1.����һ��ps  
            ps = conn.prepareStatement(sql);  
            // ������ֵ  
            if (parameters != null)  
                for (int i = 0; i < parameters.length; i++)
                {  
                    ps.setString(i + 1, parameters[i]);  
                }  
            // ִ��  
            ps.executeUpdate();  
        } catch (SQLException e) 
        {  
            e.printStackTrace();// �����׶�  
            throw new RuntimeException(e.getMessage());  
        }
    }  
  
    // select  
    public static ResultSet executeQuery(String sql, String[] parameters)
    {  
        ResultSet rs = null;  
        try {    
            ps = conn.prepareStatement(sql);  
            if (parameters != null)
            {  
                for (int i = 0; i < parameters.length; i++)
                {  
                    ps.setString(i + 1, parameters[i]);  
                }  
            }  
            rs = ps.executeQuery();  
        } catch (SQLException e)
        {  
            e.printStackTrace();  
            throw new RuntimeException(e.getMessage());  
        } 
        return rs;  
    }  
  
 
    public static void close(PreparedStatement ps, Connection conn)
    {  
      
        if (ps != null) 
        {
            try 
        	{  
                ps.close();  
            } catch (SQLException e) 
            {  
                e.printStackTrace();  
            }  
        }
        ps = null;  
        if (conn != null)  
        {
            try 
            {  
                conn.close();  
            } catch (SQLException e)
            {  
                e.printStackTrace();  
            }  
        }
        conn = null;  
    }  
}  