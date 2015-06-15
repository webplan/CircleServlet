package com.circle.servlet;/**
 * Created by snow on 15-6-1.
 */

import com.circle.function.CheckToken;
import com.circle.function.PrintToHtml;
import com.opensymphony.xwork2.ActionSupport;
import freemarker.log.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TimeLine extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response=httpServletResponse;
    }

    private String account;
    private String token;
    private String page;
    private String perpage;

    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("timeline:" + account + "," + token + "," + page);

        String ret = "";
        String url = "jdbc:mysql://localhost:3306/Circle?useUnicode=true&characterEncoding=UTF-8";
        String username = "root";
        String userpassword = "PENGZHI";
        //多行子查询 ：http://blog.csdn.net/devercn/article/details/22986
        String sql = "SELECT * FROM Message,User WHERE Message.account in" +
                "(SELECT FriendAccount FROM Friend WHERE UserAccount='" + account + "')"+
                " AND Message.account=User.account ORDER BY Message.time DESC ";
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

            JSONArray jsonarray = new JSONArray();
            if (rs == null)
                obj.put("status", 0);
            else {
                while (rs.next()) {
                    JSONObject jsob = new JSONObject();
                    //msg_id 是我存到数据库messageId
                    jsob.put("msg_id", rs.getString("messageId"));
                    jsob.put("nickname", rs.getString("nickname"));
                    jsob.put("avatar_url", rs.getString("avatarUrl"));
                    jsob.put("photo_url", rs.getString("imageUrl"));
                    jsob.put("text_description", rs.getString("textDescription"));
                    //jsob.put("voice_description_url",rs.getString("voiceDescriptionUrl"));
                    jsob.put("post_time", rs.getLong("time"));

                    jsonarray.put(jsob);
                    int s = jsonarray.length();
                    jsob = jsonarray.getJSONObject(0);
                }
            }
            obj.put("status", 1);
            obj.put("timeline", jsonarray);
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
            try {
                obj.put("status", 0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ret = obj.toString();
        //System.err.println("ret:" + ret);
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