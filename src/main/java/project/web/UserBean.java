/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import project.database.DBconnection;

/**
 *
 * @author mohanbabu
 */
@ManagedBean(name = "registerbean")
@SessionScoped
public class UserBean {
    private int id;
	private String userName;
	private String password;
        private String password2;
        private String email;
        private String answers;
	private String questions;
        private Integer mobileNo;
        private String firstname;
        private String lastname;
        
    Connection con = null;
    ResultSet rs = null;
    PreparedStatement ps = null;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    
    
    public Integer getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Integer mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
        
    
        
    public String addUser() 
    {
        
        if (!password2.equals(password)) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!", "Passwords do not match!");
        throw new ValidatorException(message);
        }

	  int rowadded = 0;	
	  
          con=DBconnection.getConnection();
	  String sql = "INSERT INTO users(user,email,pass,question,answer) VALUES(?,?,?,?,?)";
          try {
            ps= con.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, questions);
            ps.setString(5, answers);
            rowadded = ps.executeUpdate();
            ps.close(); // All open connection to be closed.
          }
          catch(Exception e)
          {
            e.printStackTrace();	
          }
          
        //} // if else        
          if (rowadded > 0) {
              System.out.println("A new USER was added successfully!");
              return "register-success";
          }else
          {
              return "login";
          }
          
    }
    
    public String updateUser(){
        
        if (!password2.equals(password)) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match! -- GO BACK AND try again", "Passwords do not match! -- GO BACK AND try again");
        throw new ValidatorException(message);
        }
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();
        int rowupdated = 0;	
	  
          con=DBconnection.getConnection();
	  String sql = "UPDATE users SET pass=?,question=?,answer=?,firstname=?,lastname=?,mobileNo=? where user='"+string_val+"'";
          try {
            ps= con.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, questions);
            ps.setString(3, answers);
            ps.setString(4, firstname);
            ps.setString(5, lastname);
            ps.setInt(6, mobileNo);
            rowupdated = ps.executeUpdate();
            ps.close(); // All open connection to be closed.
          }
          catch(Exception e)
          {
            e.printStackTrace();	
          }
          System.out.println(rowupdated);
        //} // if else        
          if (rowupdated > 0) {
              System.out.println("The profile is updated successfully!");
              return "myprofile-success";
          }else
          {
              return "myprofile-failed";
          }
    }
    
    public void onload(){
        
        //int rowfetched = 0;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();

        con = DBconnection.getConnection();
        String sql = "select user,email,question,answer,firstname,lastname,mobileNo from users where user='"+string_val+"' ";
        try {
            ps = con.prepareStatement(sql);
            rs=ps.executeQuery();
            if (rs.next()) {
                this.setUserName(rs.getString("user"));
                this.setEmail(rs.getString("email"));
                this.setQuestions(rs.getString("question"));
                this.setAnswers(rs.getString("answer"));
                this.setFirstname(rs.getString("firstname"));
                this.setLastname(rs.getString("lastname"));
                this.setMobileNo(rs.getInt("mobileNo"));
            }
            ps.close(); // All open connection to be closed.
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
            //System.out.println("DATA AFTER setting into the bean");
            //System.out.println(bike_id);
            //clear();
        }
        
    }
        private void clear() {
        //setBookingstatus(null);
        //setSharedate(null);
        //setNotes(null);
    }
}
