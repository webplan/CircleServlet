package com.circle.servlet;/**
 * Created by snow on 15-6-1.
 */

import com.circle.function.PrintToHtml;
import com.circle.function.Servlet;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

public class TimeLine extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    private String account;
    private String token;
    private String page;
    private String perpage;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("timeline:" + account + "," + token + "," + page);

        String ret = "";

        JSONObject obj = Servlet.timeLine(account,token);
        ret = obj.toString();
        PrintToHtml.PrintToHtml(response,ret);
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }
}