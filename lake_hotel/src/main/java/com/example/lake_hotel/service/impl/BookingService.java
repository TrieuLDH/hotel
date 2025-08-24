package com.example.lake_hotel.service.impl;

import com.example.lake_hotel.exception.InvalidBookingRequestException;
import com.example.lake_hotel.model.BookedRoom;
import com.example.lake_hotel.model.Room;
import com.example.lake_hotel.repository.BookingRoomRepo;
import com.example.lake_hotel.service.IBookingRoomService;
import com.example.lake_hotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.InvalidPropertiesFormatException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingRoomService {

    private final BookingRoomRepo bookingRoomRepo;
    private final IRoomService roomService;


    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {

        return bookingRoomRepo.findByRoomId(roomId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRoomRepo.deleteById(bookingId);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingRequestException("Check-In date must come before Check-Out! date");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
        if (roomIsAvailable) {
            room.addBooking(bookingRequest);
            bookingRoomRepo.save(bookingRequest);
        } else {
            throw new InvalidBookingRequestException("Triều Said: This room is not available for selected dates: ");
        }
        return bookingRequest.getBookingConfirmationCode();
    }



    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRoomRepo.findByBookingConfirmationCode(confirmationCode);
    }

    @Override
    public List<BookedRoom> getAllBookings() {
        return List.of();
    }

//    @Override
//    public List<BookedRoom> getAllBookings() {
//        return List.of();
//    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                        || bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
                        || bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckOutDate())

                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate())
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                        ||(bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()))
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())

                        ||(bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()))
                        && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckOutDate()));

    }
}
