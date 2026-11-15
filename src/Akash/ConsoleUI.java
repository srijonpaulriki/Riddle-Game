package Akash;
import Riad.*;
import Srijon.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConsoleUI {

    public static int columns = Math.max(68, Integer.getInteger("riddle.columns", 120));
    static final int TOP = Math.max(0, Math.min(6, Integer.getInteger("riddle.top", 2)));
    public static final boolean ansi = Boolean.parseBoolean(System.getProperty("riddle.ansi", String.valueOf(System.console() != null)));
    public static boolean animations = !Boolean.getBoolean("riddle.fast");
    private static final int WIDTH = 64;
    private static final int INNER = WIDTH - 4;
    private static String previousNotice = "";

    private static String indent() {
        return " ".repeat(Math.max(0, (columns - WIDTH) / 2));
    }

    public static void sleep(long milliseconds) {
        if (!animations) return;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void clear() {
        previousNotice = "";
        if (ansi) System.out.print("\033[0m\033[2J\033[H");
        else System.out.println();

        System.out.print("\n".repeat(TOP + 3));
        System.out.flush();
    }

    public static void top() {
        System.out.println(indent() + Colors.paint("+" + "=".repeat(WIDTH - 2) + "+", Colors.CYAN));
    }

    public static void divider() {
        System.out.println(indent() + Colors.paint("+" + "-".repeat(WIDTH - 2) + "+", Colors.CYAN));
    }

    public static void center(String text) {
        for (String part : wrap(text)) {
            int space = Math.max(0, INNER - part.length());
            System.out.println(indent() + Colors.paint("| ", Colors.CYAN) + " ".repeat(space / 2) + Colors.paint(part, Colors.BOLD + Colors.YELLOW) + " ".repeat(space - space / 2) + Colors.paint(" |", Colors.CYAN));
        }
    }

    public static void line(String text) {
        line(text, Colors.WHITE);
    }

    public static void line(String text, String color) {
        for (String part : wrap(text)) {
            System.out.println(indent() + Colors.paint("| ", Colors.CYAN) + Colors.paint(part, color) + " ".repeat(Math.max(0, INNER - part.length())) + Colors.paint(" |", Colors.CYAN));
        }
    }

    public static void typeLine(String text, int delay) {
        for (String part : wrap(text)) {
            System.out.print(indent() + Colors.paint("| ", Colors.CYAN));
            if (ansi) System.out.print(Colors.BOLD + Colors.PURPLE);
            for (char c : part.toCharArray()) {
                System.out.print(c);
                System.out.flush();
                sleep(delay);
            }
            if (ansi) System.out.print(Colors.RESET);
            System.out.println(" ".repeat(Math.max(0, INNER - part.length())) + Colors.paint(" |", Colors.CYAN));
        }
    }

    public static void message(String text) {
        message(text, Colors.CYAN);
    }

    public static void message(String text, String color) {
        for (String part : wrap(text)) {
            System.out.println(indent() + Colors.paint("  >> " + part, color));
        }
    }

    public static void prompt(String text) {
        System.out.print(indent() + "  " + Colors.paint(text, Colors.BOLD + Colors.YELLOW));
        System.out.flush();
    }

    public static void celebrate() {
        message("\u2713 CORRECT!", Colors.BOLD + Colors.GREEN);
        sleep(450);
    }

    public static String progressBar(int current, int total) {
        int filled = current * 20 / total;
        return "[" + "#".repeat(filled) + "-".repeat(20 - filled) + "] " + current + "/" + total;
    }

    public static void live(int seconds, int limit, Player player, String notice, String noticeColor) {
        String timerColor = seconds <= 10 ? Colors.RED : seconds <= 20 ? Colors.YELLOW : Colors.GREEN;
        String timer = String.format(Locale.ROOT, "Time %02d:%02d", seconds / 60, seconds % 60);
        int filled = Math.max(0, Math.min(10, (seconds * 10 + limit - 1) / limit));
        String header = Colors.paint(timer + " [" + "#".repeat(filled) + "-".repeat(10 - filled) + "]", timerColor)
                + "  " + Colors.paint(player.getHearts(), Colors.RED)
                + Colors.paint("  Score: " + player.getScore(), Colors.YELLOW);
        if (ansi) {


            System.out.print("\0337\033[" + (TOP + 1) + ";1H\033[2K" + indent() + header + "\033[" + (TOP + 2) + ";1H\033[2K" + indent() + Colors.paint(notice, noticeColor) + "\0338");
            System.out.flush();
        } else if (!notice.equals(previousNotice)) {

            System.out.println();
            message(timer + " | Lives: " + player.getLives() + " | Score: " + player.getScore());
            message(notice);
        }
        previousNotice = notice;
    }

    private static List<String> wrap(String text) {
        List<String> result = new ArrayList<>();
        String rest = text;
        while (rest.length() > INNER) {
            int cut = rest.lastIndexOf(' ', INNER);
            if (cut <= 0) cut = INNER;
            result.add(rest.substring(0, cut));
            rest = rest.substring(cut).stripLeading();
        }
        result.add(rest);
        return result;
    }
}
