/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.filters;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import project.web.Loginbean;
/**
 *
 * @author mohanbabu
 */
public class loginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {     
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession ses = req.getSession(false);
        //Loginbean session =(Loginbean) req.getSession().getAttribute("loginbean");
        String url = req.getRequestURI();
        
        /* 
        If request for home or logout and no session, redirect and the request to login page
        If request for register or login and with session, redirect and the request to home page
        If request is for logout and with session, remove the session and redirect to login page 
        */
//      if (session == null || !session.isLogged) {
//        if (url.indexOf("home.xhtml") >= 0 || url.indexOf("logout.xhtml") >= 0) {
//            resp.sendRedirect(req.getServletContext().getContextPath() + "/login.xhtml");
//        }else {
//            chain.doFilter(request, response);
//            //resp.sendRedirect(req.getServletContext().getContextPath() + "/home.xhtml");
//        }
//      } else {
//        if (url.indexOf("register.xhtml") >= 0 || url.indexOf("login.xhtml") >= 0) {
//            resp.sendRedirect(req.getServletContext().getContextPath() + "/home.xhtml");
//        }else if (url.indexOf("logout.xhtml") >= 0) {
//            req.getSession().removeAttribute("bean");
//            resp.sendRedirect(req.getServletContext().getContextPath() + "/login.xhtml");
//        }else {
//            chain.doFilter(request, response);
//        }
//      }
                 try {
 
            // check whether session variable is set
            
            //  allow user to proccede if url is login.xhtml or user logged in.
            if ( req.getRequestURI().contains("prompts") || url.indexOf("/index.xhtml") >= 0 || url.indexOf("/login.xhtml") >= 0 || (ses != null && ses.getAttribute("username") != null)|| url.indexOf("/register.xhtml") >= 0 || url.indexOf("/aboutus.xhtml") >= 0 || url.contains("javax.faces.resource") )
                chain.doFilter(request, response);
            else
                // user didn't log in but asking for a page that is not allowed so take user to login page
                res.sendRedirect(req.getContextPath() + "/index.xhtml");  // Anonymous user. Redirect to login page
      }
     catch(Throwable t) {
         System.out.println( t.getMessage());
     }
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
