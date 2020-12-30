import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class SpringBootServer {

    //Maps to the root URL. Default method is GET
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String helloWorld() {
        return "Hello World";
    }

    //Maps to the root URL, for POST methods
    //Uses the request body in the input variable
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> sayHello(@RequestBody String input) {
        String responseBody = "Hello, " + input;
        return ResponseEntity.ok(responseBody);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootServer.class, args);
    }
}
