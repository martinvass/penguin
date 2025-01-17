package com.github.martinyes.penguinapp.devtools;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestDataController {

    private final TestDataService testDataService;

    @PostMapping("/admin/testdata/generate")
    @ResponseBody
    private ResponseEntity<String> generateTestData(
            @RequestParam int userCount,
            @RequestParam int serverPerUser,
            Authentication authentication) {
        try {
            testDataService.generateTestData(userCount, serverPerUser);
            return ResponseEntity.ok("Test data generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating test data");
        }
    }

}
