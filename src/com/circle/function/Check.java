package com.circle.function;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by snow on 15-6-1.
 */
public class Check {

    public static boolean CheckToken(String account,Connection con,String token){
        String sql = "SELECT * FROM User WHERE account=\'"+account+"\'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString("token").equals(token)) {
                    if (rs != null) {
                        rs.close();
                    }
                    return true;
                }
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkFriend(String userAccount, String friendAccount, Connection con) {
        String sql = "SELECT * FROM friend WHERE useraccount=\'" + userAccount + "\' AND friendaccount=\'" + friendAccount + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                return true;
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkPassword(String account, String password, Connection con) {
        String sql = "SELECT * FROM User WHERE account=\'" + account + "\'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString("password").equals(password)) {
                    if (rs != null) {
                        rs.close();
                    }
                    return true;
                }
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
