package com.baimo.tools;

import com.baimo.pojo.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 该类写了所有的功能，懒得写新方法所有干脆直接在这里面使用了
 */
public class Tools {

    //通过登录，添加sid、uid到user内
    public User getUserAllParameter(User user) {
        if (user.getAccount().length() != 0 || user.getPassWord().length() != 0) {
            URLTools urlTools = new URLTools();
            String url = LocationAll.LOGIN_POST;
            Map<String, String> map = new HashMap<String, String>();
            map.put("account", user.getAccount());
            map.put("password", user.getPassWord());
            String result = urlTools.getDocmentByPost(url, map);
            //获取sid
            String sid = result.split(";sid=")[1].split("'>")[0];
            user.setSid(sid);
            //获取uid（家园id）
            String uid = result.split("uid=")[3].split("&amp;")[0];
            user.setUid(uid);
            if (user.getSid() != null || user.getSid().length() != 0) {
                System.out.println("获取sid成功：" + user.getSid());
                if (user.getUid() != null || user.getUid().length() != 0) {
                    System.out.println("获取uid成功：" + user.getUid());
                    //获取openId
                    url = LocationAll.GAME8_USZ;
                    String param = LocationAll.U + user.getUid() + LocationAll.S + user.getSid() + LocationAll.Z + 8;
                    result = new URLTools().getDocmentByGep(url, param);
                    //通过openId=截取字符串
                    String openId = result.split("openId=")[1].split("&amp;")[0];
                    user.setOpenId(openId);
                    if (user.getOpenId() != null || user.getOpenId().length() != 0) {
                        System.out.println("获取openId成功：" + user.getOpenId());
                    } else {
                        System.err.println("获取openId失败！");
                    }
                } else {
                    System.err.println("获取uid失败！");
                }
            } else {
                System.err.println("获取sid失败！");
            }


        } else {
            System.err.println("请先登录！");
        }
        return user;
    }

    //获取朋友经验排名页面全部otherId
    public List<String> getFriendsOtherId(User user, int type) {
        if (user.getSid() != null || user.getSid().length() != 0) {
            System.out.println("开始拉取好友列表...");
            long timeS = new Date().getTime();
//            URLTools urlTools = new URLTools();
            String url = LocationAll.F_FRIENDS_EXP_OSVT_P;
            ;
            //其实三个都一样...只是排名顺序不同。
            switch (type) {
                case 0:
                    url = LocationAll.F_FRIENDS_EXP_OSVT_P;//获取农场好友
                    break;
                case 1:
                    url = LocationAll.R_FRIENDS_EXP_OSVT_P;//获取牧场好友
                    break;
                case 2:
                    url = LocationAll.S_FRIENDS_EXP_OSVT_P;//获取鱼塘好友
                    break;
                default:
                    url = LocationAll.F_FRIENDS_EXP_OSVT_P;//默认获取农场好友；
            }
            String parameter = "openId=" + user.getOpenId() + "&sid=" + user.getSid() + "&ver=0&unixtime=" + new Date().getTime() + "&pageNo=" + 1;
            String result = new URLTools().getDocmentByGep(url, parameter);
            System.err.println("正在读取第1页朋友列表中...");

            //通过otherId=截取字符串,没有截取到长度为1；
            String[] strarr = result.split("otherId=");
            List<String> otherIds = new ArrayList<String>();
            int page = 2;
            while (strarr.length != 1) {
                System.err.println("正在读取第" + page + "页朋友列表中...");
                for (int i = 1; i < strarr.length; i++) {
                    //再次截取字符串，最终实现获取otherId的目的
                    String[] strings = strarr[i].split("'>");
                    if (!strings[0].equals(user.getOpenId())) {//不保存自己的id
                        otherIds.add(strings[0]);
                    }
                }
                parameter = "openId=" + user.getOpenId() + "&sid=" + user.getSid() + "&ver=0&unixtime=" + new Date().getTime() + "&pageNo=" + page;
                result = new URLTools().getDocmentByGep(url, parameter);
                //多线程Start
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
////                        result = new URLTools().getDocmentByGep(url, parameter);
//                    }
//                }).start();
                //END
                strarr = result.split("otherId=");
                page++;
            }
            long timeE = new Date().getTime();
            int time = (int) (timeE - timeS) / 1000;
            System.err.println("拉取好友列表\tOK\t共用" + time + "秒\t共" + otherIds.size() + "位好友");
            return otherIds;
        } else {
            System.err.println("请先登录！！");
            return null;
        }
    }

