package com.researchworx.cresco.library.core;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

import java.util.NoSuchElementException;

/**
 * Configuration handler for Cresco framework
 * @author V.K. Cody Bumgardner
 * @author Caylin Hickey
 * @version 0.2.7
 */
public class Config {
    /** Agent configuration object */
    protected HierarchicalINIConfiguration agentConfObj;
    /** Plugin configuration object */
    protected SubnodeConfiguration pluginConfObj;

    /**
     * Agent constructor
     * @param agentConfig               Path to the agent configuration file
     * @throws ConfigurationException   Exception loading the configuration file
     */
    public Config(String agentConfig) throws ConfigurationException {
        this.agentConfObj = new HierarchicalINIConfiguration(agentConfig);
        this.agentConfObj.setDelimiterParsingDisabled(true);
        this.agentConfObj.setAutoSave(true);
    }

    /**
     * Plugin constructor
     * @param pluginConfig              Plugin configuration object
     */
    public Config (SubnodeConfiguration pluginConfig) {
        this.pluginConfObj = pluginConfig;
    }

    public Boolean getBooleanParam(String param) {
        try {
            if (this.agentConfObj != null)
                return this.agentConfObj.getBoolean(param);
            return this.pluginConfObj.getBoolean(param);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    public Boolean getBooleanParam(String param, Boolean ifNull) {
        Boolean ret = getBooleanParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Boolean getBooleanParam(String group, String param) {
        try {
            return this.agentConfObj.getSection(group).getBoolean(param);
        } catch (NoSuchElementException e) {
            return null;
        }
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
        try {
            if (this.agentConfObj != null)
                return this.agentConfObj.getDouble(param);
            return this.pluginConfObj.getDouble(param);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    public Double getDoubleParam(String param, Double ifNull) {
        Double ret = getDoubleParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Double getDoubleParam(String group, String param) {
        try {
            return agentConfObj.getSection(group).getDouble(param);
        } catch (NoSuchElementException e) {
            return null;
        }
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
        try {
            if (this.agentConfObj != null)
                return this.agentConfObj.getInt(param);
            return this.pluginConfObj.getInt(param);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    public Integer getIntegerParam(String param, Integer ifNull) {
        Integer ret = getIntegerParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Integer getIntegerParam(String group, String param) {
        try {
            return agentConfObj.getSection(group).getInt(param);
        } catch (NoSuchElementException e) {
            return null;
        }
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
        try {
            if (this.agentConfObj != null)
                return this.agentConfObj.getLong(param);
            return this.pluginConfObj.getLong(param);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    public Long getLongParam(String param, Long ifNull) {
        Long ret = getLongParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Long getLongParam(String group, String param) {
        try {
            return agentConfObj.getSection(group).getLong(param);
        } catch (NoSuchElementException e) {
            return null;
        }
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
        try {
            if (this.agentConfObj != null)
                return this.agentConfObj.getString(param);
            return this.pluginConfObj.getString(param);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    public String getStringParam(String group, String param) {
        try {
            return agentConfObj.getSection(group).getString(param);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public HierarchicalINIConfiguration getConfig() {
        return this.agentConfObj;
    }
    public SubnodeConfiguration getPluginConfig() {
        return this.pluginConfObj;
    }
}