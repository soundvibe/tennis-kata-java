package tennis.score;

import tennis.Player;

import java.util.Objects;

public final class Advantage implements Score {

    final Player player;

    Advantage(Player player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advantage advantage = (Advantage) o;
        return player == advantage.player;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }

    @Override
    public String toString() {
        return "Advantage{" +
                "player=" + player +
                '}';
    }
}
