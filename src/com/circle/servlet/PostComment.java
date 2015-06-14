package com.circle.servlet;/**
 * Created by snow on 15-6-1.
 */

import com.circle.function.CheckToken;
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

public class PostComment extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response=httpServletResponse;
    }
    private String account;
    private String token;
    private String content;
    private double x;
    private double y;
    private int messageId;


    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println(account+","+token+","+content+","+x+","+y);
        String ret = "";
        String url = "jdbc:mysql://localhost:3306/Circle";
        String username = "root";
        String userpassword = "PENGZHI";
        String sql = "SELECT potId FROM HostPot WHERE potX='"+x+
                "' AND potY='"+y+"' AND messageId='"+messageId+"'";
        int potId = -1;
        JSONObject obj = new JSONObject();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, userpassword);
            java.sql.Statement stmt = con.createStatement();
            //判断token
            boolean istoken = CheckToken.CheckToken(account, con, token);
            if (!istoken){
                obj.put("status",0);
                ret = obj.toString();
                PrintToHtml.PrintToHtml(response, ret);
                return null;
            }
            ResultSet rs = stmt.executeQuery(sql);
//            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;

            while (rs.next()) {
                potId = rs.getInt("potId");
            }
            System.err.println("potId:"+potId);
            if (potId==-1){//找不到pot，新建一個
                sql = "INSERT INTO HostPot VALUES(\""+messageId+"\",\""+"NULL"
                        +"\",\""+x+"\",\""+y+"\")";
                stmt = con.createStatement();
                stmt.executeUpdate(sql);
            }
            //插入comment
            sql = "INSERT INTO Comment VALUES(\""+"NULL"+"\",\""+account
                    +"\",\""+content+"\",\""+System.currentTimeMillis() + "\",\""+potId+"\")";
            stmt = con.createStatement();
            int rows = stmt.executeUpdate(sql);
            if (rows==1)
                obj.put("status",1);

            if (rs != null) {
                rs.close();
            }
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

}