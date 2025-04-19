package com.localconnct.api.adminService;

import com.localconnct.api.service.AdminService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class creatingAdmin {


    @Autowired
    private AdminService adminService;

    @Disabled
    @Test
    void testCreatingAdmin(){
        adminService.createAdmin("aryanraj112111@gmail.com");
    }


}
