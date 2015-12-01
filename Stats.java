import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by owner on 9/24/15.
 */
public class Stats {

    private static long connectionTime;
    private static int averageTransmissionInSize;
    private static int averageTransmissionOutSize;
    private static int AverageTransmissionSize;
    private static ArrayList<Integer> transmissionInSizes;
    private static ArrayList<Integer> transmissionOutSizes;

    private static int averageTransmissionTime;
    private static ArrayList<Long> transmissionTimes;

    private static long efficiency;
    private static long averageEfficiency;
    private static ArrayList<Long> efficiencies;

    public Stats() {

        connectionTime = 0;

        averageTransmissionInSize = 0;
        averageTransmissionOutSize = 0;
        AverageTransmissionSize = 0;

        transmissionInSizes = new ArrayList<>();
        transmissionOutSizes = new ArrayList<>();

        averageTransmissionTime = 0;
        transmissionTimes = new ArrayList<>();

        efficiency = 0;
        averageEfficiency = 0;
        efficiencies = new ArrayList<>();

    }

    public static long getConnectionTime() {
        return connectionTime;
    }

    public static void setConnectionTime(long connectionTime) {
        Stats.connectionTime = connectionTime;
    }

    public int getAverageTransmissionInSize() {
        return averageTransmissionInSize;
    }

    public void setAverageTransmissionInSize(int averageTransmissionInSize) {
        Stats.averageTransmissionInSize = averageTransmissionInSize;
    }

    public int getAverageTransmissionOutSize() {
        return averageTransmissionOutSize;
    }

    public void setAverageTransmissionOutSize(int averageTransmissionOutSize) {
        Stats.averageTransmissionOutSize = averageTransmissionOutSize;
    }

    public int getAverageTransmissionSize() {
        return AverageTransmissionSize;
    }

    public void setAverageTransmissionSize(int averageTransmissionSize) {
        AverageTransmissionSize = averageTransmissionSize;
    }

    public ArrayList<Integer> getTransmissionInSizes() {
        return transmissionInSizes;
    }

    public void setTransmissionInSizes(ArrayList<Integer> transmissionInSizes) {
        Stats.transmissionInSizes = transmissionInSizes;
    }

    public ArrayList<Integer> getTransmissionOutSizes() {
        return transmissionOutSizes;
    }

    public void setTransmissionOutSizes(ArrayList<Integer> transmissionOutSizes) {
        Stats.transmissionOutSizes = transmissionOutSizes;
    }

    public int getAverageTransmissionTime() {
        return averageTransmissionTime;
    }

    public void setAverageTransmissionTime(int averageTransmissionTime) {
        Stats.averageTransmissionTime = averageTransmissionTime;
    }

    public ArrayList<Long> getTransmissionTimes() {
        return transmissionTimes;
    }

    public void setTransmissionTimes(ArrayList<Long> transmissionTimes) {
        Stats.transmissionTimes = transmissionTimes;
    }


    public static long getEfficiency() {
        return efficiency;
    }

    public static void setEfficiency(long efficiency) {
        Stats.efficiency = efficiency;
    }

    public static long getAverageEfficiency() {
        return averageEfficiency;
    }

    public static void setAverageEfficiency(long averageEfficiency) {
        Stats.averageEfficiency = averageEfficiency;
    }

    public static ArrayList<Long> getEfficiencies() {
        return efficiencies;
    }

    public static void setEfficiencies(ArrayList<Long> efficiencies) {
        Stats.efficiencies = efficiencies;
    }

    public void ComputeAverages(){
        // Average Transmission In Size
        for(int i = 0; i < transmissionInSizes.size(); i++){
            averageTransmissionInSize += transmissionInSizes.get(i);
        }
        averageTransmissionInSize /= transmissionInSizes.size();

        // Average Transmission Out Size
        for(int i = 0; i < transmissionOutSizes.size(); i++){
            averageTransmissionOutSize += transmissionOutSizes.get(i);
        }
        averageTransmissionOutSize /= transmissionOutSizes.size();

        // Average Transmission Time
        for(int i = 0; i < transmissionTimes.size(); i++){
            averageTransmissionTime += transmissionTimes.get(i);
        }
        averageTransmissionTime /= transmissionTimes.size();

        //Calculate Efficiency (chars/ms)
        for(int i = 0; i < efficiencies.size(); i++){
            averageEfficiency += efficiencies.get(i);
        }
        averageEfficiency /= efficiencies.size();


    }

    @Override
    public String toString() {
        try
        {
            FileWriter writer = new FileWriter("src/data.csv", true);

            writer.append("\n");

            writer.append(getAverageTransmissionInSize() + ",");

            writer.append(getAverageTransmissionOutSize() + ",");

            writer.append(getAverageTransmissionTime() + ",");

            writer.append(getConnectionTime() + ",");

            writer.append(getAverageEfficiency() + "");

            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return "Stats {" +  "\n" +
                "ConnectionTime: " + getConnectionTime() + " ms.\n" +
                "Average Transmission In Size: " + getAverageTransmissionInSize() + " chars.\n" +
                "Average Transmission Out Size: " + getAverageTransmissionOutSize() +  " chars.\n" +
                "Average Transmission Time: " + getAverageTransmissionTime() +  " ms.\n" +
                "Average Efficiency: " + getAverageEfficiency() +  " chars/ms.\n" +
                "}";
    }
}
