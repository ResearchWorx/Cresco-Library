package com.researchworx.cresco.library.core;

import com.researchworx.cresco.library.messaging.MsgEvent;
import com.researchworx.cresco.library.utilities.CLogger;

import java.util.Timer;
import java.util.TimerTask;

public class WatchDog {
    private Timer timer;
    private boolean running = false;
    private long interval;
    private long startTS;

    private String region;
    private String agent;
    private String plugin;
    private CLogger clog;
    private Config config;

    public WatchDog(String region, String agent, String plugin, CLogger clog, Config config) {
        this.region = region;
        this.agent = agent;
        this.plugin = plugin;
        this.clog = clog;
        this.config = config;
        this.interval = config.getLongParam("", "watchdogtimer", 5000L);
        startTS = System.currentTimeMillis();

        MsgEvent initial = new MsgEvent(MsgEvent.Type.INFO, this.region, this.agent, this.plugin, "WatchDog timer set to " + this.interval + " milliseconds");
        initial.setParam("src_region", this.region);
        initial.setParam("src_agent", this.agent);
        initial.setParam("src_plugin", this.plugin);
        initial.setParam("dst_region", this.region);
        this.clog.log(initial);

        start();
    }

    private class WatchDogTask extends TimerTask {
        private String region;
        private String agent;
        private String plugin;
        private Long interval;
        private CLogger clog;

        WatchDogTask(String region, String agent, String plugin, Long interval, CLogger clog) {
            this.region = region;
            this.agent = agent;
            this.plugin = plugin;
            this.interval = interval;
            this.clog = clog;
        }

        public void run() {
            long runTime = System.currentTimeMillis() - startTS;
            MsgEvent tick = new MsgEvent(MsgEvent.Type.WATCHDOG, this.region, null, null, "WatchDog timer set to " + this.interval + " milliseconds");
            tick.setParam("src_region", this.region);
            tick.setParam("src_agent", this.agent);
            tick.setParam("src_plugin", this.plugin);
            tick.setParam("dst_region", this.region);
            tick.setParam("runtime", String.valueOf(runTime));
            tick.setParam("timestamp", String.valueOf(System.currentTimeMillis()));
            this.clog.log(tick);
        }
    }

    public WatchDog start() {
        if (this.running) return this;
        this.interval = this.config.getLongParam("", "watchdogtimer", 5000L);
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new WatchDogTask(this.region, this.agent, this.plugin, this.interval, this.clog), 500, this.interval);
        return this;
    }

    public void restart() {
        if (this.running) this.timer.cancel();
        this.running = false;
        start();
    }

    public boolean stop() {
        if (!this.running) return false;
        this.timer.cancel();
        this.running = false;
        return true;
    }
}
