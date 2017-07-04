package cn.com.flaginfo.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommUtil {

    /**
     * 将字符串转换为list集合
     * @param content 要转换的字符串
     * @param regex   分隔符
     * @return List<String>
     */
    public static List<String> str2List(String content, String regex) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        Set<String> result = new HashSet<>();
        if (content.contains(regex)) {
            String[] tmp = content.split(regex);
            if (tmp != null && tmp.length > 0) {
                for (String str : tmp) {
                    result.add(str);
                }
            }
        } else {
            result.add(content);
        }
        return result == null ? null : new ArrayList<>(result);
    }

    /**
     * 用Map中的参数替换str中的${}占位符
     * @param str
     * @param param
     * @return
     */
    public static String matcherStr(String str, Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        String regex = "\\$\\{(.+?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String name = matcher.group(1);// 键名
            String value = param.get(name);// 键值
            if (value == null) {
                value = "";
            }
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 生成UUID
     *
     * @return
     */
    public static String getUuid() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        uuidStr = uuidStr.replace("-", "");
        return uuidStr;
    }

    /**
     * 返回json数据
     *
     * @param response
     * @param str
     */
    public static void writeJson(HttpServletResponse response, String str) {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.write(str);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 随机生成 num位数字字符数组
     *
     * @param num
     * @return
     */
    public static String generateRandomArray(int num) {
        String chars = "0123456789";
        String str = "";
        for (int i = 0; i < num; i++) {
            int rand = (int) (Math.random() * 10);
            str += chars.charAt(rand);
        }
        return str;
    }
}
