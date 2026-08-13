public class Stock {

    public enum AlarmLevel {
        NORMAL, WARNING, CRITICAL
    }

    private String symbol;
    private double price;
    private double change;
    private long volume;
    private AlarmLevel alarmLevel;

    public Stock(String symbol, double price, double change, long volume) {
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.volume = volume;
        this.alarmLevel = computeAlarm();
    }

    private AlarmLevel computeAlarm() {
        if (change <= -100|| change >= 200) return AlarmLevel.CRITICAL;
        if (change <= -30|| change >= 100) return AlarmLevel.WARNING;
        return AlarmLevel.NORMAL;
    }

    public String getSymbol(){ return symbol; }
    public double getPrice()        { return price; }
    public double getChange()       { return change; }
    public long getVolume()         { return volume; }
    public AlarmLevel getAlarmLevel() { return alarmLevel; }
}