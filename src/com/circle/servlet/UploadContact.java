package com.circle.servlet;/**
 * Created by snow on 15-5-31.
 */

import com.circle.function.Check;
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

public class UploadContact extends ActionSupport  implements ServletResponseAware {
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    private String account;
    private String phone_md5;
    private String token;
    private String contacts;
    private String ret;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    //定义处理用户请求的execute方法
    public String execute(){
        String ret = "";
        String url = "jdbc:mysql://localhost:3306/Circle?useUnicode=true&characterEncoding=UTF-8";
        String username = "circle";
        String userpassword = "circleServer";
        //TODO 待改
        String sql = "SELECT * FROM User WHERE account = ''";
        JSONObject obj = new JSONObject();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, userpassword);
            java.sql.Statement stmt = con.createStatement();
            //判断token
            boolean istoken = Check.CheckToken(account, con, token);
            if (!istoken){
                obj.put("status",2);
                ret = obj.toString();
                System.err.println("ret:"+ret);
                return "0";
            }
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

            }
            if (rs != null) {
                rs.close();
            }
            if (stmt != null)
                stmt.close();
            if (con != null)
                con.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getPhone_md5() {
        return phone_md5;
    }

    public void setPhone_md5(String phone_md5) {
        this.phone_md5 = phone_md5;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

}