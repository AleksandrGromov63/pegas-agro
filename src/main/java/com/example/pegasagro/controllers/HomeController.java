package com.example.pegasagro.controllers;

import com.example.pegasagro.utils.CalculationResult;
import com.example.pegasagro.utils.CustomFileReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {

        CalculationResult calculationResult = CustomFileReader.readFileAndCalculateDistance();

        model.addAttribute("distanceKM", calculationResult.getData()[0]);
        model.addAttribute("rowCounterInFile", calculationResult.getData()[1]);
        model.addAttribute("countInvalidStrings", calculationResult.getData()[2]);

        if (Integer.parseInt(calculationResult.getData()[2]) > 0)
            model.addAttribute("map", calculationResult.getInvalidDataFromCalculation());

        return "index";
    }

    @PostMapping("/loadAndReadFile")
    @ResponseBody
    public String uploadFile(@RequestBody MultipartFile file, Model model) {

        CalculationResult calculationResult = CustomFileReader.readFileAndCalculateDistance(file);

        return calculationResult.getData()[0];
    }
}

