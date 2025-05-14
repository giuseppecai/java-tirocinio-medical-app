package Nexsoft.MQTTInfluxDB;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DataController {

    private List<ThreadInsert> insertionThreads = new ArrayList<>();

    @GetMapping("/start/{numberOfThreads}")
    public ResponseEntity<String> startDataInsertion(@PathVariable int numberOfThreads) {
        // Create and start the required number of data insertion threads

        for (int i = 0; i < numberOfThreads; i++) {
            ThreadInsert thread = new ThreadInsert(i);
            thread.start();
            insertionThreads.add(thread);
        }
        System.out.println("Iniziato con " + numberOfThreads + " Persone");
        return ResponseEntity.ok("Programma iniziato con " + numberOfThreads + " Persone" );
    }

    @GetMapping("/infarto/{personId}")
    public ResponseEntity<String> startInfartoPersona(@PathVariable int personId) {
        // Increase the values generated and inserted for the specified person
       ThreadInsert thread = new ThreadInsert(personId,true);
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
        System.out.println("Programma fermato");
        return ResponseEntity.ok("Programma terminato");
    }
}
