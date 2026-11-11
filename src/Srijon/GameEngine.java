package Srijon;

import Riad.*;
import Akash.*;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

// ==================== GAME ENGINE ====================
public class GameEngine {
    private final InputReader input = new InputReader();
    private final Leaderboard leaderboard = new Leaderboard();
    private final SoundEffects sound = new SoundEffects();
    private Difficulty difficulty = Difficulty.NORMAL;
    private boolean quick;
    private Player player;

    public void run() {
        Thread reader = new Thread(input, "keyboard-reader");
        reader.setDaemon(true);
        reader.start();
        try {
            while (true) {
                ConsoleUI.clear();
                box("RIDDLE QUEST", "1. New game", "2. Leaderboards", "3. How to play", "4. Settings", "5. Exit");
                switch (choose(5)) {
                    case 1:
                        newGame();
                        break;
                    case 2:
                        showLeaderboard();
                        break;
                    case 3:
                        rules();
                        break;
                    case 4:
                        settings();
                        break;
                    case 5:
                        return;
                    default:
                        break;
                }
            }
        } catch (QuitGameException e) {
            ConsoleUI.message(e.getMessage());
        } finally {
            sound.close();
            ConsoleUI.message("Thanks for playing RIDDLE QUEST!");
        }
    }

    private void newGame() throws QuitGameException {
        ConsoleUI.prompt("Enter your name (1-15 characters): ");
        String name;
        while (true) {
            name = line().replaceAll("\\p{Cntrl}", "").trim();
            if (!name.isEmpty() && name.length() <= 15) {
                break;
            }
            ConsoleUI.prompt("Please enter a name with 1-15 characters: ");
        }
        box("CHOOSE DIFFICULTY", "1. Easy:   7 lives, 60s normal / 90s boss", "2. Normal: 5 lives, 40s normal / 60s boss", "3. Hard:   3 lives, 25s normal / 40s boss");
        difficulty = Difficulty.values()[choose(3) - 1];
        box("CHOOSE ADVENTURE", "1. Full quest: 50 riddles, all five secret letters", "2. Quick play: 10 riddles, one boss");
        quick = choose(2) == 2;
        player = new Player(name, difficulty.lives);
        List<Level> levels = createSession(quick);
        long started = System.nanoTime();
        String outcome = "COMPLETED";
        sound.startMusic();
        try {
            for (int i = 0; i < levels.size(); i++) {
                try {
                    playLevel(levels.get(i), i, levels.size());
                } finally {
                    // Never carry ticking into another question.
                    sound.stopTicking();
                }
            }
        } catch (GameOverException e) {
            outcome = "GAME OVER";
            ConsoleUI.message(e.getMessage());
        } catch (QuitGameException e) {
            outcome = "QUIT";
            ConsoleUI.message(e.getMessage());
        } finally {
            sound.stopTicking();
            sound.stopMusic();
        }
        if (outcome.equals("COMPLETED")) {
            sound.victory();
        } else if (outcome.equals("GAME OVER")) {
            sound.gameOver();
        }
        long elapsed = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - started);
        showResults(outcome, levels.size(), elapsed);
        if (!outcome.equals("QUIT")) {
            try {
                leaderboard.add(player, difficulty, quick, outcome);
                ConsoleUI.message("Score saved. View it from Leaderboards.");
            } catch (IOException | SecurityException e) {
                ConsoleUI.message("Could not save the score. Your result is still shown above.");
            }
        }
        pause();
    }

    static List<Level> createSession(boolean quick) {
        List<Level> all = LevelFactory.createLevels();
        List<Level> result = new ArrayList<>();
        if (quick) {
            List<Level> regular = new ArrayList<>();
            List<Level> bosses = new ArrayList<>();
            for (Level level : all) {
                if (level.isBoss()) {
                    bosses.add(level);
                } else {
                    regular.add(level);
                }
            }
            Collections.shuffle(regular);
            Collections.shuffle(bosses);
            result.addAll(regular.subList(0, 9));
            result.add(bosses.get(0));
        } else {
            // Shuffle regular riddles within each chapter.
            // Keep each original boss at the end of its chapter.
            for (int chapter = 0; chapter < 5; chapter++) {
                int start = chapter * 10;
                List<Level> regular = new ArrayList<>(all.subList(start, start + 9));
                Collections.shuffle(regular);
                result.addAll(regular);
                result.add(all.get(start + 9));
            }
        }
        return result;
    }

    private void playLevel(Level level, int index, int total)
            throws QuitGameException, GameOverException {
        int limit = difficulty.seconds(level);
        showQuestion(level, index, total, true);
        long opened = System.nanoTime();
        long deadline = opened + TimeUnit.SECONDS.toNanos(limit);
        boolean hintUsed = false;
        boolean warned = false;
        int lastShown = -1;
        String notice = "Type an answer, hint, skip or quit, then press ENTER.";
        String noticeColor = Colors.CYAN;
        ConsoleUI.prompt("Your answer: ");
        try {
            while (true) {
                long remaining = deadline - System.nanoTime();
                if (remaining <= 0) {
                    sound.stopTicking();
                    player.timeout();
                    player.loseLife();
                    sound.play(false);
                    checkAlive();
                    opened = System.nanoTime();
                    deadline = opened + TimeUnit.SECONDS.toNanos(limit);
                    notice = "Time up! -1 life. Clock restarted; finish your answer.";
                    noticeColor = Colors.RED;
                    warned = false;
                    lastShown = -1;
                    // Keep the partially typed answer and its cursor in place.
                    continue;
                }
                int seconds = (int) ((remaining + 999_999_999L) / 1_000_000_000L);
                if (seconds <= 10 && !warned) {
                    sound.startTicking();
                    notice = "Only 10 seconds left!";
                    noticeColor = Colors.YELLOW;
                    warned = true;
                }
                if (seconds != lastShown) {
                    ConsoleUI.live(seconds, limit, player, notice, noticeColor);
                    lastShown = seconds;
                }
                InputReader.Entry entry;
                try {
                    entry = input.poll(Math.min(remaining, TimeUnit.MILLISECONDS.toNanos(100)));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new QuitGameException("Game interrupted.");
                }
                if (entry == null) continue;
                if (entry.eof) throw new QuitGameException("Input closed.");
                if (entry.receivedAt < opened || entry.receivedAt > deadline) continue;
                String answer = entry.text.trim();
                String command = answer.toLowerCase(Locale.ROOT);
                if (command.equals("quit")) {
                    throw new QuitGameException("Returned to menu. Unfinished scores are not saved.");
                } else if (command.equals("skip")) {
                    sound.stopTicking();
                    player.skipped();
                    player.loseLife();
                    ConsoleUI.message("Skipped. Answer: " + level.getAnswer(), Colors.YELLOW);
                    checkAlive();
                    ConsoleUI.sleep(1000);
                    return;
                } else if (answer.isEmpty()) {
                    notice = "Please type an answer.";
                    noticeColor = Colors.YELLOW;
                } else if (command.equals("hint")) {
                    if (!hintUsed && player.getScore() < 5) {
                        notice = "You need at least 5 points to buy a hint.";
                        noticeColor = Colors.YELLOW;
                    } else {
                        if (!hintUsed) {
                            player.subtractScore(5);
                            player.usedHint();
                            hintUsed = true;
                        }
                        notice = "Hint: " + level.getHint();
                        noticeColor = Colors.PURPLE;
                    }
                } else if (level.isCorrect(answer)) {
                    sound.stopTicking();
                    int bonus = (int) Math.max(0, TimeUnit.NANOSECONDS.toSeconds(deadline - entry.receivedAt)) / 5;
                    int points = level.getPoints() + bonus;
                    player.addScore(points);
                    player.levelCleared();
                    sound.play(true);
                    ConsoleUI.celebrate();
                    ConsoleUI.message("+" + points + " points (speed bonus: " + bonus + ")", Colors.GREEN);
                    if (level.isBoss()) {
                        int before = player.getLives();
                        player.gainLife();
                        player.unlock(level.getNumber() / 10 - 1);
                        ConsoleUI.message("Boss defeated! " + (player.getLives() > before ? "+1 life. " : "Lives already full. ") + "Secret: " + player.secret(), Colors.YELLOW);
                    }
                    ConsoleUI.sleep(1000);
                    return;
                } else {
                    player.mistake();
                    player.loseLife();
                    sound.play(false);
                    checkAlive();
                    notice = "Wrong answer! -1 life. Try again.";
                    noticeColor = Colors.RED;
                }
                // A submitted answer ended the input line. Redraw once, then
                // resume input. Timer updates alone never redraw the question.
                showQuestion(level, index, total, false);
                ConsoleUI.prompt("Your answer: ");
                lastShown = -1;
            }
        } finally {
            sound.stopTicking();
        }
    }

    private void showQuestion(Level level, int index, int total, boolean animate) {
        box(level.getTitle() + " " + (index + 1) + " / " + total, "Player: " + player.getName() + " | Difficulty: " + difficulty, "Secret: " + player.secret(), "Progress: " + ConsoleUI.progressBar(index, total));
        ConsoleUI.divider();
        if (animate) ConsoleUI.typeLine("Q: " + level.getQuestion(), 10);
        else ConsoleUI.line("Q: " + level.getQuestion(), Colors.PURPLE);
        ConsoleUI.divider();
        ConsoleUI.line("hint: -5 points once | skip: -1 life | quit: menu");
        ConsoleUI.top();
    }

    private void checkAlive() throws GameOverException {
        if (!player.isAlive()) {
            throw new GameOverException("You ran out of lives.");
        }
    }

    private void showResults(String outcome, int total, long elapsed) {
        box(outcome + " - " + player.getName(), "Mode: " + (quick ? "Quick play" : "Full quest") + " | " + difficulty, "Final score: " + player.getScore(), "Solved: " + player.getLevelsCleared() + " / " + total + " | Skipped: " + player.getSkipped(), "Wrong answers: " + player.getMistakes() + " | Timeouts: " + player.getTimeouts(), "Hints purchased: " + player.getHints() + " | Lives left: " + player.getLives(), "Session time: " + elapsed / 60 + "m " + elapsed % 60 + "s", "Earned letters: " + player.secret());
        if (!quick && outcome.equals("COMPLETED")) {
            if (player.allLetters()) {
                ConsoleUI.message("SECRET UNLOCKED: LOGIC - Keep thinking, keep coding!");
            } else {
                ConsoleUI.message("Quest finished! Defeat all five bosses in one run " + "to unlock the secret.");
            }
        }
    }

    private void showLeaderboard() throws QuitGameException {
        box("LEADERBOARDS", "1. Easy", "2. Normal", "3. Hard");
        Difficulty selected = Difficulty.values()[choose(3) - 1];
        box("MODE", "1. Full quest", "2. Quick play");
        boolean shortMode = choose(2) == 2;
        try {
            List<Leaderboard.Score> scores = leaderboard.top(selected, shortMode);
            box(selected + " - " + (shortMode ? "QUICK PLAY" : "FULL QUEST"));
            if (scores.isEmpty()) {
                ConsoleUI.message("No scores yet. Finish a game to set a record!");
            }
            for (int i = 0; i < scores.size(); i++) {
                Leaderboard.Score score = scores.get(i);
                ConsoleUI.line((i + 1) + ". " + score.name + " | " + score.points + " pts" + " | " + score.solved + " solved" + " | " + score.outcome + " | " + score.date);
            }
        } catch (IOException | SecurityException e) {
            ConsoleUI.message("Could not read the leaderboard.");
        }
        pause();
    }

    private void rules() throws QuitGameException {
        box("HOW TO PLAY", "Solve riddles before time runs out.", "Answers ignore case, punctuation and leading a/an/the.", "Known alternative answers are accepted.", "Normal riddle: 10 points. Boss: 30 points.", "Speed bonus: 1 point per complete 5 seconds left.", "Wrong answers, timeouts and skips each cost one life.", "Wrong answers keep the clock running.", "Timeouts restart the current question's clock.", "Hints cost 5 points and require enough score.", "Repeating a purchased hint on that question is free.", "Boss wins restore one life up to your starting limit.", "Defeat all five bosses in a full quest to reveal LOGIC.", "Quick play: 9 random riddles and one random boss.", "Full quests shuffle regular riddles within each chapter.", "Quit returns to the menu without saving that run.", "Leaderboards keep the top 5 per difficulty and mode.", "Completed and game-over runs can enter leaderboards.");
        pause();
    }

    private void settings() throws QuitGameException {
        while (true) {
            box("SETTINGS", "1. Animations: " + (ConsoleUI.animations ? "ON" : "OFF"), "2. Sound effects: " + (sound.enabled ? "ON" : "OFF"), "3. Panel width: " + ConsoleUI.columns + " columns", "4. Back");
            int choice = choose(4);
            if (choice == 4) {
                return;
            }
            if (choice == 3) {
                ConsoleUI.prompt("Terminal width (68-240 columns): ");
                try {
                    int width = Integer.parseInt(line());
                    if (width >= 68 && width <= 240) ConsoleUI.columns = width;
                    else ConsoleUI.message("Width must be between 68 and 240.");
                } catch (NumberFormatException e) {
                    ConsoleUI.message("Please enter a whole number.");
                }
            }
            if (choice == 1) {
                ConsoleUI.animations = !ConsoleUI.animations;
            }
            if (choice == 2) {
                sound.setEnabled(!sound.enabled);
                if (sound.enabled) {
                    sound.play(true);
                }
            }
        }
    }

    private int choose(int maximum) throws QuitGameException {
        while (true) {
            ConsoleUI.prompt("Choose 1-" + maximum + ": ");
            String value = line();
            try {
                int choice = Integer.parseInt(value);
                if (choice >= 1 && choice <= maximum) {
                    return choice;
                }
            } catch (NumberFormatException ignored) {
                // Display a helpful message below.
            }
            ConsoleUI.message("Enter a number from 1 to " + maximum + ".");
        }
    }

    private String line() throws QuitGameException {
        try {
            InputReader.Entry entry = input.take();
            if (entry.eof) {
                throw new QuitGameException("Input closed.");
            }
            return entry.text.trim();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new QuitGameException("Game interrupted.");
        }
    }

    private void pause() throws QuitGameException {
        ConsoleUI.prompt("Press ENTER to return to the menu...");
        line();
    }

    private static void box(String title, String... lines) {
        ConsoleUI.clear();
        ConsoleUI.top();
        ConsoleUI.center(title);
        ConsoleUI.divider();
        for (String text : lines) {
            ConsoleUI.line(text);
        }
        ConsoleUI.top();
    }
}
