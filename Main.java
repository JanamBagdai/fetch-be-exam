import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static final String FILE_NAME = "transactions.csv";
    private static final String DELIMITER = ",";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static void main(String[] args) {
        int spendPoints = Integer.parseInt(args[0]);
        List<Transaction> transactions = new ArrayList<>();
        Map<String, Integer> payerBalances = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(DELIMITER);
                transactions.add(
                        new Transaction(
                                values[0],
                                Integer.parseInt(values[1]),
                                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(values[2].replace("\"", ""))));
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error reading from file " + FILE_NAME + ": " + e.getMessage());
            System.exit(1);
        }

// Sort transactions by timestamp in ascending order
        Collections.sort(transactions);
        System.out.println(transactions);
        for (Transaction transaction : transactions) {
            int currentBalance = payerBalances.getOrDefault(transaction.getPayer(), 0);
            payerBalances.put(transaction.getPayer(), currentBalance + transaction.getPoints());
        }

        System.out.println(payerBalances);

        for (Transaction transaction : transactions) {
            int currentBalance = transaction.getPoints();

          //  System.out.println("current player"+transaction.getPayer() + "current points of transaction"+currentBalance + " current amount to spend  " + spendPoints);

                if(spendPoints-currentBalance<0){
                    payerBalances.put(transaction.getPayer(), payerBalances.get(transaction.getPayer()) - spendPoints);
                    break;
                }
                else {
                   // System.out.println("before minusing: "+ payerBalances.get(transaction.getPayer()));
                    payerBalances.put(transaction.getPayer(), payerBalances.get(transaction.getPayer()) - transaction.getPoints());
                 //   System.out.println("after minusing: "+ payerBalances.get(transaction.getPayer()));
                    spendPoints -= currentBalance;
                }

        }

        System.out.println(payerBalances);
    }

    private static class Transaction implements Comparable<Transaction> {
        private String payer;
        private int points;
        private Date timestamp;

        public Transaction(String payer, int points, Date timestamp) {
            this.payer = payer;
            this.points = points;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "payer='" + payer + '\'' +
                    ", points=" + points +
                    ", timestamp=" + timestamp +
                    '}';
        }

        public String getPayer() {
            return payer;
        }

        public int getPoints() {
            return points;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        @Override
        public int compareTo(Transaction o) {
            return timestamp.compareTo(o.timestamp);
        }
    }
}