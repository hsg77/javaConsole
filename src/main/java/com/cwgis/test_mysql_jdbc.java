package com.cwgis;


import java.sql.*;

public class test_mysql_jdbc {
     public static void main()
             throws Exception
     {
         //声明Connection对象
         Connection con=null;
         //驱动程序名
         String driver="com.mysql.jdbc.Driver";
         //Url
         String url="jdbc:mysql://192.168.30.114:3307/sqltestdb";
         String user="root";
         String pwd="123456";
         try{
             Class.forName(driver);  //加载驱动程序
             con= DriverManager.getConnection(url,user,pwd);
             if(con.isClosed()==false)
             {
                 System.out.println("succeeded connection to the database!");
             }
             Statement statement=con.createStatement();
             String sql="select * from emp";
             ResultSet rs=statement.executeQuery(sql);
             System.out.println("-----------------");
             System.out.println("执行结果如下所示:");
             System.out.println("-----------------");
             System.out.println("姓名" + "\t" + "职称");
             System.out.println("-----------------");
             String job=null;
             String id=null;
             while(rs.next())
             {
                 job=rs.getString("job");
                 id=rs.getString("ename");
                 System.out.println(id+"\t"+job);
             }
             rs.close();
             con.close();
         }
         catch(ClassNotFoundException e)
         {
             //数据库驱动类异常处理
             System.out.println("Sorry,can`t find the Driver!");
             e.printStackTrace();
         }
         catch(SQLException e)
         {
             //数据库连接失败异常处理
             e.printStackTrace();
         }
         catch(Exception e)
         {
             e.printStackTrace();
         }
         finally {
             if(con!=null)
             {
                 con.close();
                 con=null;
             }
             System.out.println("数据库数据成功获取！！");
         }
     }
}
