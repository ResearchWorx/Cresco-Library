package com.researchworx.cresco.library.messaging;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * The {@code MsgEvent} class is the core unit of data transmission in the
 * Cresco framework. The simpliest way to instantiate the class is with:
 * <blockquote><pre>
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent--">MsgEvent</a>();
 * </pre></blockquote>
 * A {@code MsgEvent} object contains addresses corresponding to the source, destination,
 * and in the case of Remote Procedure Call (RPC) messages, the message originator. Other constructors
 * define the message type, destination, and initial parameters if known:
 * <blockquote><pre>
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.CAddr-">MsgEvent</a>(new <a href="CAddr.html">CAddr</a>(...));
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.MsgEvent.Type-">MsgEvent</a>(<a href="MsgEvent.Type.html">MsgEvent.Type</a>);
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.MsgEvent.Type-com.researchworx.cresco.library.messaging.CAddr-">MsgEvent</a>(<a href="MsgEvent.Type.html">MsgEvent.Type</a>, new <a href="CAddr.html">CAddr</a>(...));
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.MsgEvent.Type-com.researchworx.cresco.library.messaging.CAddr-java.util.Map-">MsgEvent</a>(<a href="MsgEvent.Type.html">MsgEvent.Type</a>, new <a href="CAddr.html">CAddr</a>(...), Map&lt;String, String&gt;);
 * </pre></blockquote>
 * Like other messaging protocols, {@code MsgEvent} contains a message payload in the form of a
 * Java Map. Payload access works as follows:
 * <blockquote><pre>
 *     msgEvent.<a href="MsgEvent.html#setParam-java.lang.String-java.lang.String-">setParam</a>("param_name", "param_value");  // sets a payload value
 *     msgEvent.<a href="MsgEvent.html#setParams-java.util.Map-">setParams</a>(Map);                         // replaces all payload values with Map values
 *     msgEvent.<a href="MsgEvent.html#addParams-java.util.Map-">addParams</a>(Map);                         // adds all Map values to existing payload values
 *     msgEvent.<a href="MsgEvent.html#getParam-java.lang.String-">getParam</a>("param_name");                 // retrieves a payload value
 *     msgEvent.<a href="MsgEvent.html#getParams--">getParams</a>();                            // returns a Map of all payload values
 * </pre></blockquote>
 * @author V.K. Cody Bumgardner
 * @author Caylin Hickey
 * @see com.researchworx.cresco.library.messaging.CAddr
 * @since 0.1.0
 */
@XmlRootElement
public class MsgEvent {

    /**
     * MsgEvent types enumeration
     */
    public enum Type {
        CONFIG, DISCOVER, ERROR, EXEC, GC, INFO, KPI, LOG, WATCHDOG
    }

    /**
     * MsgEvent scopes enumeration
     */
    public enum Scope {
        GLOBAL, REGIONAL, AGENT, CONTROLLER, PLUGIN, SELF
    }

    /** Type of message */
    private Type type = Type.INFO;
    /** Scope of message */
    private Scope scope = Scope.PLUGIN;
    /** Source address of message */
    private CAddr source = null;
    /** Destination address of message */
    private CAddr destination = null;
    /** RPC origination address of message (if RPC) */
    private CAddr rpcCaller = null;
    /** RPC call ID for message identification */
    private String rpcCallID = null;
    /** Custom message parameters */
    private Map<String, String> params = new HashMap<>();

    /**
     * Default constructor
     */
    public MsgEvent() { }

    /**
     * Constructor
     * @param destination   Message destination address
     */
    public MsgEvent(CAddr source, CAddr destination) {
        this();
        setSource(source);
        setDestination(destination);
    }

    /**
     * Constructor
     * @param destination   Message destination address
     * @param params        Map of custom message parameters
     */
    public MsgEvent(CAddr source, CAddr destination, Map<String, String> params) {
        this(source, destination);
        setParams(params);
    }

    /**
     * Constructor
     * @param type          Message type
     */
    public MsgEvent(Type type) {
        this();
        setType(type);
    }

    /**
     * Constructor
     * @param type          Message type
     * @param destination   Message destination address
     */
    public MsgEvent(Type type, CAddr source, CAddr destination) {
        this(source, destination);
        setType(type);
    }

