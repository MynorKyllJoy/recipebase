package practice.recipebase.misc;

import practice.recipebase.TokenType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 In many recipes the ingredient list often contains additional information like
 how the ingredient is supposed to be chopped or potential substitutes.
 There is little consistency on how these additional information is written down within a single website
 let alone between different websites.
 This tokenizer and its respective parser attempt to extract the necessary information.
 The tokenizer tries to put each word/symbol in a category, so the parser can properly sort the information.
 The output of the tokenizer should have a similar structure to a math term.
 No operands at the beginning, end or following each other. No two other tokens types without an operand inbetween
 unless they are brackets. Brackets work like they would in math.
*/

public class IngredientTokenizer {
    private Stack<Token> tokens;
    // temporary list of constants, create a database later or a separate static constant class
    private final String[] STATES = {
            "julienned", "smashed", "minced", "peeled", "sieved", "regular",
            "washed", "drained", "shelled", "grated", "pounded", "chopped", "scored",
            "thawed", "frozen", "(well_)?fermented", "(hard_|soft_)?boiled", "hal(ved|f)", "(de)?seed(ded|less)",
            "bone(less|_in)", "cube(d|s)?", "dr(y|ied)?", "can(ned|s)?", "dice(d|s)?", "slice(d|s)?",
            "thick(ly)?", "thin(ly)?", "rough(ly)?", "fine(ly)?", "crosswise", "optional", "cut",
            "bite_size", "matchsticks", "chunks", "small", "medium", "large", "pieces", "knob",
    };
    private final String[] UNITS = {
            "tsp", "teaspoon(s)?", "tbsp", "tablespoon(s)?",
            "(m)?l", "(mili)?lit(er|re)s?", "cc", "cup(s)?", "quart(s)?",
            "ounce(s)?", "oz", "pound(s)?", "lb", "(kilo)?gram(s)?", "(k)?g",
            "inch(es)?", "spoonful(s)?", "sprinkle(s)?", "drizzle", "a", "an", "some", "more"
    };
    private final String[] PREPOSITION = {"in", "into", "with", "of", "across", "on"};
    private final String[] OPERANDS = {"-", "/", ".", "x", ",", " ", "or", "use", "substitute", "plus", "and"};
    private final String[] OPEN_BRACKETS = {"(", "[", "{"};
    private final String[] CLOSE_BRACKET = {")", "]", "}"};

    public IngredientTokenizer(String ingredientData) {
        // Split the ingredient information into whole words, numbers and special characters
        List<String> patterns = this.getPatterns(ingredientData);
        Stack<Token> reversedTokens = new Stack<>();
        reversedTokens.add(this.createToken(patterns.getFirst()));
        int index = 1;

        while(index < patterns.size()) {
            Token prevToken = reversedTokens.getLast();
            Token currToken = this.createToken(patterns.get(index));
            String combinedTerm = prevToken.value() + " " + currToken.value();

            if(this.areOneTerm(prevToken.type(), currToken.type())) {
                /*
                 if they aren't brackets, combine two tokens of same type into one: "garlic" "clove" -> "garlic clove"
                 cannot happen with quantities due to regex, 1 1/2 in Unicode could become a problem though
                */
                reversedTokens.pop();
                currToken = new Token(combinedTerm, currToken.type());
            } else if(currToken.type() == TokenType.PREPOSITION) {
                Token nextToken = this.createToken(patterns.get(index+1));
                if(prevToken.type() == nextToken.type()) {
                    /*
                     if prev and next token have same type, combine them since preposition is only a fill word:
                     "pork belly" "with" "skin" -> "pork belly with skin"
                    */
                    reversedTokens.pop();
                    combinedTerm += " " + nextToken.value();
                    currToken = new Token(combinedTerm, prevToken.type());
                    index += 1;
                } else if(prevToken.type() != TokenType.QUANTITY) {
                    /*
                     if it is not a quantity and prev token has different type than next
                     preposition belongs to previous token "substitute" "with" "1" -> "substitute with" "1"
                    */
                    reversedTokens.pop();
                    currToken = new Token(combinedTerm, prevToken.type());
                } else {
                    // if prev token is quantity, replace with space: "2" "of" -> "2" " "
                    currToken = new Token(" ", TokenType.OPERAND);
                }
            } else if(this.needsPrecedingSpaceOperand(prevToken.type(), currToken.type())) {
                // if there is no operand between tokens of different types, add space as operand
                reversedTokens.add(new Token(" ", TokenType.OPERAND));
            }
            reversedTokens.add(currToken);
            index += 1;
        }

        this.tokens = new Stack<>();
        this.tokens.addAll(reversedTokens.reversed());
    }


