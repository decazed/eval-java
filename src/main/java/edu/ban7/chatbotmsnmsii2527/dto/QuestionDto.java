package edu.ban7.chatbotmsnmsii2527.dto;

import java.util.List;

public record QuestionDto(String content, List<Integer> includeTagIds, List<Integer> excludeTagIds) {
}
