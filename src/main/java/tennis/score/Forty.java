package tennis.score;

import tennis.Player;
import tennis.Point;

import java.util.Objects;

public final class Forty implements Score {

    final Player player;
    final Point otherPlayerPoint;

    Forty(Player player, Point otherPlayerPoint) {
        this.player = player;
        this.otherPlayerPoint = otherPlayerPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Forty forty = (Forty) o;
        return player == forty.player &&
                otherPlayerPoint == forty.otherPlayerPoint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, otherPlayerPoint);
    }

    @Override
    public String toString() {
        return "Forty{" +
                "player=" + player +
                ", otherPlayerPoint=" + otherPlayerPoint +
                '}';
    }
}
