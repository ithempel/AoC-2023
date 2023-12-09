package de.ithempel.aoc.day03;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class GearRatios {

    public static void main(String[] args) {
        File engineFile = new File("./src/main/resources/de/ithempel/aoc/engine-schematic.txt");
        
        try {
            List<String> lines = Files.readAllLines(engineFile.toPath());

            List<Integer> totalAdjacentNumbers = new LinkedList<>();

            String previousLine = "";
            Integer[] previousPositions = new Integer[0];
            for (String line : lines) {
                Integer[] positions = getSymbolPositions(line);

                List<Integer> adjacentNumbers = getAdjacentNumbersInLine(positions, line);
                totalAdjacentNumbers.addAll(adjacentNumbers);
                if (!previousLine.isBlank()) {
                    adjacentNumbers = getAdjacentNumbersInLine(previousPositions, line);
                    totalAdjacentNumbers.addAll(adjacentNumbers);

                    adjacentNumbers = getAdjacentNumbersInLine(positions, previousLine);
                    totalAdjacentNumbers.addAll(adjacentNumbers);
                }

                previousLine = line;
                previousPositions = positions;
            }

            int sum = totalAdjacentNumbers.stream()
                    .mapToInt(Integer::valueOf)
                    .sum();
            System.out.println("Sum of the total part numbers: " + sum);
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());
        }
    }

    private static Integer[] getSymbolPositions(final String line) {
        List<Integer> symbolPositions = new LinkedList<>();
        
        for (int i=0; i<line.length(); i++) {
            char currentChar = line.charAt(i);
            if (currentChar == '.') {
                continue;
            }
            if (!isNumber(currentChar)) {
                symbolPositions.add(i);
            }
        }

        return symbolPositions.toArray(new Integer[0]);
    }

    private static List<Integer> getAdjacentNumbersInLine(final Integer[] symbolPositions, final String line) {
        List<Integer> adjacentNumbers = new LinkedList<>();

        for (int i=0; i<symbolPositions.length; i++) {
            int charPosition = symbolPositions[i];
            char charAt = line.charAt(charPosition);
            if (isNumber(charAt)) {
                adjacentNumbers.add(extractNumberAt(charPosition, line));
            } else {
                if (symbolPositions[i] > 0) {
                    charPosition = symbolPositions[i] - 1;
                    char charBefore = line.charAt(charPosition);
                    if (isNumber(charBefore)) {
                        adjacentNumbers.add(extractNumberAt(charPosition, line));
                    }
                }
                if (symbolPositions[i] < line.length() - 1) {
                    charPosition = symbolPositions[i] + 1;
                    char charAfter = line.charAt(charPosition);
                    if (isNumber(charAfter)) {
                        adjacentNumbers.add(extractNumberAt(charPosition, line));
                    }
                }
            }
        }

        return adjacentNumbers;
    }

    private static boolean isNumber(char character) {
        return character >= '0' && character <= '9';
    }

    private static int extractNumberAt(int numberAt, final String line) {
        int i = numberAt;
        while (isNumber(line.charAt(i)) && i > 0) {
            i--;
        }
        int startIndex = i;
        if (!isNumber(line.charAt(startIndex))) {
            startIndex++;
        }

        i = numberAt;
        while (i < line.length() && isNumber(line.charAt(i))) {
            i++;
        }
        int endIndex = i;

        return Integer.parseInt(line.substring(startIndex, endIndex));
    }

}
