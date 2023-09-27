package com.siyecaoi.yy.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class Const {

//    public static final String BASE_URL = "https://test.hhshengwei.com/siyecao/"; //debug
//    public static final String BASE_URL = "http://www.uoizfez.cn/senhai/";
    public static final String BASE_URL = "http://123.56.155.174/senhai/";
//    public static String AGORA_APP_ID = "c797dfdab0e84a5f9de200edac6b71e6";//debug
//    public static String AGORA_APP_ID = "6ee3e0150bbb464c880dfa3f6dc65797";
    public static String AGORA_APP_ID = "a38fc709b1ec47d0a3062e2ef6e40ae6";
//    public static String AGORA_RTM_TOKEN = "0066ee3e0150bbb464c880dfa3f6dc65797IAA88ZJVcv1OQM2rSamcjmP307rRApRfQyyHcSNhT8A+UqKuUzAAAAAAEADlVhOSkhrSYgEA6AMAAAAA";
//    public static String AGORA_RTC_TOKEN = "0066ee3e0150bbb464c880dfa3f6dc65797IADtddwZRqmRAV+bZJvyEvTZhWQE9GjV+wf0HmSPQoYFpyOw25yirlMwIgCcmUQaAVjSYgQAAQCRFNFiAgCRFNFiAwCRFNFiBACRFNFi";
//    public static String AGORA_RTC_TOKEN = "007eJxTYIgtDljz6PZ/r1OLou0itA4eEgm87jORac2jJQtPsR9sEDFRYEg0tkhLNjewTDJMTTYxTzFINDYwM0o1Sk0zSzUxSEw1WxvImdoQyMiw9b0CIyMDBIL47AxZmXnpJYn5DAwA8rIhSQ==";
//    public static final int TUIKIT_KEY = 1400468262;//debug
//    public static final int TUIKIT_KEY = 1400700670;
    public static final int TUIKIT_KEY = 1600004489;
    //    public static String chatRoom = "@TGS#aX7XRJ4GO";//广播交友聊天室id debug
//    public static String chatRoom = "@TGS#aXOSCMQIV";//广播交友聊天室id
    public static String chatRoom = "@TGS#aUO4URINH";//广播交友聊天室id
    //    public static String allMsgRoom = "@TGS#a2MYRJ4G6";//全国消息聊天室id debug
//    public static String allMsgRoom = "@TGS#aBQUFMQIG";//全国消息聊天室id
    public static String allMsgRoom = "@TGS#aPYAVRIN2";//全国消息聊天室id
    //    public static String roomChannelMsg = "@TGS#aW6YRJ4GN";//房间全频道消息 debug
//    public static String roomChannelMsg = "@TGS#aP4UFMQIW";//房间全频道消息
    public static String roomChannelMsg = "@TGS#a73BVRIN2";//房间全频道消息

    public static int giftBorderFrame = 60; //礼物边框帧长

    public static int giftDanFrame = 80; //普通探险帧长
    public static int giftDanDiamondFrame = 79; //钻石蛋探险帧长

    // bugly
    public static String BUGLY_ID = "";

    // 阿里oss
    public static String accessKeyId = "";
    public static String accessKeySecret = "";
    public static String bucketName = "";
    public static String endpoint = "";

    // 华为obs
//    public static String endPoint = "obs.cn-east-3.myhuaweicloud.com";
//    public static String ak = "GGVF5PPZ8YSNSUIFBYC5";
//    public static String sk = "YzPSuq6BJ2vcrDz31K9aCmPLnDYliKMGpioxl3Go";
//    public static String bucket = "senhaiyuyin1";
    public static String endPoint = "obs.cn-east-3.myhuaweicloud.com";
    public static String ak = "3SHE8RK3I834XH6X7NHK";
    public static String sk = "cTkisjXv9DoNdcyChPXDKv7H4F286yUxbBz3JFJo";
    public static String bucket = "app-voice";

    public static final String UM_APPID = "";

    public static final String WECHAT_APPID = "";
    public static final String WECHAT_APPSECRET = "";
    public static final String QQ_APPID = "";
    public static final String QQ_APPKEY = "";

    public static boolean departRefalsh = false;//已发出刷新

    public static String RoomName = "";//房间名称
    public static String RoomId = "";//房间id
    public static String RoomIdLiang = "";//靓号
    public static String RoomNum = "";//房间人数
    public static String RoomImg = "";//房间房主头像
    public static boolean isOpenMicrophone;//判断房间麦克风状态（是否开启）
    public static boolean isOpenReceiver;//判断房间扬声器状态 （是否开启）
    public static int packetNumber;//红包个数，大于0则还有红包未领取
    public static ArrayList<String> chatShow;//聊天内容

    public static String ROOM_HIS = "room_history";
    public static String USER_HIS = "user_history";

    public static long msgSendTime;//广播交友最后一条消息的发送时间(毫秒)

    public static class User {
        public final static String USER_TOKEN = "id";//userid
        public final static String APP_TOKEN = "appToken";//用户token
        public final static String UUID = "uuid";//唯一标示
        public final static String AGE = "age";
        public final static String MARK = "mark";
        public static String IMG = "img";
        //        public static String NAME = "name";
        public static String NICKNAME = "nickname";
        public static String PAY_PASS = "payPass";
        public static String ROOMID = "roomId";//用户生成id及用户房间号
        public static String USER_LiANG = "userLiang";//靓号
        public static String ROOMNAME = "roomName";//用户房间名称
        public static String SAN_LOGIN = "sanLogin";//三方登录标示，1是微信 2 是qq


        public static String PAYPASSWORD = "payPassword"; //支付密码
        public static String PHONE = "phone"; //电话
        public static String QQSID = "qqSid";
        public static String WECHATSID = "wechatSid";
        public static String REFERRERID = "referrerId";
        public static String REGION = "region";
        public static String SEX = "sex"; // 1男  2女
        public static String STATE = "state";//1正常，2冻结
        public static String TYPE = "type";//	1 是正常， 2是 冻结
        public static String STATES = "status";//是否开通房间 1否 ， 2是
        public static String USER_SIG = "userSig";//腾讯云sig


        public static String DATEOFBIRTH = "dateOfBirth";//生日
        public static String AttentionNum = "attentionNum";//关注数
        public static String CharmGrade = "charmGrade";//魅力等级
        public static String FansNum = "fansNum";//粉丝数
        public static String GOLD = "gold";//浪花
        public static String GoldNum = "goldNum";//财富的累加值
        public static String GRADE_T = "grade_t";//财富等级
        public static String Ynum = "ynum";//钻石数
        public static String Yuml = "yuml";//钻石数累计数
        public static String VOLUME = "volume";//音乐音量
        public static String SIGNER = "signer";//个性签名
        public static String HEADWEAR_H = "headwearH";//头饰封面
        public static String CAR_H = "carH";//座驾封面
        public static String HEADWEAR = "headwear";//头饰
        public static String CAR = "car";//座驾
        public static String IS_AGENT_GIVE = "isAgentGive";//是否代充  1否 2是
        public static String IS_DEEP_SEA_SKIP = "isDeepSeaSkip";//是否跳过深海捕鱼动画
        public static String IS_DING_HAI_SKIP = "isDingHaiSkip";//是否跳过定海神针捕鱼动画
        public static String IS_DONG_HAI_SKIP = "isDongHaiSkip";//是否跳过东海龙宫捕鱼动画
        public static String IS_OPEN_DM = "isOpenDm";//开启定海神针入口
        public static String IS_OPEN_CJD = "isOpenCjd";//开启东海龙宫入口
        public static String IS_FANG_DING_HAO = "isFangDingHao";//防顶号
    }

    public static class IntShow {
        public final static int EXIT = -1;
        public final static int ZERO = 0;
        public final static int ONE = 1;
        public final static int TWO = 2;
        public final static int THREE = 3;
        public final static int FOUR = 4;
        public final static int FIVE = 5;
        public final static int SIX = 6;
        public final static int SEVEN = 7;
        public final static int EIGHT = 8;
        public final static int NINE = 9;
        public final static int TEN = 10;
    }

    public static class ShowIntent {
        public final static String NUMBER = "number";
        public final static String DATA = "data";
        public final static String URL = "url";
        public final static String TYPE = "type";
        public final static String STATE = "state";
        public final static String PHONE = "phone";
        public final static String ADDRESS = "address";
        public final static String CITY_CODE = "cityCode";
        public final static String CODE = "code";
        public final static String SMSCODE = "smsCode";
        public final static String PASS = "pass";
        public final static String SEARCH = "search";
        public final static String TITLE = "title";
        public final static String MONEY = "money";
        public final static String ID = "id";//
        public final static String OTHRE_ID = "otherId";//
        public final static String IMG = "img";//
        public final static String ROOMID = "roomId";//房间id
        public final static String TOPIC = "topic";//话题
        public final static String POSITION = "position";//下标
        public final static String NAME = "name";
        public final static String URLTYPE = "urlType";
        public final static String CHAT_INFO = "chatInfo";//单聊实例
        public final static String GOLD = "gold";//购买浪花
        public final static String ROLL_BOTTOM = "ROLL_BOTTOM";//滚动到底部
    }

    public static class Agora {
        public final static String ATTR_MICS = "attr_mics";//房间内麦上用户信息
        public final static String ATTR_XGFJ = "attr_xgfj";//房间信息更改
        public final static String ATTR_PK = "attr_pk";//pk信息推送
        public final static String ATTR_PKTP = "attr_pktp";//pk投票
        public final static String ATTR_PACKET = "attr_hb";//红包推送
    }

    public static class BroadCast {
        public final static String OPEN_SHEN = "openShen";
        public final static String MIC_ISOPEN = "micIsOpen";
        public final static String ROOM_MIX = "roomMix";//最小化房间
        public final static String ROOM_PK = "roomPk";//房间开启pk
        public final static String MUSIC_PLAY = "musicPlay";//
        public final static String MUSIC_PAUSE = "musicPause";//暂停播放
        public final static String MUSIC_REPLAY = "musicReplay";//继续播放
        public final static String MUSIC_CHANGE = "musicChange";//状态改变(列表)
        public final static String MUSIC_NEXT = "musicNext";//下一曲
        public final static String MUSIC_PRE = "musicPre";//上一曲
        public final static String MIC_DOWN = "micDown";//下麦
        public final static String MUSIC_TONEXT = "musicToNext";//自动播放下一曲

        public final static String PACKET_OVER = "packetOver";//红包已领完
        public final static String EXIT = "exit";//退出登录(锁定用户)
        public final static String WECHAT_PAYSUCCECSS = "paysuccecss";//微信支付成功
        public final static String MSG_COUN = "msgCoun";//发送全国消息
    }

    public static class MusicShow {
        public static int id;//数据库id
        public static int musicId;//音乐id
        public static boolean isHave;//是否有音乐
        public static String musicName;//音乐名称
        public static String musicPath;//音乐地址
        public static int musicLength;//音乐长度
        public static int musicPlayState;//播放状态    2 播放  3暂停
    }


    public static class WechatPay {
        public final static String PAY_SUCCESS = "pay_success";
    }

    public static class RequestCode {
        public final static int SELECTPHOTO_CODE = 1001;
        public final static int CROPTPHOTO_CODE = 1002;
        public final static int MICUPDATE_CODE = 1003;//报ta上麦
        public final static int CLEAR_PIC = 1004;//删除图片
        public final static int PROP_SEND = 1005;//赠送道具
        public final static int DATA_CHANGE = 1006;//资料修改
    }

    public static class Method {
        public final static String PING = "ping";
        public final static String FAST = "fast";
    }

    public static class Lottery{
        public final static List<Bitmap> WHEEL_IMG_LIST = new ArrayList<>();
    }

}
