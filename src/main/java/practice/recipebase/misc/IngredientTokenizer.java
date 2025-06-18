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
 The tokenizer tries to put each word/symbol in a category, so the parser can properly sort the information
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
    private final String[] BINDING_PREPOSITION = {"in", "into", "with", "of", "across"};
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
            if(
                    prevToken.type() == currToken.type()
                    && currToken.type() != TokenType.OPEN_BRACKET
                    && currToken.type() != TokenType.CLOSE_BRACKET
            ) {
                /*
                 If the current token has the same type as the previous token, they are combined since they are most
                 likely one term separated by a space like "garlic glove".
                 The exception is open or close brackets. These should never be combined.
                 Since gramma exists, two operands should never follow each other.
                */
                reversedTokens.pop();
                reversedTokens.add(new Token(
                        prevToken.value() + " " + currToken.value(),
                        currToken.type()
                ));
            } else if(currToken.type() == TokenType.BINDING_PREPOSITION) {
                Token nextToken = this.createToken(patterns.get(index+1));
                if(prevToken.type() == TokenType.QUANTITY) {
                    // skip preposition if it is supposed to be something like 1/4 of an onion
                } else if(prevToken.type() == nextToken.type()) {
                    // combine previous and next token since the preposition is only a fill word:
                    // E.g.: "chicken breast with skin"
                    reversedTokens.pop();
                    reversedTokens.add(new Token(
                            prevToken.value() + " " + currToken.value() + " " + patterns.get(index + 1),
                            prevToken.type()
                    ));
                    index += 1;
                } else {
                    // combine preposition with previous word since the next token should not be combined with the
                    // previous token: 1 teaspoon of salt
                    reversedTokens.pop();
                    reversedTokens.add(new Token(
                            prevToken.value() + " " + currToken.value(),
                            prevToken.type()
                    ));
                }
            } else if(
                    currToken.type() == TokenType.OPERAND || prevToken.type() == TokenType.OPERAND
                    || currToken.type() == TokenType.OPEN_BRACKET || prevToken.type() == TokenType.OPEN_BRACKET
                    || currToken.type() == TokenType.CLOSE_BRACKET || prevToken.type() == TokenType.CLOSE_BRACKET
            ) {
                // if prev or curr token is an operand or bracket, there is no need to add a space as an operand
                reversedTokens.add(currToken);
            } else {
                /*
                 a space will be added as operand between two tokens of different types as long as one of the tokens
                 is not itself an operand or bracket
                */
                reversedTokens.add(new Token(" ", TokenType.OPERAND));
                reversedTokens.add(currToken);
            }
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

    public List<Token> toList() {
        return this.tokens.stream().toList();
    }

    private Token createToken(String pattern) {
        TokenType type = TokenType.OTHER;

        if(this.isState(pattern)) {
            type = TokenType.STATE;
        } else if(this.isUnit(pattern)) {
            type = TokenType.UNIT;
        } else if(this.isQuantity(pattern)) {
            type = TokenType.QUANTITY;
        } else if(this.isOperand(pattern)) {
            type = TokenType.OPERAND;
        } else if(this.isOpenBracket(pattern)) {
            type = TokenType.OPEN_BRACKET;
        } else if(this.isCloseBracket(pattern)) {
            type = TokenType.CLOSE_BRACKET;
        } else if(this.isBindingPreposition(pattern)) {
            type = TokenType.BINDING_PREPOSITION;
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
            Matcher matcher = Pattern.compile("\\b" + unit + "( of)?\\b").matcher(tokenPattern);
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

    private boolean isOperand(String tokenPattern) {
        for(String operand : this.OPERANDS) {
            if(tokenPattern.equals(operand)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOpenBracket(String tokenPattern) {
        for(String bracket : this.OPEN_BRACKETS) {
            if(tokenPattern.equals(bracket)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCloseBracket(String tokenPattern) {
        for(String bracket : this.CLOSE_BRACKET) {
            if(tokenPattern.equals(bracket)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBindingPreposition(String tokenPattern) {
        for(String word : this.BINDING_PREPOSITION) {
            if(tokenPattern.equals(word)) {
                return true;
            }
        }
        return false;
    }
}
