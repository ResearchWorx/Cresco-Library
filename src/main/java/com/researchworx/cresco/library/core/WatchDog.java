package com.researchworx.cresco.library.core;

import com.researchworx.cresco.library.messaging.MsgEvent;
import com.researchworx.cresco.library.utilities.CLogger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Cresco WatchDog heartbeat system
 * @author V.K. Cody Bumgardner
 * @author Caylin Hickey
 * @version 0.3.1
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
     * Starts this WatchDog instance
     * @return              This instance
     */
    public WatchDog start() {
        if (this.running) return this;
        Long interval = config.getLongParam("watchdogtimer", 5000L);
        this.startTS = System.currentTimeMillis();

        MsgEvent initial = new MsgEvent(MsgEvent.Type.INFO, this.region, this.agent, this.pluginID, "WatchDog timer set to " + interval + " milliseconds");
        initial.setParam("src_region", this.region);
        initial.setParam("src_agent", this.agent);
        initial.setParam("src_plugin", this.pluginID);
        initial.setParam("dst_region", this.region);
        this.logger.log(initial);

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new WatchDogTask(this.region, this.agent, this.pluginID, this.logger), 500, interval);
        return this;
    }

    /**
     * Restarts this WatchDog instance
     * @return              This instance
     */
    public WatchDog restart() {
        if (this.running) this.timer.cancel();
        this.running = false;
        return start();
    }

    /**
     * Stops this WatchDog instance
     * @return              Whether the instance was stopped
     */
    public boolean stop() {
        if (!this.running) return false;
        this.timer.cancel();
        this.running = false;
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
            MsgEvent tick = new MsgEvent(MsgEvent.Type.WATCHDOG, this.region, null, null, "WatchDog timer tick.");
            tick.setParam("src_region", this.region);
            tick.setParam("src_agent", this.agent);
            tick.setParam("src_plugin", this.pluginID);
            tick.setParam("dst_region", this.region);
            tick.setParam("runtime", String.valueOf(System.currentTimeMillis() - startTS));
            tick.setParam("timestamp", String.valueOf(System.currentTimeMillis()));
            this.logger.log(tick);
        }
    }
}
