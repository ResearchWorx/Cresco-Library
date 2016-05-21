package com.researchworx.cresco.library.utilities;

import com.researchworx.cresco.library.messaging.MsgEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Cresco logger
 * @author V.K. Cody Bumgardner
 * @author Caylin Hickey
 * @version 0.3.3
 */
public class CLogger {
    public enum Level {
        None(-1), Error(0), Warn(1), Info(2), Debug(4), Trace(8);
        private final int level;
        Level(int level) { this.level = level; }
        public int getValue() { return level; }
        public boolean toShow(Level check) { return check.getValue() <= this.getValue(); }
    }
    private String region;
    private String agent;
    private String plugin;
    private Level level;
    private ConcurrentLinkedQueue<MsgEvent> msgOutQueue;

    public CLogger(ConcurrentLinkedQueue<MsgEvent> msgOutQueue, String region, String agent, String plugin) {
        this(msgOutQueue, region, agent, plugin, Level.Info);
    }

    public CLogger(ConcurrentLinkedQueue<MsgEvent> msgOutQueue, String region, String agent, String plugin, Level level) {
        this.region = region;
        this.agent = agent;
        this.plugin = plugin;
        this.level = level;
        this.msgOutQueue = msgOutQueue;
    }

    public void info(String logMessage) {
        if (!this.level.toShow(Level.Info)) return;
        log(logMessage);
    }

    public void info(String logMessage, String ... params) {
        if (!this.level.toShow(Level.Info)) return;
        int replaced = 0;
        while (logMessage.contains("{}") && replaced < params.length)
            logMessage = logMessage.replaceFirst("\\{\\}", params[replaced++]);
        info(logMessage);
    }

    public void debug(String logMessage) {
        if (!this.level.toShow(Level.Debug)) return;
        log(logMessage);
    }

    public void debug(String logMessage, String ... params) {
        if (!this.level.toShow(Level.Debug)) return;
        int replaced = 0;
        while (logMessage.contains("{}") && replaced < params.length)
            logMessage = logMessage.replaceFirst("\\{\\}", params[replaced++]);
        debug(logMessage);
    }

    public void trace(String logMessage) {
        if (!this.level.toShow(Level.Trace)) return;
        log(logMessage);
    }

    public void trace(String logMessage, String ... params) {
        if (!this.level.toShow(Level.Trace)) return;
        int replaced = 0;
        while (logMessage.contains("{}") && replaced < params.length)
            logMessage = logMessage.replaceFirst("\\{\\}", params[replaced++]);
        trace(logMessage);
    }

    public void log(String logMessage) {
        MsgEvent toSend = new MsgEvent(MsgEvent.Type.INFO, region, null, null, logMessage);
        toSend.setParam("src_region", region);
        if (agent != null) {
            toSend.setParam("src_agent", agent);
            if (plugin != null)
                toSend.setParam("src_plugin", plugin);
        }
        toSend.setParam("dst_region", region);
        this.msgOutQueue.offer(toSend);
    }

    public void log(MsgEvent logMessage) {
        this.msgOutQueue.offer(logMessage);
    }

    public void error(String ErrorMessage, String ... params) {
        if (!this.level.toShow(Level.Error)) return;
        int replaced = 0;
        while (ErrorMessage.contains("{}") && replaced < params.length) {
            ErrorMessage = ErrorMessage.replaceFirst("\\{\\}", params[replaced++]);
        }
        error(ErrorMessage);
    }

    public void error(String ErrorMessage) {
        if (!this.level.toShow(Level.Error)) return;
        MsgEvent toSend = new MsgEvent(MsgEvent.Type.ERROR, region, null, null, ErrorMessage);
        toSend.setParam("src_region", region);
        if (agent != null) {
            toSend.setParam("src_agent", agent);
            if (plugin != null)
                toSend.setParam("src_plugin", plugin);
        }
        toSend.setParam("dst_region", region);
        this.msgOutQueue.offer(toSend);
    }

    public Level getLogLevel() {
        return this.level;
    }
    public void setLogLevel(Level level) {
        this.level = level;
    }
}