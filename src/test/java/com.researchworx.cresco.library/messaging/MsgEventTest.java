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
    public void Test1_ActiveMQTest() throws Exception {
        logger.debug("Building ActiveMQConnectionFactory");
        final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "vm://localhost?broker.persistent=false");
        logger.debug("Building Connection");
        final Connection connection = connectionFactory.createConnection();
        logger.debug("Starting Connection");
        connection.start();
        logger.debug("Creating JMS Session");
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
    }

    @Test
    public void Test2_MsgEventEquals() {
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, "tst_src_region",
                "tst_src_agent", "tst_src_plugin", "Test Message");
        msgEventA.setParam("src_region", "test_src_region");
        msgEventA.setParam("src_agent", "test_src_agent");
        msgEventA.setParam("src_plugin", "test_src_plugin");
        msgEventA.setParam("dst_region", "test_dst_region");
        msgEventA.setParam("dst_agent", "test_dst_agent");
        msgEventA.setParam("dst_plugin", "test_dst_plugin");
        msgEventA.setParam("some_param", Integer.toString(3));
        Assert.assertNotNull(msgEventA);
        MsgEvent msgEventB = new MsgEvent(MsgEvent.Type.INFO, "tst_src_region",
                "tst_src_agent", "tst_src_plugin", "Test Message");
        msgEventB.setParam("src_region", "test_src_region");
        msgEventB.setParam("src_agent", "test_src_agent");
        msgEventB.setParam("src_plugin", "test_src_plugin");
        msgEventB.setParam("dst_region", "test_dst_region");
        msgEventB.setParam("dst_agent", "test_dst_agent");
        msgEventB.setParam("dst_plugin", "test_dst_plugin");
        msgEventB.setParam("some_param", Integer.toString(3));
        Assert.assertNotNull(msgEventB);
        Assert.assertEquals(msgEventA, msgEventB);
    }

    @Test
    public void Test3_MsgEventMarshalling() {
        Gson gson = new Gson();
        Assert.assertNotNull(gson);
        // ToDo: String _ = gson.toJson(MsgEvent _)
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, "tst_src_region",
                "tst_src_agent", "tst_src_plugin", "Test Message");
        msgEventA.setParam("src_region", "test_src_region");
        msgEventA.setParam("src_agent", "test_src_agent");
        msgEventA.setParam("src_plugin", "test_src_plugin");
        msgEventA.setParam("dst_region", "test_dst_region");
        msgEventA.setParam("dst_agent", "test_dst_agent");
        msgEventA.setParam("dst_plugin", "test_dst_plugin");
        msgEventA.setParam("some_param", Integer.toString(3));
        Assert.assertNotNull(msgEventA);
        String msgEventAString = gson.toJson(msgEventA);
        Assert.assertNotNull(msgEventAString);
        logger.info(msgEventAString);
        // ToDo: MsgEvent _ = gson.fromJson(String _, MsgEvent.class)
        MsgEvent msgEventB = gson.fromJson(msgEventAString, MsgEvent.class);
        Assert.assertNotNull(msgEventB);
        Assert.assertEquals(msgEventA, msgEventB);
    }
}