    //获取单个账号(他人)所有土地fieldId/温室gHome/太空aether/同心盆concentrics，土地与同心盆的类型为TreeSet<String>
    public Map<String, Object> getFriendFieldId(User user, String otherId) {
        if (user.getSid() != null || user.getSid().length() != 0) {
            if (otherId != null || otherId.length() != 0) {
                String url = LocationAll.F_FRIEND_OSVTH;
                String parameter = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T + LocationAll.H + otherId;
                String result = new URLTools().getDocmentByGep(url, parameter);
                Set<String> fieldId = new TreeSet<String>();
                String emp2 = null;//获取普通土地的字符串，暂定为空
                //尝试获取温室id
                String gHome = null;
                String[] emp=null;
                if (result.indexOf("温室:已收割") != -1) {
                    System.err.println("该好友温室已收割。");
                }else {
                emp = result.split("温室:空坑");//未种植
                if (emp.length != 2) {//温室未种植
                    emp = result.split("温室:");//无温室
                    emp2 = emp[0];
                    if (emp.length != 1) {
                        gHome = emp[1].split("fieldId=")[1].split("&amp;")[0];//拿到温室id
                    } else {
                        System.err.println("该好友温室未建设！");
                    }
                } else {
                    System.err.println("该好友温室未种植。");
                }}
                //尝试获取太空舱id
                String aether = null;
                if (result.indexOf("太空舱:已收割") != -1) {
                    System.err.println("该好友太空舱已收割。");
                } else {
                    emp = result.split("太空舱:空坑");
                    if (emp.length != 2) {//太空舱未种植
                        emp = result.split("太空舱:");//无太空舱
                        if (emp.length != 1) {
                            if (emp2 == null || emp2.length() == 0) {
                                emp2 = emp[0];
                            }
                            aether = emp[1].split("fieldId=")[1].split("&amp;")[0];//拿到太空舱id
                        } else {
                            System.err.println("该好友太空舱未建设！");
                        }
                    } else {
                        System.err.println("该好友太空舱未种植。");
                    }
                }
                //尝试获取同心盆id
                Set<String> concentrics = null;//存放同心盆
                emp = result.split("同心花盆");
                if (emp.length == 2) {//同心盆是否建设
                    if (emp2 == null || emp2.length() == 0) {
                        emp2 = emp[0];
                    }
                    emp = emp[1].split("fieldId=");
                    if (emp.length != 1) {//同心盆是否种植
                        concentrics = new TreeSet<String>();
                        for (int i = 1; i < emp.length; i++) {
                            concentrics.add(emp[i].split("&amp;")[0]);//拿到同心盆id
                        }
                    } else {
                        System.err.println("该好友同心花盆未种植。");
                    }
                } else {
                    System.err.println("该好友同心花盆未建设。");
                }
                //获取普通土地
                if (emp2 == null || emp2.length() == 0) {
                    emp = result.split("fieldId=");
                } else {
                    emp = emp2.split("fieldId=");
                }
                for (int i = 1; i < emp.length; i++) {
                    fieldId.add(emp[i].split("'>")[0]);
                }
                //将所有土地返回
                Map<String, Object> map = new HashMap<String, Object>();
                if (fieldId == null || fieldId.size() == 0)
                    map.put("fieldId", null);
                else map.put("fieldId", fieldId);

                if (gHome == null || gHome.length() == 0)
                    map.put("gHome", null);
                else map.put("gHome", gHome);
                if (aether == null || aether.length() == 0)
                    map.put("aether", null);
                else map.put("aether", aether);
                if (concentrics == null || concentrics.size() == 0)
                    map.put("concentrics", null);
                else map.put("concentrics", concentrics);
                return map;
            } else {
                System.err.println("未获取到朋友id！");
            }
        } else {
            System.err.println("请先登录！");
        }
        return null;
    }

    //获取所有土地id，传入所有朋友的H值
    public Map<String, Map<String, Object>> getFieldIdsFieldId(User user, List<String> friends) {
        Map<String, Map<String, Object>> fieldIds = null;
        if (user != null) {
            if (user.getSid() != null || user.getSid().length() != 0) {
                if (friends == null || friends.size() == 0) {//如果没有朋友的值就获取
                    friends = getFriendsOtherId(user, 0);
                }

                fieldIds = new HashMap<String, Map<String, Object>>();
                for (int i = 0; i < friends.size(); i++) {
                    //存每一个otherId的土地。已区分土地。
                    System.out.println("分析好友" + (i + 1) + "的土地\t" + friends.get(i));
                    fieldIds.put(friends.get(i), getFriendFieldId(user, friends.get(i)));
                }

            } else {
                user = getUserAllParameter(user);
            }
        } else {
            System.out.println("请先登录！");
        }

        return fieldIds;
    }

