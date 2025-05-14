# 🏥 Medical IoT Java Application

Progetto sviluppato durante il mio tirocinio universitario, volto alla realizzazione di un'applicazione distribuita per la gestione e il monitoraggio remoto di parametri vitali raccolti da un dispotivo medicale simulato (in tre versioni, due realizzate con simulazione automatizzata tramite node-red e una con un dispositivo simulato in Java). Il mio ruolo nel progetto è stato quello di integrare tutti i sistemi esistenti (applicativi Java simulati e framework vari) all'interno di un ambiente Docker. Mi sono occupato dell'inizializzazione dell'intero sistema, della gestione degli altri membri del progetto e del rilascio finale con i file di configurazione necessari per far partire l'intera infrastruttura.

## 🚀 Tecnologie utilizzate

- Java (Spring Boot): sono stati realizzati 3 progetti differenti per simulare il dispositivo medicale, tutti e 3 vengono configurati tramite applicativi Java con Spring Boot.
- NodeRed: 
- InfluxDB: i dati vengono raccolti dai due consumer
- Telegraf
- PostgreSQL / MySQL


## 📦 Funzionalità principali

- 🔄 Acquisizione e salvataggio dati da dispositivi medicali simulati
- 🗃️ Persistenza dei dati su InfluxDB
- 📦 Contenitori Docker per il deploy

## 🧱 Architettura

L'ambiente può essere suddiviso in tre sottostrutture:
  1. **Web Interface MQTT**: viene aggiornato tramite nodered e una sua Dashboard accessibile al seguente indirizzo: http://localhost:1880/ui/ All'interno della Dashboard selezionare il Tab 2 per poter modificare i dati di questa struttura (aggiungere/rimuovere persone al DB e/o simulare infarti). I dati sono visualizzabili attraverso InfluxDB e Grafana.
  2. **Web Interface Rest**: viene aggiornato tramite nodered e una sua Dashboard accessibile al seguente indirizzo: http://localhost:1880/ui/ All'interno della Dashboard selezionare il Tab 1 per poter modificare i dati di questa struttura (aggiungere/rimuovere persone al DB e/o simulare infarti). I dati sono visualizzabili attraverso InfluxDB e Grafana. 
  3. **Pulsoximeter MQTT**: i dati vengono automaticamente generati da un applicativo Java, spediti su un canale MQTT e raccolti infine da un altro applicativo Java, che si occupa di spedirli a InfluxDB e Grafana, attraverso i quali è possibile visualizzare i dati.

![Architettura](https://drive.google.com/uc?id=1sMwGDQGTuZpesI2FNJhHgDyzzZghnryX)

## ⚙️ Come eseguire il progetto

1. Clona il repository:
   ```bash
   git clone https://github.com/giuseppecai/java-tirocinio-medical-app.git
   cd java-tirocinio-medical-app
   
2. Per poter utilizzare l'ambiente su un nuovo dispositivo ci sono delle procedure da effettuare, spiegate nel file config.ini. Per quanto riguarda invece i file necessari per poter utilizzare il tutto su un nuovo dispositivo, essi si trovano nella cartella "I.CARE_ME". Il file "docker-compose.yml" consente di installare automaticamente i container in docker, ma è necessario importare ulteriori file di configurazione sul proprio dispositivo, presenti nella sottocartella "Configuration".
- Grafana: contiene una copia di un volume di Grafana
- Mosquitto: contiene il file di configurazione, necessario per l'utilizzo di Mosquitto
- NodeRed: contiene una copia di un volume di NodeRed, con all'interno già di due Flow necessari per l'utilizzo della dashboard
- Telegraf: contiene il file di configurazione, necessario per l'utilizzo di Telegraf
- Pulsoximeter MQTT consumer e MQTT producer: due cartelle contenenti i vari file per utilizzare i due applicativi in Java
- Web Interface MQTT consumer e MQTT producer: due cartelle contenenti i vari file per utilizzare i due applicativi in Java. Sono gli applicativi che utilizzano l'interfaccia di Node Red
- Web Interface Rest: una cartella contenente i vari file per utilizzare l'applicativo in Java. E' l'applicativo che utilizza l'interfaccia di Node Red.
