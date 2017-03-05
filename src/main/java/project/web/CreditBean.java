/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import project.database.DBconnection;

/**
 *
 * @author mohanbabu
 */
@ManagedBean(name = "creditbean")
@SessionScoped
public class CreditBean implements Serializable {

    private int credits;
    
    
    Connection con = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    
    
    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
    
//    public String addCredits() 
//    {
//           
//    }
//     
//     public String reduceCredits()
//     {
//         
//     }
     
     public int fetchUserCredits()
     {
        int rowfecthed = 0;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        
          con=DBconnection.getConnection();
	  String sql = "SELECT credits from users where user='" + session.getAttribute("username").toString() + "'";
          try {
            ps= con.prepareStatement(sql);
            ResultSet rs =  ps.executeQuery();
            
            if(rs.next()){
            this.setCredits(rs.getInt("credits")); 
            }
            rs.close();
            ps.close(); // All open connection to be closed.
            con.close();
          }
          catch(Exception e)
          {
            e.printStackTrace();	
          }
          
        //} // if else        
          if (rowfecthed >= 0) {
              System.out.println("Fetched the credit value.");
              System.out.println(credits);
          }
          return credits;
         
     }    
     
    
}
