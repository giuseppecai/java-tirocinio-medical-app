package Nexsoft.SpringBootInfluxDB;

public class InfluxDB {
    private static char[] token = "hir3Dxcze20m5ce6zWETuCH6oYFnb763xGZA57Zv0Q1ejkDOhyLJRBrtuZ1-SvktIquoMx8y8ythWY5F_ImUjA==".toCharArray();
    private static String bucket = "Sensor";
    private static String org = "Nexsoft";
    private static String url = "http://influxdb:8086";

    public static String getUrl() {
        return url;
    }

    public static String getOrg() {
        return org;
    }

    public static String getBucket() {
        return bucket;
    }

    public static char[] getToken() {
        return token;
    }
}