    //普通土地单个偷菜
    public void pick(User user, String ohterId, Set<String> fieldId) {
        if (user.getSid() != null || user.getSid().length() != 0) {
            if ((fieldId != null && ohterId != null) || (fieldId.size() != 0 && ohterId.length() != 0)) {
                System.out.println("开始普通偷菜...");
                String url = LocationAll.F_PICK_OSTHVF;
                long timeStart = new Date().getTime();//开始计时
                //迭代偷菜
                for (Iterator iter = fieldId.iterator(); iter.hasNext(); ) {
                    String parameter = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.T + LocationAll.H + ohterId + LocationAll.V + 0 + LocationAll.F + iter.next();
                    new URLTools().getDocmentByGep(url, parameter);
                }
                long timeEnd = new Date().getTime();//结束计时
                int time = (int) (timeEnd - timeStart) / 1000;
                System.out.println("普通偷菜\tOK\t用时" + time + "秒");
            } else {
                System.err.println("未获取到土地id或ohterId！");
            }
        } else {
            System.err.println("请登录账号再进行操作！");
        }
    }

    //温室单个偷菜
    public void pickGHouse(User user, String ohterId, String gHome) {
        if (user.getSid() != null || user.getSid().length() != 0) {
            if ((ohterId != null && gHome != null) || (ohterId.length() != 0 && gHome.length() != 0)) {
                System.out.println("偷取温室...");
                String url = LocationAll.F_PICK_G_HOUSE_OSVTFH;
                String parameter = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.T + LocationAll.H + ohterId + LocationAll.V + 0 + LocationAll.F + gHome;
                new URLTools().getDocmentByGep(url, parameter);
                System.out.println("偷取温室\tOK");
            } else {
                System.err.println("未获取到温室id或ohterId！");
            }
        } else {
            System.err.println("请登录账号再进行操作！");
        }
    }

    //太空单个偷菜
    public void pickAether(User user, String ohterId, String aether) {
        if (user.getSid() != null || user.getSid().length() != 0) {
            if ((ohterId != null && aether != null) || (ohterId.length() != 0 && aether.length() != 0)) {
                System.out.println("偷取太空舱...");
                String url = LocationAll.F_PICK_AETHER_OSVTFH;
                String parameter = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.T + LocationAll.H + ohterId + LocationAll.V + 0 + LocationAll.F + aether;
                new URLTools().getDocmentByGep(url, parameter);
                System.out.println("偷取太空舱\tOK");
            } else {
                System.err.println("未获取到太空舱id或ohterId！");
            }
        } else {
            System.err.println("请登录账号再进行操作！");
        }
    }

    //同心花盆单个偷菜
    public void pickLove(User user, String ohterId, Set<String> concentrics) {
        if (user.getSid() != null || user.getSid().length() != 0) {
            if ((concentrics != null && ohterId != null) || (concentrics.size() != 0 && ohterId.length() != 0)) {
                System.out.println("开始偷取同心花盆...");
                String url = LocationAll.F_PICK_LOVE_OSVTFH;
                //迭代偷菜
                for (Iterator iter = concentrics.iterator(); iter.hasNext(); ) {
                    String parameter = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.T + LocationAll.H + ohterId + LocationAll.V + 0 + LocationAll.F + iter.next();
                    new URLTools().getDocmentByGep(url, parameter);
                }
                System.out.println("偷取同心花盆\tOK");
            } else {
                System.err.println("未获取到花盆id或ohterId！");
            }
        } else {
            System.err.println("请登录账号再进行操作！");
        }
    }

