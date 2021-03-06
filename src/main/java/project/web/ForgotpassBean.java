/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import project.database.DBconnection;
import project.web.util.Session;

/**
 *
 * @author mohanbabu
 */
@ManagedBean(name = "forgotbean")
@SessionScoped
public class ForgotpassBean {

    private String password;
    private String conformpassword;
    private String userName;
    private String message;
    private String questions;
    private String answers;
    private String email;

    Connection con = null;
    ResultSet rs = null;
    PreparedStatement ps = null;

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

    public String getConformpassword() {
        return conformpassword;
    }

    public void setConformpassword(String conformpassword) {
        this.conformpassword = conformpassword;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    public String checkAnswer(String username,String question) throws SQLException{
    
            con = DBconnection.getConnection();
        try {
                String sql = "select user from users where user='"+username+"' and question='"+question+"' and answer='"+answers+"'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {    
                    RecoverPassword(rs.getString("user"));
                    return "passwordrecovery";
                } else {
                    return "forgotpass1-answerfailed";
                }
                
                
        }catch (Exception e) {
        e.printStackTrace();
        }finally{
        close_connecitons();
        }
        return "forgotpass1-answerfailed";
    }
    

    public String forgotcheck(String type) {

        try {
        
            if (type.equals("user")) {
                if (userExists(userName, type)){
                    return "question";
                }else {
                    return "usernotexists";
                }
            }else if (type.equals("email")) {
                if (userExists(userName, type)){
                    return "question";
                }else {
                    return "emailnotexists";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            clear();
        }
           return "usernotexists";
    }

    public boolean userExists(String usern, String validatetype) throws SQLException {
        
        con = DBconnection.getConnection();
    try {
            if ( validatetype.equals("email") ){
                String sql = "select user,question from users where user='"+email+"'";
                ps = con.prepareStatement(sql);
            }else if (validatetype.equals("user")) {
             String sql = "select user,question from users where user='"+usern+"'";
                ps = con.prepareStatement(sql);
            }       
            rs = ps.executeQuery();
            if (rs.next()) // found
            {
                this.setUserName(rs.getString("user"));
                this.setQuestions(rs.getString("question"));
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close_connecitons();
        }
        return false;
    }
    
    
        public void RecoverPassword(String username) throws SQLException {

        try {

            con = DBconnection.getConnection();
            String sql = "select pass from users where user='"+username+"'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) // found
            {
                this.setPassword(rs.getString("pass"));
            }   
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            //clear();
            close_connecitons();
        }
    }
    
        private void clear() {
            setPassword(null);
    }
        public void clear_all()
        {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            setUserName(null);
            setEmail(null);
            setQuestions(null);
            setAnswers(null);
            setPassword(null);
            setEmail(null);
        }
        
        private void close_connecitons() throws SQLException
        {   
            con.close();
            rs.close();
            ps.close();
            
        }
}
