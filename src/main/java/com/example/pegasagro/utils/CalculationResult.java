package com.example.pegasagro.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class CalculationResult {
    private String[] data;
    private Map<Integer, String> invalidDataFromCalculation;

    public CalculationResult(String[] data) {
        this.data = data;
    }
}
