package de.ithempel.aoc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Trebuchet {

    private static Map<String, String> spelledMapping;
    private static Set<String> spelledNumbers;

    public static void main(String[] args) {
        spelledMapping = Stream.of(new String[][] {
            { "one", "1" },
            { "two", "2" },
            { "three", "3" },
            { "four", "4" },
            { "five", "5" },
            { "six", "6" },
            { "seven", "7" },
            { "eight", "8" },
            { "nine", "9" }
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        spelledNumbers = spelledMapping.keySet();

        File calibrationFile = new File("./src/main/resources/de/ithempel/aoc/calibration.txt");

        try (Stream<String> calibrationStream = Files.lines(calibrationFile.toPath())) {
            long sumOfCalibrationValues = calibrationStream
                    .map(line -> line.replaceAll("[a-zA-Z]", ""))
                    .map(line -> line.substring(0, 1) + line.substring(line.length() - 1, line.length()))
                    .map(number -> Long.parseLong(number))
                    .mapToLong(Long::longValue)
                    .sum();
            
            System.out.println("Total sum of callibration values with substitution: %d".formatted(sumOfCalibrationValues));
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());
        }

        try (Stream<String> calibrationStream = Files.lines(calibrationFile.toPath())) {
            long sumOfCalibrationValues = calibrationStream
                    .map(Trebuchet::getDigits)
                    .map(number -> Long.parseLong(number))
                    .mapToLong(Long::longValue)
                    .sum();
            
            System.out.println("Total sum of callibration values with parsing: %d".formatted(sumOfCalibrationValues));
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());
        }
    }

    private static String getDigits(final String line) {
        return getFirstDigit(line) + getLastDigit(line);
    }

    private static String getFirstDigit(final String line) {
        String digit = "";
        String spelled = "";
        for (int i=0; i<line.length(); i++) {
            digit = line.substring(i, i+1);
            if (!digit.matches("[1-9]")) {
                spelled = spelled + digit;
                digit = findSpelledDigit(spelled);
            }

            if (!digit.isEmpty()) {
                break;
            }
        }

        return digit;
    }

    private static String getLastDigit(final String line) {
        String digit = "";
        String spelled = "";
        for (int i=line.length()-1; i>=0; i--) {
            digit = line.substring(i, i+1);
            if (!digit.matches("[1-9]")) {
                spelled = digit + spelled;
                digit = findSpelledDigit(spelled);
            }

            if (!digit.isEmpty()) {
                break;
            }
        }

        return digit;
    }

    private static String findSpelledDigit(final String spelled) {
        String digit = "";
        for (String currentNumber : spelledNumbers) {
            if (spelled.contains(currentNumber)) {
                digit = spelledMapping.get(currentNumber);
                break;
            }
        }

        return digit;
    }

}
