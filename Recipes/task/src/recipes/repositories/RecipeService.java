package recipes.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.entities.Recipe;
import recipes.repositories.RecipeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }

    public void delete(Long id) {
        recipeRepository.deleteById(id);
    }

    public List<Recipe> search(String category, String name) {
        if (!category.isEmpty()) {
            return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        }

        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }
}
