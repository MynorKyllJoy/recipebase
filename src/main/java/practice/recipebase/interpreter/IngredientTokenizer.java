package practice.recipebase.interpreter;

import practice.recipebase.TokenType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 In many recipes the ingredient list often contains additional information like how cut the ingredient or substitutes.
 There is little consistency on how these additions is written down within a recipe let alone between websites.
 The tokenizer attempts to extract and categorize each word adn symbol.
 The output of the tokenizer should have a similar structure to a math term.
 No (alternative) operands at the beginning, end or following each other.
 No two other tokens types without an (alternative) operand in between unless they are brackets.
 Brackets work like they would in math.
*/

public class IngredientTokenizer {
    private Stack<Token> tokens;

    public IngredientTokenizer(String ingredientData) {
        // Split the ingredient information into whole words, numbers and special characters
        List<String> patterns = this.getPatterns(ingredientData);
        Stack<Token> reversedTokens = new Stack<>();
        reversedTokens.add(this.createToken(patterns.getFirst()));
        int index = 1;

        // TODO: Shorten the loop, its too long and too spaghetti
        while(index < patterns.size()) {
            Token prevToken = reversedTokens.getLast();
            Token currToken = this.createToken(patterns.get(index));
            StringBuilder combinedTerm = new StringBuilder(prevToken.value()).append(" ").append(currToken.value());

            if(this.areOneTerm(prevToken.type(), currToken.type())) {
                /*
                 if pattern aren't brackets, combine two tokens of same type into one: "garlic"+"clove"->"garlic clove"
                 cannot happen with quantities due to regex,
                 ERROR: 1 ½ and 1½ in Unicode could become a problem
                */
                reversedTokens.pop();
                currToken = new Token(combinedTerm.toString(), currToken.type());
            } else if(currToken.type() == TokenType.PREPOSITION) {
                Token nextToken = this.createToken(patterns.get(index+1));
                if(prevToken.type() == nextToken.type()) {
                    /*
                     if prev and next token have same type, combine them since preposition is only a fill word:
                     "pork belly" "with" "skin" -> "pork belly with skin"
                    */
                    reversedTokens.pop();
                    combinedTerm.append(" ").append(nextToken.value());
                    currToken = new Token(combinedTerm.toString(), prevToken.type());
                    index += 1;
                } else if(prevToken.type() == TokenType.STATE) {
                    /*
                     If a preposition preceded by a state is followed by a non-state token, this token most likely
                     belong to the state until the next state is reached, e.g.:
                     cut into 4 quarters, scored crosswise into 1 inch cubes, etc.
                    */
                    reversedTokens.pop();
                    while (nextToken.type() != currToken.type() && index < patterns.size()-1) {
                        nextToken = this.createToken(patterns.get(index+1));
                        combinedTerm.append(" ").append(nextToken.value());
                        index += 1;
                    }
                    currToken = new Token(combinedTerm.toString(), prevToken.type());
                } else if(prevToken.type() == TokenType.QUANTITY || prevToken.type() == TokenType.UNIT) {
                    // if prev token is quantity, replace with space: "1/2" "of" "an" "apple" -> "1/2" " " "an" "apple"
                    currToken = new Token(" ", TokenType.OPERAND);
                } else {
                    // skip preposition
                    currToken = nextToken;
                    index += 1;
                }
            } else if(this.needsPrecedingSpaceOperand(prevToken.type(), currToken.type())) {
                // if there is no operand between tokens of different types, add space as operand
                reversedTokens.add(new Token(" ", TokenType.OPERAND));
            }
            reversedTokens.add(currToken);
            index += 1;
        }
        /*
         We read from left to right, so the left most word is at the bottom and the right most is at the top.
         Since we want to process the tokens from left to right, we need to reverse the stack, since the Stack works
         with LIFO
         */
        this.tokens = new Stack<>();
        this.tokens.addAll(reversedTokens.reversed());
    }


    public Token next() {
        // removes the top token from the stack and returns it, or an END token if stack is empty
        try {
            return this.tokens.pop();
        } catch (EmptyStackException ex) {
            return new Token("", TokenType.END);
        }
    }


    public Token peek() {
        // returns top token of the stack, or an END token if stack is empty
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
        // the special character in [A-Za-z‐] is a hyphen, which looks nearly indistinguishable to a minus -
        Pattern pattern = Pattern.compile("[A-Za-z‐]+|[0-9]+|\\S");
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
                && currType != TokenType.ALTERNATIVE && prevType != TokenType.ALTERNATIVE
                && prevType != TokenType.OPEN_BRACKET && currType != TokenType.CLOSE_BRACKET;
    }


    private boolean areOneTerm(TokenType prevType, TokenType currType) {
        // two tokens with same type (except brackets) are one term
        return prevType == currType && currType != TokenType.OPEN_BRACKET && currType != TokenType.CLOSE_BRACKET;
    }


    private Token createToken(String pattern) {
        // checks if the pattern is in a constant and returns a token with the appropriate type
        TokenType type = TokenType.OTHER;

        if(TypeConstants.isWordIn(pattern, TypeConstants.STATES)) {
            type = TokenType.STATE;
        } else if(TypeConstants.isWordIn(pattern, TypeConstants.UNITS) || pattern.equals("\"")) {
            type = TokenType.UNIT;
        } else if(TypeConstants.isQuantity(pattern)) {
            type = TokenType.QUANTITY;
        } else if(TypeConstants.isPatternIn(pattern, TypeConstants.ALTERNATIVE)) {
            type = TokenType.ALTERNATIVE;
        } else if(TypeConstants.isPatternIn(pattern, TypeConstants.OPERANDS)) {
            type = TokenType.OPERAND;
        }  else if(TypeConstants.isPatternIn(pattern, TypeConstants.OPEN_BRACKETS)) {
            type = TokenType.OPEN_BRACKET;
        } else if(TypeConstants.isPatternIn(pattern, TypeConstants.CLOSE_BRACKET)) {
            type = TokenType.CLOSE_BRACKET;
        } else if(TypeConstants.isPatternIn(pattern, TypeConstants.PREPOSITION)) {
            type = TokenType.PREPOSITION;
        }
        return new Token(pattern, type);
    }
}
