package tennis.score;

import funk4j.matching.Pattern;
import tennis.*;

import java.util.Optional;
import java.util.stream.Stream;

import static funk4j.matching.Matchers.*;
import static io.vavr.API.*;
import static io.vavr.Predicates.*;

public interface Score {

    String toString();

    static Score newGame() {
        return new Points(Point.LOVE, Point.LOVE);
    }

    static Score score(Score current, Player winner) {
        return new Pattern<Score>()
                .when(classOf(Points.class, points -> scoreWhenPoints(points, winner)))
                .when(classOf(Forty.class, forty -> scoreWhenForty(forty, winner)))
                .when(classOf(Deuce.class, deuce -> scoreWhenDeuce(winner)))
                .when(classOf(Advantage.class, advantage -> scoreWhenAdvantage(advantage, winner)))
                .when(classOf(Game.class, game -> scoreWhenGame(game.player)))
                .match(current);
    }

    static Score scoreVavr(Score current, Player winner) {
        return Match(current).of(
                Case($(instanceOf(Points.class)), points -> scoreWhenPoints(points, winner)),
                Case($(instanceOf(Forty.class)), forty -> scoreWhenForty(forty, winner)),
                Case($(instanceOf(Deuce.class)), deuce -> scoreWhenDeuce(winner)),
                Case($(instanceOf(Advantage.class)), advantage -> scoreWhenAdvantage(advantage, winner)),
                Case($(instanceOf(Game.class)), game -> scoreWhenGame(game.player)),
                Case($(instanceOf(String.class)), game -> newGame()) //should not compile!!!
        );
    }

    static Score scoreSeq(Player... winners) {
        return Stream.of(winners)
                .reduce(newGame(), Score::score, (score, score2) -> score);
    }

    static Player other(Player ofPlayer) {
        return ofPlayer == Player.PLAYER_ONE ? Player.PLAYER_TWO : Player.PLAYER_ONE;
    }

    static Optional<Point> incrementPoint(Point point) {
        return new Pattern<Point>()
                .when(eq(Point.LOVE, p -> Optional.of(Point.FIFTEEN)))
                .when(eq(Point.FIFTEEN, p -> Optional.of(Point.THIRTY)))
                .when(eq(Point.THIRTY, p -> Optional.empty()))
                .match(point);
    }

    static Points pointTo(Player player, Point point, Points current) {
        return player == Player.PLAYER_ONE ?
                new Points(point, current.playerTwoPoint) :
                new Points(current.playerOnePoint, point);
    }

    static Point pointFor(Player player, Points current) {
        return player == Player.PLAYER_ONE ?
                current.playerOnePoint :
                current.playerTwoPoint;
    }

    static Score scoreWhenGame(Player winner) {
        return new Game(winner);
    }

    static Score scoreWhenAdvantage(Advantage advantage, Player scoredPlayer) {
        return scoredPlayer.equals(advantage.player) ? new Game(advantage.player) : new Deuce();
    }

    static Score scoreWhenDeuce(Player scoredPlayer) {
        return new Advantage(scoredPlayer);
    }

    static Score scoreWhenForty(Forty forty, Player scoredPlayer) {
        return forty.player.equals(scoredPlayer) ?
                new Game(scoredPlayer) :
                incrementPoint(forty.otherPlayerPoint)
                        .map(point -> new Forty(forty.player, point))
                        .map(f -> (Score) f)
                        .orElseGet(Deuce::new);
    }

    static Score scoreWhenPoints(Points points, Player scoredPlayer) {
        boolean playerOneScored = scoredPlayer == Player.PLAYER_ONE;
        final Point pointToIncrease = scoredPlayer == Player.PLAYER_ONE ? points.playerOnePoint : points.playerTwoPoint;

        return incrementPoint(pointToIncrease)
                .map(point -> new Points(
                        playerOneScored ? point : points.playerOnePoint,
                        playerOneScored ? points.playerTwoPoint : point))
                .map(p -> (Score) p)
                .orElseGet(() -> new Forty(scoredPlayer, playerOneScored ? points.playerTwoPoint : points.playerOnePoint));
    }

}