    /**
     * Constructor
     * @param type          Message type
     * @param destination   Message destination address
     * @param params        Map of custom message parameters
     */
    public MsgEvent(Type type, CAddr source, CAddr destination, Map<String, String> params) {
        this(type, source, destination);
        setParams(params);
    }

    /*/**
     * Constructor
     * @param dstRegion     Destination region name
     */
    /*public MsgEvent(String dstRegion) {
        this();
        setDestination(dstRegion);
    }*/

    /*/**
     * Constructor
     * @param dstRegion     Destination region name
     * @param dstAgent      Destination agent name
     */
    /*public MsgEvent(String dstRegion, String dstAgent) {
        this();
        setDestination(dstRegion, dstAgent);
    }*/

    /*/**
     * Constructor
     * @param dstRegion     Destination region name
     * @param dstAgent      Destination agent name
     * @param dstPlugin     Destination plugin name
     */
    /*public MsgEvent(String dstRegion, String dstAgent, String dstPlugin) {
        this();
        setDestination(dstRegion, dstAgent, dstPlugin);
    }*/

    /*/**
     * Constructor
     * @param type          (MsgEvent.Type) Message type
     * @param dstRegion     Destination region name
     */
    /*public MsgEvent(Type type, String dstRegion) {
        this();
        setType(type);
        setDestination(dstRegion);
    }*/

    /*/**
     * Constructor
     * @param type          (MsgEvent.Type) Message type
     * @param dstRegion     Destination region name
     * @param dstAgent      Destination agent name
     */
    /*public MsgEvent(Type type, String dstRegion, String dstAgent) {
        this();
        setType(msgType);
        setDestination(dstRegion, dstAgent);
    }*/

    /*/**
     * Constructor
     * @param type          (MsgEvent.Type) Message type
     * @param dstRegion     Destination region name
     * @param dstAgent      Destination agent name
     * @param dstPlugin     Destination plugin name
     */
    /*public MsgEvent(Type type, String dstRegion, String dstAgent, String dstPlugin) {
        this();
        setType(msgType);
        setDestination(dstRegion, dstAgent, dstPlugin);
    }*/

    /**
     * Constructor (Deprecated)
     * @param type          Message type
     * @param dstRegion     Unused region name
     * @param dstAgent      Unused agent name
     * @param dstPlugin     Unused plugin name
     * @param msgBody       Message body parameter
     */
    @Deprecated
    public MsgEvent(Type type, String dstRegion, String dstAgent, String dstPlugin, String msgBody) {
        this();
        setType(type);
        setDestination(dstRegion, dstAgent, dstPlugin);
        setParam("msg", msgBody);
    }

    /*/**
     * Constructor
     * @param type          (MsgEvent.Type) Message type
     * @param dstRegion     Unused region name
     * @param params        Message custom parameters
     */
    /*public MsgEvent(Type type, String dstRegion, Map<String, String> params) {
        this();
        setType(type);
        setDestination(dstRegion);
        setParams(params);
    }*/

    /*/**
     * Constructor
     * @param msgType       (MsgEvent.Type) Message type
     * @param dstRegion     Unused region name
     * @param dstAgent      Unused agent name
     * @param params        Message custom parameters
     */
    /*public MsgEvent(Type msgType, String dstRegion, String dstAgent, Map<String, String> params) {
        this();
        setType(msgType);
        setDestination(dstRegion, dstAgent);
        setParams(params);
    }*/

    /*/**
     * Constructor
     * @param msgType       (MsgEvent.Type) Message type
     * @param dstRegion     Unused region name
     * @param dstAgent      Unused agent name
     * @param dstPlugin     Unused plugin name
     * @param params        Message custom parameters
     */
    /*public MsgEvent(Type msgType, String dstRegion, String dstAgent, String dstPlugin, Map<String, String> params) {
        this();
        setType(msgType);
        setDestination(dstRegion, dstAgent, dstPlugin);
        setParams(params);
    }*/

    /**
     * Message source getter
     * @return      Message source address
     */
    @XmlJavaTypeAdapter(CAddrAdapter.class)
    public CAddr getSource() {
        return source;
    }

