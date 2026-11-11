package Riad;

import java.util.Locale;

public abstract class Level implements Scorable {
    private final int number;
    private final String question;
    private final String[] answers;

    protected Level(int number, String question, String[] answers) {
        this.number = number;
        this.question = question;
        this.answers = answers.clone();
    }

    public int getNumber() {
        return number;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answers[0];
    }

    public abstract String getTitle();

    public boolean isBoss() {
        return false;
    }

    public boolean isCorrect(String input) {
        String given = normalize(input);
        for (String answer : answers) {
            if (normalize(answer).equals(given)) {
                return true;
            }
        }
        return false;
    }

    public String getHint() {
        String answer = answers[0];
        return "Starts with '"
                + Character.toUpperCase(answer.charAt(0))
                + "' and has "
                + answer.replace(" ", "").length()
                + " letter(s), excluding spaces.";
    }

    private static String normalize(String text) {
        String result = text.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9 ]", "")
                .trim()
                .replaceAll("\\s+", " ");
        if (result.startsWith("the ")) {
            result = result.substring(4);
        } else if (result.startsWith("an ")) {
            result = result.substring(3);
        } else if (result.startsWith("a ")) {
            result = result.substring(2);
        }
        return result;
    }

    public abstract int getPoints();
}
