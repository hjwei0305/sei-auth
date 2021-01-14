package com.changhong.sei.auth.service.mq;

import com.changhong.sei.auth.dao.AccessRecordDao;
import com.changhong.sei.auth.entity.AccessRecord;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.util.JsonUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

/**
 * 实现功能：访问日志消费者
 * 访问日志在网关产生并推送到kafka中,在auth进行消费
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-14 09:32
 */
@Component
public class AccessRecordConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(AccessRecordConsumer.class);

    @Autowired
    private AccessRecordDao dao;

    @KafkaListener(topics = "SeiGatewayAccessLog")
    public void processMessage(ConsumerRecord<String, String> record) {
        if (Objects.isNull(record)) {
            return;
        }

        AccessLogVo logVo;
        AccessRecord accessRecord;
        SessionUser sessionUser;
        String value = record.value();
        if (LOG.isDebugEnabled()) {
            LOG.debug("received key='{}' message = '{}'", record.key(), value);
        }
        Optional<String> optional = Optional.ofNullable(value);
        if (optional.isPresent()) {
            // 执行业务处理逻辑
            try {
                logVo = JsonUtils.fromJson(value, AccessLogVo.class);
                if (Objects.nonNull(logVo)) {
                    // 跳过消息监听
                    if (StringUtils.endsWith(logVo.getPath(), "/message/unreadCount")) {
                        return;
                    }
                    sessionUser = ContextUtil.getSessionUser(logVo.token);
                    //解析agent字符串
                    UserAgent userAgent = UserAgent.parseUserAgentString(logVo.getUserAgent());

                    accessRecord = new AccessRecord();
                    accessRecord.setType("API");

                    accessRecord.setTenantCode(sessionUser.getTenantCode());
                    accessRecord.setUserId(sessionUser.getUserId());
                    accessRecord.setUserAccount(sessionUser.getLoginAccount());
                    accessRecord.setUserName(sessionUser.getUserName());
                    accessRecord.setAppModule(logVo.getAppModule());
                    accessRecord.setTraceId(logVo.getTraceId());
                    accessRecord.setPath(logVo.getPath());
                    accessRecord.setUrl(logVo.getUrl());
                    accessRecord.setMethod(logVo.getMethod());
                    accessRecord.setDuration(logVo.getDuration());
                    accessRecord.setIp(logVo.getIp());
                    accessRecord.setBrowser(userAgent.getBrowser().getName());
                    accessRecord.setOsName(userAgent.getOperatingSystem().getName());
                    accessRecord.setAccessTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(logVo.getAccessTime()), ZoneId.systemDefault()));

                    dao.save(accessRecord);
                }
            } catch (Exception e) {
                LOG.error("访问日志消费异常!", e);
            }
        }
    }
}
