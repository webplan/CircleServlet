package com.circle.servlet;/**
 * Created by snow on 15-6-13.
 */

import com.circle.function.PrintToHtml;
import com.circle.function.Servlet;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

public class AddFriend extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;
    private String account;
    private String token;
    private String friend_account;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("addfriend:"+account+","+friend_account+","+token);
        String ret = "";

        JSONObject obj = Servlet.addFriend(account,token,friend_account);

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