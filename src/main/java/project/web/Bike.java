/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import project.database.DBconnection;

import java.io.Serializable;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mohanbabu
 */
@ManagedBean(name = "bike")
@SessionScoped
public class Bike implements Serializable {

    private int id;
    private String modelname; // year or brand name fine
    private String type; // for adult or childrens or teens
    private String comments; // for additional comments
    private String conditionstatus; // for current status ( good/average/bad )
    private List<Bike> bikes;
    private String sharestatus;
    private String sharebutton;
    private String stopsharebutton;
    public int bike_id;
    private int counterbb;
    private String bikeowner;

    Connection con = null;
    ResultSet rs = null;
    PreparedStatement ps = null;

    public String getBikeowner() {
        return bikeowner;
    }

    public void setBikeowner(String bikeowner) {
        this.bikeowner = bikeowner;
    }

    public int getCounterbb() {
        return counterbb;
    }

    public void setCounterbb(int counterbb) {
        this.counterbb = counterbb;
    }

    public int getBike_id() {
        return bike_id;
    }

    public void setBike_id(int bike_id) {
        this.bike_id = bike_id;
    }

    public String getSharebutton() {
        return sharebutton;
    }

    public void setSharebutton(String sharebutton) {
        this.sharebutton = sharebutton;
    }

    public String getStopsharebutton() {
        return stopsharebutton;
    }

    public void setStopsharebutton(String stopsharebutton) {
        this.stopsharebutton = stopsharebutton;
    }

    public String getSharestatus() {
        return sharestatus;
    }

    public void setSharestatus(String sharestatus) {
        this.sharestatus = sharestatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getConditionstatus() {
        return conditionstatus;
    }

    public void setConditionstatus(String conditionstatus) {
        this.conditionstatus = conditionstatus;
    }

    public String addBikes() {
//	if(file != null) {
//		
//		 SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//         String fileName = fmt.format(new Date())
//                + file.getFileName();
//		 upload(fileName);
//	 //    System.out.println("pravin");
        //int i = 0;

        int rowadded = 0;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();

        con = DBconnection.getConnection();
        String sql = "INSERT INTO bikes(modelname,type,comments,conditionstatus,image_id,user_id) VALUES(?,?,?,?,?,(select id from users where user=?))";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, modelname);
            ps.setString(2, type);
            ps.setString(3, comments);
            ps.setString(4, conditionstatus);
            ps.setInt(5, 999);
            ps.setString(6, string_val); // Get the user ID here.!!
            rowadded = ps.executeUpdate();
            ps.close(); // All open connection to be closed.
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clear();
        }

        //} // if else        
        if (rowadded > 0) {
            System.out.println("A new bike was added successfully!");
            return "share";
        } else {
            return "share_modals/addbike-failure";
        }

    }

    public List<Bike> getBikes() {
        List<Bike> bikes = new ArrayList<Bike>();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();

        con = DBconnection.getConnection();
        String sql = "SELECT id,modelname,type,comments,conditionstatus,sharestatus FROM bikes where user_id=(select id from users where user='" + string_val + "') and deleted='no' ORDER By id asc";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Bike bk = new Bike();
                bk.setId(rs.getInt("id"));
                bk.setModelname(rs.getString("modelname"));
                bk.setType(rs.getString("type"));
                bk.setComments(rs.getString("comments"));
                bk.setConditionstatus(rs.getString("conditionstatus"));
                bk.setSharestatus(rs.getString("sharestatus"));
                if (rs.getString("sharestatus").contains("off")) {
                    bk.setSharebutton("false"); // false means button disabled status set as false, so the button would not be disabled.
                    bk.setStopsharebutton("true");
                } else if (rs.getString("sharestatus").contains("on")) {
                    bk.setSharebutton("true");
                    bk.setStopsharebutton("false");
                }
                //store all data into a List
                bikes.add(bk);
                // All open connection to be closed.
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        clear();
        }
        ////        
        return bikes;

    }

