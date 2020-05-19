package com.changhong.sei.auth.common.weixin;

import com.changhong.sei.auth.common.RandomUtils;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.util.SerializeUtils;
import com.changhong.sei.util.Signature;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-18 08:24
 */
public class WeChatUtil {
    private static final Logger log = LoggerFactory.getLogger(WeChatUtil.class);

    /**
     * 获取access_token
     */
    private static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    /**
     * 获取访问用户身份
     */
    private static final String OAUTH_GETUSERINFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";
    /**
     * 获取访问用户
     */
    private static final String GET_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=%s&userid=%s";
    /**
     * 获取企业的jsapi_ticket
     */
    private static final String GET_JSAPI_TICKET = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=%s";

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static Map<String, Object> httpRequest(String requestUrl, String requestMethod, String outputStr) {
        // log.error("发起https请求并获取结果 :"+requestUrl+","+requestMethod+","+outputStr);
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setConnectTimeout(60 * 1000);
            httpUrlConn.setReadTimeout(60 * 1000);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            httpUrlConn.connect();
            // if ("GET".equalsIgnoreCase(requestMethod))
            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

            // 返回map
            map = JsonUtils.fromJson(buffer.toString(), Map.class);
        } catch (ConnectException ce) {
            log.error("Connection Timed Out......", ce);
        } catch (Exception e) {
            String result = String.format("Https Request Error:%s", e);
            log.error(result, e);
        }
        return map;
    }

    /**
     * 根据code调微信接口获取USERID
     */
    public static Map<String, Object> getUserInfo(String accessToken, String code) {
        String url = String.format(OAUTH_GETUSERINFO_URL, accessToken, code);
        Map<String, Object> result = httpRequest(url, "GET", null);
        return result;
    }

    /**
     * 通过userid 从微信获取用户
     */
    public static WxUser getWxUser(String accessToken, String userid) {
        WxUser user = null;
        String url = String.format(GET_USER_URL, accessToken, userid);
        Map<String, Object> returnMap = httpRequest(url, "GET", null);
        int errcode = MapUtils.getIntValue(returnMap, "errcode");
        if (errcode == 0) {
            try {
                user = SerializeUtils.convertMap(WxUser.class, returnMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    /**
     * 调用微信接口获取accessToken
     * 将取到的ACCESS_TOKEN存入数据库或者缓存中。每2个小时更新一次
     *
     * @param cropId     企业号ID
     * @param cropSecret 企业号管理组对应的secret
     */
    public static String getAccessToken(String cropId, String cropSecret) {
        String accessToken = "";
        // 添加URL中的cropid和cropsecret的值
        String url = String.format(ACCESS_TOKEN_URL, cropId, cropSecret);
        // 根据url通过https请求获取accesstoken
        Map<String, Object> returnMap = WeChatUtil.httpRequest(url, "GET", null);
        if (null != returnMap && returnMap.size() > 0) {
            // access_token
            accessToken = MapUtils.getString(returnMap, "access_token");
            // accessToken有效时间expires_in：7200秒
            // Integer expires_in = MapUtils.getInteger(returnMap, "expires_in");
        } else {
            log.error("accesstoken获取失败");
        }
        return accessToken;
    }

    /**
     * 获取企业的jsapi_ticket
     */
    public static String getJsApiTicket(String accessToken) {
        String ticket = "";
        // 获取企业的jsapi_ticket URL
        String url = String.format(GET_JSAPI_TICKET, accessToken);
        // 根据url通过https请求获取accesstoken
        Map<String, Object> returnMap = WeChatUtil.httpRequest(url, "GET", null);
        if (null != returnMap && returnMap.size() > 0) {
            // access_token
            ticket = MapUtils.getString(returnMap, "ticket");
        } else {
            log.error("accesstoken获取失败");
        }
        return ticket;
    }

    /**
     * 微信接口返回代码转换信息
     */
    public static String convertErrorCode2Msg(int errorcode) {
        String errmsg = "";
        switch (errorcode) {
            case -1:
                errmsg = "系统繁忙 ";
                break;
            case 0:
                errmsg = "请求成功 ";
                break;
            case 40001:
                errmsg = "获取access_token时Secret错误，或者access_token无效 ";
                break;
            case 40002:
                errmsg = "不合法的凭证类型";
                break;
            case 40003:
                errmsg = "不合法的UserID";
                break;
            case 40004:
                errmsg = "不合法的媒体文件类型 ";
                break;
            case 40005:
                errmsg = "不合法的文件类型 ";
                break;
            case 40006:
                errmsg = "不合法的文件大小";
                break;
            case 40007:
                errmsg = "不合法的媒体文件id";
                break;
            case 40008:
                errmsg = "不合法的消息类型 ";
                break;
            case 40013:
                errmsg = "不合法的corpid ";
                break;
            case 40014:
                errmsg = "不合法的access_token";
                break;
            case 40015:
                errmsg = "不合法的菜单类型";
                break;
            case 40016:
                errmsg = "不合法的按钮个数";
                break;
            case 40017:
                errmsg = "不合法的按钮类型";
                break;
            case 40018:
                errmsg = "不合法的按钮名字长度";
                break;
            case 40019:
                errmsg = "不合法的按钮KEY长度";
                break;
            case 40020:
                errmsg = "不合法的按钮URL长度 ";
                break;
            case 40021:
                errmsg = "不合法的菜单版本号 ";
                break;
            case 40022:
                errmsg = "不合法的子菜单级数";
                break;
            case 40023:
                errmsg = "不合法的子菜单按钮个数";
                break;
            case 40024:
                errmsg = "不合法的子菜单按钮类型";
                break;
            case 40025:
                errmsg = "不合法的子菜单按钮名字长度";
                break;
            case 40026:
                errmsg = "不合法的子菜单按钮KEY长度";
                break;
            case 40027:
                errmsg = "不合法的子菜单按钮URL长度";
                break;
            case 40028:
                errmsg = "不合法的自定义菜单使用员工";
                break;
            case 40029:
                errmsg = "不合法的oauth_code";
                break;
            case 40031:
                errmsg = "不合法的UserID列表";
                break;
            case 40032:
                errmsg = "不合法的UserID列表长度";
                break;
            case 40033:
                errmsg = "不合法的请求字符，不能包含\\uxxxx格式的字符 ";
                break;
            case 40035:
                errmsg = "不合法的参数 ";
                break;
            case 40038:
                errmsg = "不合法的请求格式 ";
                break;
            case 40039:
                errmsg = "不合法的URL长度";
                break;
            case 40040:
                errmsg = "不合法的插件token";
                break;
            case 40041:
                errmsg = "不合法的插件id";
                break;
            case 40042:
                errmsg = "不合法的插件会话";
                break;
            case 40048:
                errmsg = "url中包含不合法domain";
                break;
            case 40054:
                errmsg = "不合法的子菜单url域名";
                break;
            case 40055:
                errmsg = "不合法的按钮url域名 ";
                break;
            case 40056:
                errmsg = "不合法的agentid";
                break;
            case 40057:
                errmsg = "不合法的callbackurl";
                break;
            case 40058:
                errmsg = "不合法的红包参数 ";
                break;
            case 40059:
                errmsg = "不合法的上报地理位置标志位 ";
                break;
            case 40060:
                errmsg = "设置上报地理位置标志位时没有设置callbackurl";
                break;
            case 40061:
                errmsg = "设置应用头像失败";
                break;
            case 40062:
                errmsg = "不合法的应用模式";
                break;
            case 40063:
                errmsg = "红包参数为空";
                break;
            case 40064:
                errmsg = "管理组名字已存在";
                break;
            case 40065:
                errmsg = "不合法的管理组名字长度";
                break;
            case 40066:
                errmsg = "不合法的部门列表";
                break;
            case 40067:
                errmsg = "标题长度不合法 ";
                break;
            case 40068:
                errmsg = "不合法的标签ID";
                break;
            case 40069:
                errmsg = "不合法的标签ID列表";
                break;
            case 40070:
                errmsg = "列表中所有标签（用户）ID都不合法  ";
                break;
            case 40071:
                errmsg = "不合法的标签名字，标签名字已经存在 ";
                break;
            case 40072:
                errmsg = "不合法的标签名字长度";
                break;
            case 40073:
                errmsg = "不合法的openid";
                break;
            case 40074:
                errmsg = "news消息不支持指定为高保密消息";
                break;
            case 41001:
                errmsg = "缺少access_token参数 ";
                break;
            case 41002:
                errmsg = "缺少corpid参数";
                break;
            case 41003:
                errmsg = "缺少refresh_token参数";
                break;
            case 41004:
                errmsg = "缺少secret参数";
                break;
            case 41005:
                errmsg = "缺少多媒体文件数据";
                break;
            case 41006:
                errmsg = "缺少media_id参数";
                break;
            case 41007:
                errmsg = "缺少子菜单数据";
                break;
            case 41008:
                errmsg = "缺少oauth code";
                break;
            case 41009:
                errmsg = "缺少UserID";
                break;
            case 41010:
                errmsg = "缺少url";
                break;
            case 41011:
                errmsg = "缺少agentid";
                break;
            case 41012:
                errmsg = "缺少应用头像mediaid";
                break;
            case 41013:
                errmsg = "缺少应用名字";
                break;
            case 41014:
                errmsg = "缺少应用描述";
                break;
            case 41015:
                errmsg = "缺少Content";
                break;
            case 41016:
                errmsg = "缺少标题";
                break;
            case 41017:
                errmsg = "缺少标签ID";
                break;
            case 41018:
                errmsg = "缺少标签名字 ";
                break;
            case 42001:
                errmsg = "access_token超时 ";
                break;
            case 42002:
                errmsg = "refresh_token超时";
                break;
            case 42003:
                errmsg = "oauth_code超时 ";
                break;
            case 42004:
                errmsg = "插件token超时";
                break;
            case 43001:
                errmsg = "需要GET请求";
                break;
            case 43002:
                errmsg = "需要POST请求";
                break;
            case 43003:
                errmsg = "需要HTTPS";
                break;
            case 43004:
                errmsg = "需要接收者关注";
                break;
            case 43005:
                errmsg = "需要好友关系";
                break;
            case 43006:
                errmsg = "需要订阅";
                break;
            case 43007:
                errmsg = "需要授权";
                break;
            case 43008:
                errmsg = "需要支付授权";
                break;
            case 43009:
                errmsg = "需要员工已关注";
                break;
            case 43010:
                errmsg = "需要处于回调模式";
                break;
            case 43011:
                errmsg = "需要企业授权";
                break;
            case 44001:
                errmsg = "多媒体文件为空";
                break;
            case 44002:
                errmsg = "POST的数据包为空";
                break;
            case 44003:
                errmsg = "图文消息内容为空";
                break;
            case 44004:
                errmsg = "文本消息内容为空";
                break;
            case 45001:
                errmsg = "多媒体文件大小超过限制";
                break;
            case 45002:
                errmsg = "消息内容超过限制";
                break;
            case 45003:
                errmsg = "标题字段超过限制";
                break;
            case 45004:
                errmsg = "描述字段超过限制";
                break;
            case 45005:
                errmsg = "链接字段超过限制";
                break;
            case 45006:
                errmsg = "图片链接字段超过限制";
                break;
            case 45007:
                errmsg = "语音播放时间超过限制";
                break;
            case 45008:
                errmsg = "图文消息超过限制";
                break;
            case 45009:
                errmsg = "接口调用超过限制";
                break;
            case 45010:
                errmsg = "创建菜单个数超过限制";
                break;
            case 45015:
                errmsg = "回复时间超过限制";
                break;
            case 45016:
                errmsg = "系统分组，不允许修改";
                break;
            case 45017:
                errmsg = "分组名字过长";
                break;
            case 45018:
                errmsg = "分组数量超过上限";
                break;
            case 45024:
                errmsg = "账号数量超过上限";
                break;
            case 46001:
                errmsg = "不存在媒体数据";
                break;
            case 46002:
                errmsg = "不存在的菜单版本";
                break;
            case 46003:
                errmsg = "不存在的菜单数据";
                break;
            case 46004:
                errmsg = "不存在的员工";
                break;
            case 47001:
                errmsg = "解析JSON/XML内容错误";
                break;
            case 48002:
                errmsg = "Api禁用";
                break;
            case 50001:
                errmsg = "redirect_uri未授权";
                break;
            case 50002:
                errmsg = "员工不在权限范围";
                break;
            case 50003:
                errmsg = "应用已停用";
                break;
            case 50004:
                errmsg = "员工状态不正确（未关注状态） ";
                break;
            case 50005:
                errmsg = "企业已禁用";
                break;
            case 60001:
                errmsg = "部门长度不符合限制";
                break;
            case 60002:
                errmsg = "部门层级深度超过限制";
                break;
            case 60003:
                errmsg = "部门不存在";
                break;
            case 60004:
                errmsg = "父亲部门不存在";
                break;
            case 60005:
                errmsg = "不允许删除有成员的部门";
                break;
            case 60006:
                errmsg = "不允许删除有子部门的部门";
                break;
            case 60007:
                errmsg = "不允许删除根部门";
                break;
            case 60008:
                errmsg = "部门名称已存在";
                break;
            case 60009:
                errmsg = "部门名称含有非法字符";
                break;
            case 60010:
                errmsg = "部门存在循环关系";
                break;
            case 60011:
                errmsg = "管理员权限不足，（user/department/agent）无权限";
                break;
            case 60012:
                errmsg = "不允许删除默认应用";
                break;
            case 60013:
                errmsg = "不允许关闭应用";
                break;
            case 60014:
                errmsg = "不允许开启应用";
                break;
            case 60015:
                errmsg = "不允许修改默认应用可见范围";
                break;
            case 60016:
                errmsg = "不允许删除存在成员的标签";
                break;
            case 60017:
                errmsg = "不允许设置企业";
                break;
            case 60102:
                errmsg = "UserID已存在";
                break;
            case 60103:
                errmsg = "手机号码不合法";
                break;
            case 60104:
                errmsg = "手机号码已存在";
                break;
            case 60105:
                errmsg = "邮箱不合法";
                break;
            case 60106:
                errmsg = "邮箱已存在";
                break;
            case 60107:
                errmsg = "微信号不合法";
                break;
            case 60108:
                errmsg = "微信号已存在";
                break;
            case 60109:
                errmsg = "QQ号已存在";
                break;
            case 60110:
                errmsg = "部门个数超出限制";
                break;
            case 60111:
                errmsg = "UserID不存在";
                break;
            case 60112:
                errmsg = "成员姓名不合法";
                break;
            case 60113:
                errmsg = "身份认证信息（微信号/手机/邮箱）不能同时为空 ";
                break;
            case 60114:
                errmsg = "性别不合法";
                break;
            default:
                errmsg = "没有此错误码！ ";
                break;
        }
        return errmsg;
    }
}
