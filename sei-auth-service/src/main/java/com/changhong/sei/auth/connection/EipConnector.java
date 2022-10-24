package com.changhong.sei.auth.connection;

import com.changhong.sei.auth.dto.EipMailDto;
import com.changhong.sei.auth.util.DateUtils;
import com.changhong.sei.auth.webservice.eipMall.*;

import javax.xml.ws.Holder;
import java.util.HashMap;

/**
 * 对接eip测试待办接口
 * @author Joe
 * @date 2022/4/26
 */
public class EipConnector {

    public static final SvcHdrType svcHdr = new SvcHdrType();
    public static final AppHdrType appHdr = new AppHdrType();
    public static final AppBodyType appBody = new AppBodyType();
    public static final Holder<SvcHdrTypes> svcHdrs = new Holder<>();
    public static final Holder<AppHdrTypes> appHdrs = new Holder<>();
    public static final Holder<AppBodyTypes> appBodys = new Holder<>();
    public static final DONLIMESAGENCYNOTICEINFOSYNC086_Service service = new DONLIMESAGENCYNOTICEINFOSYNC086_Service();
    public static final DONLIMESAGENCYNOTICEINFOSYNC086 sync = service.getDONLIMESAGENCYNOTICEINFOSYNC086SOAP();
    public static final AddNoticeType notice = new AddNoticeType();
    public static final String sourceId = "ESS";
    public static final String destinationId = "EIP";
    public static final String ipAddress = "10.233.0.170";
    public static final HashMap<String, String> map = DateUtils.getStarAndEndDate();
    public static final String bo = "待办通知信息同步";
    public static final String systemName = "出门管理平台系统";
    public static final String systemSort = "A13";
    public static final String mailType = "待办";

    public static SvcHdrTypes addEipMall(EipMailDto eipMailDto){
        svcHdr.setSOURCEID(sourceId);
        svcHdr.setDESTINATIONID(destinationId);
        svcHdr.setTYPE("ADD");
        svcHdr.setBO(bo);
        svcHdr.setIPADDRESS(ipAddress);
        notice.setAccount(eipMailDto.getAccount());
        notice.setMailBody(eipMailDto.getMailBody());
        notice.setMailID(eipMailDto.getMailID());
        notice.setMailSubject(eipMailDto.getMailSubject());
        notice.setMailType(mailType);
        notice.setSystemName(systemName);
        notice.setSystemSort(systemSort);
        notice.setUrl(eipMailDto.getUrl());
        appBody.setAddNotice(notice);
        sync.donlimESAGENCYNOTICEINFOSYNC086(svcHdr, appHdr, appBody,svcHdrs,appHdrs,appBodys);
        /*if("Y".equals(svcHdrs.value.getRCODE())){
            return true;
        }
        return false;*/
        return svcHdrs.value;
    }

    public static boolean deleteEipMall(String mailId){
        svcHdr.setSOURCEID(sourceId);
        svcHdr.setDESTINATIONID(destinationId);
        svcHdr.setTYPE("DELETE");
        svcHdr.setBO(bo);
        svcHdr.setIPADDRESS(ipAddress);
        notice.setMailID(mailId);
        notice.setMailType(mailType);
        notice.setSystemName(systemName);
        notice.setSystemSort(systemSort);
        appBody.setAddNotice(notice);
        sync.donlimESAGENCYNOTICEINFOSYNC086(svcHdr, appHdr, appBody,svcHdrs,appHdrs,appBodys);
        if("Y".equals(svcHdrs.value.getRCODE())){
            return true;
        }
        return false;
    }

    public static boolean updateEipMall(EipMailDto eipMailDto){
        svcHdr.setSOURCEID(sourceId);
        svcHdr.setDESTINATIONID(destinationId);
        svcHdr.setTYPE("DELETE");
        svcHdr.setBO(bo);
        svcHdr.setIPADDRESS(ipAddress);
        notice.setAccount(eipMailDto.getAccount());
        notice.setMailBody(eipMailDto.getMailBody());
        notice.setMailID(eipMailDto.getMailID());
        notice.setMailSubject(eipMailDto.getMailSubject());
        notice.setMailType(mailType);
        notice.setSystemName(systemName);
        notice.setSystemSort(systemSort);
        notice.setUrl(eipMailDto.getUrl());
        appBody.setAddNotice(notice);
        sync.donlimESAGENCYNOTICEINFOSYNC086(svcHdr, appHdr, appBody,svcHdrs,appHdrs,appBodys);
        if("Y".equals(svcHdrs.value.getRCODE())){
            return true;
        }
        return false;
    }
}

