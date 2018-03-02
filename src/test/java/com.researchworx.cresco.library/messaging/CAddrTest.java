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
        logger.info("Equality Test");
        String[] address = new String[]{"test_src_region", "test_src_agent", "test_src_plugin"};
        CAddr cAddrA = new CAddr(address[0]);
        CAddr cAddrB = new CAddr(address[0]);
        Assert.assertEquals(cAddrA, cAddrB);
        cAddrA = new CAddr(address[0], address[1]);
        cAddrB = new CAddr(address[0], address[1]);
        Assert.assertEquals(cAddrA, cAddrB);
        cAddrA = new CAddr(address[0], address[1], address[2]);
        cAddrB = new CAddr(address[0], address[1], address[2]);
        Assert.assertEquals(cAddrA, cAddrB);
    }
}
