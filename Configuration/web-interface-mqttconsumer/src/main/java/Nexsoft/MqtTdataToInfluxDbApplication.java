package Nexsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MqtTdataToInfluxDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqtTdataToInfluxDbApplication.class, args);
		MQTTdataToInfluxDBdata mqtt = new MQTTdataToInfluxDBdata();
        mqtt.receiveMQTT();
	}

}