    /**
     * Message source region getter
     * @return      Message source region name
     */
    public String getSourceRegion() {
        if (source != null && source.getRegion() != null)
            return source.getRegion();
        return getParam("src_region");
    }

    /**
     * Message source agent getter
     * @return      Message source agent name
     */
    public String getSourceAgent() {
        if (source != null && source.getAgent() != null)
            return source.getAgent();
        return getParam("src_agent");
    }

    /**
     * Message source plugin getter
     * @return      Message source plugin name
     */
    public String getSourcePlugin() {
        if (source != null && source.getPlugin() != null)
            return source.getPlugin();
        return getParam("src_plugin");
    }

    /**
     * Message source setter
     * @param address   Message source address
     */
    public void setSource(CAddr address) {
        if (address == null)
            return;
        if (address.getRegion() != null)
            setParam("src_region", address.getRegion());
        if (address.getAgent() != null)
            setParam("src_agent", address.getAgent());
        if (address.getPlugin() != null)
            setParam("src_plugin", address.getPlugin());
        this.source = new CAddr(address);
    }

    /**
     * Message source setter
     * @param region    Message source region
     */
    public void setSource(String region) {
        setParam("src_region", region);
        this.source = new CAddr(region);
    }

    /**
     * Message source setter
     * @param region    Message source region
     * @param agent     Message source agent
     */
    public void setSource(String region, String agent) {
        setParam("src_region", region);
        setParam("src_agent", agent);
        this.source = new CAddr(region, agent);
    }

    /**
     * Message source setter
     * @param region    Message source region
     * @param agent     Message source agent
     * @param plugin    Message source plugin
     */
    public void setSource(String region, String agent, String plugin) {
        setParam("src_region", region);
        setParam("src_agent", agent);
        setParam("src_plugin", plugin);
        this.source = new CAddr(region, agent, plugin);
    }

    /**
     * Message destination getter
     * @return     Message destination address
     */
    @XmlJavaTypeAdapter(CAddrAdapter.class)
    public CAddr getDestination() {
        return destination;
    }

    /**
     * Message destination region getter
     * @return      Message destination region name
     */
    public String getDestinationRegion() {
        if (destination != null && destination.getRegion() != null)
            return destination.getRegion();
        return getParam("dst_region");
    }

    /**
     * Message destination agent getter
     * @return      Message destination agent name
     */
    public String getDestinationAgent() {
        if (destination != null && destination.getAgent() != null)
            return destination.getAgent();
        return getParam("dst_agent");
    }

    /**
     * Message destination plugin getter
     * @return      Message destination plugin name
     */
    public String getDestinationPlugin() {
        if (destination != null && destination.getPlugin() != null)
            return destination.getPlugin();
        return getParam("dst_plugin");
    }

    /**
     * Message destination setter
     * @param address   Message destination address
     */
    public void setDestination(CAddr address) {
        setScope(Scope.GLOBAL);
        if (address == null)
            return;
        if (address.getRegion() != null) {
            setParam("dst_region", address.getRegion());
            setScope(Scope.REGIONAL);
            if (address.getAgent() != null) {
                setParam("dst_agent", address.getAgent());
                setScope(Scope.AGENT);
                if (address.getPlugin() != null) {
                    setParam("dst_plugin", address.getPlugin());
                    setScope(Scope.PLUGIN);
                }
            }
        }
        this.destination = new CAddr(address);
    }

    /**
     * Message destination setter
     * @param region    Message destination region
     */
    public void setDestination(String region) {
        setParam("dst_region", region);
        setScope(Scope.REGIONAL);
        this.destination = new CAddr(region);
    }

    /**
     * Message destination setter
     * @param region    Message destination region
     * @param agent     Message destination agent
     */
    public void setDestination(String region, String agent) {
        setParam("dst_region", region);
        setParam("dst_agent", agent);
        setScope(Scope.AGENT);
        this.destination = new CAddr(region, agent);
    }

