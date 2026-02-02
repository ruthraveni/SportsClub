package com.club.service;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.club.bean.Bill;
import com.club.bean.CourtBooking;
import com.club.bean.Member;
import com.club.dao.BillDAO;
import com.club.dao.CourtBookingDAO;
import com.club.dao.MemberDAO;
import com.club.util.DBUtil;

public class ClubService {

    private MemberDAO memberDAO = new MemberDAO();
    private CourtBookingDAO bookingDAO = new CourtBookingDAO();
    private BillDAO billDAO = new BillDAO();

    public Member viewMemberDetails(String memberID) throws Exception {
        if (memberID == null || memberID.trim().isEmpty())
            return null;

        return memberDAO.findMember(memberID);
    }

    public List<Member> viewAllMembers() throws Exception {
        return memberDAO.viewAllMembers();
    }

    public boolean addNewMember(Member member) throws Exception {

        if (member == null)
            return false;

        if (member.getMemberID().isEmpty()
                || member.getFullName().isEmpty()
                || member.getMobile().isEmpty())
            return false;

        String type = member.getMembershipType();
        if (!(type.equals("BASIC")
                || type.equals("STANDARD")
                || type.equals("PREMIUM")))
            return false;

        if (memberDAO.findMember(member.getMemberID()) != null)
            return false;

        member.setOutstandingBalance(0);
        return memberDAO.insertMember(member);
    }

    public boolean removeMember(String memberID) throws Exception {

        if (memberID == null || memberID.isEmpty())
            return false;

        if (!billDAO.findPendingBillsByMember(memberID).isEmpty())
            return false;

        List<CourtBooking> bookings =
                bookingDAO.findBookingsByMember(memberID);

        for (CourtBooking cb : bookings) {
            if (cb.getStatus().equals("BOOKED"))
                return false;
        }

        return memberDAO.deleteMember(memberID);
    }

    public boolean bookCourtSlot(String memberID,
                                 String courtCode,
                                 String sport,
                                 Date bookingDate,
                                 String startTime,
                                 String endTime) throws Exception {

        if (memberID.isEmpty()
                || courtCode.isEmpty()
                || sport.isEmpty()
                || bookingDate == null
                || startTime.isEmpty()
                || endTime.isEmpty())
            return false;

        if (startTime.compareTo(endTime) >= 0)
            return false;

        Member member = memberDAO.findMember(memberID);
        if (member == null)
            return false;

        List<CourtBooking> existingBookings =
                bookingDAO.findBookingsForCourtAndDate(
                        courtCode, bookingDate);

        for (CourtBooking cb : existingBookings) {
            if (cb.getStatus().equals("BOOKED")
                    || cb.getStatus().equals("COMPLETED")) {

                boolean overlap =
                        startTime.compareTo(cb.getEndTime()) < 0 &&
                        endTime.compareTo(cb.getStartTime()) > 0;

                if (overlap)
                    return false;
            }
        }

        Connection con = DBUtil.getDBConnection();
        try {
            CourtBooking booking = new CourtBooking();
            booking.setBookingID(bookingDAO.generateBookingID());
            booking.setMemberID(memberID);
            booking.setCourtCode(courtCode);
            booking.setSport(sport);
            booking.setBookingDate(bookingDate);
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            booking.setStatus("BOOKED");

            boolean saved = bookingDAO.recordBooking(booking);
            con.commit();
            return saved;
        } catch (Exception e) {
            con.rollback();
            throw e;
        }
    }

    public boolean cancelCourtBooking(int bookingID) throws Exception {

        if (bookingID <= 0)
            return false;

        Connection con = DBUtil.getDBConnection();
        try {
            boolean updated =
                    bookingDAO.updateBookingStatus(
                            bookingID, "CANCELLED");
            con.commit();
            return updated;
        } catch (Exception e) {
            con.rollback();
            throw e;
        }
    }

    public boolean generateMonthlyBill(String memberID,
                                       String billingMonth) throws Exception {

        if (memberID.isEmpty() || billingMonth.isEmpty())
            return false;

        if (!billingMonth.contains("-"))
            return false;

        String[] parts = billingMonth.split("-");
        if (parts.length != 2)
            return false;

        if (parts[0].length() != 3 || parts[1].length() != 4)
            return false;

        Member member = memberDAO.findMember(memberID);
        if (member == null)
            return false;

        int totalSessions = 0;

        for (CourtBooking cb :
                bookingDAO.findBookingsByMember(memberID)) {

            if (cb.getStatus().equals("COMPLETED")) {
                String month =
                        new java.text.SimpleDateFormat("MMM-yyyy")
                                .format(cb.getBookingDate())
                                .toUpperCase();

                if (month.equals(billingMonth))
                    totalSessions++;
            }
        }

        double subscription;
        if (member.getMembershipType().equals("BASIC"))
            subscription = 1000;
        else if (member.getMembershipType().equals("STANDARD"))
            subscription = 1500;
        else
            subscription = 2000;

        double usageCharges = totalSessions * 100;
        double totalAmount = subscription + usageCharges;

        Connection con = DBUtil.getDBConnection();
        try {
            Bill bill = new Bill();
            bill.setBillID(billDAO.generateBillID());
            bill.setMemberID(memberID);
            bill.setBillingMonth(billingMonth);
            bill.setTotalSessions(totalSessions);
            bill.setSubscriptionAmount(subscription);
            bill.setUsageCharges(usageCharges);
            bill.setTotalAmount(totalAmount);
            bill.setStatus("PENDING");

            billDAO.recordBill(bill);

            memberDAO.updateOutstandingBalance(
                    memberID,
                    member.getOutstandingBalance() + totalAmount);

            con.commit();
            return true;
        } catch (Exception e) {
            con.rollback();
            throw e;
        }
    }
}
