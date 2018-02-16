package com.researchworx.cresco.library.messaging;

/**
 * Cresco messaging address
 * @author Caylin Hickey
 * @since 0.4.2
 */
public class CAddr {
    /** Regional component of address */
    private String region = null;
    /** Agent component of address */
    private String agent = null;
    /** Agent component of address */
    private String plugin = null;

    /**
     * Default constructor
     */
    public CAddr() { }

    /**
     * Copy existing CAddr
     * @param cAddr     CAddr object to copy from
     */
    public CAddr(CAddr cAddr) {
        if (cAddr == null)
            return;
        this.region = cAddr.getRegion();
        this.agent = cAddr.getAgent();
        this.plugin = cAddr.getPlugin();
    }

    /**
     * Regional address constructor
     * @param region    Region name
     */
    public CAddr(String region) {
        this.region = region;
    }

    /**
     * Agent address constructor
     * @param region    Region name
     * @param agent     Agent name
     */
    public CAddr(String region, String agent) {
        this(region);
        this.agent = agent;
    }

    /**
     * Plugin address constructor
     * @param region    Region name
     * @param agent     Agent name
     * @param plugin    Plugin name/ID
     */
    public CAddr(String region, String agent, String plugin) {
        this(region, agent);
        this.plugin = plugin;
    }

    /**
     * Region getter
     * @return          String name of region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Region setter
     * @param region    Region name
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Agent getter
     * @return          String name of agent
     */
    public String getAgent() {
        return agent;
    }

    /**
     * Agent setter
     * @param agent     Agent name
     */
    public void setAgent(String agent) {
        this.agent = agent;
    }

    /**
     * Plugin getter
     * @return          String name/ID of plugin
     */
    public String getPlugin() {
        return plugin;
    }

    /**
     * Plugin setter
     * @param plugin    Plugin name
     */
    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    /**
     * Equality comparator
     * @param o         Object to check for equality
     * @return          True if this and o are the same, false otherwise
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CAddr)) return false;
        CAddr cAddr = (CAddr)o;
        return ((getRegion() == null && cAddr.getRegion() == null) ||
                getRegion().equals(cAddr.getRegion())) &&
                ((getAgent() == null && cAddr.getAgent() == null) ||
                        getAgent().equals(cAddr.getAgent())) &&
                ((getPlugin() == null && cAddr.getPlugin() == null) ||
                        getPlugin().equals(cAddr.getPlugin()));
    }

    @Override
    public String toString() {
        return "{region=" + getRegion() + ", agent=" + getAgent() + ", plugin=" + getPlugin() + "}";
    }
}