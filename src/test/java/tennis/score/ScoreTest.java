package tennis.score;

import org.junit.Test;
import org.junit.runner.RunWith;
import pbt4j.PropertyBasedTesting;
import tennis.*;

import static java.util.Optional.of;
import static org.junit.Assert.*;
import static tennis.score.Score.*;

/**
 * @author OZY on 2016.08.30.
 */
@RunWith(PropertyBasedTesting.class)
public class ScoreTest {

    @Test
    public void givenAdvantagedPlayer_WhenAdvantagedPlayerWins_ScoreIsCorrect(Player advantagedPlayer) throws Exception {
        final Score actual = scoreWhenAdvantage(new Advantage(advantagedPlayer), advantagedPlayer);
        final Score expected = new Game(advantagedPlayer);
        assertEquals(expected, actual);
    }

    @Test
    public void givenAdvantagedPlayer_WhenOtherPlayerWins_ScoreIsCorrect(Player advantagedPlayer) throws Exception {
        final Score actual = scoreWhenAdvantage(new Advantage(advantagedPlayer), other(advantagedPlayer));
        final Score expected = new Deuce();
        assertEquals(expected, actual);
    }

    @Test
    public void givenDeuce_WhenOtherPlayerScores_ScoreIsCorrect(Player advantagedPlayer) throws Exception {
        final Score actual = scoreWhenDeuce(advantagedPlayer);
        final Score expected = new Advantage(advantagedPlayer);
        assertEquals(expected, actual);
    }

    @Test
    public void givenPlayer40_whenPlayerWins_ThenScoreIsCorrect(Forty forty) throws Exception {
        final Score actual = scoreWhenForty(forty, forty.player);
        final Score expected = new Game(forty.player);
        assertEquals(expected, actual);
    }

    @Test
    public void givenPlayer40_Other30_whenOtherWins_ThenScoreIsCorrect(Forty forty) throws Exception {
        final Forty f = new Forty(forty.player, Point.THIRTY);
        final Score actual = scoreWhenForty(f, other(f.player));
        final Score expected = new Deuce();
        assertEquals(expected, actual);
    }

    @Test
    public void givenPlayer40_Other15_whenOtherWins_ThenScoreIsCorrect(Forty forty) throws Exception {
        final Forty f = new Forty(forty.player, Point.FIFTEEN);
        final Score actual = scoreWhenForty(f, other(f.player));
        final Score expected = new Forty(forty.player, Point.THIRTY);
        assertEquals(expected, actual);
    }

    @Test
    public void givenPlayer40_OtherLove_whenOtherWins_ThenScoreIsCorrect(Forty forty) throws Exception {
        final Forty f = new Forty(forty.player, Point.LOVE);
        final Score actual = scoreWhenForty(f, other(f.player));
        final Score expected = new Forty(forty.player, Point.FIFTEEN);
        assertEquals(expected, actual);
    }

    @Test
    public void givenPlayer30_whenPlayerWins_ThenScoreIsCorrect(Points points, Player winner) throws Exception {
        final Points current = pointTo(winner, Point.THIRTY, points);
        final Score actual = scoreWhenPoints(current, winner);
        final Score expected = new Forty(winner, pointFor(other(winner), current));
        assertEquals(expected, actual);
    }

    @Test
    public void scoreReturnsValue(Score current, Player winner) throws Exception {
        final Score actual = score(current, winner);
        assertNotNull(actual);
    }

    @Test
    public void shouldPlayFullGame() throws Exception {
        final Score actual = of(newGame())
                .map(current -> score(current, Player.PLAYER_ONE))
                .map(current -> score(current, Player.PLAYER_ONE))
                .map(current -> score(current, Player.PLAYER_TWO))
                .map(current -> score(current, Player.PLAYER_ONE))
                .map(current -> score(current, Player.PLAYER_TWO))
                .map(current -> score(current, Player.PLAYER_ONE))
                .orElseThrow(() -> new RuntimeException("failed"));

        final Score expected = new Game(Player.PLAYER_ONE);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldPlayFullGameWithSeq() throws Exception {
        final Score actual = scoreSeq(
                Player.PLAYER_ONE,
                Player.PLAYER_ONE,
                Player.PLAYER_TWO,
                Player.PLAYER_ONE,
                Player.PLAYER_TWO,
                Player.PLAYER_ONE);
        final Score expected = new Game(Player.PLAYER_ONE);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldPlayFullGameWithSeqTooManyPlayers() throws Exception {
        final Score actual = scoreSeq(
                Player.PLAYER_ONE,
                Player.PLAYER_ONE,
                Player.PLAYER_TWO,
                Player.PLAYER_ONE,
                Player.PLAYER_TWO,
                Player.PLAYER_ONE,
                Player.PLAYER_TWO,
                Player.PLAYER_TWO,
                Player.PLAYER_TWO);
        final Score expected = new Game(Player.PLAYER_ONE);
        assertEquals(expected, actual);
    }

}