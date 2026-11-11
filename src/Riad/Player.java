package Riad;

// ==================== PLAYER ====================
public class Player {
    private final String name;
    private final int maximumLives;
    private int lives;
    private int score;
    private int levelsCleared;
    private int skipped;
    private int mistakes;
    private int timeouts;
    private int hints;
    private final boolean[] letters = new boolean[5];

    public Player(String name, int startingLives) {
        this.name = name;
        this.lives = startingLives;
        this.maximumLives = startingLives;
    }

    public String getName() {
        return name;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public int getLevelsCleared() {
        return levelsCleared;
    }

    public int getSkipped() {
        return skipped;
    }

    public int getMistakes() {
        return mistakes;
    }

    public int getTimeouts() {
        return timeouts;
    }

    public int getHints() {
        return hints;
    }

    public boolean isAlive() {
        return lives > 0;
    }

    public void addScore(int points) {
        score += Math.max(0, points);
    }

    public void subtractScore(int points) {
        score = Math.max(0, score - points);
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public void gainLife() {
        if (lives < maximumLives) {
            lives++;
        }
    }

    public void levelCleared() {
        levelsCleared++;
    }

    public void skipped() {
        skipped++;
    }

    public void mistake() {
        mistakes++;
    }

    public void timeout() {
        timeouts++;
    }

    public void usedHint() {
        hints++;
    }

    public String getHearts() {
        return "<3 ".repeat(lives).trim();
    }

    public void unlock(int index) {
        letters[index] = true;
    }

    public String secret() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < letters.length; i++) {
            result.append(letters[i] ? "LOGIC".charAt(i) : '_');
            result.append(' ');
        }
        return result.toString().trim();
    }

    public boolean allLetters() {
        for (boolean earned : letters) {
            if (!earned) {
                return false;
            }
        }
        return true;
    }
}
