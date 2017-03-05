/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import project.database.DBconnection;
/**
 *
 * @author mohanbabu
 */
public class Question {
    public static boolean login(String questions, String answers) {
	       Connection con = null;
	       PreparedStatement ps = null;
	       try {
	           con = DBconnection.getConnection();
	           ps = con.prepareStatement(
	        		   "select questions, answers from user where questions=? and answers=? ");
	           ps.setString(1, questions);
	           ps.setString(2, answers);
	 
	           ResultSet rs = ps.executeQuery();
	           if (rs.next()) 
	           {
	               System.out.println(rs.getString("answers"));
	               return true;
	           }
	           else {
	               return false;
	           }
	       } catch (Exception ex) {
	         //  System.out.println("Error in login() -->" + ex.getMessage());
	           return false;
	       } finally {
	    	   DBconnection.close(con);
	       }
	   }
    
}
