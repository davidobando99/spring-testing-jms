package com.epam.javaprogram.springtesting.controller;

import com.epam.javaprogram.springtesting.domain.Booking;
import com.epam.javaprogram.springtesting.service.BookingConsumer;
import com.epam.javaprogram.springtesting.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking newBooking){
        Booking booking = bookingService.sendBookingMessage(newBooking,"CREATED");
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @PostMapping("/test")
    public ResponseEntity<Booking> testBooking(@RequestBody Booking newBooking){
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }

}
