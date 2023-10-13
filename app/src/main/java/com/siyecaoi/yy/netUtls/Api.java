package com.siyecaoi.yy.netUtls;

import com.siyecaoi.yy.utils.Const;

/**
 * Created by Administrator on 2018/1/25.
 */
public class Api {

    public static final int SUCCESS = 0;

    public static String shareUrl = Const.BASE_URL + "apis/FX";

    /**
     * 手机登录
     */
    public static String loginPhone = "user/loginPhone";
    /**
     * 删除上传用户图片
     */
    public static String DeleteImg = "User/DeleteImg";
    /**
     * 用户拉黑记录
     */
    public static String getUserblock = "Userblock/getUserblock";

    /**
     * 广告页
     */
    public static String Advertising = "Advertising/Advertising";

    /**
     * 用户删除拉黑记录
     * <p>
     * id	int	是	数据id
     */
    public static String delUserblock = "Userblock/delUserblock";

    /**
     * 获取验证码
     * <p>
     * phone	是	String	手机号
     * type	是	Integer	1是注册 2是忘记,3 绑定手机和修改绑定手机和绑定支付宝
     */
    public static String getSmsCode = "sms/SmsCode";

    /**
     * 下载音乐次数
     * <p>
     * id	int	是	数据id
     */
    public static String userMusiceNum = "User/userMusiceNum";

    /**
     * 获取奖池
     * <p>
     * id	int	是	数据id
     */
    public static String JackpotInfoMark = "Lottery/JackpotInfoMark";
    /**
     * 我的探险记录
     * <p>
     * uid	是	int	用户id
     * pageSize	int	是	一页好的条
     * pageNum	int	是	第几页
     */
    public static String UserGifit = "Lottery/UserGifit";

    /**
     * 获取验证码
     * <p>
     * phone	是	String	手机号
     * type	是	Integer	1是注册 2是忘记,3 绑定手机和修改绑定手机和绑定支付宝
     */
    public static String getblock = "Userblock/getblock";

    /**
     * 获取提现的申请
     * <p>
     * uid	int	是	用户id
     * id	int	是	数据id
     */
    public static String AddSetWithdraws = "User/AddSetWithdraw";

    /**
     * 用户用钻石兑换浪花信息
     * <p>
     */
    public static String UserConvertlist = "User/UserConvertlist";

    /**
     * 版本更新
     * <p>
     * phone	是	String	手机号
     * type	是	Integer	1是注册 2是忘记,3 绑定手机和修改绑定手机和绑定支付宝
     */
    public static String getVersions = "Versions/getVersions";

    /**
     * 开关防顶号1=否；2=是
     */
    public static String setFangDingHao = "user/setFangDingHao";

    /**
     * 充值下单
     * <p>
     * uid	是	int	用户id
     * id	是	int	充值数据的id
     */
    public static String recharge = "user/recharge";

    /**
     * 支付接口
     * <p>
     * type	是	Integer	1 支付宝 , 2 微信，3微信公众
     * OrderNum	是	int	订单id
     * openId	否		微信公众号的openid
     */
    public static String getPayInfo = "pay/getPayInfo";

    /**
     * 验证实名认证
     * <p>
     * uid	int	是	用户id
     */
    public static String audit = "Discover/audit";

    /**
     * 广播交友最新20条
     */
    public static String BroadList = "Discover/BroadList";

    /**
     * 广播交友 发送消息
     * <p>
     * uid	int	是	用户id
     * content	String	是	发送的消息
     */
    public static String Broad = "Discover/Broad";

    /**
     * 我的邀请奖励页面
     * <p>
     * Discover/invite
     * uid	int	是	用户id
     */
    public static String invite = "Discover/invite";

    /**
     * 我的分成奖励
     * <p>
     * uid	是	int	用户id
     * pageSize	是	int	一页好的条
     * pageNum	是	int	第几页
     */
    public static String getUserDivide = "Discover/getUserDivide";
    /**
     * 获取宝箱的信息的说明信息(宝箱活动规则)
     * <p>
     */
    public static String LotteryMark = "Lottery/LotteryMark";

    /**
     * 绑定支付宝
     * <p>
     * uid	int	是	用户id
     * payAccount	String	是	支付宝账号
     * trueName	String	是	真实姓名
     * smsCode	String	是	验证码
     */
    public static String UserPay = "User/UserPay";

