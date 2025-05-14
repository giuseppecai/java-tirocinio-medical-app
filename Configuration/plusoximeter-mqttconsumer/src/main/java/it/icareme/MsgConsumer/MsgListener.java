package it.icareme.MsgConsumer;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

@Component
public class MsgListener {
	@Value("${icareme.influxdb.token_string}")
	private String influxDbToken;
	@Value("${icareme.influxdb.url}")
	private String influxDbUrl;
	@Value("${icareme.influxdb.organization}")
	private String influxDbOrganization;
	@Value("${icareme.influxdb.bucket}")
	private String influxDbBucket;

	final Gson gsonLibraryInstance = new Gson();

	InfluxDBClient influxDBClient = null;

	@KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, groupId = KafkaConstants.GROUP_ID)
	public void listen(String message) {
		if (influxDBClient == null) {
			influxDBClient = InfluxDBClientFactory.create(//
					influxDbUrl, influxDbToken.toCharArray(), influxDbOrganization, influxDbBucket);
		}

		System.out.println("> Sending via kafka listener..");
		System.out.println("Triggered message: " + message);
		final Message kafkaMessage = gsonLibraryInstance.fromJson(message, Message.class);
		System.out.println(kafkaMessage.getFields() + "   ::::::    " + kafkaMessage.getTags());

		// Get a WriteApiBlocking instance so we can write to InfluxDB later
		final WriteApiBlocking influxDBClientWriteApiBlockingInstance = influxDBClient.getWriteApiBlocking();

		// Write a data point with point of measurement "mqtt-consumer-for-pulsoximeter"
		// with example data
		// {
		// |___ "deviceId": "BluetoothLE#BluetoothLE00:macaddress",
		// |___ "oxygenation": "90",
		// |___ "timestamp": "(currenttimestamp)"
		// }
		final Point point = Point.measurement("mqtt-consumer-for-pulsoximeter")//
				.addTag("deviceId", kafkaMessage.getTags().getDeviceId())//
				.addField("oxygenation", kafkaMessage.getFields().getOxygenation())//
				.time(Instant.now().toEpochMilli(), WritePrecision.MS);

		// Write to InfluxDB
		influxDBClientWriteApiBlockingInstance.writePoint(point);
	}
}
