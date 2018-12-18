package com.jinlailiao;

import com.jinlailiao.config.SensitiveWord;
import com.jinlailiao.utils.SensitiveWordUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Component
public class LoadSensitiveWords implements ApplicationRunner {

    @Resource
    private SensitiveWord sensitiveWord;
    @Override
    public void run(ApplicationArguments var1) throws Exception{
        Set<String> sensitiveWordSet = new HashSet<>();
//        System.out.println("==================");
//        System.out.println(sensitiveWord.getSensitiveWords());
        for (String s : sensitiveWord.getSensitiveWords().split(",")) {
            sensitiveWordSet.add(s);
        }

        SensitiveWordUtil.init(sensitiveWordSet);
    }
}