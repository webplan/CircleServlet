package com.circle.servlet;/**
 * Created by snow on 15-6-13.
 */

import com.circle.function.CheckToken;
import com.circle.function.PrintToHtml;
import com.circle.function.Servlet;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.struts2.interceptor.ServletResponseAware;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletResponse;

public class SearchUser extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private String account;
    private String token;
    private String nickname;

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("searchuser:"+account+","+token+","+nickname);
        String ret = "";
        JSONObject obj = Servlet.searchUser(account,token,nickname);
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

    public String getNickname() {
        return nickname;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}