    //偷取单个好友所有土地，返回时间
    public int pickAll(User user, String ohterId, Map<String, Object> fieldIds) {
        long timeStar = new Date().getTime();
        //取出土地id
        Set<String> fieldId = (TreeSet<String>) fieldIds.get("fieldId");
        if (fieldId != null || fieldId.size() != 0) {
            pick(user, ohterId, fieldId);
        } else {
            System.out.println("未获取到普通土地。");
        }
        //取出温室id
        String gHome = (String) fieldIds.get("gHome");
        if (gHome != null || gHome.length() != 0) {
            pickGHouse(user, ohterId, gHome);
        } else {
            System.out.println("未获取到温室。");
        }
        //取出太空舱id
        String aether = (String) fieldIds.get("aether");
        if (aether != null || aether.length() != 0) {
            pickAether(user, ohterId, aether);
        } else {
            System.out.println("未获取到太空舱。");
        }
        //取出同心花盆id
        Set<String> concentrics = (TreeSet<String>) fieldIds.get("concentrics");
        if (concentrics != null || concentrics.size() != 0) {
            pickLove(user, ohterId, concentrics);
        } else {
            System.out.println("未获取到同心花盆。");
        }
        long timeEnd = new Date().getTime();
        int time = (int) (timeEnd - timeStar) / 1000;
        return time;
    }

    //偷取所有好友所有土地
    public void picksAll(User user, List<String> friends, Map<String, Map<String, Object>> fieldIds) {
        if (user != null) {
            if (friends == null || friends.size() == 0) {
                friends = getFriendsOtherId(user, 0);
            }
            if (fieldIds == null || fieldIds.size() == 0) {
                fieldIds = getFieldIdsFieldId(user, friends);
            }
            System.out.println("开始偷所有好友的菜...");
            long timeStar = new Date().getTime();
            int all = friends.size();
            for (int i = 0; i < all; i++) {
                String friend = friends.get(i);
                Map<String, Object> fieldId = fieldIds.get(friend);
                int t = pickAll(user, friend, fieldId);
                //反馈时间
                System.out.println(i + "/" + all + "\t" + t + "秒");
            }
            long timeEnd = new Date().getTime();
            int time = (int) (timeEnd - timeStar) / 1000;
            System.out.println("偷所有好友的菜\tOK\t共用" + time + "秒");
        } else {
            System.out.println("请先登录！");
        }
    }

    //一键偷菜，所有朋友
    public void oneKeyPicks(User user, List<String> friends, Map<String, Map<String, Object>> fieldIdsFieldId) {
//    public void oneKeyPicks(User user, List<String> friends) {
        if (user != null) {
            if (friends == null || friends.size() == 0) {//如果为空则获取朋友列表
                friends = getFriendsOtherId(user, 0);
            }
            System.out.println("开始一键偷全部好友的菜...");
            String url = LocationAll.F_ONE_KEY_PICK_OSTH;
            long timeStart = new Date().getTime();
            int all = friends.size();
            for (int i = 0; i < all; i++) {
                long t = -1;
                if (fieldIdsFieldId != null && fieldIdsFieldId.size() != 0) {
                    Map<String, Object> stringObjectMap = fieldIdsFieldId.get(friends.get(i));
                    String gHome = (String) stringObjectMap.get("gHome");
                    t = oneKeyPick(user, friends.get(i), gHome);
                } else {
                    t = oneKeyPick(user, friends.get(i), null);
                }
//                long t = oneKeyPick(user, friends.get(i));
                System.out.println(i + 1 + "/" + all + "\t" + t + "ms");

            }
            long timeEnd = new Date().getTime();
            int time = (int) (timeEnd - timeStart) / 1000;
            System.out.println("一键偷菜\tOK\t共用" + time + "秒");
        } else {
            System.out.println("请先登录！");
        }
    }

    //一键偷菜/偷鸡/摸鱼/帮助农牧，单个朋友
    public long oneKeyPick(User user, String ohterId, String gHome) {
        long timeS = new Date().getTime();
        if (user.getSid() != null || user.getSid().length() != 0) {
            if (ohterId != null || ohterId.length() != 0) {
                //一键农场偷菜
                String url = LocationAll.F_ONE_KEY_PICK_OSTH;
                String parameter = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.T + LocationAll.H + ohterId;
                new URLTools().getDocmentByGep(url, parameter);

                //一键牧场捉取
                String url2 = LocationAll.R_ONE_KEY_RANCH_OSVTH;
                String parameter2 = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T + LocationAll.H + ohterId;
                new URLTools().getDocmentByGep(url2, parameter2);

                //一键鱼塘摸鱼
                String url3 = LocationAll.R_ONE_KEY_FISH_OSVTHY;
                String parameter3 = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T + LocationAll.H + ohterId + LocationAll.Y + 3;
                new URLTools().getDocmentByGep(url3, parameter3);

                //一键帮助-农场
                String url4 = LocationAll.R_ONE_KEY_OPERATE_OSVTH;
                String parameter4 = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T + LocationAll.H + ohterId;
                new URLTools().getDocmentByGep(url4, parameter4);

                //一键帮助-牧场
                String url5 = LocationAll.F_ONE_KEY_OPERATE_OSVTH;
                String parameter5 = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T + LocationAll.H + ohterId;
                new URLTools().getDocmentByGep(url5, parameter5);

                if (gHome != null && gHome.length() != 0) {
                    //摘取温室
                    pickGHouse(user, ohterId, gHome);
                }

                long timeE = new Date().getTime();
                long time = timeE - timeS;
                return time;
            } else {
                System.err.println("诶？你朋友呢？！");
            }
        } else {
            System.err.println("请登录账号再进行操作！");
        }
        return -1;
    }

