package cn.cherish.learn.dockerdemo.web;

import cn.cherish.learn.dockerdemo.SnowFlake;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hongwen.chw@antfin.com
 * @date 2019/6/20 2:22 PM
 */
@RestController
@RequestMapping("/")
public class TestController {


    @RequestMapping(path = "/exit0")
    public void exit0(){
        System.exit(0);
    }

    private static volatile AtomicBoolean LOOP = new AtomicBoolean(false);
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    @RequestMapping(path = "/loop")
    public long loop(@RequestParam(defaultValue = "true") Boolean loop){

        LOOP.set(loop);


        SnowFlake snowFlake = new SnowFlake(random.nextInt(0, 10), random.nextInt(0, 100));
        long loopCount = 0;
        while (LOOP.get()) {
            // LOOP
            long nextId = snowFlake.nextId();
            double area = Math.PI * nextId * nextId;

            ++loopCount;
        }

        return loopCount;
    }


}
