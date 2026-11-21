package tyut.selab.a2.pipes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tyut.selab.a2.pipes.exception.TestException;

@RestController
public class TestController {

    @GetMapping("/string")
    public String returnString(){
        return "666!";
    }

    @GetMapping("/int")
    public int returnInt(){
        return 666;
    }

    @GetMapping("/exception")
    public String throwException(){
        throw new RuntimeException("I am a exception");
    }

    @GetMapping("/testException")
    public String throwTestException(){
        throw new TestException("I am a test exception");
    }
}
