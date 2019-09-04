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
		
		boolean useTopic = false;   //�Ƿ�ʹ������ģʽ
		boolean persistent = false;  //�Ƿ�ʹ�ó־û�
		try {
			//�û�����������ӹ���Ա����ȡ
			String user = "admin";
			String password = "activemq";
			//ϵͳ���ڻ���ip���˿�
			String url = "tcp://192.168.18.151:61616";
			//ActiveMQConnectionFactory:���ӹ��������ڴ������ӣ��˴�����ActiveMQConnectionFactoryʵ��
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory( user, password, url);
			//�����ӹ����õ����Ӷ���
			ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
			//����
			connection.start();
			//�����Ự
			Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			//ʹ�ö��л�������
			Destination destination = null;
			if(useTopic) {
				//����topic
				destination = session.createTopic("Topic.TEST.0");
			} else {
				//����queue
				destination = session.createQueue("Queue.TEST.0");
			}
			
			//������Ϣ��������
			MessageProducer producer = session.createProducer(destination);
			//�����Ƿ�־û�
			if(persistent) {
				//������Ϣ�ĳ־û�
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			} else {
				//������Ϣ�ķǳ־û�
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			}
			Date date = new Date();
			long t1 = date.getTime();
			System.out.println("connThread0 ��ʼʱ�䣺"+new Date());
			int p = 0;
			for (int i = 0; i < 100000; i++) {
				//����TextMessage��Ϣ����
				TextMessage message = session.createTextMessage();
				String s = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
						+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
						+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
						+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
						+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
						;
				//������Ϣ����
				message.setText(s);
				try {
					//�����߷�����Ϣ
					producer.send(message);
					p++;
					if(p%1000 == 0){
						System.out.println("connThread0 �ѷ��ͣ�"+p+"������ʱ��"+(new Date().getTime() - t1));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			System.out.println("connThread0 ������"+p+"��������ʱ�䣺"+new Date());
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
