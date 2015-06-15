package com.circle.servlet;/**
 * Created by snow on 15-6-1.
 */

import com.circle.function.CheckToken;
import com.circle.function.PrintToHtml;
import com.circle.function.UploadPhoto;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostPhoto extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response=httpServletResponse;
    }
    private String account;
    private String token;
    private String text_description;
    private String image;

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("enter:"+account+","+token+","+text_description+","+image);
        String ret = "";
        String url = "jdbc:mysql://localhost:3306/Circle";
        String username = "root";
        String userpassword = "PENGZHI";

        JSONObject obj = new JSONObject();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, userpassword);
            java.sql.Statement stmt = con.createStatement();
            //判断token
            boolean istoken = CheckToken.CheckToken(account, con, token);

            //image存起來  傳一個url到數據庫
            String imageUrl = UploadPhoto.UploadPhoto(image,account);
            System.err.println("imageUrl:"+imageUrl);
            String sql = "INSERT INTO Message (account,textDescription,imageUrl,time) VALUES(\""+account+"\",\""
                    +text_description+"\",\""+imageUrl +"\",\""+System.currentTimeMillis()+"\")";
            if (!istoken||imageUrl==null){
                obj.put("status",2);
                ret = obj.toString();
                PrintToHtml.PrintToHtml(response, ret);
                return null;
            }
//            ResultSet rs = stmt.executeQuery(sql);
            int rows = stmt.executeUpdate(sql);
//            boolean flag = stmt.execute(String sql) ;

            if (rows==1)
                obj.put("status",1);
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

    public String getText_description() {
        return text_description;
    }

    public void setText_description(String text_description) {
        this.text_description = text_description;
    }

    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }
}