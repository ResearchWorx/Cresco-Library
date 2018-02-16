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
 * Cresco Message Event
 * @author V.K. Cody Bumgardner
 * @author Caylin Hickey
 * @since 0.1.0
 */
@XmlRootElement
public class MsgEvent {
    /** Static CAddr for setting source for all MsgEvents generated */
    private static CAddr myAddress = null;

    public static CAddr getMyAddress() {
        return MsgEvent.myAddress;
    }

    public static void setMyAddress(String region) {
        MsgEvent.myAddress = new CAddr(region);
    }

    public static void setMyAddress(String region, String agent) {
        MsgEvent.myAddress = new CAddr(region, agent);
    }

    public static void setMyAddress(String region, String agent, String plugin) {
        MsgEvent.myAddress = new CAddr(region, agent, plugin);
    }

    public static void removeMyAddress() {
        MsgEvent.myAddress = null;
    }

    public enum Type {
        CONFIG, DISCOVER, ERROR, EXEC, GC, INFO, KPI, LOG, WATCHDOG
    }

    private Type msgType = Type.INFO;
    private CAddr source = null;
    private CAddr destination = null;
    private CAddr rpc = null;
    private String callId = null;
    private Map<String, String> params = new HashMap<>();

    public MsgEvent() {
        if (myAddress != null) {
            this.source = new CAddr(myAddress);
            setParam("src_region", myAddress.getRegion());
            setParam("src_agent", myAddress.getAgent());
            setParam("src_plugin", myAddress.getPlugin());
        }
    }

    public MsgEvent(Type msgType, String dstRegion, String dstAgent, String dstPlugin) {
        this();
        setMsgType(msgType);
        setDestination(dstRegion, dstAgent, dstPlugin);
    }

    @Deprecated
    public MsgEvent(Type msgType, String dstRegion, String dstAgent, String dstPlugin, String msgBody) {
        this();
        setMsgType(msgType);
        setDestination(dstRegion, dstAgent, dstPlugin);
        setParam("msg", msgBody);
    }

    public MsgEvent(Type msgType, String dstRegion, String dstAgent, String dstPlugin, Map<String, String> params) {
        this();
        setParams(params);
        setMsgType(msgType);
        setDestination(dstRegion, dstAgent, dstPlugin);
    }

    @XmlJavaTypeAdapter(CAddrAdapter.class)
    public CAddr getSource() {
        return source;
    }

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

    public void setSource(String region) {
        setParam("src_region", region);
        this.source = new CAddr(region);
    }

    public void setSource(String region, String agent) {
        setParam("src_region", region);
        setParam("src_agent", agent);
        this.source = new CAddr(region, agent);
    }

    public void setSource(String region, String agent, String plugin) {
        setParam("src_region", region);
        setParam("src_agent", agent);
        setParam("src_plugin", plugin);
        this.source = new CAddr(region, agent, plugin);
    }

    @XmlJavaTypeAdapter(CAddrAdapter.class)
    public CAddr getDestination() {
        return destination;
    }

    public void setDestination(CAddr address) {
        if (address == null)
            return;
        if (address.getRegion() != null)
            setParam("dst_region", address.getRegion());
        if (address.getAgent() != null)
            setParam("dst_agent", address.getAgent());
        if (address.getPlugin() != null)
            setParam("dst_plugin", address.getPlugin());
        this.destination = new CAddr(address);
    }

    public void setDestination(String region) {
        setParam("dst_region", region);
        this.destination = new CAddr(region);
    }

    public void setDestination(String region, String agent) {
        setParam("dst_region", region);
        setParam("dst_agent", agent);
        this.destination = new CAddr(region, agent);
    }

    public void setDestination(String region, String agent, String plugin) {
        setParam("dst_region", region);
        setParam("dst_agent", agent);
        setParam("dst_plugin", plugin);
        this.destination = new CAddr(region, agent, plugin);
    }

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

    public void setReturn() {
        CAddr oldSource = getSource();
        CAddr oldDestination = getDestination();

        this.source = new CAddr(oldDestination);
        this.destination = new CAddr(oldSource);

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

    @XmlJavaTypeAdapter(MsgEventTypesAdapter.class)
    public Type getMsgType() {
        return msgType;
    }

    public void setMsgType(Type msgType) {
        this.msgType = msgType;
    }

    @XmlJavaTypeAdapter(MsgEventParamsAdapter.class)
    public Map<String, String> getParams() {
        Map<String, String> uncompressedParams = new HashMap<>();
        for (String key : params.keySet()) {
            uncompressedParams.put(key, getParam(key));
        }
        return uncompressedParams;
    }

    public void setParams(Map<String, String> params) {
        this.params = new HashMap<>();
        for (String key : params.keySet()) {
            setParam(key, params.get(key));
        }
    }

    public String getParam(String key) {
        return stringUncompress(params.get(key));
    }

    public void setParam(String key, String value) {
        params.put(key, stringCompress(value));
    }

    public void removeParam(String key) {
        params.remove(key);
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MsgEvent)) return false;
        MsgEvent msgEvent = (MsgEvent)o;
        return getMsgType().equals(msgEvent.getMsgType()) &&
                ((getSource() == null && msgEvent.getSource() == null) ||
                        getSource().equals(msgEvent.getSource())) &&
                ((getDestination() == null && msgEvent.getDestination() == null) ||
                        getDestination().equals(msgEvent.getDestination())) &&
                getParams().equals(msgEvent.getParams());
    }

    @Deprecated
    public void setCompressedParam(String key, String value) {
        setParam(key, value);
    }

    @Deprecated
    public String getCompressedParam(String key) {
        return getParam(key);
    }

    @Override
    public String toString() {
        return "{" +
                "type=" + getMsgType() +
                ", source=" + getSource() +
                ", destination=" + getDestination() +
                ", params=" + getParams() +
                "}";
    }

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