    /**
     * 邀请好友-人数
     * <p>
     * uid	是	int	用户id
     * pageSize	是	int	一页好的条
     * pageNum	是	int	第几页
     */
    public static String getUserInvite = "Discover/getUserInvite";

    /**
     * 获取邀记录排行
     * <p>
     * uid	int	是	用户id
     */
    public static String userList = "User/userList";

    /**
     * 实名认证
     * <p>
     * uid	int	是	用户id
     * name	String	是	名称
     * card	String	是	身份证号
     */
    public static String Addaudit = "Discover/Addaudit";

    /**
     * 消息送礼物
     * <p>
     * uid	int	是	用户id
     * ids	String	是	被送用户id 用逗号隔开
     * gid	int	是	礼物id
     * num	int	是	送礼物数
     * sum	int	是	送礼物的人数
     */
    public static String userSaveGift = "user/SaveGift";
    /**
     * 用户添加拉黑记录
     */
    public static String getAddUserblock = "Userblock/getAddUserblock";
    /**
     * 验证验证码
     */
    public static String isCode = "user/isCode";

    /**
     * 手机注册
     */
    public static String PhoneRegistered = "user/PhoneRegistered";
    /**
     * 获取用户信息
     */
    public static String getUserInfo = "user/getUserInfo";

    /**
     * 获取游戏爆率
     */
    public static String getUserJackpotExpenseTotal = "Lottery/UserJackpotExpenseTotal";
    /**
     * 获取html页面
     */
    public static String Protocol = "Protocol/Protocol";

    /**
     * 忘记密码
     */
    public static String ForgotPassword = "user/ForgotPassword";

    //上传图片
    public static String updateImg = "app/uploadImg/add";

    //第三方注册和登录
    public static String loginSan = "user/loginThird";

    //进入房间（退出房间）
    public static String chatRoom = "chatrooms/getChatrooms";

    /**
     * 麦上用户的心跳
     */
    public static String addTime = "user/AddTime";

    /**
     * 声网token
     */
    public static String SWToken = "SWToken/SWToken";

    /**
     * 获取在房间的用户
     * <p>
     * uid	是	int	用户id
     * pid	是	int	房间id
     * pageSize	是	一页好的条
     * pageNum	是	第几页
     */
    public static String voiceUser = "chatrooms/UserChatrooms";

    //获取在房间上麦用户
    public static String voiceChatUser = "chatrooms/getUserChatrooms";

    /**
     * 用户上麦和下麦
     * <p>
     * uid	是	int	用户id
     * pid	是	int	房间id
     * sequence	int	是	麦序
     * type	int	是	1 是上麦， 2是下麦
     * state	int	是	1 是普通， 2是抱她上麦   上麦必传，下麦非必传
     * buid	int	否	抱她上麦的人的用户id
     */

    public static String micUpdate = "user/getChatrooms";

    //获取在房间的房主和我(获取房间信息)
    public static String getVoiceHome = "chatrooms/getUser";

    //是否封锁此座位
    public static String getSeatStatus = "chatrooms/getSeatStatus";

    //是否禁麦此座位
    public static String getSeatState = "chatrooms/getSeatState";

    //设置管理员
    public static String getChatroomsGLY = "chatrooms/getChatroomsGLY";

    //获取房间管理员
    public static String getRoomManagement = "Room/getRoomManagement";

    //修改房间信息
    public static String getUpRoom = "Room/getUpRoom";

    //获取房间黑名单
    public static String getRoomBlock = "Room/getRoomBlock";

    //房间魅力和财富榜
    public static String SaveRoomCFML = "Room/SaveRoomCFML";


    /**
     * //用户举报信息/房间和人用户
     * <p>
     * uid	int	是	用户ID
     * type	int	是	1是用户, 2是房间，3是音乐
     * buid	int	否	是用户就是用户id/音乐的id
     * rid	String	否	房间的唯一标识的id
     * content	String	是	举报内容
     */
    public static String ReportSave = "Report/ReportSave";

    //获取房间背景
    public static String SaveRoomBackdrop = "Room/SaveRoomBackdrop";

    //获取竞拍临时榜单
    public static String UserAuctionCharm = "Auction/UserAuctionCharm";

    //获取房主在不是我，不是房主，不在麦上
    public static String UserNoWheat = "User/UserNoWheat";

    //获取我是否关注和一些个人信息
    public static String UserAttention = "Attention/UserAttention";

