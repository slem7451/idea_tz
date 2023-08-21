import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;


public class Ticket
{
    public static void main(String args[])
    {
        try {
            ArrayList<Long> prices = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy k:mm");
            double average = 0;
            double median;
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader("tickets.json"));
            JSONArray tickets = (JSONArray) json.get("tickets");
            JSONObject test = (JSONObject) tickets.get(0);
            LocalDateTime date_departure = LocalDateTime.parse(((String) ((JSONObject) tickets.get(0)).get("departure_date")).replace(".", "-") + " " + ((String) ((JSONObject) tickets.get(0)).get("departure_time")), formatter);
            LocalDateTime date_arrival = LocalDateTime.parse(((String) ((JSONObject) tickets.get(0)).get("arrival_date")).replace(".", "-") + " " + ((String) ((JSONObject) tickets.get(0)).get("arrival_time")), formatter);
            long minDiffrence = Duration.between(date_departure, date_arrival).getSeconds();
            for (int i = 0; i < tickets.size(); ++i) {
                JSONObject item = (JSONObject) tickets.get(i);
                if (((String) item.get("origin_name")).equals("Владивосток") && ((String) item.get("destination_name")).equals("Тель-Авив") ||
                    ((String) item.get("destination_name")).equals("Владивосток") && ((String) item.get("origin_name")).equals("Тель-Авив")) {
                    prices.add((long) item.get("price"));
                    average += (long) item.get("price");
                    date_departure = LocalDateTime.parse(((String) item.get("departure_date")).replace(".", "-") + " " + ((String) item.get("departure_time")), formatter);
                    date_arrival = LocalDateTime.parse(((String) item.get("arrival_date")).replace(".", "-") + " " + ((String) item.get("arrival_time")), formatter);
                    long diffrence = Duration.between(date_departure, date_arrival).getSeconds();
                    if (diffrence < minDiffrence) {
                        minDiffrence = diffrence;
                    }
                }
            }
            average /= (double) prices.size();
            prices.sort(Comparator.naturalOrder());
            if (prices.size() % 2 == 0) {
                median = ((double) prices.get(prices.size() / 2) + (double) prices.get(prices.size() / 2 - 1)) / 2;
            } else {
                median = (double) prices.get(prices.size() / 2);
            }
            double priceDiffrence = average - median;
            System.out.println("Answer into answer.txt");
            FileWriter output = new FileWriter(new File("answer.txt"));
            output.write("Diffrence of average and median price - " + priceDiffrence + "\nMin time of flight - " + minDiffrence / 3600 + " hours " + (minDiffrence / 60) % 60 + " minutes");
            output.close();
        } catch(IOException e) {
            System.out.println(e);
        } catch(ParseException e) {
            System.out.println(e);
        }
    }
}