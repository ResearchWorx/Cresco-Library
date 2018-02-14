package com.researchworx.cresco.library.messaging;

public class CAddr {
    private String region = null;
    private String agent = null;
    private String plugin = null;

    public CAddr() { }

    public CAddr(String region) {
        this.region = region;
    }

    public CAddr(String region, String agent) {
        this(region);
        this.agent = agent;
    }

    public CAddr(String region, String agent, String plugin) {
        this(region, agent);
        this.plugin = plugin;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public String getAgent() {
        return agent;
    }
    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getPlugin() {
        return plugin;
    }
    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CAddr))
            return false;

        CAddr address = (CAddr)o;

        return getRegion().equals(address.getRegion()) &&
                getAgent().equals(address.getAgent()) &&
                getPlugin().equals(address.getPlugin());
    }
}