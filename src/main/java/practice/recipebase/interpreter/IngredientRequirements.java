package practice.recipebase.interpreter;

import lombok.Getter;
import lombok.Setter;
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
    @Setter
    private String unit;
    @Setter
    private Double amount;
    private final Set<String> states;
    private final List<Measurement> alternativeMeasurements;
    private final List<AlternativeIngredient> alternativeIngredients;

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
            case TokenType.QUANTITY -> this.amount = value != null ? Double.parseDouble(value) : null;
        }
    }

    public IngredientRequirements merge(IngredientRequirements ingredient) {
        // an overlap denotes that the member variable of this object and the other object are not null
        if(this.name != null && ingredient.name != null) {
            // if the overlap is in the name, it has to be an alternative ingredient
            this.alternativeIngredients.add(ingredient.toAlternativeIngredient());
        } else if(this.amount != null && ingredient.amount != null) {
            // if there is an overlap in amount, it is additional information
            // it should not be possible for unit overlap to occur, unless an amount exists
            // since the parse should always merge the unit with its respective amount first
            this.alternativeMeasurements.add(new Measurement(ingredient.unit, ingredient.amount));
            this.alternativeMeasurements.addAll(ingredient.getAlternativeMeasurements());
        } else {
            // no overlap, so merge
            this.name = this.name != null ? this.name : ingredient.name;
            this.unit = this.unit != null ? this.unit : ingredient.unit;
            this.amount = this.amount != null ? this.amount : ingredient.amount;
            this.states.addAll(ingredient.states);
            this.alternativeIngredients.addAll(ingredient.alternativeIngredients);
            this.alternativeMeasurements.addAll(ingredient.alternativeMeasurements);
        }
        return this;
    }

    public AlternativeIngredient toAlternativeIngredient() {
        return new AlternativeIngredient(this.name, this.unit, this.amount, this.states, this.alternativeMeasurements);
    }

    public IngredientRequirements asAlternativeIngredient() {
        // the alternative ingredients in the final IngredientRequirements object should not have alternative
        // ingredients themselves
        IngredientRequirements altIngredient = new IngredientRequirements();
        altIngredient.addAllAlternativeIngredients(this.getAlternativeIngredients());
        this.alternativeIngredients.clear();
        altIngredient.addAlternativeIngredient(this.toAlternativeIngredient());
        return altIngredient;
    }

    public void addAlternativeIngredient(AlternativeIngredient alternativeIngredient) {
        this.alternativeIngredients.add(alternativeIngredient);
    }

    public void addAllAlternativeIngredients(List<AlternativeIngredient> alternativeIngredients) {
        this.alternativeIngredients.addAll(alternativeIngredients);
    }

    public List<Requirement> getRequirements() {
        List<Requirement> requirements = new ArrayList<>();
        requirements.add(new Requirement(this.states, this.unit, this.amount, new Ingredient(this.name)));

        requirements.addAll(this.createRequirementsWithAltMeasurements(this.states, this.name));

        for(AlternativeIngredient altIngredient : this.alternativeIngredients) {
            // ERROR: What about shared states?
            String unit = altIngredient.unit() != null ? altIngredient.unit() : this.unit;
            Double amount = altIngredient.amount() != null ? altIngredient.amount() : this.amount;
            if(altIngredient.amount() == null) {
                requirements.addAll(
                        this.createRequirementsWithAltMeasurements(altIngredient.states(), altIngredient.name()))
                ;
            }
            requirements.add(new Requirement(
                    altIngredient.states(), unit, amount, new Ingredient(altIngredient.name()))
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
}
