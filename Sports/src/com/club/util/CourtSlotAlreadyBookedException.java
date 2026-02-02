package com.club.util;

public class CourtSlotAlreadyBookedException extends Exception {
    public String toString() {
        return "Court slot already booked";
    }
}
