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

public class GetHotsPot extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response=httpServletResponse;
    }
    private String account;
    private String token;
    private int msg_id;
    private int page;
    private int perpage;


    //定义处理用户请求的execute方法
    public String execute() {
        System.err.println("gethotspot:"+account+","+msg_id+","+token);

        String ret = "";
        String url = "jdbc:mysql://localhost:3306/Circle?useUnicode=true&characterEncoding=UTF-8";
        String username = "circle";
        String userpassword = "circleServer";
        String sql = "SELECT * FROM HotsPot WHERE messageId = '" + msg_id + "'";
        JSONObject obj = new JSONObject();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, userpassword);
            java.sql.Statement stmt = con.createStatement();
            //判断token
            boolean istoken = CheckToken.CheckToken(account,con,token);
            if (!istoken){
                obj.put("status",2);
                ret = obj.toString();
                PrintToHtml.PrintToHtml(response, ret);
                return null;
            }
            ResultSet rs = stmt.executeQuery(sql);
//            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;
            if (rs!=null)
                obj.put("status",1);
            JSONArray jsonarray = new JSONArray();
            while (rs.next()) {
                JSONObject jsob = new JSONObject();
                jsob.put("hotspot_id",rs.getInt("potId"));
                jsob.put("x",rs.getInt("potX"));
                jsob.put("y",rs.getInt("potY"));
                //TODO 獲取點的評論數量
                jsob.put("count",0);
                jsonarray.put(jsob);
            }
            obj.put("hotspots",jsonarray);
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

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
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

}