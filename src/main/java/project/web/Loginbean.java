/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import project.web.util.Session;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
/**
 *
 * @author mohanbabu
 */
@ManagedBean(name = "loginbean")
@SessionScoped
public class Loginbean implements Serializable{

    private static final long serialVersionUID = 1L;
    private String name;
    private String pass;
    public boolean isLogged = false;
    /**
     * Creates a new instance of bean
     */
    public Loginbean() {
        

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    public String login_action(){
        boolean result = UserDAO.login(name, pass);
        if (result) {
            // Get the http session and store the user name in it.
            HttpSession session = Session.getSession();
            session.setAttribute("username", name);
            return "home";
        } else {
            
            return "login-failure";
        }
    }
      public String logout() {
      HttpSession session = Session.getSession();
      session.invalidate();
      FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
      return "login";
   }
}
