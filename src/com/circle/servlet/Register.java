package com.circle.servlet;/**
 * Created by snow on 15-5-31.
 */

import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Register extends ActionSupport {
    private static final long serialVersionUID = 1L;

    private String account;
    private String password_md5;
    private String nickname;
    private String ret;


    //定义处理用户请求的execute方法
    public String execute(){
        System.err.println("enter:"+account+","+password_md5+","+nickname);
        String ret = "";
        String url = "jdbc:mysql://localhost:3306/Circle";
        String username = "root";
        String userpassword = "PENGZHI";
        String sql = "INSERT INTO User VALUES(\""+account+"\",\""+password_md5
                +"\",\""+nickname+"\",\""+"" + "\",\""+""+"\")";
        JSONObject obj = new JSONObject();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, userpassword);
            java.sql.Statement stmt = con.createStatement();
            //判断是否用户名重复,如果重复则跳到catch中，如果插入行数不为1则插入失败，
            int rows = stmt.executeUpdate(sql) ;

            if (rows==1){
                obj.put("status",1);
            }else
                obj.put("status",0);

            if (stmt != null)
                stmt.close();
            if (con != null)
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
        System.err.println("ret:"+ret);
        return "1";
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

    public String getNickname() {
        return account;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}