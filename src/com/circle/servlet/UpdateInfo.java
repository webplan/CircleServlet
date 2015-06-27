package com.circle.servlet;/**
 * Created by snow on 15-6-13.
 */

import com.circle.function.PrintToHtml;
import com.circle.function.Servlet;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

public class UpdateInfo extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;
    private String account;
    private String token;
    private String nickname;
    private String avatar;
    private int gender;
    private String old_pwd_md5;
    private String new_pwd_md5;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("timeline:" + account + "," + token + "," + nickname+"," +
                avatar+","+gender+","+old_pwd_md5+","+new_pwd_md5);
        String ret = "";

        JSONObject obj = Servlet.updateInfo(account,token,nickname,avatar,gender,old_pwd_md5,new_pwd_md5);

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
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