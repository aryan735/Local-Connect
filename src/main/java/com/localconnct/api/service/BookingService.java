package com.localconnct.api.service;


import com.localconnct.api.dto.*;
import com.localconnct.api.enums.BookingStatus;
import com.localconnct.api.enums.PaymentStatus;
import com.localconnct.api.exception.BookingNotFoundException;
import com.localconnct.api.exception.PaymentFailedException;
import com.localconnct.api.exception.UserNotFoundException;
import com.localconnct.api.mapper.SendEmail;
import com.localconnct.api.model.BookingModel;
import com.localconnct.api.model.User;
import com.localconnct.api.repository.BookingRepository;
import com.localconnct.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SendEmail sendEmail;

    public String  bookTheService(BookingRequestDto bookingRequestDto) {
        if(bookingRequestDto==null){
            return "The body data is not found!";
        }
        User provider = userRepository.findById(bookingRequestDto.getProviderId())
                .orElseThrow(()-> new UserNotFoundException("Provider not found with ID: " + bookingRequestDto.getProviderId()));
            String providerEmail = provider.getEmail();
            BookingModel bookingModel = BookingModel.builder()
                    .userId(bookingRequestDto.getUserId())
                    .providerId(bookingRequestDto.getProviderId())
                    .serviceId(bookingRequestDto.getServiceId())
                    .bookingTime(LocalDateTime.now())
                    .status(BookingStatus.REQUESTED)
                    .build();
        bookingRepository.save(bookingModel);
              String subject =  """
            Hello %s,

            You have received a new booking request from a user for your service on LocalConnect.

            🧾 Booking Details:
            - User ID: %s
            - Service ID: %s
            -Booking ID : %s
            - Booking Time: %s
            
            Please confirm or reject this booking request by logging in or using the API.

            Thank you,
            LocalConnect Team
            """.formatted(
                       provider.getName(),
                      bookingRequestDto.getUserId(),
                      bookingRequestDto.getServiceId(),
                      bookingModel.getBookingId(),
                      LocalDateTime.now()
                      );


            sendEmail.sendMail(providerEmail,
                    "Booking Request By A User with Service Id : " + bookingRequestDto.getServiceId(),

                    subject);
            return "Booking Done";

    }

    public String respondUser(BookingResponseDto2 bookingResponseDto) {
        if(bookingResponseDto==null){
            return "The body data is not found!";
        }

       //Finding the user for his email
        User user = userRepository.findById(bookingResponseDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User Not Found With this User Id : " + bookingResponseDto.getUserId()));

        //Finding the exact booking
        BookingModel bookingModel = bookingRepository.findById(bookingResponseDto.getBookingId())
                .orElseThrow(() -> new BookingNotFoundException("Booking with this id : " + bookingResponseDto.getBookingId() + " Not Found!"));

        bookingModel.setBookingTime(LocalDateTime.now());

        bookingRepository.save(bookingModel);
        //Accepted Mail
        String acceptedSubject = """
        Hello %s,

        ✅ Your booking request has been *ACCEPTED* by the provider.

        🧾 Booking Details:
        - Booking ID: %s
        - Provider ID: %s
        - Service ID: %s
        - Booking Time: %s

        💳 **Please pay first to proceed with the service.** Your provider will only be able to deliver the service after receiving the payment.

        Please proceed with the next steps if applicable (e.g. payment, contact the provider).

        Thank you for using LocalConnect!
        """.formatted(
                user.getName(),
                bookingResponseDto.getBookingId(),
                bookingResponseDto.getProviderId(),
                bookingResponseDto.getServiceId(),
                bookingModel.getBookingTime()
        );

        //Rejected Mail
        String rejectedSubject = """
        Hello %s,

        ❌ Your booking request has been *REJECTED* by the provider.

        🧾 Booking Details:
        - Booking ID: %s
        - Provider ID: %s
        - Service ID: %s
        - Booking Time: %s

        You can try booking a different service provider or resubmit a request later.

        Thank you for using LocalConnect!
        """.formatted(
                user.getName(),
                bookingResponseDto.getBookingId(),
                bookingResponseDto.getProviderId(),
                bookingResponseDto.getServiceId(),
                bookingModel.getBookingTime()
        );


        if (BookingStatus.REJECTED.name().equalsIgnoreCase(bookingResponseDto.getStatus().name())){
            bookingModel.setStatus(BookingStatus.REJECTED);
            sendEmail.sendMail(user.getEmail(),
                    "Request Rejected By The Booing ID: " + bookingResponseDto.getBookingId(),
                    rejectedSubject);
        }
        if (BookingStatus.ACCEPTED.name().equalsIgnoreCase(bookingResponseDto.getStatus().name())) {
            bookingModel.setStatus(BookingStatus.ACCEPTED);
            sendEmail.sendMail(user.getEmail(),
                    "Request Accepted By The Booing ID: " + bookingResponseDto.getBookingId(),
                    acceptedSubject);
        }
        bookingRepository.save(bookingModel);

        return "Done";
    }

    // PAYMENT PROCESS
    public PaymentResponseDto payment(String userId, PaymentRequestDto paymentRequestDto){
        if (Objects.isNull(userId) || userId.isEmpty()) {
            throw new UserNotFoundException("UserId is not found!");
        }


        BookingModel bookingModel = bookingRepository.findById(paymentRequestDto.getBookingId())
                .orElseThrow(() -> new BookingNotFoundException("Booking Not Found!"));

        if (!userId.equals(bookingModel.getUserId())) {
            throw new BookingNotFoundException("Booking Not Found Of The current logged in User!");

        }
        if (paymentRequestDto.getBookingId() == null) {
            throw new BookingNotFoundException("Booking ID is missing from the request.");
        }
        if (bookingModel.getPaymentStatus().equals(PaymentStatus.PAID)){
            throw new PaymentFailedException("This service is already paid!");
        }


        bookingModel.setPaymentStatus(PaymentStatus.PAID);
        bookingModel.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(bookingModel);

        PaymentResponseDto pay = PaymentResponseDto.builder()
                .bookingId(bookingModel.getBookingId())
                .providerId(bookingModel.getProviderId())
                .userId(bookingModel.getUserId())
                .serviceId(bookingModel.getServiceId())
                .paymentStatus(PaymentStatus.PAID)
                .paymentTime(LocalDateTime.now())
                .status(BookingStatus.CONFIRMED)
                .build();

        User provider = userRepository.findById(bookingModel.getProviderId())
                .orElseThrow(() -> new UserNotFoundException("Provider Not Found!"));
        String providerEmail = provider.getEmail();

        String paymentConfirmationMail = """
        Hello,

        💸 A user has successfully completed the payment for their booking.

        🧾 Payment Details:
        - Booking ID: %s
        - User ID: %s
        - Provider ID: %s
        - Service ID: %s
        - Payment Status: %s
        - Payment Time: %s

        You may now proceed with providing the service.

        Thank you for using LocalConnect!
        """.formatted(
                pay.getBookingId(),
                pay.getUserId(),
                pay.getProviderId(),
                pay.getServiceId(),
                pay.getPaymentStatus(),
                pay.getPaymentTime()
        );
        sendEmail.sendMail(providerEmail,
                "Payment Received for Booking ID: " + pay.getBookingId(),
                paymentConfirmationMail);
        log.info("Payment successful for Booking ID: {}", bookingModel.getBookingId());

        return pay;

    }



    public BookingResponseDto getBookingsStatus(String bookingId) {
        if (bookingId==null || bookingId.isEmpty()){
            throw new BookingNotFoundException("BookingId is not found!");
        }
        BookingModel bookingModel = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking With this bookingId not Found!"));
        return BookingResponseDto.builder()
                .bookingId(bookingModel.getBookingId())
                .serviceId(bookingModel.getServiceId())
                .providerId(bookingModel.getProviderId())
                .userId(bookingModel.getUserId())
                .status(bookingModel.getStatus())
                .bookingTime(bookingModel.getBookingTime())
                .build();
    }

    public List<BookingResponseDto> getAllBookings(String userId) {
        if (userId == null || userId.isEmpty()){
            throw new UserNotFoundException("UserId is not Found!");
        }
        List<BookingModel> bookings = bookingRepository.findByUserId(userId);
        if (bookings.isEmpty()){
            throw new BookingNotFoundException("Bookings Not Found!");
        }

        return bookings.stream()
                .map(bookingModel -> BookingResponseDto.builder()
                        .bookingId(bookingModel.getBookingId())
                        .serviceId(bookingModel.getServiceId())
                        .providerId(bookingModel.getProviderId())
                        .userId(bookingModel.getUserId())
                        .status(bookingModel.getStatus())
                        .bookingTime(bookingModel.getBookingTime())
                        .paymentStatus(bookingModel.getPaymentStatus())
                        .build())
                        .toList();
    }


}
