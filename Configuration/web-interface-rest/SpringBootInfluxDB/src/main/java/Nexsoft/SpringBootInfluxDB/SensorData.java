package Nexsoft.SpringBootInfluxDB;

import java.util.Random;

public class SensorData {
    private float temperaturaStanza;
    private int umiditaStanza;
    private int frequenzaCardiaca;
    private int livelloOssigenazione;
    private int pressioneSanguinea;

    public SensorData() {
        this.temperaturaStanza = 25;
        this.umiditaStanza = 50;
        this.frequenzaCardiaca = 80;
        this.livelloOssigenazione = 95;
        this.pressioneSanguinea = 120;
    }

    public float getTemperaturaStanza() {
        return temperaturaStanza;
    }

    public void setTemperaturaStanza(float temperaturaStanza) {
        this.temperaturaStanza = temperaturaStanza;
    }

    public int getPressioneSanguinea() {
        return pressioneSanguinea;
    }

    public void setPressioneSanguinea(int pressioneSanguinea) {
        this.pressioneSanguinea = pressioneSanguinea;
    }

    public int getUmiditaStanza() {
        return umiditaStanza;
    }

    public void setUmiditaStanza(int umiditaStanza) {
        this.umiditaStanza = umiditaStanza;
    }

    public int getLivelloOssigenazione() {
        return livelloOssigenazione;
    }

    public void setLivelloOssigenazione(int livelloOssigenazione) {
        this.livelloOssigenazione = livelloOssigenazione;
    }

    public int getFrequenzaCardiaca() {
        return frequenzaCardiaca;
    }

    public void setFrequenzaCardiaca(int frequenzaCardiaca) {
        this.frequenzaCardiaca = frequenzaCardiaca;
    }

    //funzioni per valori nuovi

    // temp
    public float nextTemp(Random random){
        float temp = this.getTemperaturaStanza();
        return temp + random.nextFloat()*2 -1;
    }

    //umid
    public int nextUmid(Random random){
        int umidNew;
        int umid = this.getUmiditaStanza();
        do {
            umidNew = umid + random.nextInt(3) -1;
        }while (umidNew < 0 || umidNew > 100);
        return umidNew;
    }


    //freq
    public int nextFreq(Random random){
        int freq = this.getFrequenzaCardiaca();
        return freq + random.nextInt(7) -3;
    }

    //ossig
    public int nextOssig(Random random){
        int ossigNew;
        int ossig = this.getLivelloOssigenazione();
        do {
            ossigNew = ossig + random.nextInt(3) -1;
        }while (ossigNew < 0 || ossigNew > 100);
        return ossigNew;
    }

    //press
    public int nextPress(Random random){
        int press = this.getPressioneSanguinea();
        return press + random.nextInt(5) -2;
    }


    //valori totali

    public void newValori(Random random){

        this.setTemperaturaStanza(nextTemp(random));
        this.setUmiditaStanza(nextUmid(random));
        this.setLivelloOssigenazione(nextOssig(random));
        this.setFrequenzaCardiaca(nextFreq(random));
        this.setPressioneSanguinea(nextPress(random));

    }
}

