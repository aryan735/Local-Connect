package com.localconnct.api.repository;

import com.localconnct.api.model.ServiceModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ServiceRepository extends MongoRepository<ServiceModel, String> {

    // Custom query methods
    List<ServiceModel> findByCategory(String category);

    List<ServiceModel> findByLocationPincode(String pincode);

    List<ServiceModel> findByProviderId(String providerId);

}
