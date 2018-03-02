package com.researchworx.cresco.library.core;

import com.researchworx.cresco.library.messaging.CAddr;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CStateTest {
    private final Logger logger = LoggerFactory.getLogger(CStateTest.class);

    @Before
    public void cleanCState() {
        CState.setLocalAddress(null, null);
        CState.setConnectedState(false);
        CState.removeRegionalController();
        CState.removeGlobalController();
    }

    @Test
    public void Test01_LocalAddresses() {
        logger.info("Local Addresses");
        String[] local = new String[]{"tst_local_region", "tst_local_agent", "tst_local_plugin"};
        CAddr localRegion = new CAddr(local[0]);
        logger.info("\tTest Region:\t\t\t\t\t{}", localRegion);
        CAddr localAgent = new CAddr(local[0], local[1]);
        logger.info("\tTest Agent:\t\t\t\t\t\t{}", localAgent);
        CAddr localPlugin = new CAddr(local[0], local[1], local[2]);
        logger.info("\tTest Plugin:\t\t\t\t\t{}", localPlugin);

        logger.info("\tInitial CState:\t\t\t\t\t{}", CState.print());
        Assert.assertNull(CState.getLocalRegion());
        Assert.assertNull(CState.getLocalAgent());
        Assert.assertFalse(CState.getConnectedState());
        Assert.assertNull(CState.getRegionalController());
        Assert.assertNull(CState.getGlobalController());

        CState.setLocalAddress(local[0], local[1]);
        logger.info("\tCState after setLocalAddress():\t{}", CState.print());
        Assert.assertEquals(CState.getLocalRegion(), local[0]);
        Assert.assertEquals(CState.getLocalAgent(), local[1]);
        Assert.assertEquals(CState.getLocalAddress(), localAgent);
        Assert.assertEquals(CState.getLocalAddress(localPlugin.getPlugin()), localPlugin);
    }

    @Test
    public void Test02_RemoteAddresses() {
        logger.info("Remote Addresses");
        String[] regional = new String[]{"tst_regional_region", "tst_regional_agent", "tst_regional_plugin"};
        CAddr regionalController = new CAddr(regional[0], regional[1], regional[2]);

        String[] global = new String[]{"tst_global_region", "tst_global_agent", "tst_global_plugin"};
        CAddr globalController = new CAddr(global[0], global[1], global[2]);

        logger.info("\tInitial CState:\t\t\t\t\t\t\t{}", CState.print());

        CState.setConnectedState(true);
        logger.info("\tCState after setConnectedState():\t\t{}", CState.print());
        Assert.assertTrue(CState.getConnectedState());

        CState.setRegionalController(regionalController);
        logger.info("\tCState after setRegionalController():\t{}", CState.print());
        Assert.assertEquals(CState.getRegionalController(), regionalController);

        CState.setGlobalController(globalController);
        logger.info("\tCState after setGlobalController():\t\t{}", CState.print());
        Assert.assertEquals(CState.getGlobalController(), globalController);
    }
}