//    public String updateBikes(int bike_id, int user_id) {
//        int rowdeleted = 0;
//
//        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//        String string_val = session.getAttribute("username").toString();
//
//        con = DBconnection.getConnection();
//        String sql = "DELETE FROM bikes where user_id=? and id=?";
//        try {
//            ps = con.prepareStatement(sql);
//            ps.setInt(1, bike_id);
//            ps.setString(2, user_id); // Decide how to get the user id from the session or from some where to redirect here.
//            rowdeleted = ps.executeUpdate();
//            ps.close(); // All open connection to be closed.
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (rowdeleted > 0) {
//            System.out.println("A new bike was added successfully!");
//            return "Success";
//        } else {
//            return "Failure";
//        }
//    }
    public void deleteBikes(int bike_id) {
        int rowdeleted = 0;

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();

        con = DBconnection.getConnection();
        String sql = "Update bikes set deleted='yes' where id=? and user_id=(select id from users where user=?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, bike_id);
            ps.setString(2, string_val);
            rowdeleted = ps.executeUpdate();
            ps.close(); // All open connection to be closed.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void offShareStatus(int bikeinput2) {
        int rowupdated = 0;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        //System.out.println(bike_id);
        con = DBconnection.getConnection();
        String sql = "UPDATE bikes set sharestatus='off' where user_id=(select id from users where user=?) and id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, session.getAttribute("username").toString());
            ps.setInt(2, bikeinput2);
            rowupdated = ps.executeUpdate();
            //rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //clear();
    }

    public String checkAnyBookings(int bikeinput, String execmethod) throws SQLException {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();
        con = DBconnection.getConnection();
        this.counterbb = 0;
        String sql = "SELECT count(DISTINCT sab1.id) countvalue FROM shares_and_bookings sab1, bikes bk where bk.id=sab1.bike_id and sab1.bookingstatus='Booked' and sab1.share_date >= curdate() and bk.id='" + bikeinput + "' and bk.user_id=(select id from users where user='" + string_val + "') and bk.deleted='no' group by sab1.id";
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
            if ("offshare".equals(execmethod)){
            System.out.println("Fail - to call unshare");
            return "unshare-failure";
            } else if ("deletebike".equals(execmethod)){
            return "delete-bike-failure";
            }
        } else {
            if ("offshare".equals(execmethod)) {
                offShareStatus(bikeinput);
                System.out.println("Called the unshare update");
            } else if ("deletebike".equals(execmethod)){
                 deleteBikes(bikeinput);
                 System.out.println("Called the deletebike");
            }
        }
            System.out.println("Bike No:" + bikeinput);
            return "share";
    }

    public String onShareStatus(int bike_id) {
        int rowupdated = 0;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();
        con = DBconnection.getConnection();
        String sql = "UPDATE bikes set sharestatus='on' where user_id=(select id from users where user=?) and id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, string_val);
            ps.setInt(2, bike_id);
            rowupdated = ps.executeUpdate();
            ps.close(); // All open connection to be closed.
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rowupdated > 0) {
            System.out.println("Share status updated successfully! to OFF---");
            return "share";
        } else {
            return "share-failure";
        }
    }

    public String getBikeDetail(int bikeid) {
        //int rowfetched = 0;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();
        System.out.println(bikeid);
        con = DBconnection.getConnection();
        String sql = "SELECT modelname,type,comments,conditionstatus from bikes where user_id=(select id from users where user='" + string_val + "') and id='" + bikeid + "'";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                this.setModelname(rs.getString("modelname"));
                this.setType(rs.getString("type"));
                this.setComments(rs.getString("comments"));
                this.setConditionstatus(rs.getString("conditionstatus"));
            }
            rs.close();
            ps.close(); // All open connection to be closed.
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bike_id = bikeid;
            //System.out.println("DATA AFTER setting into the bean");
            //System.out.println(bike_id);
            //clear();
        }
        return "share_bikes/viewbike";
    }

    public int fetchUserSharedCount(String select) {
        int rowfecthed = 0;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();
        String sql = null;
        con = DBconnection.getConnection();
        if ("a".equals(select)) {
            sql = "SELECT count(*) as justcount from bikes where user_id=(select id from users where user='" + string_val + "') and sharestatus='on' and deleted='no'";
        } else if ("b".equals(select)) {
            sql = "SELECT count(*) as justcount from shares_and_bookings where booked_by=(select id from users where user='" + session.getAttribute("username").toString() + "') and bookingstatus='Booked' ";
        } else if ("c".equals(select)) {
            sql = "SELECT count(*) as justcount from shares_and_bookings where booked_by=(select id from users where user='" + session.getAttribute("username").toString() + "') and bookingstatus='RideDone' and share_date <= curdate()";
        }
        try {

            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.setCounterbb(rs.getInt("justcount"));
            }
            rs.close();
            ps.close(); // All open connection to be closed.
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
           //clear();
        }

        //} // if else        
        if (rowfecthed >= 0) {
            System.out.println("Fetched the counterbb value.");
            System.out.println(counterbb);
        }
        return counterbb;

    }

    public String set_bike_rend_addtime(int bike_id_local) {
        try {
            System.out.println(bike_id_local);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.bike_id = bike_id_local;
            System.out.println(bike_id);
        }
        return "addtimeslots.xhtml";
    }

    private void clear() {
        setModelname(null);
        setType(null);
        setConditionstatus(null);
        setComments(null);
        setCounterbb(0);
    }

    private void clear_bikeid() {
        setBike_id(0);
    }

}
