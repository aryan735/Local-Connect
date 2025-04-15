package com.localconnct.api.repository;

import com.localconnct.api.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends MongoRepository<Rating,String> {
    List<Rating> findByServiceId(String serviceId);

    List<Rating> findByServiceIdIn(List<String> serviceIds);

}
