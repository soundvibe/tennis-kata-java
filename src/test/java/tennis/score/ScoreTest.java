package tennis.score;

import org.junit.Test;
import org.junit.runner.RunWith;
import pbt4j.PropertyBasedTesting;
import tennis.*;

import static java.util.Optional.of;
import static org.junit.Assert.*;
import static tennis.score.Score.*;

@RunWith(PropertyBasedTesting.class)
public class ScoreTest {

    @Test
    public void givenAdvantagedPlayer_WhenAdvantagedPlayerWins_ScoreIsCorrect(Player advantagedPlayer) {
        var actual = scoreWhenAdvantage(new Advantage(advantagedPlayer), advantagedPlayer);
        var expected = new Game(advantagedPlayer);
        assertEquals(expected, actual);
    }

    @Test
    public void givenAdvantagedPlayer_WhenOtherPlayerWins_ScoreIsCorrect(Player advantagedPlayer) {
        var actual = scoreWhenAdvantage(new Advantage(advantagedPlayer), other(advantagedPlayer));
        var expected = new Deuce();
        assertEquals(expected, actual);
    }

    @Test
    public void givenDeuce_WhenOtherPlayerScores_ScoreIsCorrect(Player advantagedPlayer) {
        var actual = scoreWhenDeuce(advantagedPlayer);
        var expected = new Advantage(advantagedPlayer);
        assertEquals(expected, actual);
    }

    @Test
    public void givenPlayer40_whenPlayerWins_ThenScoreIsCorrect(Forty forty) {
        var actual = scoreWhenForty(forty, forty.player);
        var expected = new Game(forty.player);
        assertEquals(expected, actual);
    }

    @Test
    public void givenPlayer40_Other30_whenOtherWins_ThenScoreIsCorrect(Forty forty) {
        var f = new Forty(forty.player, Point.THIRTY);
        var actual = scoreWhenForty(f, other(f.player));
        var expected = new Deuce();
        assertEquals(expected, actual);
    }

    @Test
    public void givenPlayer40_Other15_whenOtherWins_ThenScoreIsCorrect(Forty forty) {
        var f = new Forty(forty.player, Point.FIFTEEN);
        var actual = scoreWhenForty(f, other(f.player));
        var expected = new Forty(forty.player, Point.THIRTY);
        assertEquals(expected, actual);
    }

    @Test
    public void givenPlayer40_OtherLove_whenOtherWins_ThenScoreIsCorrect(Forty forty) {
        var f = new Forty(forty.player, Point.LOVE);
        var actual = scoreWhenForty(f, other(f.player));
        var expected = new Forty(forty.player, Point.FIFTEEN);
        assertEquals(expected, actual);
    }

    @Test
    public void givenPlayer30_whenPlayerWins_ThenScoreIsCorrect(Points points, Player winner) {
        var currentPoints = pointTo(winner, Point.THIRTY, points);
        var actual = scoreWhenPoints(currentPoints, winner);
        var expected = new Forty(winner, pointFor(other(winner), currentPoints));
        assertEquals(expected, actual);
    }

    @Test
    public void scoreReturnsValue(Score current, Player winner) {
        var actual = score(current, winner);
        assertNotNull(actual);
    }

    @Test
    public void shouldPlayFullGame() {
        var actual = of(newGame())
                .map(current -> score(current, Player.PLAYER_ONE))
                .map(current -> score(current, Player.PLAYER_ONE))
                .map(current -> score(current, Player.PLAYER_TWO))
                .map(current -> score(current, Player.PLAYER_ONE))
                .map(current -> score(current, Player.PLAYER_TWO))
                .map(current -> score(current, Player.PLAYER_ONE))
                .orElseThrow();

        var expected = new Game(Player.PLAYER_ONE);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldPlayFullGameWithSeq() {
        var actual = scoreSeq(
                Player.PLAYER_ONE,
                Player.PLAYER_ONE,
                Player.PLAYER_TWO,
                Player.PLAYER_ONE,
                Player.PLAYER_TWO,
                Player.PLAYER_ONE);
        var expected = new Game(Player.PLAYER_ONE);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldPlayFullGameWithSeqTooManyPlayers() {
        var actual = scoreSeq(
                Player.PLAYER_ONE,
                Player.PLAYER_ONE,
                Player.PLAYER_TWO,
                Player.PLAYER_ONE,
                Player.PLAYER_TWO,
                Player.PLAYER_ONE,
                Player.PLAYER_TWO,
                Player.PLAYER_TWO,
                Player.PLAYER_TWO);
        var expected = new Game(Player.PLAYER_ONE);
        assertEquals(expected, actual);
    }

}