package com.localconnct.api.repository;

import com.localconnct.api.model.ServiceModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ServiceRepository extends MongoRepository<ServiceModel, String> {

    // Custom query methods
    List<ServiceModel> findByCategory(String category);

    List<ServiceModel> findByLocationPincode(String pincode);

    List<ServiceModel> findByProviderId(String providerId);

}
