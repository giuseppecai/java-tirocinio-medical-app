package Nexsoft.SpringBootInfluxDB;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
public class DataController {

    private List<ThreadInsert> insertionThreads = new ArrayList<>();
    private InfluxDBClient client;

    public InfluxDBClient getClient() {
        return client;
    }

    public void startClient() {
        this.client = InfluxDBClientFactory.create(InfluxDB.getUrl(), InfluxDB.getToken(), InfluxDB.getOrg(), InfluxDB.getBucket());
    }

    public void stopClient(){
        this.client.close();
    }
    @GetMapping("/start/{numberOfThreads}")
    public ResponseEntity<String> startDataInsertion(@PathVariable int numberOfThreads) {
        // Create and start the required number of data insertion threads
        startClient();
        for (int i = 0; i < numberOfThreads; i++) {
            ThreadInsert thread = new ThreadInsert(this.getClient(),i);
            thread.start();
            insertionThreads.add(thread);
        }
        System.out.println("Iniziato con " + numberOfThreads + " Persone");
        return ResponseEntity.ok("Programma iniziato con " + numberOfThreads + " Persone" );
    }

    @GetMapping("/infarto/{personId}")
    public ResponseEntity<String> startInfartoPersona(@PathVariable int personId) {
        // Increase the values generated and inserted for the specified person
       ThreadInsert thread = new ThreadInsert(this.getClient(),personId,true);
       insertionThreads.get(personId).interrupt();
       thread.start();
       insertionThreads.set(personId,thread);
       System.out.println("Inizio infarto persona : " + personId);
       return ResponseEntity.ok("Infarto iniziato per persona con id : " + personId);
    }


    @PostMapping("/stop")
    public ResponseEntity<String> stopDataInsertion() {
        // Stop all data insertion threads and reset the program
        for (ThreadInsert thread : insertionThreads) {
            thread.interrupt();
        }
        insertionThreads.clear();
        stopClient();
        System.out.println("Programma fermato");
        return ResponseEntity.ok("Programma terminato");
    }
}
