package com.localconnct.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    private String id;

    @NonNull
    private String name;

     @NonNull
     private String email;

    @NonNull
    private String password;

    private LocalDateTime createdAt = LocalDateTime.now();
}
