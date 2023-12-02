package de.ithempel.aoc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Stream;

public class CubeConundrum {

    public static void main(String[] args) {
        File gameFile = new File("./src/main/resources/de/ithempel/aoc/games.txt");
        
        try (Stream<String> gameStream = Files.lines(gameFile.toPath())) {
            Game[] games = gameStream
                    .map(Game::fromRecord)
                    .toArray(Game[]::new);

            System.out.println("Number of Games " + games.length);

            int summedPossibleGameIds = Arrays.stream(games)
                    .filter(game -> game.isPossible(12, 13, 14))
                    .map(game -> game.id())
                    .mapToInt(Integer::intValue)
                    .sum();
            System.out.println("Summed up game ids of all possible games: " + summedPossibleGameIds);

            int summedPowerOfAllGames = Arrays.stream(games)
                    .map(game -> game.maxRed() * game.maxGreen() * game.maxBlue())
                    .mapToInt(Integer::intValue)
                    .sum();
            System.out.println("Summed power of all games: " + summedPowerOfAllGames);
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());
        }
    }

}
