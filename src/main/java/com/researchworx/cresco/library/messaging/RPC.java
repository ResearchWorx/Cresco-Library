package com.researchworx.cresco.library.messaging;

import com.researchworx.cresco.library.utilities.CLogger;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * Cresco remote procedure call helper
 * @author V.K. Cody Bumgardner
 * @author Caylin Hickey
 * @version 0.2.7
 */
public class RPC {
    /** Time between checks for RPC return message (in milliseconds) */
    private static final int CHECK_INTERVAL = 100;
    /** Maximum iterations to check for RPC return message */
    private static final int MAX_INTERVALS = 300;
    /** Cresco logger */
    private CLogger logger;
    /** Communication channel */
    private ConcurrentLinkedQueue<MsgEvent> msgOutQueue;
    /** Central RPC return message repository */
    private ConcurrentMap<String, MsgEvent> rpcMap;
    /** Region of this instance */
    private String region;
    /** Agent of this instance */
    private String agent;
    /** Plugin ID of this instance */
    private String pluginID;

    /**
     * Constructor
     * @param msgOutQueue   Outbound communication channel
     * @param rpcMap        Central return message repository
     * @param region        Region of this instance
     * @param agent         Agent of this instance
     * @param pluginID      Plugin ID of this instance
     * @param logger        Logger to use
     */
    public RPC(ConcurrentLinkedQueue<MsgEvent> msgOutQueue, ConcurrentMap<String, MsgEvent> rpcMap, String region, String agent, String pluginID, CLogger logger) {
        this.logger = logger;
        this.msgOutQueue = msgOutQueue;
        this.rpcMap = rpcMap;
        this.region = region;
        this.agent = agent;
        this.pluginID = pluginID;
    }

    /**
     * Issues a remote procedure call
     * @param msg           Message to send
     * @return              The return message, null if no return is received
     */
    public MsgEvent call(MsgEvent msg) {
        try {
            String callId = java.util.UUID.randomUUID().toString();
            msg.setParam("callId-" + this.region + "-" + this.agent + "-" + this.pluginID, callId);
            this.msgOutQueue.offer(msg);

            int count = 0;
            while (count++ < MAX_INTERVALS) {
                if (this.rpcMap.containsKey(callId)) {
                    MsgEvent callBack;
                    callBack = this.rpcMap.get(callId);
                    this.rpcMap.remove(callId);
                    return callBack;
                }
                Thread.sleep(CHECK_INTERVAL);
            }
        } catch (Exception ex) {
            this.logger.error("call {}", ex.getMessage());
        }
        return null;
    }
}
