package com.club.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.club.bean.Bill;
import com.club.util.DBUtil;

public class BillDAO {

    public int generateBillID() throws Exception {

        Connection con = DBUtil.getDBConnection();
        Statement st = con.createStatement();

        ResultSet rs =
                st.executeQuery("SELECT NVL(MAX(BILL_ID),630000)+1 FROM BILL_TBL");

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 630001;
    }

    public boolean recordBill(Bill bill) throws Exception {

        Connection con = DBUtil.getDBConnection();
        PreparedStatement ps =
                con.prepareStatement(
                        "INSERT INTO BILL_TBL VALUES(?,?,?,?,?,?,?,?)");

        ps.setInt(1, bill.getBillID());
        ps.setString(2, bill.getMemberID());
        ps.setString(3, bill.getBillingMonth());
        ps.setInt(4, bill.getTotalSessions());
        ps.setDouble(5, bill.getSubscriptionAmount());
        ps.setDouble(6, bill.getUsageCharges());
        ps.setDouble(7, bill.getTotalAmount());
        ps.setString(8, bill.getStatus());

        int rows = ps.executeUpdate();
        con.commit();

        return rows > 0;
    }

    public boolean updateBillStatus(int billID, String status)
            throws Exception {

        Connection con = DBUtil.getDBConnection();
        PreparedStatement ps =
                con.prepareStatement(
                        "UPDATE BILL_TBL SET STATUS=? WHERE BILL_ID=?");

        ps.setString(1, status);
        ps.setInt(2, billID);

        int rows = ps.executeUpdate();
        con.commit();

        return rows > 0;
    }
    public List<Bill> findPendingBillsByMember(String memberID)
            throws Exception {

        List<Bill> list = new ArrayList<>();
        Connection con = DBUtil.getDBConnection();

        PreparedStatement ps =
                con.prepareStatement(
                        "SELECT * FROM BILL_TBL WHERE MEMBER_ID=? AND STATUS='PENDING'");

        ps.setString(1, memberID);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Bill b = new Bill();
            b.setBillID(rs.getInt("BILL_ID"));
            b.setMemberID(rs.getString("MEMBER_ID"));
            b.setBillingMonth(rs.getString("BILLING_MONTH"));
            b.setTotalSessions(rs.getInt("TOTAL_SESSIONS"));
            b.setSubscriptionAmount(rs.getDouble("SUBSCRIPTION_AMOUNT"));
            b.setUsageCharges(rs.getDouble("USAGE_CHARGES"));
            b.setTotalAmount(rs.getDouble("TOTAL_AMOUNT"));
            b.setStatus(rs.getString("STATUS"));
            list.add(b);
        }
        return list;
    }
 

}
