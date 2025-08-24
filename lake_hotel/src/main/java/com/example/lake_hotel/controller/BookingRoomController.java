package com.example.lake_hotel.controller;


import com.example.lake_hotel.exception.InvalidBookingRequestException;
import com.example.lake_hotel.exception.ResourceNotFoundException;
import com.example.lake_hotel.model.BookedRoom;
import com.example.lake_hotel.model.Room;
import com.example.lake_hotel.response.BookingResponse;
import com.example.lake_hotel.response.RoomResponse;
import com.example.lake_hotel.service.IBookingRoomService;
import com.example.lake_hotel.service.IRoomService;
//import com.example.lake_hotel.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingRoomController {

    private final IBookingRoomService bookingRoomService;

    private final IRoomService  roomService;

//    private final EmailService emailService;


    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookedRoom> bookings = bookingRoomService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedRoom booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try {
            BookedRoom booking = bookingRoomService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
                                         @RequestBody BookedRoom bookingRequest) {
        try {
            String confirmationCode = bookingRoomService.saveBooking(roomId, bookingRequest);

            // Gửi email xác nhận
//            emailService.sendBookingConfirmation(bookingRequest.getGuestEmail(), confirmationCode);
            return ResponseEntity.ok("Booking saved successfully, Your booking confirmation code is: " + confirmationCode);

        } catch (InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId) {
        bookingRoomService.cancelBooking(bookingId);
    }


    private BookingResponse getBookingResponse(BookedRoom booking) {
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse room = new RoomResponse(
                theRoom.getId(),
                theRoom.getRoomType(),
                theRoom.getRoomPrice());
        return new BookingResponse(
                booking.getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getGuestFullName(),
                booking.getGuestEmail(), booking.getNumOfAdults(),
                booking.getNumOfChildren(), booking.getTotalNumOfGuest(),
                booking.getBookingConfirmationCode(), room);

    }
}