    /**
     * 添加的关注的人和取消关注
     * <p>
     * uid	int	是	用户id
     * buid	int	是	被关在用户id
     * type	int	是	1关注， 2取消关注
     */
    public static String addAttention = "Attention/addAttention";
    /**
     * 获取我的好友和我关注的和我的粉丝
     * <p>
     * uid	是	int	用户id
     * type	是	int	1是好友，2是关注的，3 关注我的
     * pageSize	int	是	一页好的条
     * pageNum	int	是	第几页
     */
    public static String addfriendList = "User/addfriendList";
    /**
     * 用户用钻石兑换浪花
     * <p>
     * uid	int	是	用户id
     * gold	int	是	消耗钻石数
     */
    public static String UserConvert = "User/UserConvert";
    /**
     * 修改用户信息
     * <p>
     * id	是	int	id
     * sex	否	Integer	性别(1 男, 2 女)
     * dateOfBirth	否	String	出生日期 2018-05-16格式是这样
     * imgTx	否	String	头像
     * nickname	否	String	昵称
     * individuation	否	String	个性签名
     */
    public static String updateUser = "user/updateUser";

    /**
     * 发现的提现的申请
     * <p>
     * uid	int	是	用户id
     * money	double	是	钱
     */
    public static String AddSetWithdraw = "Discover/AddSetWithdraw";

    /**
     * 去找指定用户所在房间
     * <p>
     * uid	int	是	用户id
     * buid	int	是	去哪个房间的用户id
     */
    public static String UserRoom = "UserRoom/UserRoom";
    /**
     * 财富，魅力基本信息
     * <p>
     * state	int	是	1是财富，2是魅力
     */
    public static String UserList = "User/UserList";
    /**
     * 用户财富，用户魅力
     * <p>
     * uid	int	是	用户id
     * state	int	是	1是财富，2是魅力
     */
    public static String UserGold = "User/UserGold";
    /**
     * 修改密码
     * <p>
     * uid	是	int	用户id
     * oldpassword	是	String	老密码 MD5加密
     * password	是	String	新密码 MD5加密
     */
    public static String ModPassword = "user/ModPassword";
    /**
     * 获取提现列表
     * <p>
     * uid	是	int	用户id
     */
    public static String SetWithdrawServiceList = "User/SetWithdrawServiceList";
    /**
     * 获取购买道具
     * <p>
     * uid	int	是	用户id
     * buid	int	是	送他人就是他人id，自己就是自己id
     * gid	int	是	礼物id
     */
    public static String addSceneList = "User/addSceneList";

    /**
     * 获取道具的列表
     * <p>
     * uid	是	int	用户id
     * state	int	是	1座驾，2 是头环
     * pageSize	int	是	一页好的条
     * pageNum	int	是	第几页
     */
    public static String getSceneList = "User/getSceneList";


    /**
     * 房间收入
     */
    public static String LOTTERY_MYINCOME = "Lottery/myIncome";

    /**
     * 用户充值记录
     */
    public static String UserRecharge = "Recharge/UserRecharge";
    /**
     * 获取提现的申请列表
     */
    public static String WithdrawList = "User/WithdrawList";
    /**
     * 兑换历史
     */
    public static String exchangeLog = "User/exchangeLog";

    //添加房间拉黑的 人
    public static String SaveRoomBlock = "Room/SaveRoomBlock";

    //开启和关闭公屏
    public static String ChatroomsGP = "Chatrooms/ChatroomsGP";

    //开启和关闭竞拍模式
    public static String UserAuction = "Auction/UserAuction";

    /**
     * 获取房间礼物列表
     */
    public static String SaveRoomGift = "Room/SaveRoomGift";

    /**
     * 获取房间礼物列表()背包礼物
     */
    public static String Knapsack = "user/Knapsack";

