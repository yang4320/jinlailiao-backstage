package com.jinlailiao.config;

import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.ConfigurationManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Yang Longxiang on 2018/7/17 017.
 */
@Configuration
public class WeappConfig implements CommandLineRunner {

    @Value("${weapp.serverHost}")
    private String serverHost;
    @Value("${weapp.authServerUrl}")
    private String authServerUrl;
    @Value("${weapp.tunnelServerUrl}")
    private String tunnelServerUrl;
    @Value("${weapp.tunnelSignatureKey}")
    private String tunnelSignatureKey;
    @Value("${weapp.networkTimeout}")
    private String networkTimeout;


    public com.qcloud.weapp.Configuration config() throws ConfigurationException {

        com.qcloud.weapp.Configuration configuration = new com.qcloud.weapp.Configuration();
        // 业务服务器访问域名
        configuration.setServerHost(serverHost);
        // 鉴权服务地址
        configuration.setAuthServerUrl(authServerUrl);
        // 信道服务地址
        configuration.setTunnelServerUrl(tunnelServerUrl);
        // 信道服务签名 key
        configuration.setTunnelSignatureKey(tunnelSignatureKey);
        // 网络请求超时设置，单位为秒
        configuration.setNetworkTimeout(Integer.parseInt(networkTimeout));

        com.qcloud.weapp.ConfigurationManager.setup(configuration);

        return configuration;
    }

    @Override
    public void run(String... strings) throws Exception {
        ConfigurationManager.setup(this.config());
    }
}
