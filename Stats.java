import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by owner on 9/24/15.
 */
public class Stats {

    private static int averageTransmissionInSize;
    private static int averageTransmissionOutSize;
    private static int AverageTransmissionSize;
    private static ArrayList<Integer> transmissionInSizes;
    private static ArrayList<Integer> transmissionOutSizes;

    private static int averageTransmissionTime;
    private static ArrayList<Long> transmissionTimes;

    public Stats() {

        averageTransmissionInSize = 0;
        averageTransmissionOutSize = 0;
        AverageTransmissionSize = 0;

        transmissionInSizes = new ArrayList<>();
        transmissionOutSizes = new ArrayList<>();

        averageTransmissionTime = 0;
        transmissionTimes = new ArrayList<>();

    }

    public static int getAverageTransmissionInSize() {
        return averageTransmissionInSize;
    }

    public static void setAverageTransmissionInSize(int averageTransmissionInSize) {
        Stats.averageTransmissionInSize = averageTransmissionInSize;
    }

    public static int getAverageTransmissionOutSize() {
        return averageTransmissionOutSize;
    }

    public static void setAverageTransmissionOutSize(int averageTransmissionOutSize) {
        Stats.averageTransmissionOutSize = averageTransmissionOutSize;
    }

    public static int getAverageTransmissionSize() {
        return AverageTransmissionSize;
    }

    public static void setAverageTransmissionSize(int averageTransmissionSize) {
        AverageTransmissionSize = averageTransmissionSize;
    }

    public static ArrayList<Integer> getTransmissionInSizes() {
        return transmissionInSizes;
    }

    public static void setTransmissionInSizes(ArrayList<Integer> transmissionInSizes) {
        Stats.transmissionInSizes = transmissionInSizes;
    }

    public static ArrayList<Integer> getTransmissionOutSizes() {
        return transmissionOutSizes;
    }

    public static void setTransmissionOutSizes(ArrayList<Integer> transmissionOutSizes) {
        Stats.transmissionOutSizes = transmissionOutSizes;
    }

    public static int getAverageTransmissionTime() {
        return averageTransmissionTime;
    }

    public static void setAverageTransmissionTime(int averageTransmissionTime) {
        Stats.averageTransmissionTime = averageTransmissionTime;
    }

    public static ArrayList<Long> getTransmissionTimes() {
        return transmissionTimes;
    }

    public static void setTransmissionTimes(ArrayList<Long> transmissionTimes) {
        Stats.transmissionTimes = transmissionTimes;
    }

    public static void ComputeAverages(){
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
            //generate whatever data you want

            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return "Stats {" +  "\n" +
                "Average Transmission In Size: " + getAverageTransmissionInSize() + " chars.\n" +
                "Average Transmission Out Size: " + getAverageTransmissionOutSize() +  " chars.\n" +
                "Average Transmission Time: " + getAverageTransmissionTime() +  " ms.\n" +
                "}";
    }
}
