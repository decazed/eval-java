package edu.ban7.chatbotmsnmsii2527.controller;

import edu.ban7.chatbotmsnmsii2527.dao.QuestionDao;
import edu.ban7.chatbotmsnmsii2527.model.Question;
import edu.ban7.chatbotmsnmsii2527.security.AppUserDetails;
import edu.ban7.chatbotmsnmsii2527.security.IsAdmin;
import edu.ban7.chatbotmsnmsii2527.security.IsUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    protected final QuestionDao questionDao;

    @GetMapping("/questions/me")
    @IsUser
    public List<Question> myQuestions(
            @AuthenticationPrincipal AppUserDetails userDetails) {

        return questionDao.findByAppUserId(userDetails.getUser().getId());
    }

    @GetMapping("/questions")
    @IsAdmin
    public List<Question> allQuestions() {
        return questionDao.findAll();
    }

}
