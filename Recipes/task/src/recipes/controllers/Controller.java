package recipes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.repositories.RecipeService;
import recipes.ResponseRecipeNew;
import recipes.entities.Recipe;
import recipes.repositories.UserService;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/recipe")
@Validated
public class Controller {
    @Autowired
    private UserService userService;
    @Autowired
    private RecipeService recipeService;

    /**
     * Добавляет новый рецепт
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public ResponseRecipeNew addRecipe(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Recipe recipe) {
        recipe.setDate(LocalDateTime.now());
        recipe.setUser(userService.findUserByEmail(userDetails.getUsername()).get(0));
        Recipe newRecipe = recipeService.save(recipe);

        return new ResponseRecipeNew(newRecipe.getId());
    }

    /**
     * Редактирует рецепт
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody Recipe recipe) {
        Optional<Recipe> findResult = recipeService.findById(id);
        findResult.ifPresentOrElse(
            (value) -> {
                if(!Objects.equals(value.getUser().getEmail(), userDetails.getUsername())){
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
                recipe.setId(value.getId());
                recipe.setDate(LocalDateTime.now());
                recipe.setUser(userService.findUserByEmail(userDetails.getUsername()).get(0));
                recipeService.save(recipe);
            },
            () -> {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        );
    }

    /**
     * Удаляет рецепт
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        Optional<Recipe> findResult = recipeService.findById(id);
        findResult.ifPresentOrElse(
            (value) -> {
                if(!Objects.equals(value.getUser().getEmail(), userDetails.getUsername())){
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
                recipeService.delete(value.getId());
            },
            () -> {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleException(ConstraintViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Возвращает сохраненный рецепт
     */
    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        Optional<Recipe> findResult = recipeService.findById(id);

        return findResult.orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    /**
     * Поиск рецептов по категории или имени, можно заполнить только одно поле
     */
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Recipe> searchRecipe(
        @RequestParam(required = false, defaultValue = "") String category,
        @RequestParam(required = false, defaultValue = "") String name
    ) {
        if (
            (!category.isEmpty() && !name.isEmpty()) ||
            (category.isEmpty() && name.isEmpty())
        ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return recipeService.search(category, name);
    }
}
