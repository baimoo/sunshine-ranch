package com.baimo.tools;

import java.util.Date;

/**
 * 该类存放所有的网址与参数名
 */
public final class LocationAll {
    /**
     * openId=O
     * sid=S
     * uid=U
     * ver=V
     * unixtime=T
     * pageNo=P
     * gameZoneId=Z  游戏ID，阳光农场=8
     * otherId=H
     * fieldId=F  摘取温室必备ID
     * type=Y 一般为0
     */
    static final String O="&openId=";
    static final String S="&sid=";
    static final String U="&uid=";
    static final String V="&ver=";//一般为0
    static final String T="&unixtime="+new Date().getTime();
    static final String P="&pageNo=";
    static final String Z="&gameZoneId=";
    static final String H="&otherId=";
    static final String F="&fieldId=";
    static final String Y="&type=";//一般为0
    static final String A1="&siteType=0&toolId=m00002";//（胡萝卜）-窝
    static final String A2="siteType=1&toolId=m00001";//（牧草）-栏
    static final String A3="siteType=0&toolId=m90006";//（精饲料）-窝
    static final String A4="siteType=1&toolId=m90006";//（精饲料）-栏
    static final String A5="&toolId=ysl001";//添加鱼饲料

    //登录地址，POST
    static final String LOGIN_POST="http://h5.pinpinhu.com/loginValidate.action";
    //农场好友排名
    static final String F_FRIENDS_EXP_OSVT_P="http://mc.pinpinhu.com/ygmc/farm/myFriendsSortByExp.go";
    //牧场好友排名
    static final String R_FRIENDS_EXP_OSVT_P="http://mc.pinpinhu.com/ygmc/ranch/myFriendsSortByExp.go";
    //鱼塘好友排名
    static final String S_FRIENDS_EXP_OSVT_P="http://mc.pinpinhu.com/ygmc/fishpond/myFriends.go";
    //进入阳光牧场
    static final String GAME8_USZ="http://h5.pinpinhu.com/session/redirectToGame.action";
    //一键偷菜（命名含义：第一下划线前字母区分农F/牧R/鱼S，最后一个下划线后的参数对应上面的静态变量名，提示url要携带的参数）
    static final String F_ONE_KEY_PICK_OSTH="http://mc.pinpinhu.com/ygmc/onekey/oneKeyFriFarmPick.go";
    //一键捉取
    static final String R_ONE_KEY_RANCH_OSVTH="http://mc.pinpinhu.com/ygmc/onekey/oneKeyFriRanchRob.go";
    //一键摸鱼type=3
    static final String R_ONE_KEY_FISH_OSVTHY="http://mc.pinpinhu.com/ygmc/onekey/oneKeyFriFishpondRob.go";
    //一键帮助-农场
    static final String R_ONE_KEY_OPERATE_OSVTH="http://mc.pinpinhu.com/ygmc/onekey/oneKeyFriFarmOperate.go";
    //一键帮助-牧场
    static final String F_ONE_KEY_OPERATE_OSVTH="http://mc.pinpinhu.com/ygmc/onekey/oneKeyFriRanchOperate.go";
    //普通偷菜
    static final String F_PICK_OSTHVF="http://mc.pinpinhu.com/ygmc/farm/pick.go";
    //摘取温室
    static final String F_PICK_G_HOUSE_OSVTFH="http://mc.pinpinhu.com/ygmc/g-house/pick.go";
    //摘取太空舱
    static final String F_PICK_AETHER_OSVTFH="http://mc.pinpinhu.com/ygmc/myField/pick.go";
    //摘取同心花盆
    static final String F_PICK_LOVE_OSVTFH="http://mc.pinpinhu.com/ygmc/farm/lovePickeIndex.go";
    //一键操作-农场
    static final String F_ONE_KEY_OPERATE_OSVT="http://mc.pinpinhu.com/ygmc/onekey/oneKeyFarmOperate.go";
    //一键操作-牧场
    static final String R_ONE_KEY_OPERATE_OSVT="http://mc.pinpinhu.com/ygmc/onekey/oneKeyRanchOperate.go";
    //一键收获-农场
    static final String F_ONE_KEY_CROP_OSVT="http://mc.pinpinhu.com/ygmc/onekey/oneKeyFarmCrop.go";
    //一键收获-牧场
    static final String R_ONE_KEY_GAIN_OSVT="http://mc.pinpinhu.com/ygmc/onekey/oneKeyRanchGain.go";
    //添加饲料
    static final String R_ADD_FODDER_OSVTA="http://mc.pinpinhu.com/ygmc/ranch/addFodder.go";
    //添加鱼饲料
    static final String R_ADD_FISH_OSVTA5="http://mc.pinpinhu.com/ygmc/fishpond/addFodder.go";
    //进入单个他人农场
    static final String F_FRIEND_OSVTH="http://mc.pinpinhu.com/ygmc/farm/friendFields.go";
    //农场签到
    static final String D_F_SIGN_OSVT="http://mc.pinpinhu.com/ygmc/sign/sign.go";
    //牧村膜拜
    static final String D_WORSHIP_OSVT="http://mc.pinpinhu.com/ygmc/village/worship.go";
    //牧村祈福
    static final String D_BLESS_OSVT="http://mc.pinpinhu.com/ygmc/village/bless.go";
    //牧场矿山/深林【2次】采矿Y=0,伐木Y=1
    static final String D_GATHER_OSVTY="http://mc.pinpinhu.com/ygmc/village/gather.go";
    //家园签到
    static final String D_SIGN_US="http://mc.pinpinhu.com/sign/toSign.action";
    //家园签到-砸蛋【未完善】
//    static final String
    //农场VIP每日礼包
    static final String D_VIP_GIFT_OSVT="http://mc.pinpinhu.com/ygmc/vip/drawVipGift.go";
    //每日风车【注意！收费风车也是这个链接】
    static final String D_AWARD_OSVT="http://mc.pinpinhu.com/ygmc/windmill/drawTodayAward.go";
    //风车维修
    static final String D_RECOVER_OSVT="http://mc.pinpinhu.com/ygmc/windmill/recoverDurability.go";

    //购买，POST，count: 、id:
    static final String D_BUY_LXLD10_OSVTY="http://mc.pinpinhu.com//ygmc/shop/buy.go";
    //牧村广播喊话 roomType: 0、retType: 0、content:[em_133]加友
    static final String SPEEK_OSVT="http://mc.pinpinhu.com/ygmc/chatRoom/speek.go";

    /*
    天圣雪山-冰风悬崖http://mc.pinpinhu.com/ygmc/dig/dig.go?openId=&sid=&ver=0&unixtime=&mapId=1&nodeId=1
    天圣雪山-冰雪洞窟http://mc.pinpinhu.com/ygmc/dig/dig.go?openId=&sid=&ver=0&unixtime=&mapId=1&nodeId=2
    幽暗深林-密林深处http://mc.pinpinhu.com/ygmc/dig/dig.go?openId=&sid=&ver=0&unixtime=&mapId=2&nodeId=5
    幽暗深林-枯树洞窟http://mc.pinpinhu.com/ygmc/dig/dig.go?openId=&sid=&ver=0&unixtime=&mapId=2&nodeId=6
    打开他人同心花盆http://mc.pinpinhu.com/ygmc/loverfarm/friCPFieldDetail.go?openId=&sid=&ver=0&unixtime=&fieldId=&otherId=
     */
}
