package com.jingtaoi.yy.utils;

import com.jingtaoi.yy.control.ChatMessage;

import java.util.ArrayList;

public class MessageUtils {

    private static volatile MessageUtils messageUtils;

    public MessageUtils() {

    }

    public static MessageUtils getInstans() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (messageUtils == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (MyUtils.class) {
                //未初始化，则初始instance变量
                if (messageUtils == null) {
                    messageUtils = new MessageUtils();
                }
            }
        }
        return messageUtils;
    }

    //频道消息回调（所有信息）
    private ArrayList<ChatMessage> fastConShows = new ArrayList<>();

    public ArrayList<ChatMessage> getChat() {
        return fastConShows;
    }

    public void addChatShows(ChatMessage chatMessage) {
        if (fastConShows == null) {
            fastConShows = new ArrayList<>();
        }
        if (chatMessage != null && !fastConShows.contains(chatMessage)) {
            fastConShows.add(chatMessage);
        }

    }

    public void removeChatShows(ChatMessage chatMessage) {
        if (fastConShows == null) {
            return;
        }
        if (chatMessage != null && fastConShows.contains(chatMessage)) {
            fastConShows.remove(chatMessage);
        }
    }

    public void removeAllChats() {
        fastConShows.clear();
    }


}
