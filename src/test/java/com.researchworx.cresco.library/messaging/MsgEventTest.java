package com.researchworx.cresco.library.messaging;

import com.google.gson.Gson;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MsgEventTest {
    private final Logger logger = LoggerFactory.getLogger(MsgEventTest.class);

    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    /*@Test
    public void Test1_ActiveMQTest() throws Exception {
        //logger.debug("Building ActiveMQConnectionFactory");
        final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "vm://localhost?broker.persistent=false");
        //logger.debug("Building Connection");
        final Connection connection = connectionFactory.createConnection();
        //logger.debug("Starting Connection");
        connection.start();
        //logger.debug("Creating JMS Session");
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        final Queue queue = session.createTemporaryQueue();
        {
            final MessageProducer producer = session.createProducer(queue);
            final TextMessage message = session.createTextMessage("testing");
            producer.send(message);
        }
        {
            final MessageConsumer consumer = session.createConsumer(queue);
            final TextMessage message = (TextMessage) consumer.receiveNoWait();
            Assert.assertNotNull(message);
            Assert.assertEquals("testing", message.getText());
        }
    }*/

    @Test
    public void Test1_Equality() {
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, "tst_src_region",
                "tst_src_agent", "tst_src_plugin");
        msgEventA.setParam("src_region", "test_src_region");
        msgEventA.setParam("src_agent", "test_src_agent");
        msgEventA.setParam("src_plugin", "test_src_plugin");
        msgEventA.setParam("dst_region", "test_dst_region");
        msgEventA.setParam("dst_agent", "test_dst_agent");
        msgEventA.setParam("dst_plugin", "test_dst_plugin");
        msgEventA.setParam("some_param", Integer.toString(3));
        Assert.assertNotNull(msgEventA);
        //logger.info("msgEventA = " + msgEventA.toString());
        MsgEvent msgEventB = new MsgEvent(MsgEvent.Type.INFO, "tst_src_region",
                "tst_src_agent", "tst_src_plugin");
        msgEventB.setParam("src_region", "test_src_region");
        msgEventB.setParam("src_agent", "test_src_agent");
        msgEventB.setParam("src_plugin", "test_src_plugin");
        msgEventB.setParam("dst_region", "test_dst_region");
        msgEventB.setParam("dst_agent", "test_dst_agent");
        msgEventB.setParam("dst_plugin", "test_dst_plugin");
        msgEventB.setParam("some_param", Integer.toString(3));
        Assert.assertNotNull(msgEventB);
        //logger.info("msgEventB = " + msgEventB.toString());
        Assert.assertEquals(msgEventA, msgEventB);
    }

    @Test
    public void Test2_Marshalling() {
        Gson gson = new Gson();
        Assert.assertNotNull(gson);
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, "tst_src_region",
                "tst_src_agent", "tst_src_plugin");
        msgEventA.setParam("src_region", "test_src_region");
        msgEventA.setParam("src_agent", "test_src_agent");
        msgEventA.setParam("src_plugin", "test_src_plugin");
        msgEventA.setParam("dst_region", "test_dst_region");
        msgEventA.setParam("dst_agent", "test_dst_agent");
        msgEventA.setParam("dst_plugin", "test_dst_plugin");
        msgEventA.setParam("some_param", Integer.toString(3));
        Assert.assertNotNull(msgEventA);
        //logger.info("msgEventA = " + msgEventA.toString());
        String msgEventAString = gson.toJson(msgEventA);
        Assert.assertNotNull(msgEventAString);
        //logger.info("msgEventAString = " + msgEventAString);
        MsgEvent msgEventB = gson.fromJson(msgEventAString, MsgEvent.class);
        //logger.info("msgEventB = " + msgEventB.toString());
        Assert.assertNotNull(msgEventB);
        Assert.assertEquals(msgEventA, msgEventB);
    }

    @Test
    public void Test3_MyAddress() {
        String[] src = new String[]{"test_src_region", "test_src_agent", "test_src_plugin"};
        String[] dst = new String[]{"test_dst_region", "test_dst_agent", "test_dst_plugin"};
        MsgEvent.setMyAddress(src[0], src[1], src[2]);

        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, dst[0], dst[1], dst[2]);
        msgEventA.setParam("some_param", Integer.toString(3));
        Assert.assertNotNull(msgEventA);

        MsgEvent msgEventB = new MsgEvent(MsgEvent.Type.INFO, dst[0], dst[1], dst[2]);
        msgEventB.setParam("some_param", Integer.toString(3));
        Assert.assertNotNull(msgEventB);
        Assert.assertEquals(msgEventA, msgEventB);

        msgEventB.setSource(src[0], src[1], src[2]);
        Assert.assertEquals(msgEventA, msgEventB);

        msgEventB.setDestination(dst[0], dst[1], dst[2]);
        Assert.assertEquals(msgEventA, msgEventB);

        msgEventB.setParam("src_region", src[0]);
        msgEventB.setParam("src_agent", src[1]);
        msgEventB.setParam("src_plugin", src[2]);
        msgEventB.setParam("dst_region", dst[0]);
        msgEventB.setParam("dst_agent", dst[1]);
        msgEventB.setParam("dst_plugin", dst[2]);
        Assert.assertEquals(msgEventA, msgEventB);

        Assert.assertEquals(msgEventA.getParam("src_region"), src[0]);
        Assert.assertEquals(msgEventA.getParam("src_agent"), src[1]);
        Assert.assertEquals(msgEventA.getParam("src_plugin"), src[2]);
        Assert.assertEquals(msgEventA.getParam("dst_region"), dst[0]);
        Assert.assertEquals(msgEventA.getParam("dst_agent"), dst[1]);
        Assert.assertEquals(msgEventA.getParam("dst_plugin"), dst[2]);

        Assert.assertEquals(msgEventB.getParam("src_region"), src[0]);
        Assert.assertEquals(msgEventB.getParam("src_agent"), src[1]);
        Assert.assertEquals(msgEventB.getParam("src_plugin"), src[2]);
        Assert.assertEquals(msgEventB.getParam("dst_region"), dst[0]);
        Assert.assertEquals(msgEventB.getParam("dst_agent"), dst[1]);
        Assert.assertEquals(msgEventB.getParam("dst_plugin"), dst[2]);
        MsgEvent.removeMyAddress();
    }

    @Test
    public void Test4_SetReturn() {
        String[] src = new String[]{"test_src_region", "test_src_agent", "test_src_plugin"};
        String[] dst = new String[]{"test_dst_region", "test_dst_agent", "test_dst_plugin"};
        MsgEvent.setMyAddress(src[0], src[1], src[2]);
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, dst[0], dst[1], dst[2]);
        msgEventA.setParam("some_param", Integer.toString(3));
        //logger.info("msgEventA = " + msgEventA.toString());
        msgEventA.setReturn();
        //logger.info("msgEventA = " + msgEventA.toString());
        Assert.assertEquals(msgEventA.getSource(), new CAddr(dst[0], dst[1], dst[2]));
        Assert.assertEquals(msgEventA.getDestination(), new CAddr(src[0], src[1], src[2]));
        MsgEvent.removeMyAddress();
    }

    // ToDo: Test ActiveMQ Queue Transport Equality
}
