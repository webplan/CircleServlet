package com.circle.servlet;/**
 * Created by snow on 15-6-13.
 */

import com.circle.function.CheckToken;
import com.circle.function.PrintToHtml;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.struts2.interceptor.ServletResponseAware;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;

public class DeleteFriend extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private String account;
    private String token;
    private String friend_account;

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("deletefriend:"+account+","+friend_account+","+token);

        String ret = "";
        String url = "jdbc:mysql://localhost:3306/Circle?useUnicode=true&characterEncoding=UTF-8";
        String username = "circle";
        String userpassword = "circleServer";
        String sql = "DELETE * FROM Friend WHERE userAccount = '" + account + "' AND " +
                "friendAccount = '"+friend_account+"';" +
                "DELETE * FROM Friend WHERE userAccount = '" + friend_account + "' AND " +
                "friendAccount = '"+account+"'" ;
        JSONObject obj = new JSONObject();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, userpassword);
            java.sql.Statement stmt = con.createStatement();
            boolean istoken = CheckToken.CheckToken(account, con, token);
            if (!istoken){
                obj.put("status",2);
                ret = obj.toString();
                PrintToHtml.PrintToHtml(response, ret);
                return null;
            }
            //ResultSet rs = stmt.executeQuery(sql);
            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;

            if (rows==2){
                obj.put("status",1);
            }else
                obj.put("status",0);

            if (stmt != null)
                stmt.close();
            if (con != null)
                con.close();

        } catch (Exception e) {
            try {
                obj.put("status", 0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        ret = obj.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
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

    public String getFriend_account() {
        return friend_account;
    }

    public void setFriend_account(String friend_account) {
        this.friend_account = friend_account;
    }

}