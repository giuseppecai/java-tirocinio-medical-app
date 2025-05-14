package Nexsoft.MQTTInfluxDB;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.simple.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Random;

public class ThreadInsert extends Thread {

    private boolean infarto = false;
    private int idPersona;

    public ThreadInsert( int idPersona) {
        this.idPersona = idPersona;
    }

    public ThreadInsert( int idPersona, boolean infarto) {
        this.idPersona = idPersona;
        this.infarto = infarto;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public boolean isInfarto() {
        return infarto;
    }

    public void setInfarto(boolean infarto) {
        this.infarto = infarto;
    }



    @Override
    public void run() {

        String broker       = "tcp://mosquitto:1883";
        String topic        = "InfluxDB/" + getIdPersona() ;
        int qos             = 1;
        String clientId     = "Persona:" + getIdPersona();
        MemoryPersistence persistence = new MemoryPersistence();


        // Generate and insert sensor data in a loop
        SensorData sensorData = new SensorData();
        Random random = new Random();
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");


            while (!Thread.currentThread().isInterrupted()) {

                sensorData.newValori(random);
                String msg = jsonMisura(sensorData, isInfarto());

                System.out.println("Publishing message: " + msg);
                MqttMessage message = new MqttMessage(msg.getBytes(StandardCharsets.UTF_8));
                message.setQos(qos);
                sampleClient.publish(topic, message);
                System.out.println("Message published");

                // Sleep for some time before generating the next set of values

                try {
                    Thread.sleep(10000 + this.getIdPersona() * 1000); // Adjust the sleep duration as per your requirements
                } catch (InterruptedException e) {
                    sampleClient.disconnect();
                    System.out.println("Disconnected");
                    Thread.currentThread().interrupt();
                }
            }
        } catch(MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }

    private String jsonMisura (SensorData sensorData, Boolean infarto){
        JSONObject jsonData = new JSONObject();
        if (infarto) {
            jsonData.put("frequenza cardiaca", sensorData.getFrequenzaCardiaca()+50);
            jsonData.put("pressione sanguinea", sensorData.getPressioneSanguinea()-30);
        }else {
            jsonData.put("frequenza cardiaca", sensorData.getFrequenzaCardiaca());
            jsonData.put("pressione sanguinea", sensorData.getPressioneSanguinea());
        }
        jsonData.put("temperatura", sensorData.getTemperaturaStanza());
        jsonData.put("umidita", sensorData.getUmiditaStanza());
        jsonData.put("ossiggenazione", sensorData.getLivelloOssigenazione());
        String misura = jsonData.toJSONString();

       return misura;
    }




}
