package com.localconnct.api.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;

    @NonNull
    private String name;

    @Indexed(unique = true) //Makes sure no two users have same email
    private String email;
    @NonNull
    private String password; //will be stored as encrypted(BCrypt)

    @NonNull
    private String phone;
    @NonNull
    private Location location;

    private List<String> roles; //To manage "USER" , "ADMIN" roles
}
