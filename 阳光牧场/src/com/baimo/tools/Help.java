package com.baimo.tools;


import com.baimo.pojo.User;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 这类没啥用，主要是想用来给大号放虫
 */
public class Help {
    public static void main(String[] args) {
        Tools tools=new Tools();
        long timeS = new Date().getTime();
        String account = "";//账号
        String passWord = "";//密码
        User user = new User(account, passWord);
        user = tools.getUserAllParameter(user);//初始化属性
        while (true) {
            tools.fangchong(user);
            String dateStr = new SimpleDateFormat("HH:mm:ss").format(new Date());
            long timeD = new Date().getTime();
            double time = (timeD - timeS) / 60000.00;
            String format = new DecimalFormat("#.0").format(time);
            System.err.println("本次用时：" + format + "分\t结束时间：" + dateStr);
//            tools.oneKeyPicks(user, null);
            try {
                Thread.currentThread().sleep(1000 * 60 * 5);//暂停五分钟
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
