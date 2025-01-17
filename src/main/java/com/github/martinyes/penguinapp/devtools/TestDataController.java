package com.github.martinyes.penguinapp.devtools;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class TestDataController {

    private final TestDataService testDataService;

    @PostMapping("/admin/testdata/generate")
    private String generateTestData(
            @RequestParam int userCount,
            @RequestParam int serverPerUser) {
        try {
            testDataService.generateTestData(userCount, serverPerUser);
            return "redirect:/admin/configuration?created=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/configuration?created=false";
        }
    }

    @PostMapping("/admin/testdata/delete")
    private String deleteTestData() {
        try {
            testDataService.deleteTestData();
            return "redirect:/admin/configuration?deleted=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/configuration?deleted=false";
        }
    }
}