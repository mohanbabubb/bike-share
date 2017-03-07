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
import java.sql.SQLException;
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
    private String firstname;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    

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
        String sql = "SELECT bk.modelname name,sb.share_date share_date,sb.booked_date bkdate,us.user shared_by,us.mobileNo mobileNo,us.firstname firstname,bk.type biketype,sb.bookingstatus bookingstatus,sb.id sbid,sb.bike_id sbbikeid "
                + "FROM shares_and_bookings sb,bikes bk,users us "
                + "WHERE bk.id=sb.bike_id and sb.bookingstatus='Booked' and bk.user_id=us.id and us.id!=(select id from users where user='"+string_val+"')";
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
                stb1.setBookingstatus(rs.getString("bookingstatus"));
                stb1.setShare_book_id(rs.getInt("sbid"));
                stb1.setBike_id(rs.getInt("sbbikeid"));
                stb1.setFirstname(rs.getString("firstname"));
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
    
        public String RideDone(int share_book_id,int bike_id) throws SQLException {
        int rowupdated = 0;

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();

        con = DBconnection.getConnection();
        String sql = "update users a "
                + "JOIN shares_and_bookings b ON a.id=b.booked_by "
                + "set a.credits=a.credits+5, b.bookingstatus='RideDone', b.creditstatus='Credited' "
                + "where b.share_date <= curdate() and b.id=? and b.bike_id=? and a.user='"+string_val+"'";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, share_book_id);
            ps.setInt(2, bike_id);
            rowupdated = ps.executeUpdate();
            System.out.println("Running from the credit 5"+sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //finally{
        //clear();
        //}
                   if (rowupdated >0)
                    {
                        String sql1 = "update users a "
						+ "JOIN bikes b ON a.id=b.user_id "
						+ "JOIN shares_and_bookings c ON b.id=c.bike_id "
						+ "set a.credits=a.credits+10 where c.bike_id="+bike_id+" and c.id="+share_book_id+"";
                                    ps = con.prepareStatement(sql1);
                                    ps.executeUpdate();      
                                    System.out.println("Running from the credit 10"+sql);
                        return "book";
                    }else
                    {

                        con = DBconnection.getConnection();
                        String sql3 = "update users a JOIN shares_and_bookings b on a.id=b.booked_by set b.bookingstatus='RideDone', b.creditstatus='Credited' where b.id=? and b.bike_id=? and a.user='"+string_val+"'";
                        try {
                            ps = con.prepareStatement(sql3);
                            ps.setInt(1, share_book_id);
                            ps.setInt(2, bike_id);
                            rowupdated = ps.executeUpdate();
                            System.out.println("Running No credit");
                            ps.close(); // All open connection to be closed.
                            con.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally{
                        clear();
                        ps.close(); // All open connection to be closed.
                        con.close();
                        }
                        return "book-ride-done-nocredit";
                    }

    }
    
    private void clear() {
        setBookingstatus(null);
        setSharedate(null);
        setNotes(null);
    }

}
