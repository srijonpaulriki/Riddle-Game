package Riad;

class RiddleLevel extends Level {
    public RiddleLevel(int number, String question, String[] answers) {
        super(number, question, answers);
    }
    @Override
    public String getTitle() { return "RIDDLE"; }
    @Override
    public int getPoints() { return 10; }
}

