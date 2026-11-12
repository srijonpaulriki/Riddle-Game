package Akash;
import Srijon.*;
import Riad.*;

public class Colors {
    static final String RESET = "\033[0m";
    static final String BOLD = "\033[1m";
    public static final String RED = "\033[91m";
    public static final String GREEN = "\033[92m";
    public static final String YELLOW = "\033[93m";
    public static final String PURPLE = "\033[95m";
    public static final String CYAN = "\033[96m";
    static final String WHITE = "\033[97m";

    static String paint(String text, String color) {
        return ConsoleUI.ansi ? color + text + RESET : text;
    }
}
