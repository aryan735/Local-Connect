package com.localconnct.api.controller;


import com.localconnct.api.dto.RatingRequestDto;
import com.localconnct.api.dto.RatingResponseDto;
import com.localconnct.api.exception.RatingNotFoundException;
import com.localconnct.api.exception.UserNotFoundException;
import com.localconnct.api.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

}
