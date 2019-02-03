package tennis;

import tennis.score.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class Application {

    private static final String KEY = "1";

    public static void main(String[] args) {
        System.out.println("************TENNIS**************");
        System.out.println("Players: " + Arrays.toString(Player.values()));
        System.out.println(String.format("Type %s if %s scores a point and press Enter. Any other key scores point for %s.",
                KEY, Player.PLAYER_ONE, Player.PLAYER_TWO));
        System.out.println("************GAME START**********");

        try (var scanner = new Scanner(System.in)) {
            var score = new AtomicReference<>(Score.newGame());
            Stream.generate(scanner::next)
                    .map(key -> KEY.equals(key) ? Player.PLAYER_ONE : Player.PLAYER_TWO)
                    .peek(winner -> System.out.println("Point to: " + winner))
                    .map(winner -> score.updateAndGet(prevScore -> Score.score(prevScore, winner)))
                    .takeWhile(not(newScore -> newScore instanceof Game))
                    .forEach(System.out::println);
            System.out.println("Game to: " + ((Game)score.get()).player);
            System.out.println("**********GAME END**********");
        }
    }
}
