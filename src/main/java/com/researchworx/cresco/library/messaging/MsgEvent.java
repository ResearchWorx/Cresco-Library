package com.researchworx.cresco.library.messaging;

import javax.xml.bind.DatatypeConverter;
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
 * define the message type, scope, destination, and initial parameters if known:
 * <blockquote><pre>
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.CAddr-">MsgEvent</a>(<a href="CAddr.html">CAddr</a>);
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-java.lang.String-">MsgEvent</a>("region");
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-java.lang.String-java.lang.String-">MsgEvent</a>("region", "agent");
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-java.lang.String-java.lang.String-java.lang.String-">MsgEvent</a>("region", "agent", "plugin");
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.MsgEvent.Type-com.researchworx.cresco.library.messaging.CAddr-">MsgEvent</a>(MsgEvent.Type, <a href="CAddr.html">CAddr</a>);
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.MsgEvent.Type-java.lang.String-">MsgEvent</a>(MsgEvent.Type, "region");
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.MsgEvent.Type-java.lang.String-java.lang.String-">MsgEvent</a>(MsgEvent.Type, "region", "agent");
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.MsgEvent.Type-java.lang.String-java.lang.String-java.lang.String-">MsgEvent</a>(MsgEvent.Type, "region", "agent", "plugin");
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.MsgEvent.Type-com.researchworx.cresco.library.messaging.CAddr-java.util.Map-">MsgEvent</a>(MsgEvent.Type, <a href="CAddr.html">CAddr</a>, Map);
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.MsgEvent.Type-java.lang.String-java.util.Map-">MsgEvent</a>(MsgEvent.Type, "region", Map);
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.MsgEvent.Type-java.lang.String-java.lang.String-java.util.Map-">MsgEvent</a>(MsgEvent.Type, "region", "agent", Map);
 *     MsgEvent msgEvent = new <a href="MsgEvent.html#MsgEvent-com.researchworx.cresco.library.messaging.MsgEvent.Type-java.lang.String-java.lang.String-java.lang.String-java.util.Map-">MsgEvent</a>(MsgEvent.Type, "region", "agent", "plugin", Map);
 * </pre></blockquote>
 * The source can
 * be set manually for each new {@code MsgEvent} instance, or the static method {@code setMyAddress()}
 * can be used to set the default address for all future instances generated. For example:
 * <blockquote><pre>
 *     MsgEvent.<a href="MsgEvent.html#setMyAddress-com.researchworx.cresco.library.messaging.CAddr-">setMyAddress</a>(new <a href="CAddr.html">CAddr</a>("region", "agent", "plugin"));   // or
 *     MsgEvent.<a href="MsgEvent.html#setMyAddress-java.lang.String-">setMyAddress</a>("region");                                 // or
 *     MsgEvent.<a href="MsgEvent.html#setMyAddress-java.lang.String-java.lang.String-">setMyAddress</a>("region", "agent");                        // or
 *     MsgEvent.<a href="MsgEvent.html#setMyAddress-java.lang.String-java.lang.String-java.lang.String-">setMyAddress</a>("region", "agent", "plugin");
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
    /** Static address for setting source for all MsgEvents generated */
    private static CAddr myAddress = null;

    /**
     * myAddress getter
     * @return CAddr    Static address of this instance for MsgEvent
     */
    public static CAddr getMyAddress() {
        return MsgEvent.myAddress;
    }

    /**
     * myAddress setter
     * @param address   Static address of MsgEvent
     */
    public static void setMyAddress(CAddr address) {
        MsgEvent.myAddress = new CAddr(address);
    }

    /**
     * myAddress setter
     * @param region    Region of static address for MsgEvent
     */
    public static void setMyAddress(String region) {
        MsgEvent.myAddress = new CAddr(region);
    }

    /**
     * myAddress setter
     * @param region    Region of static address for MsgEvent
     * @param agent     Agent of static address for MsgEvent
     */
    public static void setMyAddress(String region, String agent) {
        MsgEvent.myAddress = new CAddr(region, agent);
    }

    /**
     * myAddress setter
     * @param region    Region of static address for MsgEvent
     * @param agent     Agent of static address for MsgEvent
     * @param plugin    Plugin of static address for MsgEvent
     */
    public static void setMyAddress(String region, String agent, String plugin) {
        MsgEvent.myAddress = new CAddr(region, agent, plugin);
    }

    /**
     * Clears/removes static address for MsgEvent
     */
    public static void removeMyAddress() {
        MsgEvent.myAddress = null;
    }

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
        GLOBAL, REGIONAL, AGENT, PLUGIN
    }

    /** Type of message */
    private Type type = Type.INFO;
    /** Scope of message */
    private Scope scope = Scope.GLOBAL;
    /** Source address of message */
    private CAddr source = null;
    /** Destination address of message */
    private CAddr destination = null;
    /** RPC origination address of message (if RPC) */
    private CAddr rpc = null;
    /** RPC call ID for message identification */
    private String callId = null;
    /** Custom message parameters */
    private Map<String, String> params = new HashMap<>();

    /**
     * Default constructor
     */
    public MsgEvent() {
        if (myAddress != null) {
            this.source = new CAddr(myAddress);
            setParam("src_region", myAddress.getRegion());
            setParam("src_agent", myAddress.getAgent());
            setParam("src_plugin", myAddress.getPlugin());
        }
    }

    /**
     * Constructor
     * @param destination   (CAddr) Destination address
     */
    public MsgEvent(CAddr destination) {
        this();
        setDestination(destination);
    }

    /**
     * Constructor
     * @param msgType       (MsgEvent.Type) Message type
     * @param destination   (CAddr) Destination address
     */
    public MsgEvent(Type msgType, CAddr destination) {
        this(destination);
        setType(msgType);
    }

    /**
     * Constructor
     * @param msgType       (MsgEvent.Type) Message type
     * @param destination   (CAddr) Destination address
     * @param params        (Map(String,String)) Map of custom message parameters
     */
    public MsgEvent(Type msgType, CAddr destination, Map<String, String> params) {
        this(msgType, destination);
        setParams(params);
    }

    /**
     * Constructor
     * @param dstRegion     Destination region name
     */
    public MsgEvent(String dstRegion) {
        this();
        setDestination(dstRegion);
    }

    /**
     * Constructor
     * @param dstRegion     Destination region name
     * @param dstAgent      Destination agent name
     */
    public MsgEvent(String dstRegion, String dstAgent) {
        this();
        setDestination(dstRegion, dstAgent);
    }

    /**
     * Constructor
     * @param dstRegion     Destination region name
     * @param dstAgent      Destination agent name
     * @param dstPlugin     Destination plugin name
     */
    public MsgEvent(String dstRegion, String dstAgent, String dstPlugin) {
        this();
        setDestination(dstRegion, dstAgent, dstPlugin);
    }

    /**
     * Constructor
     * @param msgType       (MsgEvent.Type) Message type
     * @param dstRegion     Destination region name
     */
    public MsgEvent(Type msgType, String dstRegion) {
        this();
        setType(msgType);
        setDestination(dstRegion);
    }

    /**
     * Constructor
     * @param msgType       (MsgEvent.Type) Message type
     * @param dstRegion     Destination region name
     * @param dstAgent      Destination agent name
     */
    public MsgEvent(Type msgType, String dstRegion, String dstAgent) {
        this();
        setType(msgType);
        setDestination(dstRegion, dstAgent);
    }

    /**
     * Constructor
     * @param msgType       (MsgEvent.Type) Message type
     * @param dstRegion     Destination region name
     * @param dstAgent      Destination agent name
     * @param dstPlugin     Destination plugin name
     */
    public MsgEvent(Type msgType, String dstRegion, String dstAgent, String dstPlugin) {
        this();
        setType(msgType);
        setDestination(dstRegion, dstAgent, dstPlugin);
    }

    /**
     * Constructor (Deprecated)
     * @param msgType       (MsgEvent.Type) Message type
     * @param dstRegion     Unused region name
     * @param dstAgent      Unused agent name
     * @param dstPlugin     Unused plugin name
     * @param msgBody       Message body parameter
     */
    @Deprecated
    public MsgEvent(Type msgType, String dstRegion, String dstAgent, String dstPlugin, String msgBody) {
        this();
        setType(msgType);
        setDestination(dstRegion, dstAgent, dstPlugin);
        setParam("msg", msgBody);
    }

    /**
     * Constructor
     * @param msgType       (MsgEvent.Type) Message type
     * @param dstRegion     Unused region name
     * @param params        Message custom parameters
     */
    public MsgEvent(Type msgType, String dstRegion, Map<String, String> params) {
        this();
        setType(msgType);
        setDestination(dstRegion);
        setParams(params);
    }

    /**
     * Constructor
     * @param msgType       (MsgEvent.Type) Message type
     * @param dstRegion     Unused region name
     * @param dstAgent      Unused agent name
     * @param params        Message custom parameters
     */
    public MsgEvent(Type msgType, String dstRegion, String dstAgent, Map<String, String> params) {
        this();
        setType(msgType);
        setDestination(dstRegion, dstAgent);
        setParams(params);
    }

    /**
     * Constructor
     * @param msgType       (MsgEvent.Type) Message type
     * @param dstRegion     Unused region name
     * @param dstAgent      Unused agent name
     * @param dstPlugin     Unused plugin name
     * @param params        Message custom parameters
     */
    public MsgEvent(Type msgType, String dstRegion, String dstAgent, String dstPlugin, Map<String, String> params) {
        this();
        setType(msgType);
        setDestination(dstRegion, dstAgent, dstPlugin);
        setParams(params);
    }

    /**
     * Message source getter
     * @return      (CAddr) Message source address
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
     * @param address   (CAddr) Source address
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
     * @return     (CAddr) Message destination address
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
     * @param address   (CAddr) Destination address
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

    // ToDo: Add methods for working with RPC

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
     * @return      (MsgEvent.Type) Message type
     */
    @XmlJavaTypeAdapter(MsgEventTypesAdapter.class)
    public Type getType() {
        return type;
    }

    /**
     * Message type setter
     * @param type   (MsgEvent.Type) Message type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Message scope getter
     * @return      (MsgEvent.Scope) Message scope
     */
    @XmlJavaTypeAdapter(MsgEventScopesAdapter.class)
    public Scope getScope() {
        return scope;
    }

    /**
     * Message scope setter
     * @param scope   (MsgEvent.Scope) Message scope
     */
    public void setScope(Scope scope) {
        this.scope = scope;
    }

    /**
     * Message payload getter
     * @return      (Map(String,String)) Message payload
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
     * @param params    (Map(String,String)) Message payload
     */
    public void setParams(Map<String, String> params) {
        this.params = new HashMap<>();
        for (String key : params.keySet()) {
            setParam(key, params.get(key));
        }
    }

    /**
     * Adds new parameters to message payload
     * @param params    (Map(String,String)) Message payload
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