package edu.ban7.chatbotmsnmsii2527.service;

import edu.ban7.chatbotmsnmsii2527.dao.QuestionDao;
import edu.ban7.chatbotmsnmsii2527.dao.RecipeDao;
import edu.ban7.chatbotmsnmsii2527.dao.TagDao;
import edu.ban7.chatbotmsnmsii2527.dto.QuestionDto;
import edu.ban7.chatbotmsnmsii2527.model.AppUser;
import edu.ban7.chatbotmsnmsii2527.model.Question;
import edu.ban7.chatbotmsnmsii2527.model.Recipe;
import edu.ban7.chatbotmsnmsii2527.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    protected final TagDao tagDao;
    protected final RecipeDao recipeDao;
    protected final AiService aiService;
    private final QuestionDao questionDao;

    public String ask(QuestionDto questionDto, AppUser appUser) {
        List<Integer> includeTagIds = questionDto.includeTagIds();
        List<Integer> excludeTagIds = questionDto.excludeTagIds();

        if (includeTagIds == null) {
            includeTagIds = new ArrayList<>();
        }

        if (excludeTagIds == null) {
            excludeTagIds = new ArrayList<>();
        }

        List<Tag> includeTags = tagDao.findAllById(includeTagIds);
        List<Tag> excludeTags = tagDao.findAllById(excludeTagIds);

        List<Recipe> allRecipes = recipeDao.findAll();
        List<Recipe> filteredRecipes = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            if (recipeMatches(recipe, includeTags, excludeTags)) {
                filteredRecipes.add(recipe);
            }
        }

        String answer = "";
        if (filteredRecipes.isEmpty()) {
            answer = "Pas de recette correspondant aux tags sélectionnées";
        } else {
            for (Recipe selectedRecipe : filteredRecipes) {
                selectedRecipe.setCounter(selectedRecipe.getCounter() + 1);
                recipeDao.save(selectedRecipe);
            }

            answer = aiService.askGemini(questionDto.content(), filteredRecipes);
        }

        Question question = new Question();
        question.setContent(questionDto.content());
        question.setAppUser(appUser);
        question.setIncludeTags(includeTags);
        question.setExcludeTags(excludeTags);
        questionDao.save(question);

        return answer;
    }

    private boolean recipeMatches(Recipe recipe, List<Tag> includeTags, List<Tag> excludeTags) {
        List<Integer> recipeTagIds = new ArrayList<>();

        for (Tag recipeTag : recipe.getTags()) {
            recipeTagIds.add(recipeTag.getId());
        }

        for (Tag includeTag : includeTags) {
            if (!recipeTagIds.contains(includeTag.getId())) {
                return false;
            }
        }

        for (Tag excludeTag : excludeTags) {
            if (recipeTagIds.contains(excludeTag.getId())) {
                return false;
            }
        }

        return true;
    }
}
