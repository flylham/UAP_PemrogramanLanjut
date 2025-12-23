package com.hotel.model;

import java.time.LocalDate;

public class Reservation {
    private int id;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private int roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status; // PENDING, CONFIRMED, CANCELLED

    // KONSTRUKTOR
    public Reservation(int id, String guestName, String guestEmail, String guestPhone,
                       int roomId, LocalDate checkInDate, LocalDate checkOutDate, String status) {
        this.id = id;
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.guestPhone = guestPhone;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    // GETTER dan SETTER lengkap
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}