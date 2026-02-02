package com.club.util;

public class ActiveDuesException extends Exception {
    public String toString() {
        return "Member has pending dues";
    }
}
