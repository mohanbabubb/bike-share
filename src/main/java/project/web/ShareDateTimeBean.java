/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
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
@ManagedBean(name = "sharedatettime", eager = true)
@SessionScoped

public class ShareDateTimeBean {

    private int share_book_id; // Alias for the timeslot record id
    private Date sharedate;
    private String notes;
    private String bookingstatus;
    private List<ShareDateTimeBean> viewtimeslots;
    public int bike_id;
    private int counterbb;

    public int getCounterbb() {
        return counterbb;
    }

    public void setCounterbb(int counterbb) {
        this.counterbb = counterbb;
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

    public String addTimeSlot(int bike_id) throws ParseException {
//	if(file != null) {
//		
//		 SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//         String fileName = fmt.format(new Date())
//                + file.getFileName();
//		 upload(fileName);
//	 //    System.out.println("pravin");
        //int i = 0;

        SimpleDateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd");
        String dateformatted = dfmt.format(sharedate);

        System.out.println(sharedate);
        System.out.println(dateformatted);

        Date todayDate = new Date();

        if (sharedate.after(todayDate)) {
            System.out.println("Date1 is after Date2");

            int rowadded = 0;
            con = DBconnection.getConnection();
            String sql = "INSERT INTO shares_and_bookings (bike_id,share_date,notes) VALUES(?,?,?)";
            try {
                ps = con.prepareStatement(sql);
                ps.setInt(1, bike_id);
                Object obj = dateformatted;
                if (obj == null) {
                    ps.setDate(2, null);
                } else {
                    java.sql.Date dt = java.sql.Date.valueOf(new String(dateformatted));
                    ps.setDate(2, dt);
                }

                ps.setString(3, notes);
                rowadded = ps.executeUpdate();
                ps.close(); // All open connection to be closed.
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                clear();
                //System.out.println(sharedate);
            }

        } else if (sharedate.before(todayDate)) {
            System.out.println("Date1 is before Date2");
            return "addtimeslots-fail";
        }

        return "viewbike";
    }

    public void deleteTimeSlot(int bike_id, int share_book_id) {

        //HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        //String string_val = session.getAttribute("username").toString();
        int rowdeleted = 0;
        con = DBconnection.getConnection();
        String sql = "DELETE FROM shares_and_bookings where id=? and bike_id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, share_book_id); // refers the attribute name as id in the table.
            ps.setInt(2, bike_id); // refers same attribute name in the table.
            if (bookingstatus == "Booked") {
                System.out.println("Cannot delete Booked timeslot.");
            } else {
                rowdeleted = ps.executeUpdate();
            }

            ps.close(); // All open connection to be closed.
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rowdeleted > 0) {
            System.out.println("Successfully deleted the timeslot");
            //return "failed";
        } else {
            System.out.println("FAiled to delete the timeslot");
            //return "";
        }
    }

    public String checkTimeSlotBookings(int bike_id, int share_book_id) throws SQLException {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();
        con = DBconnection.getConnection();
        this.counterbb = 0;
        String sql = "SELECT count(DISTINCT sab1.id) countvalue FROM shares_and_bookings sab1, bikes bk where bk.id=sab1.bike_id and sab1.bookingstatus='Booked' and sab1.share_date >= curdate() and bk.id='" + bike_id + "' and bk.user_id=(select id from users where user='" + string_val + "') and sab1.id='"+share_book_id+"' group by sab1.id";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                this.setCounterbb(rs.getInt("countvalue"));
                //this.setBike_id(bikeinput);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close();
            ps.close();
            con.close();
        }

        if (counterbb > 0) {
            return "delete-timeslot-failure";
        } else {
            deleteTimeSlot(bike_id, share_book_id);
        }
        return "viewbike";
    }

    public String set_bike_rend_addtime(int bike_id_local) {
        try {
            System.out.println(bike_id_local);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.bike_id = bike_id_local;

        }
        return "addtimeslots.xhtml";
    }

    public List<ShareDateTimeBean> viewTimeSlots(int bike_id_local) {

        List<ShareDateTimeBean> viewtimeslots = new ArrayList<ShareDateTimeBean>();
        con = DBconnection.getConnection();
        String sql = "SELECT id,share_date,bookingstatus,notes from shares_and_bookings where bike_id='" + bike_id_local + "' and share_date >= curdate() and bookingstatus!='RideDone'";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                ShareDateTimeBean stb = new ShareDateTimeBean();
                stb.setShare_book_id(rs.getInt("id"));
                stb.setSharedate(rs.getDate("share_date"));
                stb.setNotes(rs.getString("notes"));
                stb.setBookingstatus(rs.getString("bookingstatus"));
                stb.setBike_id(bike_id_local);
                //store all data into a List
                viewtimeslots.add(stb);
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

        return viewtimeslots;
    }

    private void clear() {
        setBookingstatus(null);
        setSharedate(null);
        setNotes(null);
    }
}
