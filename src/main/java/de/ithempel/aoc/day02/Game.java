package de.ithempel.aoc.day02;

import java.util.Arrays;

public record Game(int id, Set[] sets) {

    public static Game fromRecord(final String record) {
        String[] parts = record.split(":");

        int id = Integer.parseInt(parts[0].split(" ")[1]);

        String setRecords[] = parts[1].split(";");
        Set[] sets = Arrays.stream(setRecords)
            .map(Set::fromRecord)
            .toArray(Set[]::new);

        return new Game(id, sets);
    }

    public boolean isPossible(int maxRed, int maxGreen, int maxBlue) {
        long imPossibleSets = Arrays.stream(sets)
                .filter(set -> !set.isPossible(maxRed, maxGreen, maxBlue))
                .count();

        return imPossibleSets == 0;
    }

    public int maxRed() {
        return Arrays.stream(sets)
                .map(set -> set.red())
                .mapToInt(Integer::intValue)
                .max().orElse(0);
    }

    public int maxGreen() {
        return Arrays.stream(sets)
                .map(set -> set.green())
                .mapToInt(Integer::intValue)
                .max().orElse(0);
    }

    public int maxBlue() {
        return Arrays.stream(sets)
                .map(set -> set.blue())
                .mapToInt(Integer::intValue)
                .max().orElse(0);
    }

}
