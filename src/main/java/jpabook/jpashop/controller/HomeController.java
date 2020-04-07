package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    //  Logger logger = LoggerFactory.getLogger()

    @RequestMapping("/")
    public String home(){
        // 타임리프의 홈을 불러와라
        log.info("home Controller");
        return "home";
    }
}
