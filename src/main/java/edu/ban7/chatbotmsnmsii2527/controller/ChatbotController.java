package edu.ban7.chatbotmsnmsii2527.controller;

import edu.ban7.chatbotmsnmsii2527.dto.QuestionDto;
import edu.ban7.chatbotmsnmsii2527.security.AppUserDetails;
import edu.ban7.chatbotmsnmsii2527.security.IsUser;
import edu.ban7.chatbotmsnmsii2527.service.AiService;
import edu.ban7.chatbotmsnmsii2527.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChatbotController {

    protected final QuestionService questionService;

    @PostMapping("/ask")
    @IsUser
    public ResponseEntity<String> ask(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @RequestBody QuestionDto questionDto) {

        System.out.println(userDetails.getUser().getPseudo());

        return new ResponseEntity<>(
                questionService.ask(questionDto, userDetails.getUser()),
                HttpStatus.OK);
    }

}
