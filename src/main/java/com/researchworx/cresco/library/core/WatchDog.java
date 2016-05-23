package com.researchworx.cresco.library.core;

import com.researchworx.cresco.library.messaging.MsgEvent;
import com.researchworx.cresco.library.utilities.CLogger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Cresco WatchDog heartbeat system
 * @author V.K. Cody Bumgardner
 * @author Caylin Hickey
 */
public class WatchDog {
    /** Timer control object */
    private Timer timer;
    /** Control object */
    private boolean running = false;
    /** Start time of the WatchDog timer */
    private long startTS;
    /** Region to report from */
    private String region;
    /** Agent to report from */
    private String agent;
    /** Plugin ID to report from */
    private String pluginID;
    /** Logger instance */
    private CLogger logger;
    /** Configuration instance */
    private Config config;

    /**
     * Constructor
     * @param region        Region to report from
     * @param agent         Agent to report from
     * @param pluginID      Plugin ID to report from
     * @param logger        Logger instance
     * @param config        Configuration instance
     */
    public WatchDog(String region, String agent, String pluginID, CLogger logger, Config config) {
        this.region = region;
        this.agent = agent;
        this.pluginID = pluginID;
        this.logger = logger;
        this.config = config;
    }

    /**
     * Updates the identity broadcast by this WatchDog instance
     * @param region        New Region to report from
     * @param agent         New Agent to report from
     * @param pluginID      New Plugin ID to report from
     */
    public void update(String region, String agent, String pluginID) {
        setRegion(region);
        setAgent(agent);
        setPluginID(pluginID);
        restart();
    }

    /**
     * Updates the parameters of this WatchDog instance
     * @param region        New Region to report from
     * @param agent         New Agent to report from
     * @param pluginID      New Plugin ID to report from
     * @param logger        New CLogger to use for errors
     * @param config        New Config to use for settings
     */
    public void update(String region, String agent, String pluginID, CLogger logger, Config config) {
        setLogger(logger);
        setConfig(config);
        update(region, agent, pluginID);
    }

    /**
     * Starts this WatchDog instance
     * @return              This instance
     */
    public WatchDog start() {
        if (running) return this;
        Long interval = config.getLongParam("watchdogtimer", 5000L);
        startTS = System.currentTimeMillis();

        MsgEvent initial = new MsgEvent(MsgEvent.Type.INFO, region, null, null, "WatchDog timer set to " + interval + " milliseconds");
        initial.setParam("src_region", region);
        initial.setParam("src_agent", agent);
        initial.setParam("src_plugin", pluginID);
        initial.setParam("dst_region", region);
        logger.log(initial);

        timer = new Timer();
        timer.scheduleAtFixedRate(new WatchDogTask(region, agent, pluginID, logger), 500, interval);
        running = true;
        return this;
    }

    /**
     * Restarts this WatchDog instance
     * @return              This instance
     */
    public WatchDog restart() {
        if (running) timer.cancel();
        this.running = false;
        return start();
    }

    /**
     * Stops this WatchDog instance
     * @return              Whether the instance was stopped
     */
    public boolean stop() {
        if (!running) return false;
        timer.cancel();
        running = false;
        return true;
    }

    /**
     * Tick of the WatchDog timer
     */
    private class WatchDogTask extends TimerTask {
        /** Region to report from */
        private String region;
        /** Agent to report from */
        private String agent;
        /** Plugin ID to report from */
        private String pluginID;
        /** Logger channel */
        private CLogger logger;

        /**
         * Constructor
         * @param region        Region to report from
         * @param agent         Agent to report from
         * @param pluginID      Plugin ID to report from
         * @param logger        Logger channel
         */
        WatchDogTask(String region, String agent, String pluginID, CLogger logger) {
            this.region = region;
            this.agent = agent;
            this.pluginID = pluginID;
            this.logger = logger;
        }

        /**
         * Tick
         */
        public void run() {
            MsgEvent tick = new MsgEvent(MsgEvent.Type.WATCHDOG, region, null, null, "WatchDog timer tick.");
            tick.setParam("src_region", this.region);
            tick.setParam("src_agent", this.agent);
            tick.setParam("src_plugin", this.pluginID);
            tick.setParam("dst_region", this.region);
            tick.setParam("runtime", String.valueOf(System.currentTimeMillis() - startTS));
            tick.setParam("timestamp", String.valueOf(System.currentTimeMillis()));
            this.logger.log(tick);
        }
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

    public String getPluginID() {
        return pluginID;
    }
    public void setPluginID(String pluginID) {
        this.pluginID = pluginID;
    }

    public CLogger getLogger() {
        return logger;
    }
    public void setLogger(CLogger logger) {
        this.logger = logger;
    }

    public Config getConfig() {
        return config;
    }
    public void setConfig(Config config) {
        this.config = config;
    }
}