    //农场签到
    public void fSign(User user) {
        String url = LocationAll.D_F_SIGN_OSVT;
        String par = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T;
        new URLTools().getDocmentByGep(url, par);
        System.out.println("农场签到成功！");
    }

    //自用
    public void ziyong(User user) {
        List<String> friendsOtherId = getFriendsOtherId(user, 0);
        Map<String, Map<String, Object>> fieldIdsFieldId = getFieldIdsFieldId(user, friendsOtherId);
        oneKeyPicks(user, friendsOtherId, fieldIdsFieldId);

        String url = LocationAll.F_ONE_KEY_OPERATE_OSVT;//一键操作-农
        String par = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T;
//        new URLTools().getDocmentByGep(url,par);
        url = LocationAll.R_ONE_KEY_OPERATE_OSVT;//一键操作-牧
        new URLTools().getDocmentByGep(url, par);
        url = LocationAll.F_ONE_KEY_CROP_OSVT;//一键收获-农
        new URLTools().getDocmentByGep(url, par);
        url = LocationAll.R_ONE_KEY_GAIN_OSVT;//一键收获-牧
        new URLTools().getDocmentByGep(url, par);
        url = LocationAll.R_ADD_FODDER_OSVTA;//添加饲料
        par = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T + LocationAll.A3;
        new URLTools().getDocmentByGep(url, par);
        par = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T + LocationAll.A2;
        new URLTools().getDocmentByGep(url, par);
        url = LocationAll.R_ADD_FISH_OSVTA5;//添加鱼饲料
        par = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T + LocationAll.A5;
        new URLTools().getDocmentByGep(url, par);
    }
    //帮助放虫
    public void fangchong(User user) {
        List<String> friendsOtherId = getFriendsOtherId(user, 0);
        Map<String, Map<String, Object>> fieldIdsFieldId = getFieldIdsFieldId(user, friendsOtherId);
        oneKeyPicks(user, friendsOtherId, fieldIdsFieldId);

        String url = LocationAll.F_ONE_KEY_OPERATE_OSVT;//一键操作-农
        String par = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T;
//        new URLTools().getDocmentByGep(url,par);
        url = LocationAll.R_ONE_KEY_OPERATE_OSVT;//一键操作-牧
        new URLTools().getDocmentByGep(url, par);
        url = LocationAll.F_ONE_KEY_CROP_OSVT;//一键收获-农
        new URLTools().getDocmentByGep(url, par);
        url = LocationAll.R_ONE_KEY_GAIN_OSVT;//一键收获-牧
        new URLTools().getDocmentByGep(url, par);
        url = LocationAll.R_ADD_FODDER_OSVTA;//添加饲料
        par = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T + LocationAll.A3;
        new URLTools().getDocmentByGep(url, par);
        par = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T + LocationAll.A2;
        new URLTools().getDocmentByGep(url, par);
        url = LocationAll.R_ADD_FISH_OSVTA5;//添加鱼饲料
        par = LocationAll.O + user.getOpenId() + LocationAll.S + user.getSid() + LocationAll.V + 0 + LocationAll.T + LocationAll.A5;
        new URLTools().getDocmentByGep(url, par);
    }

    public static void main(String[] args) {
        Tools tools = new Tools();
        String account = "";//这里输入账号
        String passWord = "";//这里输入密码
        User user = new User(account, passWord);
        //自动初始化属性
        user = tools.getUserAllParameter(user);
        //手动初始化属性  (手动初始化好处是可以随时暂停程序进行手动操作，不改变sid，浏览器书签可以长期保持）
        //接下来放入浏览器url里面的三个属性  OpenId、Sid、Uid
//        user.setOpenId("");
//        user.setSid("");
//        user.setUid("");
        while (true) {
            long timeS = new Date().getTime();
            tools.ziyong(user);
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
