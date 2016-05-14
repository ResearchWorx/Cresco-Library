package com.researchworx.cresco.library.core;

import org.apache.commons.configuration.SubnodeConfiguration;

import java.util.NoSuchElementException;

/**
 * Cresco configuration handler
 * @author V.K. Cody Bumgardner
 * @author Caylin Hickey
 * @version 0.3.1
 */
public class Config {
    /** Plugin configuration object */
    protected SubnodeConfiguration confObj;
    /**
     * Constructor
     * @param pluginConfig      Plugin configuration object
     */
    public Config (SubnodeConfiguration pluginConfig) {
        this.confObj = pluginConfig;
    }
    /**
     * Grab configuration entry as Boolean
     * @param param             Entry name to retrieve
     * @return                  Value of entry, null if missing
     */
    public Boolean getBooleanParam(String param) {
        try {
            return this.confObj.getBoolean(param);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    /**
     * Grab configuration entry as Boolean
     * @param param             Entry name to retrieve
     * @param ifNull            Default value to return on error
     * @return                  Value of entry, ifNull value on error
     */
    public Boolean getBooleanParam(String param, Boolean ifNull) {
        Boolean ret = getBooleanParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    /**
     * Grab configuration entry as Double
     * @param param             Entry name to retrieve
     * @return                  Value of entry, null if missing
     */
    public Double getDoubleParam(String param) {
        try {
            return this.confObj.getDouble(param);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    /**
     * Grab configuration entry as Double
     * @param param             Entry name to retrieve
     * @param ifNull            Default value to return on error
     * @return                  Value of entry, ifNull value on error
     */
    public Double getDoubleParam(String param, Double ifNull) {
        Double ret = getDoubleParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    /**
     * Grab configuration entry as Integer
     * @param param             Entry name to retrieve
     * @return                  Value of entry, null if missing
     */
    public Integer getIntegerParam(String param) {
        try {
            return this.confObj.getInt(param);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    /**
     * Grab configuration entry as Integer
     * @param param             Entry name to retrieve
     * @param ifNull            Default value to return on error
     * @return                  Value of entry, ifNull value on error
     */
    public Integer getIntegerParam(String param, Integer ifNull) {
        Integer ret = getIntegerParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    /**
     * Grab configuration entry as Long
     * @param param             Entry name to retrieve
     * @return                  Value of entry, null if missing
     */
    public Long getLongParam(String param) {
        try {
            return this.confObj.getLong(param);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    /**
     * Grab configuration entry as Long
     * @param param             Entry name to retrieve
     * @param ifNull            Default value to return on error
     * @return                  Value of entry, ifNull value on error
     */
    public Long getLongParam(String param, Long ifNull) {
        Long ret = getLongParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    /**
     * Grab configuration entry as String
     * @param param             Entry name to retrieve
     * @return                  Value of entry, null if missing
     */
    public String getStringParam(String param) {
        try {
            return this.confObj.getString(param);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    /**
     * Grab configuration entry as String
     * @param param             Entry name to retrieve
     * @param ifNull            Default value to return on error
     * @return                  Value of entry, ifNull value on error
     */
    public String getStringParam(String param, String ifNull) {
        String ret = getStringParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    /**
     * Returns the underlying configuration object
     * @return                  The underlying configuration object
     */
    public SubnodeConfiguration getConfig() {
        return this.confObj;
    }
}