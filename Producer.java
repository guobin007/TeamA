package typicalSample;

import java.util.Date;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {
	
	public static void main(String[] args) {
		
		boolean useTopic = false;   //是否使用主题模式
		boolean persistent = false;  //是否使用持久化
		try {
			//用户名和密码请从管理员处获取
			String user = "admin";
			String password = "activemq";
			//系统所在机器ip及端口
			String url = "tcp://192.168.18.151:61616";
			//ActiveMQConnectionFactory:连接工厂，用于创建连接，此处构造ActiveMQConnectionFactory实例
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory( user, password, url);
			//从连接工厂得到连接对象
			ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
			//启动
			connection.start();
			//创建会话
			Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			//使用队列还是主题
			Destination destination = null;
			if(useTopic) {
				//创建topic
				destination = session.createTopic("Topic.TEST.0");
			} else {
				//创建queue
				destination = session.createQueue("Queue.TEST.0");
			}
			
			//创建消息的生产者
			MessageProducer producer = session.createProducer(destination);
			//设置是否持久化
			if(persistent) {
				//设置消息的持久化
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			} else {
				//设置消息的非持久化
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			}
			Date date = new Date();
			long t1 = date.getTime();
			System.out.println("connThread0 开始时间："+new Date());
			int p = 0;
			for (int i = 0; i < 100000; i++) {
				//创建TextMessage消息类型
				TextMessage message = session.createTextMessage();
				String s = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
						+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
						+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
						+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
						+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
						;
				//设置消息主体
				message.setText(s);
				try {
					//生产者发送消息
					producer.send(message);
					p++;
					if(p%1000 == 0){
						System.out.println("connThread0 已发送："+p+"条，用时："+(new Date().getTime() - t1));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			System.out.println("connThread0 共发送"+p+"条，结束时间："+new Date());
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