    /**
     * 获取充值金额
     */
    public static String SaveRecharge = "Room/SaveRecharge";
    /**
     * 房间发送红包
     */
    public static String SvaeRed = "Red/SvaeRed";
    /**
     * 房间送礼物
     * <p>
     * rid	是	int	房间id
     * uid	int	是	用户id
     * ids	String	是	被送用户id 用逗号隔开
     * gid	int	是	礼物id
     * num	int	是	送礼物数
     * sum	int	是	送礼物的人数
     */
    public static String SaveGift = "Gift/SaveGift";
    /**
     * 获取 排麦列表
     */
    public static String getRowWheat = "RowWheat/getRowWheat";
    /**
     * 取消排麦
     */
    public static String delRowWheat = "RowWheat/delRowWheat";
    /**
     * 排麦
     */
    public static String addRowWheat = "RowWheat/addRowWheat";
    /**
     * 领取房间红包
     */
    public static String GetRed = "Red/GetRed";
    /**
     * 获取领取红包页面信息
     */
    public static String GetRedList = "Red/GetRedList";
    /**
     * 获取在房间上麦用户和房主
     */
    public static String getUserChatrooms = "user/getChatroomsUser";
    /**
     * 开启房间PK
     */
    public static String ChatroomsPk = "Chatrooms/ChatroomsPk";
    /**
     * 房间PK投票
     */
    public static String addPk = "Chatrooms/addPk";
    /**
     * 获取房间pk记录
     */
    public static String ChatroomsPK = "Chatrooms/ChatroomsPK";
    /**
     * 关闭房间pk记录
     */
    public static String delChatroomsPK = "Chatrooms/delChatroomsPK";
    /**
     * 获取房间pk一条记录（关闭pk时获取pk数据）
     */
    public static String ChatroomsPKOne = "Chatrooms/ChatroomsPKOne";
    /**
     * 获取在房间上麦和房主
     */
    public static String getChatroomsUser = "user/getChatroomsUser";
    /**
     * 获取 开启宝箱
     * <p>
     * uid	是	int	用户id
     * num	是	int	开启次数
     * rid	是	String	房间id
     */
    public static String GetLottery = "Lottery/GetLottery"; //深海捕鱼 转盘
    public static String GetLottery2 = "Lottery/GetLottery2";//定海神针 银宝箱
    public static String GetLottery3 = "Lottery/GetLotteryCjd";//东海龙宫 黄金宝箱
    /**
     * 获取 宝箱信息
     */
    public static String getUserLottery = "Lottery/getUserLottery";
    /**
     * 获取 实时奖池
     */
    public static String jackpotSize = "fx/jackpotSize";
    /**
     * 获取 钻石蛋奖池
     */
    public static String jackpotPage = "fx/jackpotPage";
    /**
     * 获取 钻石蛋锦鲤
     */
    public static String jackpot = "fx/jackpot";
    /**
     * 获取 热门音乐
     */
    public static String getMusic = "Music/getMusic";
    /**
     * 获取 token
     */
    public static String Token = "Token/Token";
    /**
     * 踢出房间
     */
    public static String deltrooms = "chatrooms/deltrooms";
    /**
     * 获取用户最后登录的时间
     */
    public static String Logbook = "user/Logbook";
    /**
     * 获取我上传的音乐
     */
    public static String userMusice = "user/userMusice";
    /**
     * 获取未领取红包列表
     */
    public static String UserRedList = "Red/UserRedList";
    /**
     * 反馈信息
     */
    public static String FeedbackSave = "Feedback/FeedbackSave";
    /**
     * 用户邀请记录
     */
    public static String UserInvite = "invite/UserInvite";
    /**
     * 个人资料界面
     */
    public static String PersonageUser = "User/PersonageUser";
    /**
     * 绑定手机
     * <p>
     * uid	是	int	用户id
     * phone	是	String	电话
     * password	是	String	密码
     * smsCode	是	String	验证码
     */
    public static String bindPhone = "user/bindPhone";
    /**
     * 修改绑定手机
     * <p>
     * phone	是	String	手机号 (这个手机获取验证码)
     * oldphone	是	String	老手机号
     * smsCode	是	String	验证码
     */
    public static String ModifyPhone = "user/ModifyPhone";
    /**
     * 用户获取礼物数量
     */
    public static String userScene = "User/userScene";
    /**
     * 用户上传图片(个人资料页面)
     */
    public static String addUserImg = "user/addUserImg";
    /**
     * 获取礼物记录的列表
     */
    public static String GiftRecordList = "User/GiftRecordList2";

    /**
     * 首页banner
     */
    public static String Banner = "home/FLbranner";

    /**
     * 首页banner点击
     */
    public static String Banner_Click = "brannerNum";

    /**
     * 活动奖池大小
     */
    public static String getCarveUpPoolSize = "fx/getCarveUpPoolSize";

    /**
     * 瓜分排行
     */
    public static String getCarveUpPool = "fx/getCarveUpPool";

    /**
     * 推荐
     */
    public static String HOME_TUIJIAN = "hot/getHometJ";

    /**
     * 推荐
     */
    public static String HOME_DATA = "hot/getHomeList";

