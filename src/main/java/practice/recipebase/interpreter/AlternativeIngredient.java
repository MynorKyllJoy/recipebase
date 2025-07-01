package practice.recipebase.interpreter;

import java.util.List;
import java.util.Set;

public record AlternativeIngredient(
        String name,
        String unit,
        Double amount,
        Set<String> states,
        List<Measurement> alternativeMeasurements
) {}
