package com.club.app;

import java.text.ParseException;

import java.util.Date;
import java.util.Scanner;

import com.club.service.ClubService;
import com.club.util.CourtSlotAlreadyBookedException;
import com.club.util.ValidationException;

public class ClubMain {

    private static ClubService clubService;

    public static void main(String[] args) throws ParseException {

        clubService = new ClubService();
        Scanner sc = new Scanner(System.in);

        System.out.println("--- Sports Club Court Booking Console ---");

        try {
            boolean r = clubService.bookCourtSlot(
                    "MEMC1003",
                    "B-2",
                    "BADMINTON",
                     new Date(),
                    "19:00",
                    "20:00");

            System.out.println(r ? "BOOKING SUCCESS" : "BOOKING FAILED");

        } catch (CourtSlotAlreadyBookedException e) {
            System.out.println("Court Slot Error: " + e.toString());
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.toString());
        } catch (Exception e) {
            System.out.println("System Error: " + e.getMessage());
        }

        try {
            boolean r = clubService.cancelCourtBooking(520003);
            System.out.println(r ? "CANCEL SUCCESS" : "CANCEL FAILED");

        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.toString());
        } catch (Exception e) {
            System.out.println("System Error: " + e.getMessage());
        }

        try {
            boolean r = clubService.generateMonthlyBill(
                    "MEMC1003",
                    "MAR-2025");

            System.out.println(r ? "BILL GENERATED" : "BILL GENERATION FAILED");

        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.toString());
        } catch (Exception e) {
            System.out.println("System Error: " + e.getMessage());
        }

        sc.close();
    }
}
