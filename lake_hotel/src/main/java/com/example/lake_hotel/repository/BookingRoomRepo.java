package com.example.lake_hotel.repository;

import com.example.lake_hotel.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRoomRepo extends JpaRepository<BookedRoom, Long> {

    BookedRoom findByBookingConfirmationCode(String findByBookingConfirmationCode);



    List<BookedRoom> findByRoomId(Long id);
}
