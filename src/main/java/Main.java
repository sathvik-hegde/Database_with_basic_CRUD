import java.sql.*;
import java.util.Scanner;

public class Main {

    //static Connection conn = null;o

    static final String currentTable = "contacts";

    //would have been better to make the scanner and the connection objects static so
    //they can be referenced anywhere.
    public static void main(String[] args) {

        // var keyword is recently added to java in java 11, dynamically assigned typed.

        String dbPath = "src/main/resources/";
        String dbName = "database.db";

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:"+dbPath+dbName);

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS contacts(firstName TEXT, " +
                    "lastName TEXT ,age INTEGER) ");

            statement.execute("CREATE TABLE IF NOT EXISTS contacts2(firstName TEXT, " +
                    "lastName TEXT ,age INTEGER) ");

            menu(connection);

        } catch (SQLException e) {
            //this catches all the exceptions in the program, because all the functions are linked together
            //and the parent is called above.
            e.printStackTrace();
        }

    }

    public static void menu(Connection connection) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------");
        System.out.println("Database editor program");
        System.out.println("Type '5' to quit ");
        System.out.println("-----------------------");
        System.out.print("Press 1-4 for CRUD: ");
        int choice = sc.nextInt();

        if(choice == 1) {
            prepareDataToAddToTheDatabase(connection, sc);
        } else if (choice == 2) {
            displayTable(connection);
        } else if (choice == 3) {
            updateDatabase(connection);
        } else if (choice == 4) {
            deleteData(connection);
        } else {
            System.out.println("Please choose a valid number from 1-4 for needed CRUD operation!");
            menu(connection);
        }
    }

    public static void prepareDataToAddToTheDatabase(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("First Name: ");
        String firstName = scanner.next();

        System.out.print("Last Name: ");
        String lastName = scanner.next();

        System.out.print("Age: ");
        int age = scanner.nextInt();

        addToDatabase(connection, firstName, lastName, age);

    }

    public static void addToDatabase(Connection connection, String firstName, String lastName, int age) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO contacts " +
                "(firstName, lastName, age) " +
                "VALUES(?,?,?)");

        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setInt(3, age);

        preparedStatement.execute();

        System.out.println("Successfully added new data!");
        menu(connection);
    }

    //could make return type 'boolean' for these methods for additional checking
    public static void displayTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM contacts ");

        while(rs.next()) {
            System.out.print(rs.getString(1)+",");
            System.out.print(rs.getString(2)+",");
            System.out.println(rs.getString(3));
            System.out.println("---");
        }

        menu(connection);
    }

    public static void updateDatabase(Connection connection) throws SQLException {

        Statement st = connection.createStatement();

        st.execute("UPDATE contacts SET firstName = 'Sathvik' WHERE age = 200");

        menu(connection);
    }

    public static void deleteData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        statement.execute("DELETE FROM contacts WHERE age = 70");

        menu(connection);
    }
}
