package practice.recipebase.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadedRecipeWrapper {
    private String title;
    private String description;
    private String userID;
    private List<String> ingredients;
    private List<String> instructions;
}
