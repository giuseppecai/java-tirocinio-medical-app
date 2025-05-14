package Nexsoft;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.time.Instant;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MQTTdataToInfluxDBdata implements MqttCallback {

    String broker       = "tcp://mosquitto:1883";
    String topic        = "InfluxDB/#";

    String clientId     = "MiddleManInfluxDB";

    InfluxDBClient client;
    MqttClient mqttClient;



    public MQTTdataToInfluxDBdata() {
        this.client = InfluxDBClientFactory.create(InfluxDB.getUrl(), InfluxDB.getToken(), InfluxDB.getOrg(), InfluxDB.getBucket());;
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }
    public String getBroker() {
        return broker;
    }

    public String getTopic() {
        return topic;
    }


    public String getClientId() {
        return clientId;
    }

    public InfluxDBClient getClient() {
        return client;
    }

    public void receiveMQTT(){
        try {
            this.mqttClient = new MqttClient(this.getBroker(), this.getClientId(), new MemoryPersistence());
            this.mqttClient.setCallback(this);
            MqttConnectOptions conOpts = new MqttConnectOptions();
            conOpts.setAutomaticReconnect(true);
            conOpts.setCleanSession(false); // Set clean session to false
            this.mqttClient.connect(conOpts);
            System.out.println("Connected");
            this.mqttClient.subscribe(this.getTopic());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {




        String idPersonaString = s.substring("InfluxDB/".length());
        int idPersona = Integer.parseInt(idPersonaString);
        String message = mqttMessage.toString();

        System.out.println("ricevuto data persona:" +idPersona);
        System.out.println("data:" + message);

        new Thread(() -> {
            try {

                try {

                    // Parse the JSON string
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(message);

                    // Extract values from the JSON object
                    float temp = Float.parseFloat(jsonObject.get("temperatura").toString());
                    int umid = Integer.parseInt(jsonObject.get("umidita").toString());
                    int oss = Integer.parseInt(jsonObject.get("ossiggenazione").toString());
                    int freq = Integer.parseInt(jsonObject.get("frequenza cardiaca").toString());
                    int press = Integer.parseInt(jsonObject.get("pressione sanguinea").toString());

                    System.out.println("temp:"+temp+" umid:"+umid+" oss:"+oss+" freq:"+freq+" press:"+press);

                    Point point = Point
                            .measurement("Sensori medici")
                            .addTag("persona id", String.valueOf(idPersona))
                            .addField("temperatura", temp)
                            .addField("umidita %", umid)
                            .addField("frequenza cardiaca", freq)
                            .addField("Ossigenazione sangue %", oss)
                            .addField("Pressione sanguinea", press)
                            .time(Instant.now(), WritePrecision.NS);

                    WriteApiBlocking writeApi = this.getClient().getWriteApiBlocking();
                    writeApi.writePoint(InfluxDB.getBucket(), InfluxDB.getOrg(), point);
                    System.out.println("Inserito in InfluxDB");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }


}