    public Token next() {
        try {
            return this.tokens.pop();
        } catch (EmptyStackException ex) {
            return new Token("", TokenType.END);
        }
    }


    public Token peek() {
        try {
            return this.tokens.getLast();
        } catch (NoSuchElementException ex) {
            return new Token("", TokenType.END);
        }
    }

    public List<Token> toList() {
        return this.tokens.stream().toList();
    }


    public int size() {
        return this.tokens.size();
    }


    private List<String> getPatterns(String ingredientData) {
        ingredientData = ingredientData.toLowerCase();
        Pattern pattern = Pattern.compile("[A-Za-z_]+|[0-9]+|\\S");
        Matcher matcher = pattern.matcher(ingredientData);
        List<String> ingredients = new ArrayList<>();

        while (matcher.find()) {
            ingredients.add(matcher.group());
        }

        return ingredients;
    }


    private boolean needsPrecedingSpaceOperand(TokenType prevType, TokenType currType) {
        // if prev and curr token do not have an operand between them, then curr token needs preceding operand
        return currType != TokenType.OPERAND && prevType != TokenType.OPERAND
                && currType != TokenType.OPEN_BRACKET && prevType != TokenType.OPEN_BRACKET
                && currType != TokenType.CLOSE_BRACKET && prevType != TokenType.CLOSE_BRACKET;
    }


    private boolean areOneTerm(TokenType prevType, TokenType currType) {
        // two tokens with same type (except brackets) are most likely one word
        return prevType == currType && currType != TokenType.OPEN_BRACKET && currType != TokenType.CLOSE_BRACKET;
    }


    private Token createToken(String pattern) {
        TokenType type = TokenType.OTHER;

        if(this.isState(pattern)) {
            type = TokenType.STATE;
        } else if(this.isUnit(pattern)) {
            type = TokenType.UNIT;
        } else if(this.isQuantity(pattern)) {
            type = TokenType.QUANTITY;
        } else if(this.isIn(pattern, this.OPERANDS)) {
            type = TokenType.OPERAND;
        } else if(this.isIn(pattern, this.OPEN_BRACKETS)) {
            type = TokenType.OPEN_BRACKET;
        } else if(this.isIn(pattern, this.CLOSE_BRACKET)) {
            type = TokenType.CLOSE_BRACKET;
        } else if(this.isIn(pattern, this.PREPOSITION)) {
            type = TokenType.PREPOSITION;
        }
        return new Token(pattern, type);
    }


    private boolean isState(String tokenPattern) {
        for(String state : this.STATES) {
            Matcher matcher = Pattern.compile("\\b" + state + "\\b").matcher(tokenPattern);
            if(matcher.find()) {
                return true;
            }
        }
        return false;
    }


    private boolean isUnit(String tokenPattern) {
        for(String unit : this.UNITS) {
            Matcher matcher = Pattern.compile("\\b" + unit + "\\b").matcher(tokenPattern);
            if(matcher.find()) {
                return true;
            }
        }
        // There is a problem with quotation marks in regex, so this is currently done separately
        return tokenPattern.equals("\"");
    }


    private boolean isQuantity(String tokenPattern) {
        Pattern regexNumber = Pattern.compile("[0-9]+");
        return regexNumber.matcher(tokenPattern).find();
    }

    private boolean isIn(String tokenPattern, String[] constantArray) {
        for(String word : constantArray) {
            if(tokenPattern.equals(word)) {
                return true;
            }
        }
        return false;
    }
}
