package de.ithempel.aoc.day03;

import static java.util.stream.Collectors.groupingBy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GearRatios {

    /**
     * @param args
     */
    public static void main(String[] args) {
        File engineFile = new File("./src/main/resources/de/ithempel/aoc/engine-schematic.txt");
        
        try {
            List<String> lines = Files.readAllLines(engineFile.toPath());

            List<AdjacentNumber> totalAdjacentNumbers = new LinkedList<>();

            int lineNumber = 0;
            String previousLine = "";
            List<SymbolPosition> previousPositions = Collections.emptyList();
            for (String line : lines) {
                List<SymbolPosition> positions = getSymbolPositions(lineNumber, line);

                List<AdjacentNumber> adjacentNumbers = getAdjacentNumbersInLine(positions, line, lineNumber);
                totalAdjacentNumbers.addAll(adjacentNumbers);
                if (!previousLine.isBlank()) {
                    adjacentNumbers = getAdjacentNumbersInLine(previousPositions, line, lineNumber);
                    totalAdjacentNumbers.addAll(adjacentNumbers);

                    adjacentNumbers = getAdjacentNumbersInLine(positions, previousLine, lineNumber);
                    totalAdjacentNumbers.addAll(adjacentNumbers);
                }

                lineNumber++;
                previousLine = line;
                previousPositions = positions;
            }

            int sum = totalAdjacentNumbers.stream()
                    .map(adjacentNumber -> adjacentNumber.number())
                    .mapToInt(Integer::valueOf)
                    .sum();
            System.out.println("Sum of the total part numbers: " + sum);

            Map<SymbolPosition, List<AdjacentNumber>> groupedGearSymbols = totalAdjacentNumbers.stream()
                    .filter(adjacentNumber -> adjacentNumber.symbolPosition().symbol() == '*')
                    .collect(groupingBy(adjacentNumber -> adjacentNumber.symbolPosition()));
            sum = groupedGearSymbols.entrySet().stream()
                    .filter(entry -> entry.getValue().size() == 2)
                    .map(entry -> entry.getValue())
                    .map(GearRatios::calculateGearRatio)
                    .mapToInt(Integer::valueOf)
                    .sum();
            System.out.println("Sum of all gear ratios: " + sum);
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());
        }
    }

    private static List<SymbolPosition> getSymbolPositions(int lineNumber, final String line) {
        List<SymbolPosition> symbolPositions = new LinkedList<>();
        
        for (int i=0; i<line.length(); i++) {
            char currentChar = line.charAt(i);
            if (currentChar == '.') {
                continue;
            }
            if (!isNumber(currentChar)) {
                SymbolPosition symbolPosition = new SymbolPosition(lineNumber, i, currentChar);
                symbolPositions.add(symbolPosition);
            }
        }

        return symbolPositions;
    }

    private static List<AdjacentNumber> getAdjacentNumbersInLine(final List<SymbolPosition> symbolPositions, final String line, int lineNumber) {
        List<AdjacentNumber> adjacentNumbers = new LinkedList<>();

        for (SymbolPosition symbolPosition : symbolPositions) {
            int position = symbolPosition.position();
            char charAt = line.charAt(position);
            if (isNumber(charAt)) {
                int number = extractNumberAt(position, line);
                AdjacentNumber adjacentNumber = new AdjacentNumber(symbolPosition, position, lineNumber, number);
                adjacentNumbers.add(adjacentNumber);
            } else {
                if (position > 0) {
                    position = symbolPosition.position() - 1;
                    char charBefore = line.charAt(position);
                    if (isNumber(charBefore)) {
                        int number = extractNumberAt(position, line);
                        AdjacentNumber adjacentNumber = new AdjacentNumber(symbolPosition, position, lineNumber, number);
                        adjacentNumbers.add(adjacentNumber);
                    }
                }
                if (position < line.length() - 1) {
                    position = symbolPosition.position() + 1;
                    char charAfter = line.charAt(position);
                    if (isNumber(charAfter)) {
                        int number = extractNumberAt(position, line);
                        AdjacentNumber adjacentNumber = new AdjacentNumber(symbolPosition, position, lineNumber, number);
                        adjacentNumbers.add(adjacentNumber);
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

    private static int calculateGearRatio(final List<AdjacentNumber> adjacentNumbers) {
        int[] numbers = adjacentNumbers.stream()
                .map(adjacentNumber -> adjacentNumber.number())
                .mapToInt(Integer::valueOf)
                .toArray();
        
        int ratio = 1;
        for (int number : numbers) {
            ratio = ratio * number;
        }

        return ratio;
    }

}
