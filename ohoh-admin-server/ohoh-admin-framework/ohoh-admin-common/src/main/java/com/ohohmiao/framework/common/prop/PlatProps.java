package com.ohohmiao.framework.common.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 平台配置读取
 *
 * @author ohohmiao
 * @date 2023-04-03 15:16
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "plat.config")
public class PlatProps {

    private Boolean encryptTransportSwitch;

    private String smServerPublickey;

    private String smServerPrivatekey;

    private String smClientPublickey;

    private String smClientPrivatekey;

    private String smSecretKey;

    private String smIv;

}
