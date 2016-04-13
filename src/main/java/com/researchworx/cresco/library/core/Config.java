package com.researchworx.cresco.library.core;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

public class Config {
    protected HierarchicalINIConfiguration iniConfObj;
    protected SubnodeConfiguration subConfObj;

    public Config(String configFile) throws ConfigurationException {
        this.iniConfObj = new HierarchicalINIConfiguration(configFile);
        this.iniConfObj.setDelimiterParsingDisabled(true);
        this.iniConfObj.setAutoSave(true);
    }

    public Config (SubnodeConfiguration pluginConfig) {
        this.subConfObj = pluginConfig;
    }

    public Boolean getBooleanParam(String param) {
        if (this.iniConfObj != null)
            return this.iniConfObj.getBoolean(param);
        return this.subConfObj.getBoolean(param);
    }
    public Boolean getBooleanParam(String param, Boolean ifNull) {
        Boolean ret = getBooleanParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Boolean getBooleanParam(String group, String param) {
        return this.iniConfObj.getSection(group).getBoolean(param);
    }
    public Boolean getBooleanParam(String group, String param, Boolean ifNull) {
        try {
            Boolean ret = getBooleanParam(group, param);
            if (ret != null)
                return ret;
            return ifNull;
        } catch (NullPointerException e) {
            return getBooleanParam(param, ifNull);
        }
    }

    public Double getDoubleParam(String param) {
        if (this.iniConfObj != null)
            return this.iniConfObj.getDouble(param);
        return this.subConfObj.getDouble(param);
    }
    public Double getDoubleParam(String param, Double ifNull) {
        Double ret = getDoubleParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Double getDoubleParam(String group, String param) {
        return iniConfObj.getSection(group).getDouble(param);
    }
    public Double getDoubleParam(String group, String param, Double ifNull) {
        try {
            Double ret = getDoubleParam(group, param);
            if (ret != null)
                return ret;
            return ifNull;
        } catch (NullPointerException e) {
            return getDoubleParam(param, ifNull);
        }
    }

    public Integer getIntegerParam(String param) {
        if (this.iniConfObj != null)
            return this.iniConfObj.getInt(param);
        return this.subConfObj.getInt(param);
    }
    public Integer getIntegerParam(String param, Integer ifNull) {
        Integer ret = getIntegerParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Integer getIntegerParam(String group, String param) {
        return iniConfObj.getSection(group).getInt(param);
    }
    public Integer getIntegerParam(String group, String param, Integer ifNull) {
        try {
            Integer ret = getIntegerParam(group, param);
            if (ret != null)
                return ret;
            return ifNull;
        } catch (NullPointerException e) {
            return getIntegerParam(param, ifNull);
        }
    }

    public Long getLongParam(String param) {
        if (this.iniConfObj != null)
            return this.iniConfObj.getLong(param);
        return this.subConfObj.getLong(param);
    }
    public Long getLongParam(String param, Long ifNull) {
        Long ret = getLongParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Long getLongParam(String group, String param) {
        return iniConfObj.getSection(group).getLong(param);
    }
    public Long getLongParam(String group, String param, Long ifNull) {
        try {
            Long ret = getLongParam(group, param);
            if (ret != null)
                return ret;
            return ifNull;
        } catch (NullPointerException e) {
            return getLongParam(param, ifNull);
        }
    }

    public String getStringParam(String param) {
        if (this.iniConfObj != null)
            return this.iniConfObj.getString(param);
        return this.subConfObj.getString(param);
    }
    public String getStringParam(String group, String param) {
        return iniConfObj.getSection(group).getString(param);
    }

    protected HierarchicalINIConfiguration getConfig() {
        return this.iniConfObj;
    }
    protected SubnodeConfiguration getPluginConfig() {
        return this.subConfObj;
    }
}