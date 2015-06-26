package com.circle.servlet;/**
 * Created by snow on 15-5-31.
 */

import com.circle.function.MD5;
import com.circle.function.PrintToHtml;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response=httpServletResponse;
    }
    private String ret;
    private String account;
    private String password_md5;

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("login:"+account+","+password_md5);
        String ret = "";
        String url = "jdbc:mysql://localhost:3306/Circle?useUnicode=true&characterEncoding=UTF-8";
        String username = "circle";
        String userpassword = "circleServer";
        String sql = "SELECT * FROM User WHERE account=\'"+account+"\'";
        JSONObject obj = new JSONObject();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,username,userpassword);
            java.sql.Statement stmt = con.createStatement();
//            1、ResultSet executeQuery(String sqlString)：执行查询数据库的SQL语句
//            ，返回一个结果集（ResultSet）对象。
//            2、int executeUpdate(String sqlString)：用于执行INSERT、UPDATE或
//            DELETE语句以及SQL DDL语句，如：CREATE TABLE和DROP TABLE等
//            3、execute(sqlString):用于执行返回多个结果集、多个更新计数或二者组合的
//            语句。
            ResultSet rs = stmt.executeQuery(sql);
//            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;

            while(rs.next()){
                if (rs.getString("password").equals(password_md5)){
                    int x=(int)(Math.random()*100);
                    String token = MD5.MD5(x + "");
                    sql = "UPDATE User set token='"+token+"' WHERE account='"+account+"'";
                    int rows = stmt.executeUpdate(sql) ;
                    if (rows==1){
                        obj.put("status",1);
                        obj.put("token",token);
                    }
                    else{
                        obj.put("status",0);
                        obj.put("token",token);
                    }
                    break;
                }else{
                    obj.put("status",0);
                }
            }
            System.err.println("ret:"+ret);
            if (rs!=null){
                rs.close();
            }
            if (stmt!=null)
                stmt.close();
            if (con!=null)
                con.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            try {
                obj.put("status",0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ret = obj.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword_md5() {
        return password_md5;
    }

    public void setPassword_md5(String password_md5) {
        this.password_md5 = password_md5;
    }

}