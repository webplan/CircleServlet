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

public class UpdateInfo extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private String account;
    private String token;
    private String nickname;
    private byte[] avatar;
    private int gender;
    private String old_pwd_md5;
    private String new_pwd_md5;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        String url = "jdbc:mysql://localhost:3306/Circle";
        String username = "root";
        String userpassword = "PENGZHI";
        //TODO 存下來avatar，轉成avatarUrl
        String avatarUrl = "";
        String sql;
        if (old_pwd_md5!=null&&new_pwd_md5!=null)
            sql = "UPDATE User SET nickname="+nickname+
                ",avatarUrl="+avatarUrl+
                ",gender="+gender+
                ",password="+new_pwd_md5+
                "WHERE account = '" + account + "' AND password = " +
                    "(SELECT password FROM USER WHERE account=" +account+")";
        else
            sql = "UPDATE User SET nickname="+nickname+
                ",avatarUrl="+avatarUrl+
                ",gender="+gender+
                "WHERE account = '" + account + "'";

        JSONObject obj = new JSONObject();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, userpassword);
            java.sql.Statement stmt = con.createStatement();
            boolean istoken = CheckToken.CheckToken(account, con, token);
            if (!istoken){
                obj.put("status",0);
                ret = obj.toString();
                PrintToHtml.PrintToHtml(response, ret);
                return null;
            }
//            ResultSet rs = stmt.executeQuery(sql);
            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;
            if (rows==1)
                obj.put("status",1);
            else
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getOld_pwd_md5() {
        return old_pwd_md5;
    }

    public void setOld_pwd_md5(String old_pwd_md5) {
        this.old_pwd_md5 = old_pwd_md5;
    }

    public String getNew_pwd_md5() {
        return new_pwd_md5;
    }

    public void setNew_pwd_md5(String new_pwd_md5) {
        this.new_pwd_md5 = new_pwd_md5;
    }
}