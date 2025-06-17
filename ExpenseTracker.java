import java.io.*;
import java.util.*;

class Expense {
    String date;
    String category;
    double amount;
    String description;

    public Expense(String date, String category, double amount, String description) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }
}

public class ExpenseTracker {

    public static void main(String[] args) {
        String fileName = "expenses.csv";
        List<Expense> expenses = readExpenses(fileName);

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        printTotal(expenses);
        printMaxMin(expenses);
        printCategoryWise(expenses);
    }

    static List<Expense> readExpenses(String fileName) {
        List<Expense> expenseList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String date = parts[0];
                String category = parts[1];
                double amount = Double.parseDouble(parts[2]);
                String description = parts[3];
                expenseList.add(new Expense(date, category, amount, description));
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return expenseList;
    }

    static void printTotal(List<Expense> expenses) {
        double total = 0;
        for (Expense e : expenses) {
            total += e.amount;
        }
        System.out.println("\nTotal Spending: ₹" + total);
    }

    static void printMaxMin(List<Expense> expenses) {
        Expense max = expenses.get(0);
        Expense min = expenses.get(0);

        for (Expense e : expenses) {
            if (e.amount > max.amount) max = e;
            if (e.amount < min.amount) min = e;
        }

        System.out.println("\nHighest Expense: ₹" + max.amount + " (" + max.category + " - " + max.description + ")");
        System.out.println("Lowest Expense: ₹" + min.amount + " (" + min.category + " - " + min.description + ")");
    }

    static void printCategoryWise(List<Expense> expenses) {
        Map<String, List<Expense>> categoryMap = new HashMap<>();
        double total = 0;

        for (Expense e : expenses) {
            total += e.amount;
            categoryMap.computeIfAbsent(e.category, k -> new ArrayList<>()).add(e);
        }

        System.out.println("\nCategory-wise Summary:");
        for (String category : categoryMap.keySet()) {
            List<Expense> list = categoryMap.get(category);
            double sum = list.stream().mapToDouble(e -> e.amount).sum();
            int count = list.size();
            double percent = (sum / total) * 100;
            System.out.printf("%-10s | ₹%.2f | %d txns | %.2f%%\n", category, sum, count, percent);
        }
    }
}
