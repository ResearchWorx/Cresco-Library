package com.researchworx.cresco.library.messaging;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MsgEventTest {
    private final Logger logger = LoggerFactory.getLogger(MsgEventTest.class);

    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @Test
    public void Test1_Equality() {
        logger.info("Equality Test");
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
        logger.info("\tmsgEventA:\t" + msgEventA.toString());
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
        logger.info("\tmsgEventB:\t" + msgEventB.toString());
        Assert.assertEquals(msgEventA, msgEventB);
    }

    @Test
    public void Test2_Marshalling() {
        logger.info("Marshalling Test");
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
        logger.info("\tOriginal Message:\t" + msgEventA.toString());
        String msgEventAString = gson.toJson(msgEventA);
        Assert.assertNotNull(msgEventAString);
        logger.info("\tGSON Marshal:\t\t" + msgEventAString);
        MsgEvent msgEventB = gson.fromJson(msgEventAString, MsgEvent.class);
        logger.info("\tGSON Unmarshal:\t\t" + msgEventB.toString());
        Assert.assertNotNull(msgEventB);
        Assert.assertEquals(msgEventA, msgEventB);
    }

    @Test
    public void Test3_MyAddress() {
        logger.info("MyAddress Test");
        String[] src = new String[]{"test_src_region", "test_src_agent", "test_src_plugin"};
        String[] dst = new String[]{"test_dst_region", "test_dst_agent", "test_dst_plugin"};
        MsgEvent.setMyAddress(src[0], src[1], src[2]);

        logger.info("\tMsgEvent.getMyAddress():\t" + MsgEvent.getMyAddress());

        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, dst[0], dst[1], dst[2]);
        msgEventA.setParam("some_param", Integer.toString(3));
        Assert.assertNotNull(msgEventA);
        Assert.assertEquals(msgEventA.getSource(), MsgEvent.getMyAddress());
        logger.info("\tmsgEventA.getSource():\t\t" + msgEventA.getSource());

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

        MsgEvent msgEventC = new MsgEvent();
        Assert.assertNull(msgEventC.getSource());
        Assert.assertNull(msgEventC.getDestination());
    }

    @Test
    public void Test4_SetReturn() {
        logger.info("SetReturn() Test");
        String[] src = new String[]{"test_src_region", "test_src_agent", "test_src_plugin"};
        String[] dst = new String[]{"test_dst_region", "test_dst_agent", "test_dst_plugin"};
        MsgEvent.setMyAddress(src[0], src[1], src[2]);
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, dst[0], dst[1], dst[2]);
        msgEventA.setParam("some_param", Integer.toString(3));
        logger.info("\tOriginal Message:\t" + msgEventA.toString());
        msgEventA.setReturn();
        logger.info("\tAfter setReturn():\t" + msgEventA.toString());
        Assert.assertEquals(msgEventA.getSource(), new CAddr(dst[0], dst[1], dst[2]));
        Assert.assertEquals(msgEventA.getDestination(), new CAddr(src[0], src[1], src[2]));
        MsgEvent.removeMyAddress();
    }

    @Test
    public void Test5_Upgrade() {
        logger.info("Upgrade() Test");
        logger.info("\tNo old-style parameters:");
        String[] src = new String[]{"test_src_region", "test_src_agent", "test_src_plugin"};
        String[] dst = new String[]{"test_dst_region", "test_dst_agent", "test_dst_plugin"};
        MsgEvent msgEventA = new MsgEvent();
        logger.info("\t\tBefore upgrade():\t" + msgEventA.toString());
        Assert.assertNull(msgEventA.getSource());
        Assert.assertNull(msgEventA.getDestination());
        msgEventA.upgrade();
        logger.info("\t\tAfter upgrade():\t" + msgEventA.toString());
        Assert.assertNull(msgEventA.getSource());
        Assert.assertNull(msgEventA.getDestination());
        logger.info("\tExisting old-style parameters:");
        msgEventA.setParam("src_region", src[0]);
        msgEventA.setParam("src_agent", src[1]);
        msgEventA.setParam("src_plugin", src[2]);
        msgEventA.setParam("dst_region", dst[0]);
        msgEventA.setParam("dst_agent", dst[1]);
        msgEventA.setParam("dst_plugin", dst[2]);
        logger.info("\t\tBefore upgrade():\t" + msgEventA.toString());
        Assert.assertNull(msgEventA.getSource());
        Assert.assertNull(msgEventA.getDestination());
        msgEventA.upgrade();
        logger.info("\t\tAfter upgrade():\t" + msgEventA.toString());
        Assert.assertNotNull(msgEventA.getSource());
        Assert.assertNotNull(msgEventA.getDestination());
    }

    @Test
    public void Test6_TextMessage() throws Exception {
        logger.info("TextMessage Test");
        final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "vm://localhost?broker.persistent=false");
        final Connection connection = connectionFactory.createConnection();
        connection.start();
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Gson gson = new Gson();
        String[] src = new String[]{"test_src_region", "test_src_agent", "test_src_plugin"};
        String[] dst = new String[]{"test_dst_region", "test_dst_agent", "test_dst_plugin"};
        MsgEvent.setMyAddress(src[0], src[1], src[2]);
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, dst[0], dst[1], dst[2]);
        msgEventA.setParam("some_param", Integer.toString(3));
        String msgEventAString = gson.toJson(msgEventA);
        TextMessage message = session.createTextMessage(msgEventAString);
        MsgEvent msgEventB = gson.fromJson(message.getText(), MsgEvent.class);
        Assert.assertEquals(msgEventA, msgEventB);
        MsgEvent.removeMyAddress();
    }

    @Test
    public void Test7_ActiveMQTransportEquality() throws Exception {
        logger.info("ActiveMQ Queue Transport Test");
        Gson gson = new Gson();
        String[] src = new String[]{"test_src_region", "test_src_agent", "test_src_plugin"};
        String[] dst = new String[]{"test_dst_region", "test_dst_agent", "test_dst_plugin"};
        MsgEvent.setMyAddress(src[0], src[1], src[2]);
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, dst[0], dst[1], dst[2]);
        msgEventA.setParam("some_param", Integer.toString(3));
        logger.info("\tOriginal Message:\t" + msgEventA.toString());
        String msgEventAString = gson.toJson(msgEventA);
        final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "vm://localhost?broker.persistent=false");
        final Connection connection = connectionFactory.createConnection();
        connection.start();
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        final Queue queue = session.createTemporaryQueue();
        {
            final MessageProducer producer = session.createProducer(queue);
            final TextMessage message = session.createTextMessage(msgEventAString);
            producer.send(message);
        }
        {
            final MessageConsumer consumer = session.createConsumer(queue);
            final TextMessage message = (TextMessage) consumer.receive();
            Assert.assertNotNull(message);
            MsgEvent msgEventB = gson.fromJson(message.getText(), MsgEvent.class);
            logger.info("\tDequeued Message:\t" + msgEventB.toString());
            Assert.assertEquals(msgEventA, msgEventB);
        }
        MsgEvent.removeMyAddress();
    }
}
