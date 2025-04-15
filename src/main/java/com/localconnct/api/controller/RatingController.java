package com.localconnct.api.controller;


import com.localconnct.api.dto.RatingListDto;
import com.localconnct.api.dto.RatingRequestDto;
import com.localconnct.api.dto.RatingResponseDto;
import com.localconnct.api.exception.RatingNotFoundException;
import com.localconnct.api.exception.ServiceNotFoundException;
import com.localconnct.api.exception.UserNotFoundException;
import com.localconnct.api.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/rate-provider")
    public ResponseEntity<RatingResponseDto> createRating(@Valid @RequestBody RatingRequestDto requestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null){
            throw new UserNotFoundException("User not found!");
        }
        if (requestDto==null || requestDto.toString().isEmpty()){
            throw new RatingNotFoundException("Rating not found exception");
        }
        RatingResponseDto ratingResponseDto = ratingService.postRating(authentication.getName(), requestDto);
        if (ratingResponseDto != null){
            return new ResponseEntity<>(ratingResponseDto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(ratingResponseDto,HttpStatus.NO_CONTENT);
    }

    @GetMapping("/ratings-by-serviceId/{serviceId}")
    public ResponseEntity<RatingListDto> getRatingByServiceId(@Valid @PathVariable String serviceId){
        if (serviceId == null || serviceId.isEmpty()){
            throw new ServiceNotFoundException("Please provide the Service Id!");
        }
        RatingListDto allRatingsOfAService = ratingService.getAllRatingsOfAService(serviceId);
        if (allRatingsOfAService==null){
            throw new RatingNotFoundException("Ratings not found of this service Id!");
        }
        return new ResponseEntity<>(allRatingsOfAService,HttpStatus.OK);
    }
    @GetMapping("/provider/{providerId}/average-rating")
    public ResponseEntity<String> getAvgRatingOfProvider(@PathVariable String providerId) {
        double avgRating = ratingService.getProviderAverageRating(providerId);
        return ResponseEntity.ok("His Average Rating is : "+avgRating);
    }
    @DeleteMapping("delete-rating/providerId/{providerId}/ratingId/{ratingId}")
    public ResponseEntity<String> deleteRating(
            @PathVariable String providerId,
            @PathVariable String ratingId) {

        String result = ratingService.deleteTheRating(providerId, ratingId);

        if (result.toLowerCase().contains("deleted")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

}
