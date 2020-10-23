package cn.cherish.learn.dockerdemo.web;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hongwen.chw@antfin.com
 * @date 2019/6/20 2:22 PM
 */
@RestController
@RequestMapping("/")
public class IndexController {

    private static final String VERSION = "v1.2";

    @RequestMapping(path = {"", "/", "index"})
    public String index(){
        InetAddress addr = null;
        String ip = null;
        String hostName = null;
        try {
            addr = InetAddress.getLocalHost();
            ip= addr.getHostAddress(); //获取本机ip
            hostName= addr.getHostName(); //获取本机计算机名称
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        String env = System.getProperty("env");

        String sTmp = "hk-aks-test, env=%s, hostName=%s, ip=%s, version=%s \r\n";
        return String.format(sTmp, env, hostName, ip, VERSION) + SysMsg.property();
    }

}
