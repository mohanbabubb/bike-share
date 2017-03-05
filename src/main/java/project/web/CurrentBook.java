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
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;    
import javax.servlet.http.HttpSession;
import project.database.DBconnection;

/**
 *
 * @author mohanbabu
 */
@ManagedBean(name = "CurrentBook", eager = true)
@SessionScoped
public class CurrentBook implements Serializable{

    private int share_book_id; // Alias for the timeslot record id
    private Date sharedate;
    private String notes;
    private String bookingstatus;
    public int bike_id;
    private String modelname;
    private Date booked_date;
    private String shared_by;
    private int mobileNo;
    private String bike_type;

    public int getShare_book_id() {
        return share_book_id;
    }

    public void setShare_book_id(int share_book_id) {
        this.share_book_id = share_book_id;
    }

    public Date getSharedate() {
        return sharedate;
    }

    public void setSharedate(Date sharedate) {
        this.sharedate = sharedate;
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

    public int getBike_id() {
        return bike_id;
    }

    public void setBike_id(int bike_id) {
        this.bike_id = bike_id;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public Date getBooked_date() {
        return booked_date;
    }

    public void setBooked_date(Date booked_date) {
        this.booked_date = booked_date;
    }

    public String getShared_by() {
        return shared_by;
    }

    public void setShared_by(String shared_by) {
        this.shared_by = shared_by;
    }

    public int getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(int mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBike_type() {
        return bike_type;
    }

    public void setBike_type(String bike_type) {
        this.bike_type = bike_type;
    }

    Connection con = null;
    ResultSet rs = null;
    PreparedStatement ps = null;

    public List<CurrentBook> getCurrentBookings() {
        List<CurrentBook> currentbookedlist = new ArrayList<CurrentBook>();          
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();
        con = DBconnection.getConnection();
        String sql = "SELECT bk.modelname name,sb.share_date share_date,sb.booked_date bkdate,(select user from users u1 where u1.id=bk.user_id) shared_by,us.mobileNo mobileNo,bk.type biketype FROM shares_and_bookings sb,bikes bk,users us WHERE sb.share_date >= curdate() and bk.id=sb.bike_id and sb.bookingstatus in ('Booked')and us.id=sb.booked_by and sb.booked_by=(select id from users where user='" + string_val + "')";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                CurrentBook stb1 = new CurrentBook();
                stb1.setModelname(rs.getString("name"));
                stb1.setSharedate(rs.getDate("share_date"));
                stb1.setBooked_date(rs.getDate("bkdate"));
                stb1.setShared_by(rs.getString("shared_by"));
                stb1.setMobileNo(rs.getInt("mobileNo"));
                stb1.setBike_type(rs.getString("biketype"));
                //store all data into a Lists
                currentbookedlist.add(stb1);
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
        return currentbookedlist;
    }
    

    private void clear() {
        setBookingstatus(null);
        setSharedate(null);
        setNotes(null);
    }

}
