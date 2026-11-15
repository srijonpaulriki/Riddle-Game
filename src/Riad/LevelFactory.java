package Riad;

import java.util.ArrayList;
import java.util.List;

// zoro
public class LevelFactory {
    private static final String[][] DATA = {
            {"What has keys but cannot open a single lock?", "keyboard|piano"},
            {"What has hands but cannot clap?", "clock|watch"},
            {"What gets wetter the more it dries?", "towel"},
            {"What has a head and a tail but no body?", "coin"},
            {"What can you catch but never throw?", "cold"},
            {"What has many teeth but cannot bite?", "comb"},
            {"What goes up but never comes down?", "age"},
            {"What has a neck but no head?", "bottle"},
            {"What runs but never walks?", "river|water"},
            {"I speak without a mouth and hear without ears. I have no body, but I come alive with wind. What am I?", "echo"},
            {"What has one eye but cannot see?", "needle"},
            {"What has a ring but no finger?", "phone|telephone"},
            {"The more of me you take, the more you leave behind. What am I?", "footsteps|footstep|steps"},
            {"What can travel around the world while staying in one corner?", "stamp"},
            {"What has words but never speaks?", "book"},
            {"What is full of holes but still holds water?", "sponge"},
            {"What begins with T, ends with T, and is full of T?", "teapot"},
            {"Which months of the year have 28 days?", "all|all of them|every month|all months"},
            {"What is always ahead of you but can never be seen?", "future"},
            {"Forward I am heavy, but backward I am not. What am I?", "ton"},
            {"What can fill a room but takes up no space?", "light"},
            {"What invention lets you look right through a wall?", "window"},
            {"What building has the most stories?", "library"},
            {"What walks on four legs in the morning, two at noon, and three in the evening?", "human|man|person|humans|people"},
            {"What occurs once in a minute, twice in a moment, but never in a thousand years?", "m|letter m"},
            {"What gets sharper the more you use it?", "brain|mind"},
            {"I am tall when I am young and short when I am old. What am I?", "candle"},
            {"What is easy to get into but hard to get out of?", "trouble"},
            {"What can you break without ever touching or dropping it?", "promise"},
            {"Which word becomes shorter when you add two letters to it?", "short"},
            {"I have 13 hearts but no other organs. What am I?", "deck of cards|cards|deck|playing cards"},
            {"What has a bottom at the top?", "legs|leg"},
            {"What goes through towns and over hills but never moves?", "road|street|path"},
            {"I am black when you buy me, red when you use me, and gray when you throw me away. What am I?", "charcoal|coal"},
            {"The more of me there is, the less you see. What am I?", "darkness|dark"},
            {"What has to be broken before you can use it?", "egg"},
            {"What can you hold in your right hand but never in your left?", "left hand|your left hand"},
            {"I follow you all day long, but when the night comes I am gone. What am I?", "shadow"},
            {"What belongs to you, but other people use it more than you do?", "name"},
            {"I have cities but no houses, forests but no trees, and water but no fish. What am I?", "map"},
            {"What has a thumb and four fingers but is not alive?", "glove"},
            {"What goes up and down the stairs without moving?", "carpet|rug"},
            {"What type of cheese is made backward?", "edam"},
            {"What is always coming but never arrives?", "tomorrow"},
            {"There is one word that is spelled wrong in every dictionary. What is it?", "wrong"},
            {"What letter comes next? O, T, T, F, F, S, S, E, ...", "n|letter n"},
            {"I am not alive, but I grow. I have no lungs, but I need air. Water kills me. What am I?", "fire"},
            {"What has legs but cannot walk?", "table|chair|bed|desk"},
            {"What disappears the moment you say its name?", "silence"},
            {"I am taken from a mine and shut in a wooden case, from which I am never released, yet nearly everyone uses me. What am I?", "pencil lead|pencil|lead|graphite"}
    };

    private LevelFactory() {
    }

    public static List<Level> createLevels() {
        List<Level> levels = new ArrayList<>();
        for (int i = 0; i < DATA.length; i++) {
            int number = i + 1;
            String question = DATA[i][0];
            String[] answers = DATA[i][1].split("\\|");
            if (number % 10 == 0) {
                levels.add(new BossLevel(number, question, answers));
            } else {
                levels.add(new RiddleLevel(number, question, answers));
            }
        }
        return levels;
    }
}
