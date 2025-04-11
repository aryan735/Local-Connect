package com.localconnct.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserDetailsDto {


        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Phone is required")
        private String phone;

        @Valid
        private LocationDto location;

}
