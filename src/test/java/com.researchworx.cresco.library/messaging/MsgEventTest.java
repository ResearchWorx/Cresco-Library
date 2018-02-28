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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MsgEventTest {
    private final Logger logger = LoggerFactory.getLogger(MsgEventTest.class);

    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @Test
    public void Test01_CompressedParams() {
        logger.info("Compressed Parameters Test");
        Map<String, String> params = new HashMap<>();
        params.put("paramA", "paramAvalue");
        params.put("paramB", "paramBvalue");
        params.put("paramC", "paramCvalue");
        params.put("paramD", Integer.toString(1));
        params.put("paramE", Boolean.toString(false));
        logger.info("\tParameters:\t\t\t\t\t\t\t{}", params);
        MsgEvent msgEventA = new MsgEvent();
        logger.info("\tOriginal MsgEvent:\t\t\t\t\t{}", msgEventA);
        for (Map.Entry<String, String> entry : params.entrySet())
            msgEventA.setParam(entry.getKey(), entry.getValue());
        logger.info("\tMsgEvent Params Set w/ setParam:\t{}", msgEventA);
        for (Map.Entry<String, String> entry : params.entrySet())
            Assert.assertEquals(msgEventA.getParam(entry.getKey()), entry.getValue());
        Map<String, String> msgEventAParams = msgEventA.getParams();
        for (Map.Entry<String, String> entry : msgEventAParams.entrySet())
            Assert.assertEquals(params.get(entry.getKey()), entry.getValue());
        MsgEvent msgEventB = new MsgEvent();
        msgEventB.setParams(params);
        logger.info("\tMsgEvent Params Set w/ setParams:\t{}", msgEventB);
        for (Map.Entry<String, String> entry : params.entrySet())
            Assert.assertEquals(msgEventB.getParam(entry.getKey()), entry.getValue());
        Map<String, String> msgEventBParams = msgEventB.getParams();
        for (Map.Entry<String, String> entry : msgEventBParams.entrySet())
            Assert.assertEquals(params.get(entry.getKey()), entry.getValue());
    }

    @Test
    public void Test02_Addresses() {
        logger.info("Address Accessor Test");
        String[] src = new String[]{"test_src_region", "test_src_agent", "test_src_plugin"};
        String[] dst = new String[]{"test_dst_region", "test_dst_agent", "test_dst_plugin"};
        logger.info("\tSource Address:\t\t\t\t\t\t\t{}, {}, {}", src[0], src[1], src[2]);
        logger.info("\tDestination Address:\t\t\t\t\t{}, {}, {}", dst[0], dst[1], dst[2]);
        MsgEvent msgEventA = new MsgEvent();
        msgEventA.setSource(src[0]);
        logger.info("\tMsgEvent (only source region):\t\t\t{}", msgEventA);
        Assert.assertEquals(msgEventA.getSourceRegion(), src[0]);
        Assert.assertEquals(msgEventA.getSource().getRegion(), src[0]);
        Assert.assertEquals(msgEventA.getParam("src_region"), src[0]);
        Assert.assertNull(msgEventA.getSourceAgent());
        Assert.assertNull(msgEventA.getSource().getAgent());
        Assert.assertNull(msgEventA.getParam("src_agent"));
        Assert.assertNull(msgEventA.getSourcePlugin());
        Assert.assertNull(msgEventA.getSource().getPlugin());
        Assert.assertNull(msgEventA.getParam("src_plugin"));
        msgEventA = new MsgEvent();
        msgEventA.setSource(src[0], src[1]);
        logger.info("\tMsgEvent (source region & agent):\t\t{}", msgEventA);
        Assert.assertEquals(msgEventA.getSourceRegion(), src[0]);
        Assert.assertEquals(msgEventA.getSource().getRegion(), src[0]);
        Assert.assertEquals(msgEventA.getParam("src_region"), src[0]);
        Assert.assertEquals(msgEventA.getSourceAgent(), src[1]);
        Assert.assertEquals(msgEventA.getSource().getAgent(), src[1]);
        Assert.assertEquals(msgEventA.getParam("src_agent"), src[1]);
        Assert.assertNull(msgEventA.getSourcePlugin());
        Assert.assertNull(msgEventA.getSource().getPlugin());
        Assert.assertNull(msgEventA.getParam("src_plugin"));
        msgEventA = new MsgEvent();
        msgEventA.setSource(src[0], src[1], src[2]);
        logger.info("\tMsgEvent (full source address):\t\t\t{}", msgEventA);
        Assert.assertEquals(msgEventA.getSourceRegion(), src[0]);
        Assert.assertEquals(msgEventA.getSource().getRegion(), src[0]);
        Assert.assertEquals(msgEventA.getParam("src_region"), src[0]);
        Assert.assertEquals(msgEventA.getSourceAgent(), src[1]);
        Assert.assertEquals(msgEventA.getSource().getAgent(), src[1]);
        Assert.assertEquals(msgEventA.getParam("src_agent"), src[1]);
        Assert.assertEquals(msgEventA.getSourcePlugin(), src[2]);
        Assert.assertEquals(msgEventA.getSource().getPlugin(), src[2]);
        Assert.assertEquals(msgEventA.getParam("src_plugin"), src[2]);

        msgEventA = new MsgEvent();
        msgEventA.setDestination(dst[0]);
        logger.info("\tMsgEvent (only destination region):\t\t{}", msgEventA);
        Assert.assertEquals(msgEventA.getDestinationRegion(), dst[0]);
        Assert.assertEquals(msgEventA.getDestination().getRegion(), dst[0]);
        Assert.assertEquals(msgEventA.getParam("dst_region"), dst[0]);
        Assert.assertNull(msgEventA.getDestinationAgent());
        Assert.assertNull(msgEventA.getDestination().getAgent());
        Assert.assertNull(msgEventA.getParam("dst_agent"));
        Assert.assertNull(msgEventA.getSourcePlugin());
        Assert.assertNull(msgEventA.getDestination().getPlugin());
        Assert.assertNull(msgEventA.getParam("dst_plugin"));
        msgEventA = new MsgEvent();
        msgEventA.setDestination(dst[0], dst[1]);
        logger.info("\tMsgEvent (destination region & agent):\t{}", msgEventA);
        Assert.assertEquals(msgEventA.getDestinationRegion(), dst[0]);
        Assert.assertEquals(msgEventA.getDestination().getRegion(), dst[0]);
        Assert.assertEquals(msgEventA.getParam("dst_region"), dst[0]);
        Assert.assertEquals(msgEventA.getDestinationAgent(), dst[1]);
        Assert.assertEquals(msgEventA.getDestination().getAgent(), dst[1]);
        Assert.assertEquals(msgEventA.getParam("dst_agent"), dst[1]);
        Assert.assertNull(msgEventA.getSourcePlugin());
        Assert.assertNull(msgEventA.getDestination().getPlugin());
        Assert.assertNull(msgEventA.getParam("dst_plugin"));
        msgEventA = new MsgEvent();
        msgEventA.setDestination(dst[0], dst[1], dst[2]);
        logger.info("\tMsgEvent (full destination address):\t{}", msgEventA);
        Assert.assertEquals(msgEventA.getDestinationRegion(), dst[0]);
        Assert.assertEquals(msgEventA.getDestination().getRegion(), dst[0]);
        Assert.assertEquals(msgEventA.getParam("dst_region"), dst[0]);
        Assert.assertEquals(msgEventA.getDestinationAgent(), dst[1]);
        Assert.assertEquals(msgEventA.getDestination().getAgent(), dst[1]);
        Assert.assertEquals(msgEventA.getParam("dst_agent"), dst[1]);
        Assert.assertEquals(msgEventA.getDestinationPlugin(), dst[2]);
        Assert.assertEquals(msgEventA.getDestination().getPlugin(), dst[2]);
        Assert.assertEquals(msgEventA.getParam("dst_plugin"), dst[2]);
    }

    @Test
    public void Test03_Equality() {
        logger.info("Equality Test");
        CAddr source = new CAddr("test_src_region", "test_src_agent", "test_src_plugin");
        CAddr destination = new CAddr("test_dst_region", "test_dst_agent", "test_dst_plugin");
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO);
        msgEventA.setParam("src_region", source.getRegion());
        msgEventA.setParam("src_agent", source.getAgent());
        msgEventA.setParam("src_plugin", source.getPlugin());
        msgEventA.setParam("dst_region", destination.getRegion());
        msgEventA.setParam("dst_agent", destination.getAgent());
        msgEventA.setParam("dst_plugin", destination.getPlugin());
        msgEventA.setParam("some_param", Integer.toString(3));
        Assert.assertNotNull(msgEventA);
        logger.info("\tmsgEventA:\t{}", msgEventA);
        MsgEvent msgEventB = new MsgEvent(MsgEvent.Type.INFO);
        msgEventB.setParam("src_region", source.getRegion());
        msgEventB.setParam("src_agent", source.getAgent());
        msgEventB.setParam("src_plugin", source.getPlugin());
        msgEventB.setParam("dst_region", destination.getRegion());
        msgEventB.setParam("dst_agent", destination.getAgent());
        msgEventB.setParam("dst_plugin", destination.getPlugin());
        msgEventB.setParam("some_param", Integer.toString(3));
        Assert.assertNotNull(msgEventB);
        logger.info("\tmsgEventB:\t{}", msgEventB);
        Assert.assertEquals(msgEventA, msgEventB);
    }

    @Test
    public void Test04_Marshalling() {
        logger.info("Marshalling Test");
        Gson gson = new Gson();
        Assert.assertNotNull(gson);
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO);
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
    public void Test05_SetReturn() {
        logger.info("SetReturn() Test");
        CAddr source = new CAddr("test_src_region", "test_src_agent", "test_src_plugin");
        CAddr destination = new CAddr("test_dst_region", "test_dst_agent", "test_dst_plugin");
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, source, destination);
        msgEventA.setParam("some_param", Integer.toString(3));
        logger.info("\tOriginal Message:\t" + msgEventA.toString());
        msgEventA.setReturn();
        logger.info("\tAfter setReturn():\t" + msgEventA.toString());
        Assert.assertEquals(msgEventA.getSource(), destination);
        Assert.assertEquals(msgEventA.getDestination(), source);
    }

    @Test
    public void Test06_Upgrade() {
        logger.info("Upgrade() Test");
        logger.info("\tNo old-style parameters:");
        CAddr source = new CAddr("test_src_region", "test_src_agent", "test_src_plugin");
        CAddr destination = new CAddr("test_dst_region", "test_dst_agent", "test_dst_plugin");
        MsgEvent msgEventA = new MsgEvent();
        logger.info("\t\tBefore upgrade():\t" + msgEventA.toString());
        Assert.assertNull(msgEventA.getSource());
        Assert.assertNull(msgEventA.getDestination());
        msgEventA.upgrade();
        logger.info("\t\tAfter upgrade():\t" + msgEventA.toString());
        Assert.assertNull(msgEventA.getSource());
        Assert.assertNull(msgEventA.getDestination());
        logger.info("\tExisting old-style parameters:");
        msgEventA.setParam("src_region", source.getRegion());
        msgEventA.setParam("src_agent", source.getAgent());
        msgEventA.setParam("src_plugin", source.getPlugin());
        msgEventA.setParam("dst_region", destination.getRegion());
        msgEventA.setParam("dst_agent", destination.getAgent());
        msgEventA.setParam("dst_plugin", destination.getPlugin());
        logger.info("\t\tBefore upgrade():\t" + msgEventA.toString());
        Assert.assertNull(msgEventA.getSource());
        Assert.assertNull(msgEventA.getDestination());
        msgEventA.upgrade();
        logger.info("\t\tAfter upgrade():\t" + msgEventA.toString());
        Assert.assertNotNull(msgEventA.getSource());
        Assert.assertEquals(msgEventA.getSource(), source);
        Assert.assertNotNull(msgEventA.getDestination());
        Assert.assertEquals(msgEventA.getDestination(), destination);
    }

    @Test
    public void Test07_TextMessage() throws Exception {
        logger.info("TextMessage Test");
        final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "vm://localhost?broker.persistent=false");
        final Connection connection = connectionFactory.createConnection();
        connection.start();
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Gson gson = new Gson();
        CAddr source = new CAddr("test_src_region", "test_src_agent", "test_src_plugin");
        CAddr destination = new CAddr("test_dst_region", "test_dst_agent", "test_dst_plugin");
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, source, destination);
        msgEventA.setParam("some_param", Integer.toString(3));
        String msgEventAString = gson.toJson(msgEventA);
        TextMessage message = session.createTextMessage(msgEventAString);
        MsgEvent msgEventB = gson.fromJson(message.getText(), MsgEvent.class);
        Assert.assertEquals(msgEventA, msgEventB);
    }

    @Test
    public void Test08_ActiveMQTransportEquality() throws Exception {
        logger.info("ActiveMQ Queue Transport Test");
        Gson gson = new Gson();
        CAddr source = new CAddr("test_src_region", "test_src_agent", "test_src_plugin");
        CAddr destination = new CAddr("test_dst_region", "test_dst_agent", "test_dst_plugin");
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, source, destination);
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
    }

    @Test
    public void Test09_RPC_and_Route() throws Exception {
        logger.info("RPC Test");
        List<String> expected_route = new ArrayList<>();
        CAddr source = new CAddr("test_src_region", "test_src_agent", "test_src_plugin");
        CAddr destination = new CAddr("test_dst_region", "test_dst_agent", "test_dst_plugin");
        Gson gson = new Gson();
        Map<String, MsgEvent> rpcMap = new HashMap<>();
        // Generate Message
        MsgEvent msgEventA = new MsgEvent(MsgEvent.Type.INFO, source, destination);
        logger.info("\tInitial message:\t\t{}", msgEventA);
        // "Send" message as RPC (set RPC flags)
        String callId = msgEventA.makeRPC();
        logger.info("\tMessage w/ RPC:\t\t\t{}", msgEventA);
        // Put message on a queue
        final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "vm://localhost?broker.persistent=false");
        final Connection connection = connectionFactory.createConnection();
        connection.start();
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        final Queue queue = session.createTemporaryQueue();
        final MessageProducer producer = session.createProducer(queue);
        Assert.assertEquals(msgEventA.getTTL(), 0);
        msgEventA.addRouteEntry(msgEventA.getSource(), 1);
        Assert.assertEquals(msgEventA.getTTL(), 1);
        expected_route.add(source + " : " + 1);
        Assert.assertEquals(msgEventA.getRoute(), expected_route);
        final TextMessage messageA = session.createTextMessage(gson.toJson(msgEventA));
        producer.send(messageA);
        // Collect message from queue
        final MessageConsumer consumer = session.createConsumer(queue);
        final TextMessage messageB = (TextMessage) consumer.receive();
        Assert.assertNotNull(messageB);
        MsgEvent msgEventB = gson.fromJson(messageB.getText(), MsgEvent.class);
        Assert.assertEquals(msgEventA, msgEventB);
        // Set Return
        msgEventB.setReturn();
        logger.info("\tMessage w/ SetReturn():\t{}", msgEventB);
        msgEventB.addRouteEntry(msgEventB.getSource(), 1);
        expected_route.add(destination + " : " + 1);
        Assert.assertEquals(msgEventB.getTTL(), 2);
        Assert.assertEquals(msgEventB.getRoute(), expected_route);
        // Put message back on queue
        final TextMessage messageC = session.createTextMessage(gson.toJson(msgEventB));
        producer.send(messageC);
        // Collect message from queue
        final TextMessage messageD = (TextMessage) consumer.receive();
        Assert.assertNotNull(messageD);
        MsgEvent msgEventD = gson.fromJson(messageD.getText(), MsgEvent.class);
        Assert.assertEquals(msgEventD.getTTL(), 2);
        Assert.assertEquals(msgEventD.getRoute(), expected_route);
        logger.info("\tReturned Message:\t\t{}", msgEventD);
        String oldCallId = msgEventD.getParam("callId-" + source.getRegion() + "-" + source.getAgent() + "-" + source.getPlugin());
        Assert.assertNotNull(oldCallId);
        Assert.assertEquals(callId, oldCallId);
        // Put in "RPC Map"
        rpcMap.put(msgEventD.getRPCID(), msgEventD);
        // Check for RPC to match expected values
        MsgEvent msgEventE = rpcMap.get(callId);
        Assert.assertNotNull(msgEventE);
        logger.info("\tRPC Result:\t\t\t\t{}", msgEventE);
        Assert.assertEquals(msgEventD, msgEventE);
        Assert.assertEquals(source, msgEventE.getDestination());
        Assert.assertEquals(source, msgEventE.getRPCOrigin());
    }
}
