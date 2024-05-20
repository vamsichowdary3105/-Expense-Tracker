import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "data/expenses.csv";
    private static List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        loadExpenses();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nExpense Tracker");
            System.out.println("1. Add Expense");
            System.out.println("2. Edit Expense");
            System.out.println("3. Delete Expense");
            System.out.println("4. View Expenses");
            System.out.println("5. Generate Report");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addExpense(scanner);
                    break;
                case 2:
                    editExpense(scanner);
                    break;
                case 3:
                    deleteExpense(scanner);
                    break;
                case 4:
                    viewExpenses();
                    break;
                case 5:
                    generateReport();
                    break;
                case 6:
                    saveExpenses();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void loadExpenses() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String line : lines) {
                String[] parts = line.split(",");
                expenses.add(new Expense(parts[0], parts[1], Double.parseDouble(parts[2])));
            }
        } catch (IOException e) {
            System.out.println("No existing expenses found.");
        }
    }

    private static void saveExpenses() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            for (Expense expense : expenses) {
                writer.write(expense.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        expenses.add(new Expense(description, category, amount));
        System.out.println("Expense added.");
    }

    private static void editExpense(Scanner scanner) {
        viewExpenses();
        System.out.print("Enter the index of the expense to edit: ");
        int index = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (index < 0 || index >= expenses.size()) {
            System.out.println("Invalid index.");
            return;
        }

        Expense expense = expenses.get(index);
        System.out.print("Enter new description (" + expense.getDescription() + "): ");
        String description = scanner.nextLine();
        System.out.print("Enter new category (" + expense.getCategory() + "): ");
        String category = scanner.nextLine();
        System.out.print("Enter new amount (" + expense.getAmount() + "): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        expense.setDescription(description.isEmpty() ? expense.getDescription() : description);
        expense.setCategory(category.isEmpty() ? expense.getCategory() : category);
        expense.setAmount(amount == 0 ? expense.getAmount() : amount);

        System.out.println("Expense updated.");
    }

    private static void deleteExpense(Scanner scanner) {
        viewExpenses();
        System.out.print("Enter the index of the expense to delete: ");
        int index = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (index < 0 || index >= expenses.size()) {
            System.out.println("Invalid index.");
            return;
        }

        expenses.remove(index);
        System.out.println("Expense deleted.");
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        System.out.println("\nExpenses:");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
        }
    }

    private static void generateReport() {
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            categoryTotals.put(expense.getCategory(), categoryTotals.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
        }

        System.out.println("\nExpense Report:");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            System.out.println(entry.getKey() + ": $" + entry.getValue());
        }
    }
}

class Expense {
    private String description;
    private String category;
    private double amount;

    public Expense(String description, String category, double amount) {
        this.description = description;
        this.category = category;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String toCSV() {
        return description + "," + category + "," + amount;
    }

    @Override
    public String toString() {
        return "Description: " + description + ", Category: " + category + ", Amount: $" + amount;
    }
}
