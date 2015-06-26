package com.circle.servlet;/**
 * Created by snow on 15-5-31.
 */

import com.circle.function.PrintToHtml;
import com.circle.function.Servlet;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Register extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response=httpServletResponse;
    }
    private String account;
    private String password_md5;
    private String nickname;


    //定义处理用户请求的execute方法
    public String execute(){
        System.err.println("register:"+account+","+password_md5+","+nickname);
        String ret = "";

        JSONObject obj = Servlet.register(account,password_md5,nickname);

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