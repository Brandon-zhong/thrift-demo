package com.lupf.thriftclient.config;

import java.util.List;

/**
 * @author brandon
 * create on 2020-07-09
 * desc:
 */
public class ThriftServerConfigBean {

    private String appName;
    private String host;
    private int port;
    private List<String> interfaceList;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<String> getInterfaceList() {
        return interfaceList;
    }

    public void setInterfaceList(List<String> interfaceList) {
        this.interfaceList = interfaceList;
    }
}
