package practice.recipebase.interpreter;


import org.junit.jupiter.api.Test;
import practice.recipebase.TokenType;
import practice.recipebase.exceptions.WrongTokenTypeException;
import practice.recipebase.model.Requirement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class IngredientRequirementMakerTest {
    private IngredientRequirementMaker createMaker(String name, String unit, Double amount, Set<String> states) {
        IngredientRequirementMaker maker = new IngredientRequirementMaker();
        maker.setValue(new Token(name, TokenType.OTHER));
        maker.setAmount(amount);
        maker.setUnit(unit);
        for(String state : states)
            maker.setValue(new Token(state, TokenType.STATE));
        return maker;
    }

    @Test
    public void testConstructor() {
        IngredientRequirementMaker maker = new IngredientRequirementMaker();
        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testSetValueName() throws WrongTokenTypeException {
        IngredientRequirementMaker maker = new IngredientRequirementMaker();
        maker.setValue(new Token("name", TokenType.OTHER));
        assertEquals("name", maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testSetValueUnit() throws WrongTokenTypeException {
        IngredientRequirementMaker maker = new IngredientRequirementMaker();
        maker.setValue(new Token("unit", TokenType.UNIT));
        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertEquals("unit", maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testSetValueAmount() throws WrongTokenTypeException {
        IngredientRequirementMaker maker = new IngredientRequirementMaker();
        maker.setValue(new Token("1", TokenType.QUANTITY));
        assertNull(maker.getName());
        assertEquals(1d, maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().isEmpty());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testSetValueState() throws WrongTokenTypeException {
        IngredientRequirementMaker maker = new IngredientRequirementMaker();
        maker.setValue(new Token("state1", TokenType.STATE));
        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().contains("state1"));
        assertEquals(1, maker.getStates().size());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());


        maker.setValue(new Token("state2", TokenType.STATE));
        assertNull(maker.getName());
        assertNull(maker.getAmount());
        assertNull(maker.getUnit());
        assertTrue(maker.getStates().contains("state1"));
        assertTrue(maker.getStates().contains("state2"));
        assertEquals(2, maker.getStates().size());
        assertTrue(maker.getAlternativeIngredients().isEmpty());
        assertTrue(maker.getAlternativeMeasurements().isEmpty());
    }
/*
    @Test
    public void testSetValueWrongTokenTypeExceptionOperand() {
        IngredientRequirementMaker maker = new IngredientRequirementMaker();
        String expected = "The TokenType OPERAND should not occur in IngredientRequirementMaker.";
        Token inputToken = new Token(" ", TokenType.OPERAND);

        WrongTokenTypeException ex = assertThrows(
                WrongTokenTypeException.class, () -> {maker.setValue(inputToken);}
        );
        assertEquals(expected, ex.getMessage());
    }

    @Test
    public void testSetValueWrongTokenTypeExceptionAlternative() {
        IngredientRequirementMaker maker = new IngredientRequirementMaker();
        String expected = "The TokenType OPERAND should not occur in IngredientRequirementMaker.";
        Token inputToken = new Token("or", TokenType.ALTERNATIVE);

        WrongTokenTypeException ex = assertThrows(
                WrongTokenTypeException.class, () -> {maker.setValue(inputToken);}
        );
        assertEquals(expected, ex.getMessage());
    }
*/
    @Test
    public void testMergeNoOverlapAmountUnit() {
        IngredientRequirementMaker hasUnit = this.createMaker(null, "tsp", null, new HashSet<>());
        IngredientRequirementMaker hasAmount = this.createMaker(null, null, 1d, new HashSet<>());

        IngredientRequirementMaker merged = hasAmount.merge(hasUnit);
        assertNull(merged.getName());
        assertEquals(1d, merged.getAmount());
        assertEquals("tsp", merged.getUnit());
        assertTrue(merged.getStates().isEmpty());
        assertTrue(merged.getAlternativeIngredients().isEmpty());
        assertTrue(merged.getAlternativeMeasurements().isEmpty());

        IngredientRequirementMaker reversedMerge = hasUnit.merge(hasAmount);
        assertNull(reversedMerge.getName());
        assertEquals(1d, reversedMerge.getAmount());
        assertEquals("tsp", reversedMerge.getUnit());
        assertTrue(reversedMerge.getStates().isEmpty());
        assertTrue(reversedMerge.getAlternativeIngredients().isEmpty());
        assertTrue(reversedMerge.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testMergeNoOverlapAmountUnitReversed() {
        IngredientRequirementMaker hasUnit = this.createMaker(null, "tsp", null, new HashSet<>());
        IngredientRequirementMaker hasAmount = this.createMaker(null, null, 1d, new HashSet<>());

        IngredientRequirementMaker reversedMerge = hasUnit.merge(hasAmount);
        assertNull(reversedMerge.getName());
        assertEquals(1d, reversedMerge.getAmount());
        assertEquals("tsp", reversedMerge.getUnit());
        assertTrue(reversedMerge.getStates().isEmpty());
        assertTrue(reversedMerge.getAlternativeIngredients().isEmpty());
        assertTrue(reversedMerge.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testMergeNoOverlapNameAmount() {
        IngredientRequirementMaker hasName = this.createMaker("sugar", null, null, new HashSet<>());
        IngredientRequirementMaker hasAmount = this.createMaker(null, null, 1d, new HashSet<>());

        IngredientRequirementMaker merged = hasAmount.merge(hasName);
        assertEquals("sugar", merged.getName());
        assertEquals(1d, merged.getAmount());
        assertNull(merged.getUnit());
        assertTrue(merged.getStates().isEmpty());
        assertTrue(merged.getAlternativeIngredients().isEmpty());
        assertTrue(merged.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testMergeNoOverlapNameAmountReversed() {
        IngredientRequirementMaker hasName = this.createMaker("sugar", null, null, new HashSet<>());
        IngredientRequirementMaker hasAmount = this.createMaker(null, null, 1d, new HashSet<>());

        IngredientRequirementMaker reversedMerge = hasName.merge(hasAmount);
        assertEquals("sugar", reversedMerge.getName());
        assertEquals(1d, reversedMerge.getAmount());
        assertNull(reversedMerge.getUnit());
        assertTrue(reversedMerge.getStates().isEmpty());
        assertTrue(reversedMerge.getAlternativeIngredients().isEmpty());
        assertTrue(reversedMerge.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testMergeNoOverlapNameAmountUnit() {
        IngredientRequirementMaker hasUnitAndAmount = this.createMaker(null, "tsp", 1d, new HashSet<>());
        IngredientRequirementMaker hasName = this.createMaker("sugar", null, null, new HashSet<>());

        IngredientRequirementMaker merged = hasName.merge(hasUnitAndAmount);
        assertEquals("sugar", merged.getName());
        assertEquals(1d, merged.getAmount());
        assertEquals("tsp", merged.getUnit());
        assertTrue(merged.getStates().isEmpty());
        assertTrue(merged.getAlternativeIngredients().isEmpty());
        assertTrue(merged.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testMergeNoOverlapNameAmountUnitReversed() {
        IngredientRequirementMaker hasUnitAndAmount = this.createMaker(null, "tsp", 1d, new HashSet<>());
        IngredientRequirementMaker hasName = this.createMaker("sugar", null, null, new HashSet<>());

        IngredientRequirementMaker reversedMerge = hasUnitAndAmount.merge(hasName);
        assertEquals("sugar", reversedMerge.getName());
        assertEquals(1d, reversedMerge.getAmount());
        assertEquals("tsp", reversedMerge.getUnit());
        assertTrue(reversedMerge.getStates().isEmpty());
        assertTrue(reversedMerge.getAlternativeIngredients().isEmpty());
        assertTrue(reversedMerge.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testMergeNameOverlap() {
        IngredientRequirementMaker req1 = this.createMaker("sugar", "tsp", 1d, new HashSet<>());
        IngredientRequirementMaker req2 = this.createMaker("honey", "tsp", 0.5d, new HashSet<>());

        IngredientRequirementMaker overlap = req1.merge(req2);
        assertEquals("sugar", overlap.getName());
        assertEquals(1d, overlap.getAmount());
        assertEquals("tsp", overlap.getUnit());
        assertTrue(overlap.getStates().isEmpty());
        assertTrue(overlap.getAlternativeIngredients().contains(new AlternativeIngredient(
                "honey", "tsp", 0.5d, new HashSet<>(), new ArrayList<>()
        )));
        assertEquals(1, overlap.getAlternativeIngredients().size());
        assertTrue(overlap.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testMergeNameOverlapReversed() {
        IngredientRequirementMaker req1 = this.createMaker("sugar", "tsp", 1d, new HashSet<>());
        IngredientRequirementMaker req2 = this.createMaker("honey", "tsp", 0.5d, new HashSet<>());

        IngredientRequirementMaker overlap = req2.merge(req1);
        assertEquals("honey", overlap.getName());
        assertEquals(0.5d, overlap.getAmount());
        assertEquals("tsp", overlap.getUnit());
        assertTrue(overlap.getStates().isEmpty());
        assertTrue(overlap.getAlternativeIngredients().contains(new AlternativeIngredient(
                "sugar", "tsp", 1d, new HashSet<>(), new ArrayList<>()
        )));
        assertEquals(1, overlap.getAlternativeIngredients().size());
        assertTrue(overlap.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testMergeNameWithStatesOverlap() {
        HashSet<String> stateSet = new HashSet<>();
        stateSet.add("toasted");
        stateSet.add("ground");
        IngredientRequirementMaker req1 = this.createMaker("sesame seed", "tsp", 1d, stateSet);
        IngredientRequirementMaker req2 = this.createMaker("black sesame seed", "tsp", 1d, stateSet);

        IngredientRequirementMaker overlap = req1.merge(req2);
        assertEquals("sesame seed", overlap.getName());
        assertEquals(1d, overlap.getAmount());
        assertEquals("tsp", overlap.getUnit());
        assertTrue(overlap.getStates().contains("toasted"));
        assertTrue(overlap.getStates().contains("ground"));
        assertEquals(2, overlap.getStates().size());
        assertTrue(overlap.getAlternativeIngredients().contains(new AlternativeIngredient(
                "black sesame seed", "tsp", 1d, stateSet, new ArrayList<>()
        )));
        assertEquals(1, overlap.getAlternativeIngredients().size());
        assertTrue(overlap.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testMergeNameWithStatesOverlapReversed() {
        HashSet<String> stateSet = new HashSet<>();
        stateSet.add("toasted");
        stateSet.add("ground");
        IngredientRequirementMaker req1 = this.createMaker("sesame seed", "tsp", 1d, stateSet);
        IngredientRequirementMaker req2 = this.createMaker("black sesame seed", "tsp", 1d, stateSet);

        IngredientRequirementMaker overlap = req2.merge(req1);
        assertEquals("black sesame seed", overlap.getName());
        assertEquals(1d, overlap.getAmount());
        assertEquals("tsp", overlap.getUnit());
        assertTrue(overlap.getStates().contains("toasted"));
        assertTrue(overlap.getStates().contains("ground"));
        assertEquals(2, overlap.getStates().size());
        assertTrue(overlap.getAlternativeIngredients().contains(new AlternativeIngredient(
                "sesame seed", "tsp", 1d, stateSet, new ArrayList<>()
        )));
        assertEquals(1, overlap.getAlternativeIngredients().size());
        assertTrue(overlap.getAlternativeMeasurements().isEmpty());
    }

    @Test
    public void testMergeAmountOverlap() {
        IngredientRequirementMaker req1 = this.createMaker(null, "tsp", 1d, new HashSet<>());
        IngredientRequirementMaker req2 = this.createMaker("honey", "tsp", 0.5d, new HashSet<>());

        IngredientRequirementMaker overlap = req1.merge(req2);
        assertNull(overlap.getName());
        assertEquals(1d, overlap.getAmount());
        assertEquals("tsp", overlap.getUnit());
        assertTrue(overlap.getStates().isEmpty());
        assertTrue(overlap.getAlternativeMeasurements().contains(new Measurement("tsp", 0.5d)));
        assertEquals(1, overlap.getAlternativeMeasurements().size());
        assertTrue(overlap.getAlternativeIngredients().isEmpty());
    }

    @Test
    public void testMergeAmountOverlapReversed() {
        IngredientRequirementMaker req1 = this.createMaker(null, "tsp", 1d, new HashSet<>());
        IngredientRequirementMaker req2 = this.createMaker("honey", "tsp", 0.5d, new HashSet<>());

        IngredientRequirementMaker overlap = req2.merge(req1);
        assertEquals("honey", overlap.getName());
        assertEquals(0.5d, overlap.getAmount());
        assertEquals("tsp", overlap.getUnit());
        assertTrue(overlap.getStates().isEmpty());
        assertTrue(overlap.getAlternativeMeasurements().contains(new Measurement("tsp", 1d)));
        assertEquals(1, overlap.getAlternativeMeasurements().size());
        assertTrue(overlap.getAlternativeIngredients().isEmpty());
    }

    @Test
    public void testToAlternativeIngredient() {
        IngredientRequirementMaker maker = this.createMaker("flour", "tbsp", 3d, new HashSet<>());

        AlternativeIngredient expected = new AlternativeIngredient(
                "flour", "tbsp", 3d, new HashSet<>(), new ArrayList<>()
        );
        assertEquals(expected, maker.toAlternativeIngredient());
    }

    @Test
    public void testAsAlternativeIngredient() {
        IngredientRequirementMaker maker = this.createMaker("flour", "tbsp", 3d, new HashSet<>());
        IngredientRequirementMaker actual = maker.asAlternativeIngredient();

        assertNull(actual.getName());
        assertNull(actual.getAmount());
        assertNull(actual.getUnit());
        assertTrue(actual.getStates().isEmpty());
        assertTrue(actual.getAlternativeMeasurements().isEmpty());
        assertEquals(1, actual.getAlternativeIngredients().size());

        AlternativeIngredient actualAsAlt = actual.getAlternativeIngredients().getFirst();
        AlternativeIngredient expectedAlt = new AlternativeIngredient(
                "flour", "tbsp", 3d, new HashSet<>(), new ArrayList<>()
        );
        assertEquals(expectedAlt, actualAsAlt);
    }

    @Test
    public void testGetRequirementsSimple() {
        IngredientRequirementMaker maker = this.createMaker("sugar", "tsp", 0.5d, new HashSet<>());
        List<Requirement> expected = maker.getRequirements();
        assertEquals(1, expected.size());

        Requirement sugar = expected.getFirst();
        assertNull(sugar.getId());
        assertEquals("tsp", sugar.getUnit());
        assertEquals(0.5d, sugar.getAmount());
        assertTrue(sugar.getStates().isEmpty());
        assertEquals("sugar", sugar.getIngredient().getName());
    }

    @Test
    public void testGetRequirementsAlternativeMeasurements() {
        IngredientRequirementMaker req1 = this.createMaker("sugar", "tsp", 0.5d, new HashSet<>());
        IngredientRequirementMaker req2 = this.createMaker(null, "tsp", 1d, new HashSet<>());
        List<Requirement> expected = req1.merge(req2).getRequirements();
        assertEquals(2, expected.size());

        Requirement firstMeasurement = expected.getFirst();
        assertNull(firstMeasurement.getId());
        assertEquals("tsp", firstMeasurement.getUnit());
        assertEquals(0.5d, firstMeasurement.getAmount());
        assertTrue(firstMeasurement.getStates().isEmpty());
        assertEquals("sugar", firstMeasurement.getIngredient().getName());


        Requirement secondMeasurement = expected.get(1);
        assertNull(secondMeasurement.getId());
        assertEquals("tsp", secondMeasurement.getUnit());
        assertEquals(1d, secondMeasurement.getAmount());
        assertTrue(secondMeasurement.getStates().isEmpty());
        assertEquals("sugar", secondMeasurement.getIngredient().getName());
    }

    @Test
    public void testGetRequirementsAlternativeIngredientWithMeasurements() {
        IngredientRequirementMaker req1 = this.createMaker("sugar", "tsp", 0.5d, new HashSet<>());
        IngredientRequirementMaker req2 = this.createMaker("honey", "tsp", 1d, new HashSet<>());
        List<Requirement> expected = req1.merge(req2).getRequirements();
        assertEquals(2, expected.size());

        Requirement firstMeasurement = expected.getFirst();
        assertNull(firstMeasurement.getId());
        assertEquals("tsp", firstMeasurement.getUnit());
        assertEquals(0.5d, firstMeasurement.getAmount());
        assertTrue(firstMeasurement.getStates().isEmpty());
        assertEquals("sugar", firstMeasurement.getIngredient().getName());


        Requirement secondMeasurement = expected.get(1);
        assertNull(secondMeasurement.getId());
        assertEquals("tsp", secondMeasurement.getUnit());
        assertEquals(1d, secondMeasurement.getAmount());
        assertTrue(secondMeasurement.getStates().isEmpty());
        assertEquals("honey", secondMeasurement.getIngredient().getName());
    }

    @Test
    public void testGetRequirementsAlternativeIngredientWithoutMeasurements() {
        IngredientRequirementMaker req1 = this.createMaker("sugar", "tsp", 0.5d, new HashSet<>());
        IngredientRequirementMaker req2 = this.createMaker("honey", null, null, new HashSet<>());
        List<Requirement> expected = req1.merge(req2).getRequirements();
        assertEquals(2, expected.size());

        Requirement firstMeasurement = expected.getFirst();
        assertNull(firstMeasurement.getId());
        assertEquals("tsp", firstMeasurement.getUnit());
        assertEquals(0.5d, firstMeasurement.getAmount());
        assertTrue(firstMeasurement.getStates().isEmpty());
        assertEquals("sugar", firstMeasurement.getIngredient().getName());


        Requirement secondMeasurement = expected.get(1);
        assertNull(secondMeasurement.getId());
        assertEquals("tsp", secondMeasurement.getUnit());
        assertEquals(0.5d, secondMeasurement.getAmount());
        assertTrue(secondMeasurement.getStates().isEmpty());
        assertEquals("honey", secondMeasurement.getIngredient().getName());
    }

    @Test
    public void testGetRequirementsNoAmount() {
        IngredientRequirementMaker maker = this.createMaker("sugar", "sprinkle", null, new HashSet<>());
        List<Requirement> expected = maker.getRequirements();
        assertEquals(1, expected.size());

        Requirement firstMeasurement = expected.getFirst();
        assertNull(firstMeasurement.getId());
        assertEquals("some", firstMeasurement.getUnit());
        assertEquals(1d, firstMeasurement.getAmount());
        assertTrue(firstMeasurement.getStates().isEmpty());
        assertEquals("sugar", firstMeasurement.getIngredient().getName());
    }
}