    /**
     * Message destination setter
     * @param region    Message destination region
     * @param agent     Message destination agent
     * @param plugin    Message destination plugin
     */
    public void setDestination(String region, String agent, String plugin) {
        setParam("dst_region", region);
        setParam("dst_agent", agent);
        setParam("dst_plugin", plugin);
        setScope(Scope.PLUGIN);
        this.destination = new CAddr(region, agent, plugin);
    }

    /**
     * Set Remote Procedure Call parameters
     * @param caller    RPC initiator address
     * @param callID    RPC identifier
     */
    public void setRPC(CAddr caller, String callID) {
        setRPCCaller(caller);
        setRPCCallID(callID);
        setParam("callId-" + caller.getRegion() + "-" + caller.getAgent() + "-" + caller.getPlugin(), callID);
    }

    /**
     * RPC initiator getter
     * @return  Address of RPC initiator
     */
    @XmlJavaTypeAdapter(CAddrAdapter.class)
    public CAddr getRPCCaller() {
        return rpcCaller;
    }

    /**
     * RPC initiator setter
     * @param rpcCaller    Address of RPC initiator
     */
    public void setRPCCaller(CAddr rpcCaller) {
        this.rpcCaller = rpcCaller;
    }

    /**
     * RPC identifier getter
     * @return  RPC identifier
     */
    @XmlElement
    public String getRpcCallID() {
        return rpcCallID;
    }

    /**
     * RPC identifier setter
     * @param rpcCallID  RPC identifier
     */
    public void setRPCCallID(String rpcCallID) {
        this.rpcCallID = rpcCallID;
    }

    /**
     * Upgrade an old-style addressed message to the new CAddr style
     * (This should be used in the Consumer on the Controller Plugin only)
     */
    public void upgrade() {
        if (source == null && getParam("src_region") != null) {
            this.source = new CAddr(getParam("src_region"), getParam("src_agent"),
                    getParam("src_plugin"));
        }
        if (destination == null && getParam("dst_region") != null) {
            this.destination = new CAddr(getParam("dst_region"), getParam("dst_agent"),
                    getParam("dst_plugin"));
        }
    }

    /**
     * Swaps the message source and destination for returning the message
     */
    public void setReturn() {
        // CAddr Style
        CAddr oldSource = getSource();
        CAddr oldDestination = getDestination();

        this.source = new CAddr(oldDestination);
        this.destination = new CAddr(oldSource);

        // Old Style
        String src_region = getParam("src_region");
        String src_agent = getParam("src_agent");
        String src_plugin = getParam("src_plugin");

        removeParam("src_region");
        removeParam("src_agent");
        removeParam("src_plugin");

        if (getParam("dst_region") != null) {
            setParam("src_region", getParam("dst_region"));
        }
        if (getParam("dst_agent") != null) {
            setParam("src_agent", getParam("dst_agent"));
        }
        if (getParam("dst_plugin") != null) {
            setParam("src_plugin", getParam("dst_plugin"));
        }

        if (src_region != null) {
            setParam("dst_region", src_region);
        } else {
            removeParam("dst_region");
        }
        if (src_agent != null) {
            setParam("dst_agent", src_agent);
        } else {
            removeParam("dst_agent");
        }
        if (src_plugin != null) {
            setParam("dst_plugin", src_plugin);
        } else {
            removeParam("dst_plugin");
        }
    }

    /**
     * Message type getter
     * @return      Message type
     */
    @XmlJavaTypeAdapter(MsgEventTypesAdapter.class)
    public Type getType() {
        return type;
    }

    /**
     * Message type setter
     * @param type   Message type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Message scope getter
     * @return      Message scope
     */
    @XmlJavaTypeAdapter(MsgEventScopesAdapter.class)
    public Scope getScope() {
        return scope;
    }

    /**
     * Message scope setter
     * @param scope   Message scope
     */
    public void setScope(Scope scope) {
        this.scope = scope;
    }

    /**
     * Message payload getter
     * @return      Message payload
     */
    @XmlJavaTypeAdapter(MsgEventParamsAdapter.class)
    public Map<String, String> getParams() {
        Map<String, String> uncompressedParams = new HashMap<>();
        for (String key : params.keySet()) {
            uncompressedParams.put(key, getParam(key));
        }
        return uncompressedParams;
    }

