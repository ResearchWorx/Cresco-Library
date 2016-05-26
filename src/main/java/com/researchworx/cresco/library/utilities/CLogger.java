package com.researchworx.cresco.library.utilities;

import com.researchworx.cresco.library.messaging.MsgEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Cresco logger
 * @author V.K. Cody Bumgardner
 * @author Caylin Hickey
 * @since 0.1.0
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

    public void error(String logMessage) {
        if (!level.toShow(Level.Error)) return;
        log(logMessage, Level.Error);
    }

    public void error(String logMessage, String ... params) {
        if (!level.toShow(Level.Error)) return;
        error(replaceBrackets(logMessage));
    }

    public void warn(String logMessage) {
        if (!level.toShow(Level.Warn)) return;
        log(logMessage, Level.Warn);
    }

    public void warn(String logMessage, String ... params) {
        if (!level.toShow(Level.Warn)) return;
        warn(replaceBrackets(logMessage));
    }

    public void info(String logMessage) {
        if (!level.toShow(Level.Info)) return;
        log(logMessage, Level.Info);
    }

    public void info(String logMessage, String ... params) {
        if (!level.toShow(Level.Info)) return;
        info(replaceBrackets(logMessage));
    }

    public void debug(String logMessage) {
        if (!level.toShow(Level.Debug)) return;
        log(logMessage, Level.Debug);
    }

    public void debug(String logMessage, String ... params) {
        if (!level.toShow(Level.Debug)) return;
        debug(replaceBrackets(logMessage));
    }

    public void trace(String logMessage) {
        if (!level.toShow(Level.Trace)) return;
        log(logMessage, Level.Trace);
    }

    public void trace(String logMessage, String ... params) {
        if (!level.toShow(Level.Trace)) return;
        trace(replaceBrackets(logMessage));
    }

    public void log(String logMessage, Level level) {
        MsgEvent toSend = new MsgEvent(MsgEvent.Type.LOG, region, null, null, logMessage);
        toSend.setParam("src_region", region);
        if (agent != null) {
            toSend.setParam("src_agent", agent);
            if (plugin != null)
                toSend.setParam("src_plugin", plugin);
        }
        toSend.setParam("dst_region", region);
        toSend.setParam("log_level", level.name());
        log(toSend);
    }

    public void log(MsgEvent logMessage) {
        msgOutQueue.offer(logMessage);
    }

    public Level getLogLevel() {
        return level;
    }

    public void setLogLevel(Level level) {
        this.level = level;
    }

    private String replaceBrackets(String logMessage, String ... params) {
        int replaced = 0;
        while (logMessage.contains("{}") && replaced < params.length)
            logMessage = logMessage.replaceFirst("\\{\\}", params[replaced++]);
        return logMessage;
    }
}