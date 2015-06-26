package com.circle.function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

/** 设置servlet
 * Created by PZ on 15-6-26.
 */
public class Servlet {
    public static Connection con = null;

    public static Connection connectSQL(){
        if (con==null) {
            String url = "jdbc:mysql://localhost:3306/Circle?useUnicode=true&characterEncoding=UTF-8";
            String username = "circle";
            String userpassword = "circleServer";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url, username, userpassword);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return con;
    }

    public static JSONObject addFriend(String account,String token,String friend_account){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        String sql = "INSERT INTO Friend VALUES(\""+account+"\",\""+
                friend_account +"\")"+",(\""+friend_account+"\",\""+
                account +"\");";
        try {
            Statement stmt = con.createStatement();
            boolean istoken = CheckToken.CheckToken(account, con, token);
            if (!istoken){
                obj.put("status",2);
                return obj;
            }
//            ResultSet rs = stmt.executeQuery(sql);
            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;
            if (rows==2){
                obj.put("status",1);
            }else{
                obj.put("status",0);
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
        return obj;
    }

    public static JSONObject deleteFriend(String account,String token,String friend_account){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        String sql = "DELETE * FROM Friend WHERE userAccount = '" + account + "' AND " +
                "friendAccount = '"+friend_account+"';" +
                "DELETE * FROM Friend WHERE userAccount = '" + friend_account + "' AND " +
                "friendAccount = '"+account+"'" ;
        try {
            Statement stmt = con.createStatement();
            boolean istoken = CheckToken.CheckToken(account, con, token);
            if (!istoken){
                obj.put("status",2);
                return obj;
            }
            //ResultSet rs = stmt.executeQuery(sql);
            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;

            if (rows==2){
                obj.put("status",1);
            }else
                obj.put("status",0);

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
        return obj;
    }

    public static JSONObject getComments(String account,String token,int hotspot_id){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        String sql = "SELECT * FROM Comment,User WHERE Comment.userAccount=User.account " +
                "AND potId = '" + hotspot_id + "'";
        try {
            java.sql.Statement stmt = con.createStatement();
            //判断token
            boolean istoken = CheckToken.CheckToken(account,con,token);
            if (!istoken){
                obj.put("status",2);
                return obj;
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
            if (rs != null)
                rs.close();
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
        return obj;
    }

    public static JSONObject getFriends(String account,String token){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        String sql = "SELECT * FROM User WHERE account IN (" +
                "SELECT friendAccount FROM Friend WHERE userAccount = '" + account + "')";
        try {
            java.sql.Statement stmt = con.createStatement();
            boolean istoken = CheckToken.CheckToken(account, con, token);
            if (!istoken){
                obj.put("status",2);
                return null;
            }
            ResultSet rs = stmt.executeQuery(sql);
//            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;
            JSONArray jsonarray = new JSONArray();
            while (rs.next()) {
                JSONObject jsob = new JSONObject();
                jsob.put("account",rs.getString("account"));
                jsob.put("nickname",rs.getString("nickname"));
                jsob.put("avatar_url",rs.getString("avatarUrl"));
                jsonarray.put(jsob);
            }
            obj.put("status",1);
            obj.put("friends",jsonarray);
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
        return obj;
    }

    public static JSONObject getHotsPot(String account,String token,int msg_id){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        String sql = "SELECT * FROM HotsPot WHERE messageId = '" + msg_id + "'";
        try {
            java.sql.Statement stmt = con.createStatement();
            //判断token
            boolean istoken = CheckToken.CheckToken(account,con,token);
            if (!istoken){
                obj.put("status",2);
                return obj;
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

        } catch (Exception e) {
            try {
                obj.put("status", 0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject login(String account,String password_md5){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        String sql = "SELECT * FROM User WHERE account=\'"+account+"\'";
        try {
            java.sql.Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                if (rs.getString("password").equals(password_md5)){
                    int x=(int)(Math.random()*100);
                    String token = MD5.MD5(x + "");
                    sql = "UPDATE User set token='"+token+"' WHERE account='"+account+"'";
                    int rows = stmt.executeUpdate(sql) ;
                    if (rows==1){
                        obj.put("status",1);
                        obj.put("token",token);
                    }
                    else{
                        obj.put("status",0);
                        obj.put("token",token);
                    }
                    break;
                }else{
                    obj.put("status",0);
                }
            }
            if (rs!=null)
                rs.close();
            if (stmt!=null)
                stmt.close();
            if (con!=null)
                con.close();

        } catch (Exception e) {
            try {
                obj.put("status",0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject postComment(String account,String token,String content,double x,double y,int msg_id){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        String sql = "SELECT potId FROM HotsPot WHERE potX='"+x+
                "' AND potY='"+y+"' AND messageId='"+msg_id+"'";
        int potId = -1;
        try {
            java.sql.Statement stmt = con.createStatement();
            //判断token
            boolean istoken = CheckToken.CheckToken(account, con, token);
            if (!istoken){
                obj.put("status",2);
                return obj;
            }
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                potId = rs.getInt("potId");
            }
            while (potId==-1){//找不到pot，新建一個

                sql = "INSERT INTO HotsPot (messageId,potX,potY) VALUES(\""+msg_id+
                        "\",\""+x+"\",\""+y+"\")";
                stmt = con.createStatement();
                stmt.executeUpdate(sql);
                sql = "SELECT potId FROM HotsPot WHERE messageId="+msg_id+" AND potX="+x+" AND potY="+y;
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    potId = rs.getInt("potId");
                }
                System.err.println("potId::::"+potId);
            }
            //插入comment
            sql = "INSERT INTO Comment (userAccount,content,time,potId) VALUES(\""+account
                    +"\",\""+content+"\",\""+System.currentTimeMillis() + "\",\""+potId+"\")";
            stmt = con.createStatement();
            int rows = stmt.executeUpdate(sql);
            if (rows==1)
                obj.put("status",1);

            if (rs != null)
                rs.close();
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
        return obj;
    }

    public static JSONObject postPhoto(String account,String token,String text_description,String image){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        try {
            java.sql.Statement stmt = con.createStatement();
            //判断token
            boolean istoken = CheckToken.CheckToken(account, con, token);

            //image存起來  傳一個url到數據庫
            String imageUrl = UploadPhoto.UploadPhoto(image,account);
            System.err.println("imageUrl:"+imageUrl);
            String sql = "INSERT INTO Message (account,textDescription,imageUrl,time) VALUES(\""+account+"\",\""
                    +text_description+"\",\""+imageUrl +"\",\""+System.currentTimeMillis()+"\")";
            if (!istoken){
                obj.put("status",2);
                return obj;
            }
            if(imageUrl==null){
                obj.put("status",0);
                return obj;
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
        return obj;
    }

    public static JSONObject register(String account,String password_md5,String nickname){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        String sql = "INSERT INTO User ( account,password,nickname) VALUES(\""+account+"\",\""+password_md5
                +"\",\""+nickname+""+"\")";
        try {
            java.sql.Statement stmt = con.createStatement();
            //判断是否用户名重复,如果重复则跳到catch中，如果插入行数不为1则插入失败，
            int rows = stmt.executeUpdate(sql) ;

            if (rows==1){
                obj.put("status",1);
            }else
                obj.put("status",0);
            sql = "INSERT INTO Friend VALUES(\""+account+"\",\""+account+"\")";
            rows = stmt.executeUpdate(sql) ;
            if (rows==1){
                obj.put("status",1);
            }else
                obj.put("status",0);
            if (stmt != null)
                stmt.close();
            if (con != null)
                con.close();

        } catch (Exception e) {
            try {
                obj.put("status",0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject searchUser(String account,String token,String nickname){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        String sql = "SELECT * FROM User WHERE nickname = '" + nickname + "'";
        try {
            java.sql.Statement stmt = con.createStatement();
            boolean istoken = CheckToken.CheckToken(account, con, token);
            if (!istoken){
                obj.put("status",2);
                return obj;
            }
            ResultSet rs = stmt.executeQuery(sql);
//            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;

            JSONArray jsonarray = new JSONArray();
            while (rs.next()) {
                JSONObject jsob = new JSONObject();
                jsob.put("account",rs.getString("account"));
                jsob.put("avatar_url",rs.getString("avatarUrl"));
                jsonarray.put(jsob);
            }
            obj.put("person",jsonarray);
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
        return obj;
    }

    public static JSONObject timeLine(String account,String token){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        //多行子查询 ：http://blog.csdn.net/devercn/article/details/22986
        String sql = "SELECT * FROM Message,User WHERE Message.account in" +
                "(SELECT FriendAccount FROM Friend WHERE UserAccount='" + account + "')"+
                " AND Message.account=User.account ORDER BY Message.time DESC ";
        try {
            java.sql.Statement stmt = con.createStatement();
            //判断token
            boolean istoken = CheckToken.CheckToken(account, con, token);
            if (!istoken){
                obj.put("status",2);
                return obj;
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

        } catch (Exception e) {
            try {
                obj.put("status", 0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject updateInfo(String account,String token,String nickname,String avatar,int gender,
    String old_pwd_md5,String new_pwd_md5){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        try {
            Statement stmt = con.createStatement();
            boolean istoken = CheckToken.CheckToken(account, con, token);
            // 存下來avatar，轉成avatarUrl
            String avatarUrl = UploadPhoto.UploadPhoto(avatar,account);
            String sql;
            if (old_pwd_md5!=null&&new_pwd_md5!=null)
                sql = "UPDATE User SET nickname="+nickname+
                        ",avatarUrl="+avatarUrl+
                        ",gender="+gender+
                        ",password="+new_pwd_md5+
                        "WHERE account = '" + account + "' AND password = " +
                        "(SELECT password FROM USER WHERE account=" +account+")";
            else
                sql = "UPDATE User SET nickname="+nickname+
                        ",avatarUrl="+avatarUrl+
                        ",gender="+gender+
                        "WHERE account = '" + account + "'";
            if (!istoken){
                obj.put("status",2);
                return obj;
            }
//            ResultSet rs = stmt.executeQuery(sql);
            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;
            if (rows==1)
                obj.put("status",1);
            else
                obj.put("status",0);

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
        return obj;
    }

    public static JSONObject userInfo(String account,String token,String user_account){
        Connection con = connectSQL();
        JSONObject obj = new JSONObject();
        String sql = "SELECT * FROM User WHERE account = '" + user_account + "'";
        try {
            java.sql.Statement stmt = con.createStatement();
            boolean istoken = CheckToken.CheckToken(account, con, token);
            if (!istoken){
                obj.put("status",2);
                return obj;
            }
            ResultSet rs = stmt.executeQuery(sql);
//            int rows = stmt.executeUpdate(sql) ;
//            boolean flag = stmt.execute(String sql) ;

            while (rs.next()) {
                obj.put("nickname",rs.getString("nickname"));
                obj.put("avatar_url",rs.getString("avatarUrl"));
                obj.put("gender",rs.getInt("gender"));
                obj.put("status",1);
            }
            if (rs != null)
                rs.close();
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
        return obj;
    }
}
