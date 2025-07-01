package practice.recipebase.interpreter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TypeConstants {
    static String[] STATES = {
            // hyphens are used here, not minus
            // add colors?
            "bite‐size",
            "(hard‐|soft‐)?boiled",
            "bone(less|‐in)",
            "can(ned|s)?",
            "chopped",
            "chunks",
            "coarse(ly)?",
            "cold",
            "crosswise",
            "crumbled",
            "cube(d|s)?",
            "cut",
            "dice(d|s)?",
            "drained",
            "dr(y|ied)?",
            "(well‐)?fermented",
            "fine(ly)?",
            "fresh(ly)?",
            "frozen",
            "grated",
            "ground",
            "hal(ved|f)",
            "hot",
            "knob",
            "large",
            "matchsticks",
            "medium",
            "minced",
            "julienned",
            "optional",
            "peeled",
            "pieces",
            "pounded",
            "regular",
            "rough(ly)?",
            "scored",
            "(de)?seed(ded|less)",
            "shelled",
            "sieved",
            "skinless",
            "skin‐on",
            "slice(d|s)?",
            "small",
            "smashed",
            "toasted",
            "thawed",
            "thick(ly)?",
            "thin(ly)?",
            "warm",
            "washed",
    };

    static String[] UNITS = {
            // replace long version with short form
            "tbsp", "tablespoon(s)?",
            "tsp", "teaspoon(s)?",
            "(m)?l", "(mili)?lit(er|re)s?", "cc",
            "cup(s)?", "quart(s)?",
            "oz", "ounce(s)?",
            "lb", "pound(s)?",
            "(k)?g", "(kilo)?gram(s)?",
            "inch(es)?",
            "a", // replace with 1
            "an", // replace with 1
            // replace with some
            /*"drizzle",
            "handful",
            "portion(s)?",
            "block(s)?",
            "spoonful(s)?",
            "sprinkle(s)?",*/
            "some",
    };
    static String[] PREPOSITION = {
            "in",
            "into",
            "with",
            "of",
            "across",
            "on",
            "and"
    };
    static String[] OPERANDS = {
            "-",
            "/",
            ".",
            "x",
            ",",
            " ",
            "+"
    };
    static String[] ALTERNATIVE = {
            "or",
            "use",
            "substitute",
            "plus"
    };
    static String[] OPEN_BRACKETS = {
            "(",
            "[",
            "{"
    };
    static String[] CLOSE_BRACKET = {
            ")",
            "]",
            "}"
    };

    private TypeConstants() {
    }


    public static boolean isWordIn(String tokenPattern, String[] wordArray) {
        for(String state : wordArray) {
            Matcher matcher = Pattern.compile("\\b" + state + "\\b").matcher(tokenPattern);
            if(matcher.find()) {
                return true;
            }
        }
        return false;
    }


    public static boolean isPatternIn(String tokenPattern, String[] constantArray) {
        for(String word : constantArray) {
            if(tokenPattern.equals(word)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isQuantity(String tokenPattern) {
        Pattern regexNumber = Pattern.compile("[0-9]+");
        return regexNumber.matcher(tokenPattern).find();
    }
}
