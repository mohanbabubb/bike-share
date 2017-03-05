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
@ManagedBean(name = "bookdatettime", eager = true)
@SessionScoped

public class BookDateTimeBean implements Serializable {

    private int share_book_id; // Alias for the timeslot record id
    private Date sharedate;
    private String notes;
    private String bookingstatus;
    private List<BookDateTimeBean> viewtimeslots;
    public int bike_id;

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

    public String bookDateslot(int bike_id, int share_book_id) {

        SimpleDateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateformatted = dfmt.format(date);

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();
        con = DBconnection.getConnection();
        String sql = "Update shares_and_bookings SET booked_by=(select id from users where user=? ),bookingstatus='Booked',booked_date=? where id=? and bike_id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, string_val);
            Object obj = dateformatted;
            java.sql.Date dt = java.sql.Date.valueOf(new String(dateformatted));
            ps.setDate(2, dt);
            ps.setInt(3, share_book_id);
            ps.setInt(4, bike_id);
            ps.executeUpdate();
            ps.close(); // All open connection to be closed.
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clear();
            //System.out.println(sharedate);
        }

        return "home/book.xhtml";

    }

    public List<BookDateTimeBean> viewDateSlots(int bike_id_local) {

        List<BookDateTimeBean> viewdateslots = new ArrayList<BookDateTimeBean>();
        con = DBconnection.getConnection();
        String sql = "SELECT id,share_date,bookingstatus,notes from shares_and_bookings where bike_id='" + bike_id_local + "' and bookingstatus in ('NotBooked')";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                BookDateTimeBean stb = new BookDateTimeBean();
                stb.setShare_book_id(rs.getInt("id"));
                stb.setSharedate(rs.getDate("share_date"));
                stb.setNotes(rs.getString("notes"));
                stb.setBookingstatus(rs.getString("bookingstatus"));
                stb.setBike_id(bike_id_local);
                //store all data into a List
                viewdateslots.add(stb);
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

        return viewdateslots;
    }

    public String set_book_rend_viewbook(int bike_id_local) {
        try {
            System.out.println("From the boo date time bean");
            System.out.println(bike_id_local);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.bike_id = bike_id_local;
        }
        return "book_bikes/viewbikedetails.xhtml";
    }

//    public String show_booked_hitory() {
//
//    }
//
//    public String show_current_bookings() {
//
//    }

    private void clear() {
        setBookingstatus(null);
        setSharedate(null);
        setNotes(null);
    }
}
