package com.cwgis;


import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class test_mysql_jdbc {
     public static void main()
             throws Exception
     {
         //声明Connection对象
         Connection con=null;
         //驱动程序名
         String driver="com.mysql.jdbc.Driver";
         //修改数据库连接，使用以下方式指定编码：
         //Url=jdbc:mysql://192.168.30.114:3307/sqltestdb
         //添加指定字符编码?useUnicode=true&characterEncoding=utf8
         String url="jdbc:mysql://192.168.30.114:3307/sqltestdb?useUnicode=true&characterEncoding=utf8";
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
             //添加数据
             PreparedStatement psql;
             psql=con.prepareStatement("delete from emp where empno=?");
             psql.setFloat(1,9999);
             psql.executeUpdate();
             ResultSet res;
             psql=con.prepareStatement("insert into emp(empno,ename,job,hiredate,sal) "
                     +"values(?,?,?,?,?)");
             psql.setInt(1,9999);   //设置参数1，创建empno为3212的数据
             psql.setString(2,"王刚");   //ename=王刚
             psql.setString(3,"总裁");

             DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
             java.util.Date mydate2= df.parse("2010-09-13");
             psql.setDate(4,new java.sql.Date(mydate2.getTime()));
             psql.setFloat(5,(float)2000.3);
             psql.executeUpdate();
             psql.close();
             //修改数据
             PreparedStatement update_sql;
             update_sql=con.prepareStatement("update emp set sal=? where empno=?");
             update_sql.setFloat(1,(float)5000.0);
             update_sql.setInt(2,3212);
             update_sql.executeUpdate();
             update_sql.close();
             //删除数据
             PreparedStatement delsql;
             delsql=con.prepareStatement("delete from emp where sal>?");
             delsql.setFloat(1,4500);
             delsql.executeUpdate();
             delsql.close();
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
