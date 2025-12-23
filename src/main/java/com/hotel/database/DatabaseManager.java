package com.hotel.database;

import com.hotel.model.*;
import java.time.LocalDate;
import java.util.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private List<Room> rooms;
    private List<Reservation> reservations;
    private int nextRoomId = 1;
    private int nextReservationId = 1;

    // KONSTRUKTOR private untuk Singleton
    private DatabaseManager() {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
        initializeSampleData();
    }

    // METHOD Singleton pattern
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeSampleData() {
        // Data kamar terinspirasi Baobab Safari Resort
        rooms.add(new Room(nextRoomId++, "Junior Suite - Hill View", "Suite", 2500000,
                "Junior Suite dengan pemandangan bukit, luas 56 m2", true));
        rooms.add(new Room(nextRoomId++, "Family Room - Savannah View", "Family", 1800000,
                "Kamar keluarga dengan pemandangan savana", true));
        rooms.add(new Room(nextRoomId++, "Deluxe Room - Mountain View", "Deluxe", 1500000,
                "Kamar deluxe dengan pemandangan Gunung Arjuna", true));
        rooms.add(new Room(nextRoomId++, "Standard Room", "Standard", 800000,
                "Kamar standar nyaman dengan fasilitas lengkap", true));
    }

    // ========== CRUD untuk Room ==========
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public Room getRoomById(int id) {
        return rooms.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addRoom(Room room) {
        room.setId(nextRoomId++);
        rooms.add(room);
    }

    public boolean deleteRoom(int id) {
        return rooms.removeIf(r -> r.getId() == id);
    }

    public void addReservation(Reservation reservation) {
        reservation.setId(nextReservationId++);
        reservations.add(reservation);
    }

    public boolean updateReservation(Reservation updatedReservation) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId() == updatedReservation.getId()) {
                reservations.set(i, updatedReservation);
                return true;
            }
        }
        return false;
    }

    // METHOD TAMBAHAN untuk kemudahan
    public int getTotalRooms() {
        return rooms.size();
    }

    public int getAvailableRooms() {
        return (int) rooms.stream()
                .filter(Room::isAvailable)
                .count();
    }

    public int getTotalReservations() {
        return reservations.size();
    }
}