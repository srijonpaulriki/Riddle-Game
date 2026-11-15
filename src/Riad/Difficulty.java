package Riad;


public enum Difficulty {
    EASY(7, 60, 90),
    NORMAL(5, 40, 60),
    HARD(3, 25, 40);
    public final int lives;
    final int normalSeconds;
    final int bossSeconds;

    Difficulty(int lives, int normalSeconds, int bossSeconds) {
        this.lives = lives;
        this.normalSeconds = normalSeconds;
        this.bossSeconds = bossSeconds;
    }

    public int seconds(Level level) {
        return level.isBoss() ? bossSeconds : normalSeconds;
    }
}
