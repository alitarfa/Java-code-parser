package ast.code.parser.javacodeparser.controllers;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parser-project")
@AllArgsConstructor
public class ParseController {

    private SimpMessageSendingOperations messagingTemplate;

    @GetMapping
    public void parseProject() {
        messagingTemplate.convertAndSend("/topic/progress", "Hello From Web socket!!!");
    }
}
