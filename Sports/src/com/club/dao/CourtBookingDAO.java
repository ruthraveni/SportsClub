package com.club.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.club.bean.CourtBooking;
import com.club.util.DBUtil;

public class CourtBookingDAO {
	public int generateBookingID() throws Exception {

	    int nextId = 520001;

	    Connection con = DBUtil.getDBConnection();
	    Statement st = con.createStatement();

	    ResultSet rs = st.executeQuery(
	            "SELECT MAX(BOOKING_ID) FROM COURT_BOOKING_TBL");

	    if (rs.next()) {
	        int maxId = rs.getInt(1);

	        if (maxId != 0) {
	            nextId = maxId + 1;
	        }
	    }

	    return nextId;
	}

    public boolean recordBooking(CourtBooking booking) throws Exception {

        Connection con = DBUtil.getDBConnection();
        PreparedStatement ps =
                con.prepareStatement(
                        "INSERT INTO COURT_BOOKING_TBL VALUES(?,?,?,?,?,?,?,?)");

        ps.setInt(1, booking.getBookingID());
        ps.setString(2, booking.getMemberID());
        ps.setString(3, booking.getCourtCode());
        ps.setString(4, booking.getSport());
        ps.setDate(5, new java.sql.Date(booking.getBookingDate().getTime()));
        ps.setString(6, booking.getStartTime());
        ps.setString(7, booking.getEndTime());
        ps.setString(8, booking.getStatus());

        int rows = ps.executeUpdate();
        con.commit();

        return rows > 0;
    }

    public boolean updateBookingStatus(int bookingID, String status)
            throws Exception {

        Connection con = DBUtil.getDBConnection();
        PreparedStatement ps =
                con.prepareStatement(
                        "UPDATE COURT_BOOKING_TBL SET STATUS=? WHERE BOOKING_ID=?");

        ps.setString(1, status);
        ps.setInt(2, bookingID);

        int rows = ps.executeUpdate();
        con.commit();

        return rows > 0;
    }

    public List<CourtBooking> findBookingsByMember(String memberID)
            throws Exception {

        List<CourtBooking> list = new ArrayList<>();
        Connection con = DBUtil.getDBConnection();

        PreparedStatement ps =
                con.prepareStatement(
                        "SELECT * FROM COURT_BOOKING_TBL WHERE MEMBER_ID=?");

        ps.setString(1, memberID);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            CourtBooking cb = new CourtBooking();
            cb.setBookingID(rs.getInt("BOOKING_ID"));
            cb.setMemberID(rs.getString("MEMBER_ID"));
            cb.setCourtCode(rs.getString("COURT_CODE"));
            cb.setSport(rs.getString("SPORT"));
            cb.setBookingDate(rs.getDate("BOOKING_DATE"));
            cb.setStartTime(rs.getString("START_TIME"));
            cb.setEndTime(rs.getString("END_TIME"));
            cb.setStatus(rs.getString("STATUS"));
            list.add(cb);
        }
        return list;
    }

    public List<CourtBooking> findBookingsForCourtAndDate(
            String courtCode, Date bookingDate) throws Exception {

        List<CourtBooking> list = new ArrayList<>();
        Connection con = DBUtil.getDBConnection();

        PreparedStatement ps =
                con.prepareStatement(
                        "SELECT * FROM COURT_BOOKING_TBL WHERE COURT_CODE=? AND BOOKING_DATE=?");

        ps.setString(1, courtCode);
        ps.setDate(2, new java.sql.Date(bookingDate.getTime()));

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            CourtBooking cb = new CourtBooking();
            cb.setBookingID(rs.getInt("BOOKING_ID"));
            cb.setMemberID(rs.getString("MEMBER_ID"));
            cb.setCourtCode(rs.getString("COURT_CODE"));
            cb.setSport(rs.getString("SPORT"));
            cb.setBookingDate(rs.getDate("BOOKING_DATE"));
            cb.setStartTime(rs.getString("START_TIME"));
            cb.setEndTime(rs.getString("END_TIME"));
            cb.setStatus(rs.getString("STATUS"));
            list.add(cb);
        }
        return list;
    }
}
