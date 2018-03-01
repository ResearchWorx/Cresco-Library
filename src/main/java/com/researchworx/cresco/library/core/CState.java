package com.researchworx.cresco.library.core;

import com.researchworx.cresco.library.messaging.CAddr;

/**
 * The {@code CState} class encapsulates a static representation of the current state of a running Cresco Agent that is
 * shared amongst all classes running in the agent's parent JVM. Currently this includes information related to the
 * messaging and routing addresses required by core components of the Cresco framework.
 * <ul>
 *     <li><b>localRegion</b> - The Cresco region name associated with this agent</li>
 *     <li><b>localAgent</b> - The Cresco agent name associated with this agent<br></li>
 *     <li><b>regionalController</b> - The address of the Regional Controller this agent reports to</li>
 *     <li><b>globalController</b> - The address of the Global Controller this agent reports to<br></li>
 *     <li><b>isConnected</b> - The status of Cresco communications on this agent</li>
 * </ul>
 * @author Caylin Hickey
 * @author V.K. Cody Bumgardner
 * @see CAddr
 * @since 0.4.2
 */
public class CState {
    private static String localRegion;
    private static final Object localRegionLock = new Object();
    private static String localAgent;
    private static final Object localAgentLock = new Object();

    private static CAddr regionalController;
    private static final Object regionalControllerLock = new Object();
    private static CAddr globalController;
    private static final Object globalControllerLock = new Object();

    private static Boolean isConnected = false;

    public static String getLocalRegion() {
        synchronized (localRegionLock) {
            return localRegion;
        }
    }

    public static String getLocalAgent() {
        return localAgent;
    }

    public static CAddr getLocalAddress() {
        String currentLocalRegion;
        synchronized (localRegionLock) {
            currentLocalRegion = localRegion;
        }
        String currentLocalAgent;
        synchronized (localAgentLock) {
            currentLocalAgent = localAgent;
        }
        return new CAddr(currentLocalRegion, currentLocalAgent);
    }

    public static CAddr getLocalAddress(String plugin) {
        String currentLocalRegion;
        synchronized (localRegionLock) {
            currentLocalRegion = localRegion;
        }
        String currentLocalAgent;
        synchronized (localAgentLock) {
            currentLocalAgent = localAgent;
        }
        return new CAddr(currentLocalRegion, currentLocalAgent, plugin);
    }

    public static void setLocalAddress(String region, String agent) {
        synchronized (localRegionLock) {
            localRegion = region;
        }
        String currentLocalAgent;
        synchronized (localAgentLock) {
            localAgent = agent;
        }
    }

    public static CAddr getRegionalController() {
        synchronized (regionalControllerLock) {
            return regionalController;
        }
    }

    public static void setRegionalController(CAddr address) {
        synchronized (regionalControllerLock) {
            regionalController = new CAddr(address);
        }
    }

    public static CAddr getGlobalController() {
        synchronized (globalControllerLock) {
            return globalController;
        }
    }

    public static void setGlobalController(CAddr address) {
        synchronized (globalControllerLock) {
            globalController = new CAddr(address);
        }
    }

    public static boolean getConnectedState() {
        return isConnected;
    }

    public static void setConnectedState(Boolean state) {
        isConnected = state;
    }
}
