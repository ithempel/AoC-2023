package de.ithempel.aoc;

public record Set(int red, int green, int blue) {

    public static Set fromRecord(final String record) {
        int red = 0;
        int green = 0;
        int blue = 0;

        String[] elements = record.split(",");
        for (int i=0; i<elements.length; i++) {
            String[] parts = elements[i].trim().split(" ");
            switch (parts[1].trim()) {
                case "red":
                    red = Integer.parseInt(parts[0]);
                    break;
                case "green":
                    green = Integer.parseInt(parts[0]);
                    break;
                case "blue":
                    blue = Integer.parseInt(parts[0]);
                    break;
                default:
            }
        }

        return new Set(red, green, blue);
    }

    public boolean isPossible(int maxRed, int maxGreen, int maxBlue) {
        boolean isPossible = red <= maxRed && green <= maxGreen && blue <= maxBlue;

        return isPossible;
    }

}
