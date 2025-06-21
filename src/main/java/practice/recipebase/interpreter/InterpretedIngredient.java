package practice.recipebase.interpreter;

import lombok.Getter;
import practice.recipebase.TokenType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class InterpretedIngredient {
    private String name;
    private String unit;
    private Float amount;
    private final Set<String> states;
    private final List<InterpretedIngredient> additionalInfo;
    private final List<InterpretedIngredient> alternativeIngredients;

    public InterpretedIngredient() {
        this.name = null;
        this.unit = null;
        this.amount = null;
        this.states = new HashSet<>();
        this.additionalInfo = new ArrayList<>();
        this.alternativeIngredients = new ArrayList<>();
    }

    public void setValue(Token token) {
        TokenType type = token.type();
        String value = token.value();

        switch (type) {
            case TokenType.STATE -> this.states.add(value);
            case TokenType.UNIT -> this.unit = value;
            case TokenType.OTHER -> this.name = value;
            case TokenType.QUANTITY -> this.amount = this.setAmount(token.value());
        }
    }

    public InterpretedIngredient merge(InterpretedIngredient ingredient) {
        if(this.hasOverlap(this.name, ingredient.name)) {
            // if the overlap is in the name, it has to be an alternative ingredient
            this.alternativeIngredients.add(ingredient);
        } else if(this.hasOverlap(this.amount, ingredient.amount)) {
            // if there is an overlap in amount, it is additional information
            // it should not be possible for unit overlap to occur, unless an amount exists
            // since the parse should always merge the unit with its respective amount first
            this.additionalInfo.add(ingredient);
        } else {
            // no overlap, so merge
            this.name = this.getValueOrNull(this.name, ingredient.name);
            this.unit = this.getValueOrNull(this.unit, ingredient.unit);
            this.amount = this.getValueOrNull(this.amount, ingredient.amount);
            this.states.addAll(ingredient.states);
            this.alternativeIngredients.addAll(ingredient.alternativeIngredients);
            this.additionalInfo.addAll(ingredient.additionalInfo);
        }
        return this;
    }

    public InterpretedIngredient addAlternativeIngredient(InterpretedIngredient alternativeIngredient) {
        this.alternativeIngredients.add(alternativeIngredient);
        return this;
    }

    private Float setAmount(String value) {
        if(value == null)
            return null;
        return Float.parseFloat(value);
    }

    private boolean hasOverlap(String thisValue, String otherValue) {
        return thisValue != null && otherValue != null;
    }

    private boolean hasOverlap(Float thisValue, Float otherValue) {
        return thisValue != null && otherValue != null;
    }

    private String getValueOrNull(String thisValue, String otherValue) {
        if(thisValue != null) {
            return thisValue;
        }
        return otherValue;
    }

    private Float getValueOrNull(Float thisValue, Float otherValue) {
        if(thisValue != null) {
            return thisValue;
        }
        return otherValue;
    }
}
