package com.example.pegasagro.utils;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomFileReader {

    public static CalculationResult readFileAndCalculateDistance(@Nullable MultipartFile... file) {
        String fileName = "src/main/resources/pegasData.log";
        String[] all = new String[3];
        Map<Integer, String> invalidStrings = new HashMap<>();
        double distanceKM = 0d;
        Pattern patternForCoords = Pattern.compile("\\d*\\.\\d*,[NS],\\d*\\.\\d*,[EW]");
        Pattern patternForSpeed = Pattern.compile("\\d*\\.\\d*,K");
        int rowCounterInFile = 0;

        try (BufferedReader reader = (file.length == 0 ? new BufferedReader(new FileReader(fileName))
                : new BufferedReader(new InputStreamReader(file[0].getInputStream())))) {

            int idSourceVTG = 0;
            int idSourceZDA = 0;
            int idSourceGGA = 0;


            Map<String, Double> previousGPSData = new HashMap<>(3);
            previousGPSData.put("latitude", 0d);
            previousGPSData.put("longitude", 0d);
            previousGPSData.put("speed", 0d);

            while (reader.ready()) {

                String inputString = reader.readLine();


                if (inputString.matches("^\\$[A-Z]{2}VTG.*")) {
                    idSourceVTG++;

                    Matcher matcher = patternForSpeed.matcher(inputString);

                    if (matcher.find()) {

                        double speedInKM = Double.parseDouble(matcher.group().replaceAll(",K", ""));

                        if (speedInKM > 1) previousGPSData.replace("speed", speedInKM);

                    }
                } else if (inputString.matches("^\\$[A-Z]{2}GGA.*")) {
                    idSourceGGA++;

                    Matcher matcher = patternForCoords.matcher(inputString);

                    if (matcher.find()) {
                        String stringLatLong = matcher.group();
                        String[] dataLatLong = stringLatLong.split(",[NS],");
                        dataLatLong[1] = dataLatLong[1].replaceAll(",[EW]", "");

                        double havers = 0d;

                        if (rowCounterInFile != 0) {
                            havers = Haversine.calculateDistanceInKM(previousGPSData.get("latitude"), previousGPSData.get("longitude"),
                                    Double.parseDouble(dataLatLong[0]), Double.parseDouble(dataLatLong[1]));
                        }

                        if (previousGPSData.get("speed") > 1 | rowCounterInFile == 0) {
                            previousGPSData.put("latitude", Double.parseDouble(dataLatLong[0]));
                            previousGPSData.put("longitude", Double.parseDouble(dataLatLong[1]));
                        }

                        distanceKM += havers;
                    }

                } else if (inputString.matches("^\\$[A-Z]{2}ZDA.*")) {
                    idSourceZDA++;
                } else {
                    invalidStrings.put(rowCounterInFile + 1, inputString);
                }

                rowCounterInFile++;
            }
            all[2] = String.valueOf(rowCounterInFile - (idSourceGGA + idSourceVTG + idSourceZDA));
        } catch (IOException io) {
            io.printStackTrace();
        }

        all[0] = String.format("%.8f", distanceKM);
        all[1] = String.valueOf(rowCounterInFile);

        long countInvalidString = invalidStrings.keySet().stream().count();

        if (countInvalidString > 0) return new CalculationResult(all, invalidStrings);

        return new CalculationResult(all);
    }
}