    /**
     * 排行榜
     */
    public static String HOME_RANK = "Charm/SaveCharmCFML";

    /**
     * 排行榜
     */
    public static String HOT_RANK = "home/topThree";

    /**
     * 房间榜
     */
    public static String CHARM_ROOMBILLS = "Charm/RoomBills";

    /**
     * 申请推荐
     */
    public static String APPLY_TUIJIAN = "hot/getRecommend";
    /**
     * 推荐次数
     */
    public static String TUIJIAN_NUM = "hot/getUserNum";
    /**
     * 签到情况
     */
    public static String SIGN_DATA = "Home/Addsign";
    /**
     * 声音签名
     */
    public static String VOICE = "hot/getVoiceTime";
    /**
     * 签到
     */
    public static String SIGN = "Home/sign";
    /**
     * 推荐记录
     */
    public static String TUIJIAN_RECORD = "hot/getclaimerList";
    /**
     * 同城
     */
    public static String SAME_CITY = "hot/getlocal";
    /**
     * 速配
     */
    public static String MATCH = "hot/getSpeed";
    /**
     * 热搜词
     */
    public static String HOT_WORDS = "hot/hotList";
    /**
     * 搜
     */
    public static String SEARCH = "hot/gethotList";
    /**
     * 设置支付密码
     * uid	是	int	用户id
     * smsCode	是	String	验证码
     * password	是	String	密码
     */
    public static String PlayPassword = "user/PlayPassword";
    /**
     * 清除房主礼物值
     * uid	是	int	用户id
     * rid	是	String	房间id
     */
    public static String RoomDel = "user/RoomDel";


    /**
     * 清除其他坑位
     */
    public static String QTROOMDEL = "user/clearUserCharm";
    /**
     * 用户上传图片房间背景图
     * uid	int	是	用户ID
     * rid	String	是	房间id
     * imagess	String	否	用户上传图片 多张中间逗号隔开
     */
    public static String addRoomImg = "user/addRoomImg";
    /**
     * 全国消息数据
     * uid	是	int	用户id
     */
    public static String getScreen = "Discover/getScreen";
    /**
     * 发送全国消息
     * uid	是	int	用户id
     * content	是	String	发送内容
     */
    public static String AddScreen = "Discover/AddScreen";
    /**
     *
     */
    public static String SZ = "user/SZ";
    /**
     * Discover/QBgetScreen
     * 全国消息数据
     */
    public static String QBgetScreen = "Discover/QBgetScreen";
    /**
     * 获取进发生全国消息需要的浪花
     */
    public static String ScreenGold = "Discover/ScreenGold";
    /**
     * 代冲浪花
     * uid	是	int	送用户id
     * buid	是	int	被充值用户id
     * gold	是	int	充值浪花
     * rid	否	String	房间
     */
    public static String getGeneration = "Discover/getGeneration";
    /**
     * 用户浪花
     * gold	string	浪花数
     * state	string	是否冻结 1否,2是
     */
    public static String getUserMoney = "Discover/getUserMoney";
    /**
     * 用户赠送纪录
     * uid 用户id
     * pageSize 条数
     * pageNum 页数
     */
    public static String givingRecords = "Recharge/GivingRecords";

    /**
     *背包礼物  一键全刷
     * rid 房间id
     * uid 用户id
     * toUid 被送用户id
     */
    public static String aKeyAllBrush = "myGift/AKeyAllBrush";

    /**
     * 转赠记录
     * uid 用户ID
     * pageNum 页数
     * pageSize 每页大小
     * type 1送出 2收到
     */
    public static String givePartyGiftLog = "activitie/log";

    /**
     * 活动礼物 转赠好友
     * rid 房间id
     * uid 用户id
     * buid 被送用户id
     * gid 礼物id
     */
    public static String givePartyGift = "activitie/giving";

    /**
     * 获取声网RtmToken
     * uid 用户ID
     */
    public static String getAgoraRtmToken = "agora/getRtmToken";

    /**
     * 获取声网RtmToken
     * uid 用户ID
     * channelName 频道名称
     */
    public static String getAgoraRtcToken = "agora/getRtcToken";

    /**
     *手气榜
     * uid 用户ID    pageNum 页数    pageSize 每页大小
     */
    public static String luckList = "Lottery/luckList";
    /**
     *
     */
    public static String PlayPassword1 = "";

    /**
     * 保存设备ID
     * @param uid
     * @param uuid
     */
    public static String saveUuid = "user/saveUuid";

}
