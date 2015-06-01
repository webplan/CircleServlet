package com.circle.servlet;/**
 * Created by snow on 15-6-1.
 */

import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostPhoto extends ActionSupport {
    private static final long serialVersionUID = 1L;

    private String account;
    private String token;
    private String text_description;
    private String photoUrl;

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("enter:"+account+","+token+","+text_description+","+photoUrl);
        String ret = "";
        String url = "jdbc:mysql://localhost:3306/Circle";
        String username = "root";
        String userpassword = "PENGZHI";

        //TODO 对token的处理  photoUrl在文档中没有
        String sql = "INSERT INTO Message VALUES(\""+account+"\",\""+"NULL"
                +"\",\""+text_description+"\",\""+photoUrl +
                "\",\""+System.currentTimeMillis()+"\")";
        JSONObject obj = new JSONObject();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, userpassword);
            java.sql.Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(sql);
            int rows = stmt.executeUpdate(sql);
//            boolean flag = stmt.execute(String sql) ;

            if (rows==1)
                obj.put("status",1);
            if (stmt != null)
                stmt.close();
            if (con != null)
                con.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            try {
                obj.put("status", 0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ret = obj.toString();
        System.err.println("ret:"+ret);
        return "1";
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getText_description() {
        return text_description;
    }

    public void setText_description(String text_description) {
        this.text_description = text_description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


}