package cn.cherish.learn.dockerdemo.web;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

/**
 * @author hongwen.chw@antfin.com
 * @date 2019/6/21 8:14 AM
 */
public class SysMsg {

    public static String property() {
        StringBuilder sb = new StringBuilder(500);
        Runtime r = Runtime.getRuntime();
        Properties props = System.getProperties();
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress();
            Map<String, String> map = System.getenv();
            String userName = map.get("USERNAME");// 获取用户名
            String computerName = map.get("COMPUTERNAME");// 获取计算机名
            String userDomain = map.get("USERDOMAIN");// 获取计算机域名
            sb.append("USERNAME:    ").append(userName).append("\r\n");
            sb.append("COMPUTER_NAME:    ").append(computerName).append("\r\n");
            sb.append("USER_DOMAIN:    ").append(userDomain).append("\r\n");
            sb.append("MACHINE_IP:    ").append(ip).append("\r\n");
            sb.append("HostName:    ").append(addr.getHostName()).append("\r\n");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        sb.append("JVM totalMemory:    " + r.totalMemory()).append("\r\n");
        sb.append("JVM freeMemory:    " + r.freeMemory()).append("\r\n");
        sb.append("JVM CPU availableProcessors:    " + r.availableProcessors()).append("\r\n");
        sb.append("Java java.version:    " + props.getProperty("java.version")).append("\r\n");
        sb.append("Java java.vendor:    " + props.getProperty("java.vendor")).append("\r\n");
        sb.append("Java java.vendor.url:    " + props.getProperty("java.vendor.url")).append("\r\n");
        sb.append("Java java.home:    " + props.getProperty("java.home")).append("\r\n");
        sb.append("Java java.vm.specification.version:    " + props.getProperty("java.vm.specification.version")).append("\r\n");
        sb.append("Java java.vm.specification.vendor:    " + props.getProperty("java.vm.specification.vendor")).append("\r\n");
        sb.append("Java java.vm.specification.name:    " + props.getProperty("java.vm.specification.name")).append("\r\n");
        sb.append("Java java.vm.version:    " + props.getProperty("java.vm.version")).append("\r\n");
        sb.append("Java java.vm.vendor:    " + props.getProperty("java.vm.vendor")).append("\r\n");
        sb.append("Java java.vm.name:    " + props.getProperty("java.vm.name")).append("\r\n");
        sb.append("Java java.specification.version:    " + props.getProperty("java.specification.version")).append("\r\n");
        sb.append("Java java.specification.vender:    " + props.getProperty("java.specification.vender")).append("\r\n");
        sb.append("Java java.specification.name:    " + props.getProperty("java.specification.name")).append("\r\n");
        sb.append("Java java.class.version:    " + props.getProperty("java.class.version")).append("\r\n");
        sb.append("Java java.class.path:    " + props.getProperty("java.class.path")).append("\r\n");
        sb.append("java.library.path:    " + props.getProperty("java.library.path")).append("\r\n");
        sb.append("java.io.tmpdir:    " + props.getProperty("java.io.tmpdir")).append("\r\n");
        sb.append("java.ext.dirs:    " + props.getProperty("java.ext.dirs")).append("\r\n");
        sb.append("os.name:    " + props.getProperty("os.name")).append("\r\n");
        sb.append("os.arch:    " + props.getProperty("os.arch")).append("\r\n");
        sb.append("os.version:    " + props.getProperty("os.version")).append("\r\n");
        sb.append("file.separator:    " + props.getProperty("file.separator")).append("\r\n");
        sb.append("path.separator:    " + props.getProperty("path.separator")).append("\r\n");
        sb.append("line.separator:    " + props.getProperty("line.separator")).append("\r\n");
        sb.append("user.name:    " + props.getProperty("user.name")).append("\r\n");
        sb.append("user.home:    " + props.getProperty("user.home")).append("\r\n");
        sb.append("user.dir:    " + props.getProperty("user.dir")).append("\r\n");

        return sb.toString();
    }

}