    /**
     * Message payload setter
     * @param params    Message payload
     */
    public void setParams(Map<String, String> params) {
        this.params = new HashMap<>();
        for (String key : params.keySet()) {
            setParam(key, params.get(key));
        }
    }

    /**
     * Adds new parameters to message payload
     * @param params    Message payload
     */
    public void addParams(Map<String, String> params) {
        for (String key : params.keySet()) {
            setParam(key, params.get(key));
        }
    }

    /**
     * Message payload item getter
     * @param key   Key of payload item to get
     * @return      Payload value at key
     */
    public String getParam(String key) {
        return stringUncompress(params.get(key));
    }

    /**
     * Compressed message payload item getter<br><em>DEPRECATED: All payload items are
     * compressed/uncompressed by default since 0.4.2</em>
     * @param key   Key of payload item to get
     * @return      Payload value at key
     */
    @Deprecated
    public String getCompressedParam(String key) {
        return getParam(key);
    }

    /**
     * Message payload item setter
     * @param key       Key of payload item to set
     * @param value     Payload value at key
     */
    public void setParam(String key, String value) {
        params.put(key, stringCompress(value));
    }

    /**
     * Compressed message payload item setter<br><em>DEPRECATED: All payload items are
     * compressed/uncompressed by default since 0.4.2</em>
     * @param key       Key of payload item to set
     * @param value     Payload value at key
     */
    @Deprecated
    public void setCompressedParam(String key, String value) {
        setParam(key, value);
    }

    /**
     * Removes message payload item
     * @param key       Key of payload item to remove
     */
    public void removeParam(String key) {
        params.remove(key);
    }

    /**
     * Equality method
     * @param o     Other MsgEvent
     * @return      Whether the content of the two MsgEvents are equal
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MsgEvent)) return false;
        MsgEvent msgEvent = (MsgEvent)o;
        return getType().equals(msgEvent.getType()) &&
                getScope().equals(msgEvent.getScope()) &&
                ((getRPCCaller() == null && msgEvent.getRPCCaller() == null) ||
                        getRPCCaller().equals(msgEvent.getRPCCaller())) &&
                ((getRpcCallID() == null && msgEvent.getRpcCallID() == null) ||
                        getRpcCallID().equals(msgEvent.getRpcCallID())) &&
                ((getSource() == null && msgEvent.getSource() == null) ||
                        getSource().equals(msgEvent.getSource())) &&
                ((getDestination() == null && msgEvent.getDestination() == null) ||
                        getDestination().equals(msgEvent.getDestination())) &&
                getParams().equals(msgEvent.getParams());
    }

    /**
     * Converts MsgEvent to a string
     * @return      String representation of the MsgEvent
     */
    @Override
    public String toString() {
        return "{" +
                "type=" + getType() +
                ", scope=" + getScope() +
                ", source=" + getSource() +
                ", rpc_caller=" + getRPCCaller() + ", rpc_callid=" + getRpcCallID() +
                ", destination=" + getDestination() +
                ", params=" + getParams() +
                "}";
    }

    /**
     * Uncompresses a given String
     * @param compressed    String to uncompress
     * @return              Uncompressed String
     */
    private String stringUncompress(String compressed) {
        if (compressed == null)
            return null;
        try {
            byte[] exportDataRawCompressed = DatatypeConverter.parseBase64Binary(compressed);
            InputStream iss = new ByteArrayInputStream(exportDataRawCompressed);
            InputStream is = new GZIPInputStream(iss);
            return new Scanner(is,"UTF-8").useDelimiter("\\A").next();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Compresses a given String
     * @param str   String to compress
     * @return      Compressed String
     */
    private String stringCompress(String str) {
        byte[] dataToCompress = str.getBytes(StandardCharsets.UTF_8);
        byte[] compressedData;
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(dataToCompress.length)) {
            try (GZIPOutputStream zipStream = new GZIPOutputStream(byteStream)) {
                zipStream.write(dataToCompress);
            }
            compressedData = byteStream.toByteArray();
        } catch(Exception e) {
            return null;
        }
        return DatatypeConverter.printBase64Binary(compressedData);
    }
}