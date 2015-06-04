package com.circle.function;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by snow on 15-6-1.
 */
public class CheckToken {

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

}
