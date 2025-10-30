package org.example.springboot_lectures.lecture01_basics.controller;

import org.example.springboot_lectures.lecture01_basics.model.Message;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TestController {
    private final Message message1 = new Message("order", UUID.randomUUID().toString(), "order");
    private final Message message2 = new Message("buy", UUID.randomUUID().toString(), "buy");
    private final Message message3 = new Message("sell", UUID.randomUUID().toString(), "sell");

    private List<Message> messages = new ArrayList<>(List.of(message1, message2, message3));


    @GetMapping("/message")
    public List<Message> message() {
        return messages;
    }

    @PostMapping("/newMessage")
    public String newMessage(@RequestBody Message message) {
        messages.add(message);
        return "Message created: " + message;
    }

    @DeleteMapping("/deleteMessage/{id}")
    public String deleteMessage(@PathVariable String id) {
        Message deletedMessage = messages.stream().filter(message -> message.id().equals(id)).findFirst().orElse(null);
        messages = messages.stream().filter(message -> !message.id().equals(id)).toList();
        return "Message deleted: " + deletedMessage;
    }

    // @GetMapping
    // public String sayHello() {
    //     return "Hello World";
    // }
    //
    // @GetMapping("/2")
    // public String sayHello2() {
    //     return "Hello World2";
    // }
    //
    // @GetMapping("/{id}")
    // public String sayHello3(@PathVariable String id) {
    //     System.out.println("test" + id);
    //     return "Given id is " + id;
    // }
    //
    // @GetMapping("/search")
    // public String printQuery(@RequestParam String query) {
    //     return "Your query is " + query;
    // }
    //
    // @PostMapping
    // public String printValue(@RequestBody Student student) {
    //     return "Hello " + student.name();
    // }

}
