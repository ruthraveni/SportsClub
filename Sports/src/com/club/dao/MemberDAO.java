package com.club.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.club.bean.Member;
import com.club.util.DBUtil;

public class MemberDAO {

    public Member findMember(String memberID) {

        Member member = null;
        String sql = "SELECT * FROM MEMBER_TBL WHERE MEMBER_ID = ?";

        try {
            Connection con = DBUtil.getDBConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, memberID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                member = new Member();
                member.setMemberID(rs.getString("MEMBER_ID"));
                member.setFullName(rs.getString("FULL_NAME"));
                member.setMobile(rs.getString("MOBILE"));
                member.setPrimarySport(rs.getString("PRIMARY_SPORT"));
                member.setMembershipType(rs.getString("MEMBERSHIP_TYPE"));
                member.setOutstandingBalance(rs.getDouble("OUTSTANDING_BALANCE"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return member;
    }

    public List<Member> viewAllMembers() {

        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM MEMBER_TBL";

        try {
            Connection con = DBUtil.getDBConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Member member = new Member();
                member.setMemberID(rs.getString("MEMBER_ID"));
                member.setFullName(rs.getString("FULL_NAME"));
                member.setMobile(rs.getString("MOBILE"));
                member.setPrimarySport(rs.getString("PRIMARY_SPORT"));
                member.setMembershipType(rs.getString("MEMBERSHIP_TYPE"));
                member.setOutstandingBalance(rs.getDouble("OUTSTANDING_BALANCE"));

                list.add(member);
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insertMember(Member member) {

        String sql = "INSERT INTO MEMBER_TBL VALUES (?,?,?,?,?,?)";

        try {
            Connection con = DBUtil.getDBConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, member.getMemberID());
            ps.setString(2, member.getFullName());
            ps.setString(3, member.getMobile());
            ps.setString(4, member.getPrimarySport());
            ps.setString(5, member.getMembershipType());
            ps.setDouble(6, member.getOutstandingBalance());

            int rows = ps.executeUpdate();
            con.close();

            if (rows > 0)
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteMember(String memberID) {

        String sql = "DELETE FROM MEMBER_TBL WHERE MEMBER_ID = ?";

        try {
            Connection con = DBUtil.getDBConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, memberID);

            int rows = ps.executeUpdate();
            con.close();

            if (rows > 0)
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateOutstandingBalance(String memberID, double balance) {

        String sql = "UPDATE MEMBER_TBL SET OUTSTANDING_BALANCE = ? WHERE MEMBER_ID = ?";

        try {
            Connection con = DBUtil.getDBConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDouble(1, balance);
            ps.setString(2, memberID);

            int rows = ps.executeUpdate();
            con.close();

            if (rows > 0)
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
