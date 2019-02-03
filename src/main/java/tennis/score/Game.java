package tennis.score;

import tennis.Player;

import java.util.Objects;

public final class Game implements Score {

    public final Player player;

    Game(Player player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return player == game.player;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }

    @Override
    public String toString() {
        return "Game{" +
                "player=" + player +
                '}';
    }
}
