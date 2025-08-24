package com.example.lake_hotel.service;

import com.example.lake_hotel.model.BookedRoom;

import java.util.List;

public interface IBookingRoomService {
    void cancelBooking(Long bookingId);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBookings();
}
