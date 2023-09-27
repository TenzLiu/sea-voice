# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 指定不去忽略包可见的库类的成员
-dontskipnonpubliclibraryclassmembers
#不进行优化，建议使用此选项，
-dontoptimize
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 保护代码中的Annotation不被混淆
-keepattributes *Annotation*
# 避免混淆泛型, 这在JSON实体映射时非常重要
-keepattributes Signature
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

#公司基础库实体
-keep class cn.sinata.xldutils.bean.** { *; }
-keep class cn.sinata.xldutils.utils.** { *; }


#工具类--------------------------------------------------
-keep class com.siyecaoi.yy.utils.** {*;}

#项目数据实体类文件
-keep class com.siyecaoi.yy.bean.**{*;}
-keep class com.siyecaoi.yy.model.**{*;}


#---------------Oss图片上传--------------
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**

#-------------------------------------

#--------------------SpEditTool:SpEditText------------------------
-keep class com.sunhapper.x.spedit.**{*;}
#--------------------------------------------

#-------------------极光推送------------------
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
#-------------------------------------

#-----------------腾讯云IM--------------------
-keep class com.tencent.**{*;}
-dontwarn com.tencent.**
-keep class tencent.**{*;}
-dontwarn tencent.**
-keep class qalsdk.**{*;}
-dontwarn qalsdk.**

#-------------------------------------

#---------------- Facebook--------------
-keep class com.facebook.** {*;}
-keep interface com.facebook.** {*;}
-keep enum com.facebook.** {*;}

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }
-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keep class com.linkedin.** { *; }
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keepattributes Signature

#－－－－－－－－retrofit2－－－－－－－－
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-keep class okio.** { *;}
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}

-dontwarn rx.internal.**
-keep class rx.internal.** { *;}


-dontwarn com.alipay.**
-keep class com.alipay.** { *;}

-dontwarn io.reactivex.**
-keep class io.reactivex.** { *;}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

#apache
-keep class org.apache.** {*;}
-dontwarn  org.apache.**
-keep class android.net.** {*;}
-dontwarn  android.net.**
#华为
-keep class com.huawei.android.** {*;}
-dontwarn  com.huawei.android.**
#google gcm
-keep class com.google.android.gms.** {*;}
-dontwarn  com.google.android.gms.**

-keepattributes Exceptions,InnerClasses

-keepattributes Signature

#百度地图
-keep class com.baidu.** {*;}
-keep class mapsdkvi.com.** {*;}
-dontwarn com.baidu.**

# BaiduNavi SDK
-dontoptimize
-ignorewarnings
-keeppackagenames com.baidu.**
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod

-dontwarn com.baidu.**
-dontwarn com.baidu.navisdk.**
-dontwarn com.baidu.navi.**

-keep class com.baidu.** { *; }
-keep interface com.baidu.** { *; }

-keep class vi.com.gdi.** { *; }

-dontwarn com.google.protobuf.**
-keep class com.google.protobuf.** { *;}
-keep interface com.google.protobuf.** { *;}

-dontwarn com.google.android.support.v4.**
-keep class com.google.android.support.v4.** { *; }
-keep interface com.google.android.support.v4.app.** { *; }
-keep public class * extends com.google.android.support.v4.**
-keep public class * extends com.google.android.support.v4.app.Fragment
-keep class androidx.** {*;}

#-------------高德地图----------------
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}

#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

#--------------------------------------


#----------------netty-------
-dontwarn io.netty.**
-keep class io.netty.** { *;}
-dontwarn org.slf4j.**
-keep class org.slf4j.** { *;}
#----------------netty-------


#------------------android-saripaar------------------
-keep class com.mobsandgeeks.saripaar.** {*;}
-keep @com.mobsandgeeks.saripaar.annotation.ValidateUsing class * {*;}
#-------------------------------------------


# butterknife-------------------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#--------------------------------

# 保留R下面的资源
-keep class **.R$* {
 *;
}
#不混淆资源类下static的
-keepclassmembers class **.R$* {
    public static <fields>;
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#
#----------------------------- WebView(项目中没有可以忽略) -----------------------------
#
#webView需要进行特殊处理
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#在app中与HTML5的JavaScript的交互进行特殊处理
#我们需要确保这些js要调用的原生方法不能够被混淆，于是我们需要做如下处理：
-keepclassmembers class com.ljd.example.JSInterface {
    <methods>;
}

#----------- gson ----------------
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.qiancheng.carsmangersystem.**{*;}
#=-----------------------------------

# Saripaar
-keep class com.mobsandgeeks.saripaar.** {*;}
-keep @com.mobsandgeeks.saripaar.annotation.ValidateUsing class * {*;}


#------------------BaseRecyclerViewAdapterHelper----------
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}
#------------------------------------------------

#-----------------svgaplayer-------------
-keep class com.opensource.svgaplayer.**{*;}
-keep class com.squareup.wire.** { *; }
-keep class com.opensource.svgaplayer.proto.** { *; }
#----------------------------------------

#-----------------bugly-------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#----------------------------------------

#---------------------声网sdk--------------------
-keep class io.agora.**{*;}
#------------------------------------------------

#---------------------FastJson----------------
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}

-keepattributes Signature

-keepclassmembers class * implements java.io.Serializable { *; }
#-----------------------------------------------

#-------------------litepal-------------------------
-keep class org.litepal.** {
    *;
}

-keep class * extends org.litepal.crud.LitePalSupport{
    *;
}
#----------------------------------------------





