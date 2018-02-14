package com.researchworx.cresco.library.messaging;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CAddrTest {
    private final Logger logger = LoggerFactory.getLogger(CAddrTest.class);

    @Test
    public void Test1_Equality() {
        CAddr cAddrA = new CAddr("some_region", "some_agent", "some_plugin");
        CAddr cAddrB = new CAddr("some_region", "some_agent", "some_plugin");
        Assert.assertEquals(cAddrA, cAddrB);
    }
}
