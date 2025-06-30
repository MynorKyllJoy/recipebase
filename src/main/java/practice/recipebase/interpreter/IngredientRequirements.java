package practice.recipebase.interpreter;

import lombok.Getter;
import practice.recipebase.TokenType;
import practice.recipebase.model.Ingredient;
import practice.recipebase.model.Requirement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class IngredientRequirements {
    private String name;
    private String unit;
    private Double amount;
    private final Set<String> states;
    private final List<Measurement> alternativeMeasurements;
    private final List<IngredientRequirements> alternativeIngredients;

    public IngredientRequirements() {
        this.name = null;
        this.unit = null;
        this.amount = null;
        this.states = new HashSet<>();
        this.alternativeMeasurements = new ArrayList<>();
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

    public IngredientRequirements merge(IngredientRequirements ingredient) {
        if(this.hasOverlap(this.name, ingredient.name)) {
            // if the overlap is in the name, it has to be an alternative ingredien
            this.alternativeIngredients.add(ingredient);
        } else if(this.hasOverlap(this.amount, ingredient.amount)) {
            // if there is an overlap in amount, it is additional information
            // it should not be possible for unit overlap to occur, unless an amount exists
            // since the parse should always merge the unit with its respective amount first
            this.alternativeMeasurements.add(new Measurement(ingredient.unit, ingredient.amount));
            this.alternativeMeasurements.addAll(ingredient.getAlternativeMeasurements());
        } else {
            // no overlap, so merge
            this.name = this.getValueOrNull(this.name, ingredient.name);
            this.unit = this.getValueOrNull(this.unit, ingredient.unit);
            this.amount = this.getValueOrNull(this.amount, ingredient.amount);
            this.states.addAll(ingredient.states);
            this.alternativeIngredients.addAll(ingredient.alternativeIngredients);
            this.alternativeMeasurements.addAll(ingredient.alternativeMeasurements);
        }
        return this;
    }

    public void addAlternativeIngredient(IngredientRequirements alternativeIngredient) {
        this.alternativeIngredients.add(alternativeIngredient);
    }

    public void addAllAlternativeIngredients(List<IngredientRequirements> alternativeIngredients) {
        this.alternativeIngredients.addAll(alternativeIngredients);
    }

    public void removeAltIngredients() {
        this.alternativeIngredients.clear();
    }

    public List<Requirement> getRequirements() {
        List<Requirement> requirements = new ArrayList<>();
        requirements.add(new Requirement(this.states, this.unit, this.amount, new Ingredient(this.name)));

        requirements.addAll(this.createRequirementsWithAltMeasurements(this.states, this.name));

        for(IngredientRequirements altIngredient : this.alternativeIngredients) {
            // ERROR: What about shared states?
            if(altIngredient.getAmount() == null) {
                altIngredient.amount = this.amount;
                altIngredient.unit = this.unit;
                requirements.addAll(
                        this.createRequirementsWithAltMeasurements(altIngredient.states, altIngredient.name))
                ;
            }
            requirements.add(new Requirement(
                    altIngredient.getStates(), altIngredient.getUnit(), altIngredient.getAmount(),
                    new Ingredient(altIngredient.getName()))
            );
        }

        return requirements;
    }

    private List<Requirement> createRequirementsWithAltMeasurements(Set<String> states, String ingredientName) {
        List<Requirement> requirements = new ArrayList<>();
        for(Measurement measurement : this.alternativeMeasurements) {
            requirements.add(new Requirement(
                    states, measurement.unit(), measurement.amount(), new Ingredient(ingredientName)
            ));
        }
        return requirements;
    }

    private Double setAmount(String value) {
        if(value == null)
            return null;
        return Double.parseDouble(value);
    }

    private boolean hasOverlap(String thisValue, String otherValue) {
        return thisValue != null && otherValue != null;
    }

    private boolean hasOverlap(Double thisValue, Double otherValue) {
        return thisValue != null && otherValue != null;
    }

    private String getValueOrNull(String thisValue, String otherValue) {
        if(thisValue != null) {
            return thisValue;
        }
        return otherValue;
    }

    private Double getValueOrNull(Double thisValue, Double otherValue) {
        if(thisValue != null) {
            return thisValue;
        }
        return otherValue;
    }
}
