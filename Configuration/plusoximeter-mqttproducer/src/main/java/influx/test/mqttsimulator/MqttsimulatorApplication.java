package influx.test.mqttsimulator;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MqttsimulatorApplication {

	public static void main(String[] args) throws InterruptedException, MqttException {
		SpringApplication.run(MqttsimulatorApplication.class, args);

		final String topic = "devices";
		final int qos = 2;
		final String broker = "tcp://mosquitto:1883";
		final String clientId = "device12";
		final MemoryPersistence persistence = new MemoryPersistence();

		try {
			final MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			final MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");
			// get timestamp

			long timestampLong;

			while (true) {
				final Date date = new Date();
				timestampLong = date.getTime() * 1000000;

				final String currentTimestamp = Instant.now().toString();
				// get bpm
				final int value1_randomNum = ThreadLocalRandom.current().nextInt(30, 200 + 1);
				final int value2_randomNum = ThreadLocalRandom.current().nextInt(30, 200 + 1);
				final String bpmValue1 = String.valueOf(value1_randomNum);
				final String bpmValue2 = String.valueOf(value2_randomNum);
				String content = null;

				if (value2_randomNum % 2 == 0) {
					System.out.println(currentTimestamp);
					content = "{\"deviceId\":\"BluetoothLE#BluetoothLE00:e0:4c:c7:88:03-24:6f:28:af:5e:62\",\"timestamp\":\""
							+ currentTimestamp + "\",\"oxygenation\":" + bpmValue1 + ",\"value2\":" + bpmValue2 + "}";
				} else {
					System.out.println(currentTimestamp);
					content = "{\"deviceId\":\"BluetoothLE#BluetoothLE00:e0:4c:c7:88:03-24:6f:28:af:5e:62\",\"timestamp\":\""
							+ currentTimestamp + "\",\"oxygenation\":" + bpmValue1 + ",\"value2\":null}";
				}
				System.out.println(content);

				final MqttMessage message = new MqttMessage(content.getBytes());
				message.setQos(qos);
				sampleClient.publish(topic, message);
				System.out.println("Message published");

				if (value1_randomNum > 1000) {
					break;
				}

				Thread.sleep(10000);
			}

			sampleClient.disconnect();
			System.out.println("Disconnected");
			System.exit(0);
		} catch (final MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}

}
