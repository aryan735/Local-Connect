package com.localconnct.api.controller;


import com.localconnct.api.dto.BookingRequestDto;
import com.localconnct.api.dto.BookingResponseDto;
import com.localconnct.api.dto.PaymentRequestDto;
import com.localconnct.api.dto.PaymentResponseDto;
import com.localconnct.api.exception.BookingNotFoundException;
import com.localconnct.api.exception.UserNotFoundException;
import com.localconnct.api.model.User;
import com.localconnct.api.repository.UserRepository;
import com.localconnct.api.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;


    //Book a service
    @PostMapping("/book-service")
    public ResponseEntity<String> bookService(@Valid @RequestBody BookingRequestDto bookingRequestDto){
        String message = bookingService.bookTheService(bookingRequestDto);
        if(message == null || message.isEmpty()){
            return new ResponseEntity<>("Booking is failed!", HttpStatus.BAD_REQUEST);
        }
        if (!message.equals("Booking Done")){
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @PostMapping("/respond")
    @PreAuthorize("hasRole('PROVIDER')") //Only provider can access it
    public ResponseEntity<String> respondToTheUser(@Valid @RequestBody BookingResponseDto bookingResponseDto){
        String message = bookingService.respondUser(bookingResponseDto);
        if (message==null || message.isEmpty()){
            return new ResponseEntity<>("Response is failed!",HttpStatus.BAD_REQUEST);
        }
        if (message.equals("Done")){
            return new ResponseEntity<>("Response sent to the User.",HttpStatus.OK);
        }
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    //Find the booking by bookingId
    @GetMapping("/booking-status/{bookingId}")
    public ResponseEntity<BookingResponseDto> getBookingStatus(@PathVariable String bookingId){
        BookingResponseDto bookingsStatus = bookingService.getBookingsStatus(bookingId);
        if (bookingsStatus==null){
            throw new BookingNotFoundException("Bookings Not Found!");
        }
        return new ResponseEntity<>(bookingsStatus,HttpStatus.OK);

    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponseDto>> getUsersAllBookings(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user==null){
            throw new UserNotFoundException("User Not Found!");
        }
        String userId = user.getId();
        List<BookingResponseDto> bookingList = bookingService.getAllBookings(userId);
        if (bookingList==null){
            throw new BookingNotFoundException("Bookings Not Found!");
        }
        return new ResponseEntity<>(bookingList,HttpStatus.OK);
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentResponseDto> paymentToProvider(@RequestBody PaymentRequestDto paymentResponseDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        if (user==null){
            throw new UserNotFoundException("User Not Found!");
        }
        String userId = user.getId();
        PaymentResponseDto payment = bookingService.payment(userId, paymentResponseDto);
        return new ResponseEntity<>(payment,HttpStatus.OK);
    }
}
