import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;

public class CsvGenerator {
    public static void exportToCsv(BlockingDeque<Record> records, String fileName){
        try(FileWriter writer = new FileWriter(fileName)){
            writer.append("beforeRequest, requestFunction, responseCode, latency");
            writer.append('\n');

            for(Record record : records){
                writer.append(record.getBeforeRequest().toString());
                writer.append(',');
                writer.append(record.getRequestFunction());
                writer.append(',');
                writer.append(String.valueOf(record.getResponseCode()));
                writer.append(',');
                writer.append(String.valueOf(record.getLatency()));
                writer.append('\n');
            }

            System.out.println("csv file generated.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
