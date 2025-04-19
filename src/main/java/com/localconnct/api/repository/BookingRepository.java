package com.localconnct.api.repository;

import com.localconnct.api.dto.BookingResponseDto;
import com.localconnct.api.model.BookingModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<BookingModel,String> {
    List<BookingModel> findByUserId(String userId);

    List<BookingResponseDto> findByProviderId(String providerId);
}
