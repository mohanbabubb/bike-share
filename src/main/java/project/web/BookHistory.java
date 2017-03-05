/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import project.database.DBconnection;

/**
 *
 * @author mohanbabu
 */
@ManagedBean(name = "bookhistory", eager = true)
@SessionScoped

public class BookHistory implements Serializable {
    
    private int share_book_id; // Alias for the timeslot record id
    private Date sharedate;
    private String notes;
    private String bookingstatus;
    public int bike_id;
    private String modelname;
    private Date booked_date;
    private String shared_by;

    public String getShared_by() {
        return shared_by;
    }

    public void setShared_by(String shared_by) {
        this.shared_by = shared_by;
    }

    public Date getBooked_date() {
        return booked_date;
    }

    public void setBooked_date(Date booked_date) {
        this.booked_date = booked_date;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    
    public int getShare_book_id() {
        return share_book_id;
    }

    public void setShare_book_id(int share_book_id) {
        this.share_book_id = share_book_id;
    }

    public int getBike_id() {
        return bike_id;
    }

    public void setBike_id(int bike_id) {
        this.bike_id = bike_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBookingstatus() {
        return bookingstatus;
    }

    public void setBookingstatus(String bookingstatus) {
        this.bookingstatus = bookingstatus;
    }

    public Date getSharedate() {
        return sharedate;
    }

    public void setSharedate(Date sharedate) {
        this.sharedate = sharedate;
    }

    Connection con = null;
    ResultSet rs = null;
    PreparedStatement ps = null;

  
    public List<BookHistory> viewBookHistory() {
        
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
     String string_val = session.getAttribute("username").toString();

        List<BookHistory> viewbookedlist = new ArrayList<BookHistory>();
        con = DBconnection.getConnection();     
        String sql = "SELECT bk.modelname name,sb.share_date share_date,sb.booked_date bkdate,(select user from users u1 where u1.id=bk.user_id) shared_by FROM shares_and_bookings sb,bikes bk,users us WHERE sb.share_date < curdate() and bk.id=sb.bike_id and sb.bookingstatus in ('Booked')and us.id=sb.booked_by and us.id=(select id from users where user='"+string_val+"')";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                BookHistory stb1 = new BookHistory();
                stb1.setModelname(rs.getString("name"));
                stb1.setSharedate(rs.getDate("share_date"));
                stb1.setBooked_date(rs.getDate("bkdate"));
                stb1.setShared_by(rs.getString("shared_by"));
                //store all data into a List
                viewbookedlist.add(stb1);
                // All open connection to be closed.
   
            }
            rs.close();
            ps.close(); // All open connection to be closed.
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clear();
        }

        return viewbookedlist;
    }

    
    private void clear() {
        setBookingstatus(null);
        setSharedate(null);
        setNotes(null);
    }
}
