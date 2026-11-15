package Riad;
import Srijon.*;
import Akash.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;


public class Leaderboard {
    public static final class Score {
        public final String name;
        final Difficulty difficulty;
        final boolean quick;
        public final int points;
        public final int solved;
        public final String outcome;
        public final String date;

        Score(String name, Difficulty difficulty, boolean quick, int points, int solved, String outcome, String date) {
            this.name = name;
            this.difficulty = difficulty;
            this.quick = quick;
            this.points = points;
            this.solved = solved;
            this.outcome = outcome;
            this.date = date;
        }

        String encode() {
            return name + "\t"
                    + difficulty + "\t"
                    + quick + "\t"
                    + points + "\t"
                    + solved + "\t"
                    + outcome + "\t"
                    + date;
        }

        static Score decode(String text) {
            String[] fields = text.split("\t", -1);
            if (fields.length != 7 || fields[0].isEmpty() || fields[0].length() > 15 || fields[0].matches(".*\\p{Cntrl}.*")) {
                throw new IllegalArgumentException();
            }
            if (!fields[2].equals("true") && !fields[2].equals("false")) {
                throw new IllegalArgumentException();
            }
            int points = Integer.parseInt(fields[3]);
            int solved = Integer.parseInt(fields[4]);
            boolean quick = Boolean.parseBoolean(fields[2]);
            if (points < 0 || solved < 0 || solved > (quick ? 10 : 50)) {
                throw new IllegalArgumentException();
            }
            if (!fields[5].equals("COMPLETED") && !fields[5].equals("GAME OVER")) {
                throw new IllegalArgumentException();
            }
            LocalDate.parse(fields[6]);
            return new Score(fields[0], Difficulty.valueOf(fields[1]), quick, points, solved, fields[5], fields[6]);
        }
    }

    private final Path file;

    public Leaderboard() {
        Path directory = Paths.get(System.getProperty("riddle.dataDir", Paths.get(System.getProperty("user.home"), ".riddle-quest").toString()));
        file = directory.resolve("leaderboard.tsv");
    }

    private List<Score> read() throws IOException {
        List<Score> scores = new ArrayList<>();
        if (!Files.exists(file)) {
            return scores;
        }
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            try {
                scores.add(Score.decode(line));
            } catch (RuntimeException ignored) {

            }
        }
        return scores;
    }

    public List<Score> top(Difficulty difficulty, boolean quick)
            throws IOException {
        List<Score> result = new ArrayList<>();
        for (Score score : read()) {
            if (score.difficulty == difficulty && score.quick == quick) {
                result.add(score);
            }
        }
        sortScores(result);
        return new ArrayList<>(result.subList(0, Math.min(5, result.size())));
    }

    private void sortScores(List<Score> scores) {
        scores.sort(Comparator.comparingInt((Score s) -> s.points).reversed().thenComparing(Comparator.comparingInt((Score s) -> s.solved).reversed()));
    }

    public void add(Player player, Difficulty difficulty, boolean quick, String outcome) throws IOException {
        List<Score> scores = read();
        scores.add(new Score(player.getName(), difficulty, quick, player.getScore(), player.getLevelsCleared(), outcome, LocalDate.now().toString()));
        sortScores(scores);
        Map<String, Integer> counts = new HashMap<>();
        List<String> lines = new ArrayList<>();
        for (Score score : scores) {
            String key = score.difficulty + ":" + score.quick;
            int count = counts.getOrDefault(key, 0);
            if (count < 5) {
                lines.add(score.encode());
                counts.put(key, count + 1);
            }
        }
        Files.createDirectories(file.getParent());
        Path temporary = Files.createTempFile(file.getParent(), "scores-", ".tmp");
        try {
            Files.write(temporary, lines, StandardCharsets.UTF_8);
            try {
                Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
