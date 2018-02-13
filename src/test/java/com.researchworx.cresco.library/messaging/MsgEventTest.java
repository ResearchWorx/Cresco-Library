package com.researchworx.cresco.library.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class MsgEventTest {
    private final Logger logger = LoggerFactory.getLogger(MsgEventTest.class);

    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @Test
    public void GeneralActiveMQ() throws Exception {
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
    public void MsgEventMarshalling() throws Exception {
        final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "vm://localhost?broker.persistent=false");
        final Connection connection = connectionFactory.createConnection();
        connection.start();
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
}
