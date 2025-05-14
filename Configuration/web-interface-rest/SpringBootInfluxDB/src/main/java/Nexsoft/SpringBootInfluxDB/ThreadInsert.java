package Nexsoft.SpringBootInfluxDB;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

import java.time.Instant;
import java.util.Random;

public class ThreadInsert extends Thread {
    private InfluxDBClient client;
    private boolean infarto = false;
    private int idPersona;

    public ThreadInsert(InfluxDBClient client, int idPersona) {
        this.client = client;
        this.idPersona = idPersona;
    }

    public ThreadInsert(InfluxDBClient client,int idPersona, boolean infarto) {
        this.client = client;
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

    public InfluxDBClient getClient() {
        return client;
    }

    public void setClient(InfluxDBClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        // Generate and insert sensor data in a loop
        SensorData sensorData = new SensorData();
        Random random = new Random();
        while (!Thread.currentThread().isInterrupted()) {

            sensorData.newValori(random);

            insertDataIntoInfluxDB(sensorData,isInfarto());
            System.out.println(this.getIdPersona());
            // Sleep for some time before generating the next set of values

            try {
                Thread.sleep(10000 + this.getIdPersona()*1000); // Adjust the sleep duration as per your requirements
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }



    private void insertDataIntoInfluxDB(SensorData sensorData,Boolean infarto) {
        // Use the influxDB instance to insert the sensor data into InfluxDB
        int freq , press;
        WriteApiBlocking writeApi = this.getClient().getWriteApiBlocking();
        if(infarto){
            freq = sensorData.getFrequenzaCardiaca() +50;
            press = sensorData.getPressioneSanguinea()-30;
        }else {
            freq = sensorData.getFrequenzaCardiaca();
            press = sensorData.getPressioneSanguinea();
        }
        Point point = Point
                .measurement("Sensori medici")
                .addTag("persona id", String.valueOf(this.getIdPersona()))
                .addField("temperatura", sensorData.getTemperaturaStanza())
                .addField("umidita %", sensorData.getUmiditaStanza())
                .addField("frequenza cardiaca", freq)
                .addField("Ossigenazione sangue %", sensorData.getLivelloOssigenazione())
                .addField("Pressione sanguinea", press)
                .time(Instant.now(), WritePrecision.NS);
        writeApi.writePoint(InfluxDB.getBucket(), InfluxDB.getOrg(), point);
    }
}
