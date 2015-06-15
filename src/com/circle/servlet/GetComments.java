package com.circle.servlet;/**
 * Created by snow on 15-6-1.
 */

import com.circle.function.CheckToken;
import com.circle.function.PrintToHtml;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetComments extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response=httpServletResponse;
    }

    private String account;
    private String token;
    private int page;
    private int perpage;
    private int hotspot_id;

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("getcomments:"+account+","+hotspot_id+","+token);
        String ret = "";
        String url = "jdbc:mysql://localhost:3306/Circle?useUnicode=true&characterEncoding=UTF-8";
        String username = "root";
        String userpassword = "PENGZHI";
        String sql = "SELECT * FROM Comment,User WHERE Comment.userAccount=User.account " +
                "AND potId = '" + hotspot_id + "'";
        JSONObject obj = new JSONObject();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, userpassword);
            java.sql.Statement stmt = con.createStatement();
            //判断token
            boolean istoken = CheckToken.CheckToken(account,con,token);
            if (!istoken){
                obj.put("status",0);
                ret = obj.toString();
                PrintToHtml.PrintToHtml(response, ret);
                return null;
            }
            ResultSet rs = stmt.executeQuery(sql);
//            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;
            JSONArray jsonarray = new JSONArray();
            while (rs.next()) {
                JSONObject jsob = new JSONObject();
                jsob.put("comment_id",rs.getInt("commentId"));
                jsob.put("nickname",rs.getString("nickname"));
                jsob.put("avatar_url",rs.getString("avatarUrl"));
                jsob.put("content",rs.getString("content"));
                jsob.put("post_time",rs.getLong("time"));
                jsonarray.put(jsob);
            }
            obj.put("status",1);
            obj.put("hotspots",jsonarray);
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getHotspot_id() {
        return hotspot_id;
    }

    public void setHotspot_id(int hotspot_id) {
        this.hotspot_id = hotspot_id;
    }

}