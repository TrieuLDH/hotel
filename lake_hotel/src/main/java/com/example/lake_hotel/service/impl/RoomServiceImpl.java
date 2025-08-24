package com.example.lake_hotel.service.impl;

import com.example.lake_hotel.exception.InternalServerException;
import com.example.lake_hotel.exception.ResourceNotFoundException;
import com.example.lake_hotel.model.Room;
import com.example.lake_hotel.repository.RoomRepo;
import com.example.lake_hotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {

    private final RoomRepo roomRepo;

    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if (!file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }
        return roomRepo.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepo.findDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Optional<Room> room = roomRepo.findById(roomId);
        if (room.isEmpty()) {
            throw new ResourceNotFoundException("Xin Lỗi, không tìm thấy phòng !");
        }
        Blob photoBlob = room.get().getPhoto();
        if (photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomRepo.findById(roomId);
        if (theRoom.isPresent()) {
            roomRepo.deleteById(roomId);
        }
    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        Room room = roomRepo.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Khong tim thay id room Trieu oi"));
        if (roomType != null) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (photoBytes != null && photoBytes.length > 0) {
            try {
                room.setPhoto(new SerialBlob(photoBytes));
            } catch (Exception e) {
                throw new InternalServerException("Loi update Internal Trieu oi !");
            }
        }
        return roomRepo.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return Optional.of(roomRepo.findById(roomId).get());
    }
}
