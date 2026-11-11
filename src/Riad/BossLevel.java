package Riad;

public class BossLevel extends RiddleLevel {

    public BossLevel(int number, String question, String[] answers) {
        super(number, question, answers);
    }

    @Override
    public String getTitle() {
        return "BOSS CHALLENGE";
    }

    @Override
    public int getPoints() {
        return 30;
    }

    @Override
    public boolean isBoss() {
        return true;
    }
}