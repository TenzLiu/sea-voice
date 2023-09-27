package com.siyecaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.model.CarShowMessageBean;
import com.siyecaoi.yy.model.EmojiMessageBean;
import com.siyecaoi.yy.model.GetOutBean;
import com.siyecaoi.yy.utils.ImageShowUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.model.ChatMessageBean;
import com.siyecaoi.yy.model.GiftSendMessage;
import com.siyecaoi.yy.model.MessageBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.LogUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class ChatRecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    SpannableStringBuilder stringBuilder;


    public ChatRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    public interface OnAttentionClicker {
        void onClicker(int userId);
        void onFollowUser(int userId);

        void onOtherRoomClicker(String roomId);
    }

    OnAttentionClicker onAttentionClicker;

    public OnAttentionClicker getOnAttentionClicker() {
        return onAttentionClicker;
    }

    public void setOnAttentionClicker(OnAttentionClicker onAttentionClicker) {
        this.onAttentionClicker = onAttentionClicker;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        RelativeLayout rl_back_voice = helper.getView(R.id.rl_back_voice);
        TextView tv_show_chat = helper.getView(R.id.tv_show_chat);
        TextView tv_end_chat = helper.getView(R.id.tv_end_chat);
        SimpleDraweeView iv_show_chat = helper.getView(R.id.iv_show_chat);
        tv_end_chat.setVisibility(View.GONE);
        iv_show_chat.setVisibility(View.GONE);
        rl_back_voice.setBackgroundResource(R.drawable.bg_round5_chat);
        MessageBean messageBean = new Gson().fromJson(item, MessageBean.class);
        String messageShow;//显示信息
        int resShowid;
        ImageSpan imageSpan;
        String userName;
        ForegroundColorSpan colorSpan;
        ClickableSpan clickableSpan;
        switch (messageBean.getCode()) {
            case 100:
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                LogUtils.e("msg", item);
                ChatMessageBean chatMessageBean1 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(chatMessageBean1.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = chatMessageBean1.getData().getName();
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean1.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append("：");
                stringBuilder.append(chatMessageBean1.getData().getMessageShow());
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 101://礼物消息
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                GiftSendMessage giftSendMessage = new Gson().fromJson(item, GiftSendMessage.class);
//                String[] idsShow = giftSendMessage.getData().getSendId().split(",");
                String[] namesShow = giftSendMessage.getData().getNames().split(",");
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(giftSendMessage.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = giftSendMessage.getData().getName();
                if (userName.length() > 6) {
                    userName = userName.substring(0, 6) + "...";
                }
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(giftSendMessage.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" 送给 ");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.xt_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 4,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                String nameStr;
                if (namesShow.length > 1) {
                    nameStr = namesShow.length + "位嘉宾";

                } else {
                    nameStr = namesShow[0];
                    if (nameStr.length() > 6) {
                        nameStr = nameStr.substring(0, 6) + "...";
                    }
                    clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (onAttentionClicker != null) {
                                    onAttentionClicker.onClicker(Integer.valueOf(giftSendMessage.getData().getSendId()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtils.e("点击失效,id为空");
                            }
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                            /**Remove the underline**/
                            ds.setUnderlineText(false);
                        }
                    };

                }

                stringBuilder.append(nameStr);
                stringBuilder.setSpan(clickableSpan, stringBuilder.length() - nameStr.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - nameStr.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//                for (int i = 0; i < namesShow.length; i++) {
//                    String othername = namesShow[i];
//                    stringBuilder.append(othername);
//                    colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
//                    stringBuilder.setSpan(colorSpan, stringBuilder.length() - othername.length(),
//                            stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    if (i < namesShow.length - 1) {
//                        stringBuilder.append(",");
//                    }
//                }
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());

                String urlImg = giftSendMessage.getData().getImg();
                iv_show_chat.setVisibility(View.VISIBLE);
                tv_end_chat.setVisibility(View.VISIBLE);
                ImageUtils.loadUri(iv_show_chat, urlImg);
                tv_end_chat.setText(" x" + giftSendMessage.getData().getNum());

                break;
            case 102://系统消息
                messageShow = (String) messageBean.getData();
                tv_show_chat.setText(messageShow);
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                break;
            case 103:
                messageShow = (String) messageBean.getData();
                tv_show_chat.setText(messageShow);
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                break;
            case 104:
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                LogUtils.e("msg", item);
                ChatMessageBean chatMessageBean2 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(chatMessageBean2.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = chatMessageBean2.getData().getName();
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean2.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append("：");
                int nowLenght = stringBuilder.length();
                stringBuilder.append(chatMessageBean2.getData().getMessageShow());
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FF3333));
                stringBuilder.setSpan(colorSpan, nowLenght,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 105://设置管理员
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean3 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(chatMessageBean3.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = chatMessageBean3.getData().getName();
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean3.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (chatMessageBean3.getData().getState() == 1) {
                    stringBuilder.append("被设置为管理员");
                } else if (chatMessageBean3.getData().getState() == 2) {
                    stringBuilder.append("被移除管理员");
                }
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 106:
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean4 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
                userName = chatMessageBean4.getData().getName();
                if (userName != null){
                    stringBuilder.append(userName);
                    colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
                    stringBuilder.setSpan(colorSpan, 0,
                            stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                stringBuilder.append("等级提升为 ");
                stringBuilder.append("img");
                Bitmap bitmap = ImageShowUtils.getGradeBitmap(mContext,chatMessageBean4.getData().getGrade());
                imageSpan = new ImageSpan(mContext, bitmap);
                stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean4.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 107://踢出房间
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.FFE795));
                GetOutBean getOutBean = new Gson().fromJson(item, GetOutBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(getOutBean.getData().getBgrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(getOutBean.getData().getBname());
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(getOutBean.getData().getBuid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" 被 ");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.white));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 3,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (getOutBean.getData().getState() == 1) {
                    stringBuilder.append("房主");
                } else if (getOutBean.getData().getState() == 2) {
                    stringBuilder.append("管理员");
                }
                stringBuilder.append(" 踢出房间");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.white));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 4,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 108:// 表情消息
                EmojiMessageBean emojiMessageBean = new Gson().fromJson(item, EmojiMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(emojiMessageBean.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = emojiMessageBean.getData().getName();
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(emojiMessageBean.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append("：");
                int emojiCode = emojiMessageBean.getData().getEmojiCode();

                if (emojiCode >= 128564) {
                    iv_show_chat.setVisibility(View.VISIBLE);
                    ImageUtils.loadDrawableStatic(iv_show_chat, ImageShowUtils.getInstans().getResId(emojiCode, emojiMessageBean.getData().getNumberShow()));
                } else if (emojiCode >= 128552) { //特殊表情
                    stringBuilder.append("img");
                    imageSpan = new ImageSpan(mContext, ImageShowUtils.getInstans().getResId(emojiCode, emojiMessageBean.getData().getNumberShow()));
                    stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    String emoji = new String(Character.toChars(emojiMessageBean.getData().getEmojiCode()));
                    stringBuilder.append(emoji);
                }
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 109:
                stringBuilder = new SpannableStringBuilder();
                ChatMessageBean chatMessageBean5 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder.append(chatMessageBean5.getData().getMessageShow());
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.C11FFD8));
                stringBuilder.setSpan(colorSpan, 0,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append("img");
                imageSpan = new ImageSpan(mContext, R.drawable.attention);
                stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onFollowUser(chatMessageBean5.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, stringBuilder.length() - 3, stringBuilder.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 110://下麦(上麦)
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean6 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(chatMessageBean6.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = chatMessageBean6.getData().getName();
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean6.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (chatMessageBean6.getData().getMessageShow() != null) {
                    stringBuilder.append(" ").append(chatMessageBean6.getData().getMessageShow());
                }
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 111://禁麦
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean111 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();

                if (chatMessageBean111.getData().getMessageShow() != null) {
                    stringBuilder.append(chatMessageBean111.getData().getMessageShow());
                    colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                    stringBuilder.setSpan(colorSpan, 0,
                            stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tv_show_chat.setText(stringBuilder);
                break;
            case 112://加入黑名单
                ChatMessageBean chatMessageBean112 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
                stringBuilder.append("img ");
                resShowid = ImageShowUtils.getGrade(chatMessageBean112.getData().getGrade());
                imageSpan = new ImageSpan(mContext, resShowid);
                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = chatMessageBean112.getData().getName();
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean112.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" 被 ");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.white));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 3,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                stringBuilder.append("加入黑名单");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.white));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 4,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 114://抱他上麦
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean114 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();

                if (chatMessageBean114.getData().getMessageShow() != null) {
                    stringBuilder.append(chatMessageBean114.getData().getMessageShow());
                    colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                    stringBuilder.setSpan(colorSpan, 0,
                            stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tv_show_chat.setText(stringBuilder);
                break;
            case 115://全频道通知消息(探险？)
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean115 = new Gson().fromJson(item, ChatMessageBean.class);

                stringBuilder = new SpannableStringBuilder();

                stringBuilder.append("恭喜 ");
//                stringBuilder.append("img ");
//                imageSpan = new ImageSpan(mContext, R.drawable.gifts);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                String nickName = chatMessageBean115.getData().getNickname();
                stringBuilder.append(nickName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - nickName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean115.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" ");

                stringBuilder.append(chatMessageBean115.getData().getMessageShow());
//                stringBuilder.append("  ");
//                stringBuilder.append("点击这里快速前往");
//                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FF3333));
//                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 8,
//                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                String roomId = chatMessageBean115.getData().getRid();
//                ClickableSpan clickableSpan1 = new ClickableSpan() {
//                    @Override
//                    public void onClick(View widget) {
//                        if (onAttentionClicker != null) {
//                            onAttentionClicker.onOtherRoomClicker(roomId);
//                        }
//                    }
//
//                    @Override
//                    public void updateDrawState(TextPaint ds) {
////                        super.updateDrawState(ds);
//                        /**Remove the underline**/
//                        ds.setUnderlineText(false);
//                    }
//                };
//
//                stringBuilder.setSpan(clickableSpan1, stringBuilder.length() - 8, stringBuilder.length(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 116://座驾消息
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                CarShowMessageBean carShowMessageBean = new Gson().fromJson(item, CarShowMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(carShowMessageBean.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = carShowMessageBean.getData().getUserName();
                if (userName.length() > 6) {
                    userName = userName.substring(0, 6) + "...";
                }
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(carShowMessageBean.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" 驾着 ");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.xt_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 4,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tv_show_chat.setText(stringBuilder);

                String carCover = carShowMessageBean.getData().getCarCover();
                iv_show_chat.setVisibility(View.VISIBLE);
                tv_end_chat.setVisibility(View.VISIBLE);
                ImageUtils.loadUri(iv_show_chat, carCover);
                tv_end_chat.setText(" 进入房间");
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 117://117 进房通知消息（1级及以上）
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean117 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img");
//                resShowid = ImageShowUtils.getGrade(chatMessageBean117.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                String accessName = chatMessageBean117.getData().getName();
                stringBuilder.append(accessName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - accessName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean117.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" ");
                stringBuilder.append("进入房间");

                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 122://全频道通知消息(探险)
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean122 = new Gson().fromJson(item, ChatMessageBean.class);

                stringBuilder = new SpannableStringBuilder();
                stringBuilder.append("恭喜  ");

                String nickName1 = chatMessageBean122.getData().getNickname();
                stringBuilder.append(nickName1);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.egg_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - nickName1.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean122.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                stringBuilder.append(" 探险获得了 ");

                stringBuilder.append(chatMessageBean122.getData().getMessageShow());
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.egg_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - chatMessageBean122.getData().getMessageShow().length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                if (chatMessageBean122.getData().getCost() < 1000) {
                    rl_back_voice.setBackgroundResource(R.drawable.bg_egg_3);
                } else if (chatMessageBean122.getData().getCost() < 20000) {
                    rl_back_voice.setBackgroundResource(R.drawable.bg_egg_2);
                } else {
                    rl_back_voice.setBackgroundResource(R.drawable.bg_egg_1);
                }

                break;
        }

    }


//    else if (emojiCode == 128553) { //爆灯
//        stringBuilder.append("img");
//        imageSpan = new ImageSpan(mContext, R.drawable.deng_1);
//        stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    } else if (emojiCode == 128554) { //举手
//        stringBuilder.append("img");
//        imageSpan = new ImageSpan(mContext, R.drawable.hand_4);
//        stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    }


}
