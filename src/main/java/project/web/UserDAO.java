/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;
import project.database.*;
import java.sql.*;


/**
 *
 * @author mohanbabu
 */
public class UserDAO {
    public static boolean login(String user, String password) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
            con = DBconnection.getConnection();
            ps = con.prepareStatement(
                    "select user, pass from users where user= ? and pass= ? ");
            ps.setString(1, user);
            ps.setString(2, password);
  
            ResultSet rs = ps.executeQuery();
            if (rs.next()) // found
            {
                System.out.println(rs.getString("user"));
                return true;
            }
            else {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Error in login() -->" + ex.getMessage());
            return false;
        } finally {
            DBconnection.close(con);
        }
    }
}