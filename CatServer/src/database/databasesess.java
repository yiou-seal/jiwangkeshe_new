package database;

import database.entity.UsersEntity;

import javax.xml.registry.infomodel.User;
import java.sql.*;
public class databasesess
{
    Connection con;
    public static void main(String [] args)
    {
        Connection con;
        Statement st;
        ResultSet result;

        String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL="jdbc:sqlserver://LAPTOP-U0GIVCOU\\ZANG;database=musicgwydb";
        String userName="admin";
        String userPwd="1234";
        //jdbc:sqlserver://localhost:1433;integratedSecurity=true选择windows本地验证登陆。
//        String sql=("select st.Sid'学号',st.Sname'姓名',ug.gname'班级',uc.Cname'课程',sc.score2'期末成绩'from uStudent st inner join uSC sc on st.Sid=sc.sid inner join uGrade ug on st.gid=ug.gid inner join uCourse uc on sc.cid=uc.Cid order by st.Sid");
        String sql="select * from users";
        try
        {
            Class.forName(driverName);
            con= DriverManager.getConnection(dbURL,userName,userPwd);
            st=con.createStatement();
            result=st.executeQuery(sql);
            int col=result.getMetaData().getColumnCount();

            System.out.println("成功");
            while(result.next())
            {
                for(int i=1;i<=col;i++)
                    System.out.print(result.getString("username") + "\t");
                System.out.println();
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public databasesess()
    {
        try
        {
            String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String dbURL="jdbc:sqlserver://LAPTOP-U0GIVCOU\\ZANG;database=musicgwydb";
            String userName="admin";
            String userPwd="1234";
            Class.forName(driverName);
            con= DriverManager.getConnection(dbURL,userName,userPwd);

        }
        catch (Exception e)
        {

        }

    }

    public  UsersEntity getuserinfo(String userid)
    {

        Statement st;
        ResultSet result;


        //jdbc:sqlserver://localhost:1433;integratedSecurity=true选择windows本地验证登陆。
//        String sql=("select st.Sid'学号',st.Sname'姓名',ug.gname'班级',uc.Cname'课程',sc.score2'期末成绩'from uStudent st inner join uSC sc on st.Sid=sc.sid inner join uGrade ug on st.gid=ug.gid inner join uCourse uc on sc.cid=uc.Cid order by st.Sid");
        String sql="select * from users where userID="+userid;
        UsersEntity user=new UsersEntity();
        try
        {
            st=con.createStatement();
            result=st.executeQuery(sql);
            int col=result.getMetaData().getColumnCount();

            System.out.println("成功");
            while(result.next())
            {
                for(int i=1;i<=col;i++)
                {
                    System.out.print(result.getString("username") + "\t");
                    user.setPassword(result.getString("password"));
                }
                System.out.println();
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return user;
    }
}

