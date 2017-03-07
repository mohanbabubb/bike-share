/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import project.database.DBconnection;

import java.io.Serializable;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
/**
 *
 * @author mohanbabu
 */
@ManagedBean(name = "BookBike", eager = true)
@SessionScoped
public class BookBike implements Serializable{
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

    public int getBike_id() {
        return bike_id;
    }

    public void setBike_id(int bike_id) {
        this.bike_id = bike_id;
    }

    public String getBikeowner() {
        return bikeowner;
    }

    public void setBikeowner(String bikeowner) {
        this.bikeowner = bikeowner;
    }

     public List<BookBike> getBookBikeList() {
        List<BookBike> bookbikelist = new ArrayList<BookBike>();
        
          HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String string_val = session.getAttribute("username").toString();

        con = DBconnection.getConnection();
        //String sql = "SELECT bk.id,bk.modelname,bk.type,bk.comments,bk.conditionstatus,bk.sharestatus,us.user bikeowner FROM bikes bk,users us where bk.user_id=us.id and bk.sharestatus='on' and bk.deleted='no'";
        String sql = "SELECT bk.id,bk.modelname,bk.type,bk.comments,bk.conditionstatus,bk.sharestatus,us.firstname bikeowner FROM bikes bk,users us where bk.user_id=us.id and bk.sharestatus='on' and bk.deleted='no' and us.user!='"+string_val+"'";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                BookBike bk = new BookBike();
                bk.setId(rs.getInt("id"));
                bk.setModelname(rs.getString("modelname"));
                bk.setType(rs.getString("type"));
                bk.setComments(rs.getString("comments"));
                bk.setConditionstatus(rs.getString("conditionstatus"));
                bk.setBikeowner(rs.getString("bikeowner"));
                //store all data into a List
                bookbikelist.add(bk);
                // All open connection to be closed.
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ////        
        return bookbikelist;

    }
    
    
}
