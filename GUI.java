import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;


public class GUI implements ActionListener {
    private Connection connection;
    private static JFrame frame;

    private final String url = "jdbc:mysql://localhost:3306/ATM";
    private final String username = "root";
    private final String password = "Gauri@2004";
    private Map<String, String> accountPins;
    private ArrayList<String> accountList;
    private Map<String, ArrayList<String>> transactionHistory;
    private JTextField accountNameField;
    private JTextField initialBalanceField;
    JLabel label;
    JButton bankingButton;
    JButton accountButton;
    JButton transferButton;
    JButton withdrawButton;
    JButton cashDepositButton;
    JButton balanceButton;
    JButton backButton;
    JButton createabutton;
    JButton deleteabutton;
    JButton ministatementButton;
    JButton cuurencycoversionButton;
    JButton donationButton;
    JButton pinchangeButton;
    JButton exitButton;
    JButton emergencyserviceButton;
    JButton useridButton;

    JLabel thanksButton;
    JLabel contact;




    public GUI() {

        // Initialize JDBC connection
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
        accountList = fetchAccountListFromDatabase();

        frame = new JFrame();
        accountPins = new HashMap<>();
        JPanel panel = new JPanel();
        panel.setLayout(null);  // Set layout to null for absolute positioning
        accountList = new ArrayList<>();
        label = new JLabel("Welcome to our ATM services");
        label.setBounds(10, 10, 300, 30);  // x, y, width, height
        contact = new JLabel("Contact to the our  helpline number : 89**");
        contact.setBounds(100,100,1000,50);
        contact.setVisible(false);

        bankingButton = new JButton("Banking");
        bankingButton.setBounds(10, 50, 200, 30);
        bankingButton.addActionListener(this);

        accountButton = new JButton("Account Control");
        accountButton.addActionListener(this);
        accountButton.setBounds(250, 50, 200, 30);

        ministatementButton = new JButton("Mini Statement");
        ministatementButton.addActionListener(this);
        ministatementButton.setBounds(10, 120, 200, 30);

        cuurencycoversionButton = new JButton("Currency \nConversion");
        cuurencycoversionButton.addActionListener(this);
        cuurencycoversionButton.setBounds(250, 260, 200, 30);

        donationButton = new JButton("Donation");
        donationButton.addActionListener(this);
        donationButton.setBounds(10, 190, 200, 30);

        pinchangeButton = new JButton("Pin Change");
        pinchangeButton.addActionListener(this);
        pinchangeButton.setBounds(250, 190, 200, 30);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        exitButton.setBounds(10, 260, 200, 30);

        emergencyserviceButton = new JButton("Emergency Service");
        emergencyserviceButton.addActionListener(this);
        emergencyserviceButton.setBounds(250, 120, 200, 30);

        transferButton = new JButton("Transfer Fund");
        transferButton.addActionListener(this);
        transferButton.setBounds(10, 90, 150, 30);
        transferButton.setVisible(false);

        withdrawButton = new JButton("Withdraw Fund");
        withdrawButton.addActionListener(this);
        withdrawButton.setBounds(200, 90, 150, 30);
        withdrawButton.setVisible(false);

        cashDepositButton = new JButton("Cash Deposit");
        cashDepositButton.addActionListener(this);
        cashDepositButton.setBounds(10, 140, 150, 30);
        cashDepositButton.setVisible(false);

        balanceButton = new JButton("Balance Inquiry");
        balanceButton.addActionListener(this);
        balanceButton.setBounds(200, 140, 150, 30);
        balanceButton.setVisible(false);

        createabutton = new JButton("Create Account");
        createabutton.addActionListener(this);
        createabutton.setBounds(10, 90, 200, 30);
        createabutton.setVisible(false);

        deleteabutton = new JButton("Delete Account");
        deleteabutton.addActionListener(this);
        deleteabutton.setBounds(250, 90, 200, 30);
        deleteabutton.setVisible(false);


        backButton = new JButton("Back");
        backButton.addActionListener(this);
        backButton.setBounds(100, 200, 150, 30);
        backButton.setVisible(false);

        useridButton = new JButton("User ID");
        useridButton.addActionListener(this);
        useridButton.setBounds(100,100,100,30);
        useridButton.setVisible(false);


        thanksButton = new JLabel("Thank you for visiting ATM Machine");
        thanksButton.setBounds(100,100 ,300,50);
        thanksButton.setVisible(false);


        panel.setBorder(BorderFactory.createEmptyBorder(400, 400, 400, 400));

        frame.add(panel);
        panel.add(label);
        panel.add(bankingButton );
        panel.add(accountButton);
        panel.add(ministatementButton);
        panel.add(cuurencycoversionButton);
        panel.add(donationButton);
        panel.add(pinchangeButton);
        panel.add(exitButton);
        panel.add(emergencyserviceButton);
        panel.add(transferButton);
        panel.add(withdrawButton);
        panel.add(cashDepositButton);
        panel.add(balanceButton);
        panel.add(createabutton);
        panel.add(deleteabutton);
        panel.add(backButton);
        panel.add(thanksButton);
        panel.add(contact);

        frame.add(panel);
        frame.setTitle("ATM machine");
        frame.setIconImage(new ImageIcon("C:\\Users\\Gauri\\OneDrive\\Pictures\\icon.jpeg").getImage());
        frame.setSize(500, 400);  // Adjusted the height to 200
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        transactionHistory = new HashMap<>();
    }


    private void insertTransaction(String accountName, String transactionType, BigDecimal amount) {
        String insertQuery = "INSERT INTO transaction_history (account_name, transaction_type, amount, timestamp) VALUES (?, ?, ?, NOW())";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, accountName);
            preparedStatement.setString(2, transactionType);
            preparedStatement.setBigDecimal(3, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private ArrayList<String> fetchAccountListFromDatabase() {
        ArrayList<String> accounts = new ArrayList<>();
        String query = "SELECT account_name FROM accounts";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String accountName = resultSet.getString("account_name");
                accounts.add(accountName);
                // Debug statement to print retrieved account name
                System.out.println("Retrieved account: " + accountName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
    private boolean withdrawAmount(String accountName, BigDecimal amount) {
        Connection connection = null;
        PreparedStatement updateStatement = null;
        PreparedStatement insertStatement = null;

        try {
            // Establish database connection
            connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL statement to update account balance
            String updateSql = "UPDATE accounts SET balance = balance - ? WHERE account_name = ?";
            updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setBigDecimal(1, amount);
            updateStatement.setString(2, accountName);

            // Execute the update
            int rowsAffected = updateStatement.executeUpdate();

            // Check if the update was successful
            if (rowsAffected <= 0) {
                return false; // Withdrawal failed
            }

            // Prepare the SQL statement to insert a record into the transaction history table
            String transactionInsertSql = "INSERT INTO transaction_history (account_name, transaction_type, amount) VALUES (?, ?, ?)";
            insertStatement = connection.prepareStatement(transactionInsertSql);
            insertStatement.setString(1, accountName);
            insertStatement.setString(2, "Withdrawal");
            insertStatement.setBigDecimal(3, amount);

            // Execute the insert into transaction history
            int rowsInsertedTransaction = insertStatement.executeUpdate();

            // Prepare the SQL statement to insert a record into the withdrawal_transactions table
            String withdrawInsertSql = "INSERT INTO withdrawal_transactions (account_name, amount) VALUES (?, ?)";
            insertStatement = connection.prepareStatement(withdrawInsertSql);
            insertStatement.setString(1, accountName);
            insertStatement.setBigDecimal(2, amount);

            // Execute the insert into withdrawal transactions
            int rowsInsertedWithdrawal = insertStatement.executeUpdate();

            // Check if both inserts were successful
            return rowsInsertedTransaction > 0 && rowsInsertedWithdrawal > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception
            return false; // Withdrawal failed
        } finally {
            // Close resources in finally block to ensure they are closed even if an exception occurs
            try {
                if (updateStatement != null) {
                    updateStatement.close();
                }
                if (insertStatement != null) {
                    insertStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception
            }
        }
    }

    private boolean depositAmount(String accountName, BigDecimal amount) {
        Connection connection = null;
        PreparedStatement updateStatement = null;
        PreparedStatement insertStatement = null;

        try {
            // Establish database connection
            connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL statement to update account balance
            String updateSql = "UPDATE accounts SET balance = balance + ? WHERE account_name = ?";
            updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setBigDecimal(1, amount);
            updateStatement.setString(2, accountName);

            // Execute the update
            int rowsAffected = updateStatement.executeUpdate();

            // Check if the update was successful
            if (rowsAffected <= 0) {
                return false; // Deposit failed
            }

            // Prepare the SQL statement to insert a record into the transaction history table
            String transactionInsertSql = "INSERT INTO transaction_history (account_name, transaction_type, amount) VALUES (?, ?, ?)";
            insertStatement = connection.prepareStatement(transactionInsertSql);
            insertStatement.setString(1, accountName);
            insertStatement.setString(2, "Deposit");
            insertStatement.setBigDecimal(3, amount);

            // Execute the insert
            int rowsInsertedTransaction = insertStatement.executeUpdate();

            // Prepare the SQL statement to insert a record into the deposit_transactions table
            String depositInsertSql = "INSERT INTO deposit_transactions (account_name, amount) VALUES (?, ?)";
            insertStatement = connection.prepareStatement(depositInsertSql);
            insertStatement.setString(1, accountName);
            insertStatement.setBigDecimal(2, amount);

            // Execute the insert
            int rowsInsertedDeposit = insertStatement.executeUpdate();

            // Check if both inserts were successful
            return rowsInsertedTransaction > 0 && rowsInsertedDeposit > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception
            return false; // Deposit failed
        } finally {
            // Close resources in finally block to ensure they are closed even if an exception occurs
            try {
                if (updateStatement != null) {
                    updateStatement.close();
                }
                if (insertStatement != null) {
                    insertStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception
            }
        }
    }


    private void performTransfer() {
        // Get sender's account, recipient's account, and amount from user input
        String fromAccount = JOptionPane.showInputDialog(frame, "Enter your account name:");
        String pin1 = JOptionPane.showInputDialog(frame, "Enter your PIN:");

        String toAccount = JOptionPane.showInputDialog(frame, "Enter the recipient's account name:");
        //sString pin2 = JOptionPane.showInputDialog(frame, "Enter recipient's PIN:");

        String amountString = JOptionPane.showInputDialog(frame, "Enter the amount to transfer:");

        try {
            BigDecimal amount = new BigDecimal(amountString);

            // Validate sender's account and PIN
            if (!validateAccount(fromAccount, pin1)) {
                JOptionPane.showMessageDialog(frame, "Invalid sender account name or PIN.");
                return;
            }

            // Validate recipient's account
//            if (!validateAccount(toAccount, pin2)) {
//                JOptionPane.showMessageDialog(frame, "Recipient account not found.");
//                return;
//            }

            // Withdraw amount from sender's account
            boolean withdrawalSuccess = withdrawAmount(fromAccount, amount);
            if (!withdrawalSuccess) {
                JOptionPane.showMessageDialog(frame, "Insufficient balance for transfer.");
                return;
            }

            // Deposit amount into recipient's account
            boolean depositSuccess = depositAmount(toAccount, amount);
            if (!depositSuccess) {
                JOptionPane.showMessageDialog(frame, "Transfer failed. Unable to deposit amount into recipient's account.");
                // Refund amount to sender's account if deposit into recipient's account fails
                depositAmount(fromAccount, amount);
                return;
            }
            boolean transferInserted = insertTransferTransaction(fromAccount, toAccount, amount);
            if (!transferInserted) {
                JOptionPane.showMessageDialog(frame, "Failed to record transfer transaction.");
                // Refund amount to sender's account if insertion fails
                depositAmount(fromAccount, amount);
                depositAmount(toAccount, amount.negate()); // Undo deposit into recipient's account
                return;
            }

            // Display transfer success message
            JOptionPane.showMessageDialog(frame, "Transfer of Rs" + amount + " from " + fromAccount + " to " + toAccount + " successful.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.");
        } finally {
            // Reset the visibility of buttons after performing the transfer
            resetButtonVisibility();
        }
    }
    private boolean insertTransferTransaction(String fromAccount, String toAccount, BigDecimal amount) {
        Connection connection = null;
        PreparedStatement insertStatement = null;

        try {
            // Establish database connection
            connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL statement to insert a record into the transfer_transactions table
            String insertSql = "INSERT INTO transfer_transactions (from_account_name, to_account_name, amount) VALUES (?, ?, ?)";
            insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, fromAccount);
            insertStatement.setString(2, toAccount);
            insertStatement.setBigDecimal(3, amount);

            // Execute the insert
            int rowsInserted = insertStatement.executeUpdate();

            // Check if the insert was successful
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception
            return false; // Insertion failed
        } finally {
            // Close resources in finally block to ensure they are closed even if an exception occurs
            try {
                if (insertStatement != null) {
                    insertStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception
            }
        }
    }

    private void performWithdraw() {
        String accountName = JOptionPane.showInputDialog(frame, "Enter your account name:");
        String amountString = JOptionPane.showInputDialog(frame, "Enter the amount to withdraw:");

        try {
            BigDecimal amount = new BigDecimal(amountString);

            // Call the updateBalanceInDatabase method with the negative amount to withdraw
            boolean withdrawalSuccess = updateBalanceInDatabase(accountName, amount.negate());

            if (withdrawalSuccess) {
                // Insert into withdrawal_transactions table
                boolean withdrawalInserted = withdrawAmount(accountName, amount);

                // Insert into transaction_history table
//            boolean transactionInserted = insertTransactionHistory(accountName, "Withdrawal", amount);

                if (withdrawalInserted ) {
                    JOptionPane.showMessageDialog(frame, "Withdrawal of Rs" + amount + " from " + accountName + " successful.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to record withdrawal transaction.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Withdrawal failed.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.");
            updateTransactionHistory(accountName, "Withdrawal: Rs" + amountString);
        }

        // Reset the visibility of buttons after performing the withdrawal
        resetButtonVisibility();
    }

    private boolean updateBalanceInDatabase(String accountName, BigDecimal amount) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Prepare and execute an SQL update query to update the balance
            String updateQuery = "UPDATE accounts SET balance = balance + ? WHERE account_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setBigDecimal(1, amount);
                preparedStatement.setString(2, accountName);

                // Execute the update query
                int rowsAffected = preparedStatement.executeUpdate();

                // Check if the update was successful
                if (rowsAffected > 0) {
                    System.out.println("Balance updated successfully.");
                    return true;
                } else {
                    System.out.println("Failed to update balance.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return false;
        }
    }


    private void performCashDeposit() {
        String accountName = JOptionPane.showInputDialog(frame, "Enter your account name:");
        String pin = JOptionPane.showInputDialog(frame, "Enter your PIN:");

        // Validate the account using the stored PINs
        if (validateAccount(accountName, pin)) {
            // Account validation successful, proceed with deposit
            String amountString = JOptionPane.showInputDialog(frame, "Enter the amount to deposit:");
            try {
                BigDecimal amount = new BigDecimal(amountString);

                // Update the balance in the database (assuming you have a method to do this)
                boolean depositSuccess = updateBalanceInDatabase(accountName, amount);

                if (depositSuccess) {
                    // Insert into deposit_transactions table
                    boolean depositInserted = depositAmount(accountName, amount);

                    // Insert into transaction_history table
//                    boolean transactionInserted = insertTransactionHistory(accountName, "Cash Deposit", amount);

                    if (depositInserted ) {
                        JOptionPane.showMessageDialog(frame, "Deposit of Rs" + amount + " into " + accountName + " successful.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to record deposit transaction.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Deposit failed.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.");
                updateTransactionHistory(accountName, "Cash Deposit: Rs" + amountString);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid account name or PIN. Please try again.");
        }

        // Reset the visibility of buttons after performing the cash deposit
        resetButtonVisibility();
    }
    private  boolean validateAccount(String accountName, String pin) {
        // Check if the account name exists in the map and validate the PIN
        if (accountPins.containsKey(accountName)) {
            String storedPin = accountPins.get(accountName);
            if (storedPin.equals(pin)) {
                return true; // Account and PIN validation successful
            }
        }
        return false; // Account name not found or PIN validation failed
    }
    private void performBalanceInquiry() {
        String accountName = JOptionPane.showInputDialog(frame, "Enter your account name:");
        String pin = JOptionPane.showInputDialog(frame, "Enter your PIN:");

        // Validate the account using the stored PINs
        if (validateAccount(accountName, pin)) {
            // Get the current balance from the database
            BigDecimal currentBalance = getAccountBalance(accountName);
            if (currentBalance != null) {
                JOptionPane.showMessageDialog(frame, "Your current balance is: Rs" + currentBalance);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to fetch balance for the account.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid account name or PIN. Please try again.");
        }

        // Reset the visibility of buttons after displaying the balance
        resetButtonVisibility();
    }
    private BigDecimal getAccountBalance(String accountName) {
        BigDecimal balance = null;
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT balance FROM accounts WHERE account_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, accountName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        balance = resultSet.getBigDecimal("balance");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return balance;
    }


    private int showCreateAccountDialog() {
        String[] options = {"Create Account", "Cancel"};
        return JOptionPane.showOptionDialog(frame,
                "Do you want to create a new bank account?",
                "Create Bank Account",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);  //default enter
    }

    private void setupAccountCreationUI() {

        JPanel panel = new JPanel(new GridLayout(6, 2)); // Increase rows for PIN input

        JLabel nameLabel = new JLabel("Account Name:");
        accountNameField = new JTextField();
        JLabel balanceLabel = new JLabel("Initial Balance:");
        initialBalanceField = new JTextField();
        JLabel pinLabel = new JLabel("Set PIN:");
        JPasswordField pinField = new JPasswordField();
        JLabel confirmPinLabel = new JLabel("Confirm PIN:");
        JPasswordField confirmPinField = new JPasswordField();
        JButton createButton = new JButton("Create Account");
        panel.add(nameLabel);
        panel.add(accountNameField);
        panel.add(balanceLabel);
        panel.add(initialBalanceField);
        panel.add(pinLabel);
        panel.add(pinField);
        panel.add(confirmPinLabel);
        panel.add(confirmPinField);
        panel.add(createButton);
        int result = JOptionPane.showConfirmDialog(frame, panel, "Create Account", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String enteredPIN = new String(pinField.getPassword());
            String confirmedPIN = new String(confirmPinField.getPassword());

            if (enteredPIN.equals(confirmedPIN)) {
                createBankAccount(accountNameField.getText(), initialBalanceField.getText(), enteredPIN);
            } else {
                JOptionPane.showMessageDialog(frame, "PINs do not match. Please try again.");
            }
        }


    }

    private void createBankAccount(String accountName, String initialBalanceText, String pin) {
        // Establish database connection
        String url = "jdbc:mysql://localhost:3306/ATM";
        String username = "root";
        String password = "Gauri@2004";
        accountPins.put(accountName, pin);
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Create the 'accounts' table if it doesn't exist
            createAccountsTable(connection);

            // Insert account details into 'accounts' table
            BigDecimal initialBalance = new BigDecimal(initialBalanceText);
            BigDecimal minimumBalance = new BigDecimal("0.00");
            BigDecimal maximumBalance = new BigDecimal("1000000.00");

            if (initialBalance.compareTo(minimumBalance) < 0 || initialBalance.compareTo(maximumBalance) > 0) {
                JOptionPane.showMessageDialog(frame, "Initial balance must be between $0.00 and $1,000,000.00.");
                return;
            }

            String insertQuery = "INSERT INTO accounts (account_name, balance, pin) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, accountName);
                preparedStatement.setBigDecimal(2, initialBalance);
                preparedStatement.setString(3, pin);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Account '" + accountName + "' created successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to create account.");
                }
            }

            // Reset the visibility of buttons
            resetButtonVisibility();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "SQL Exception: " + e.getMessage());
        }
    }

    // Helper method to create 'accounts' table if it doesn't exist
    private void createAccountsTable(Connection connection) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS accounts (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "account_name VARCHAR(255) NOT NULL," +
                "balance DECIMAL(10, 2) NOT NULL," +
                "pin VARCHAR(4) NOT NULL" +
                ")";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
            preparedStatement.executeUpdate();
        }
    }


    private void updateTransactionHistory(String accountName, String transactionDetail) {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.toString();
        transactionHistory.get(accountName).add(formattedDateTime + " - " + transactionDetail);
    }

    private void resetButtonVisibility() {
        bankingButton.setVisible(true);
        transferButton.setVisible(false);
        withdrawButton.setVisible(false);
        cashDepositButton.setVisible(false);
        balanceButton.setVisible(false);
        backButton.setVisible(false);
        accountButton.setVisible(true);
        createabutton.setVisible(false);
        deleteabutton.setVisible(false);
        ministatementButton.setVisible(true);
        donationButton.setVisible(true);
        pinchangeButton.setVisible(true);
        cuurencycoversionButton.setVisible(true);
        emergencyserviceButton.setVisible(true);
        exitButton.setVisible(true);
        label.setText("For banking services, click one of the options below.");
    }


    private void fetchAccountNamesFromDatabase() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_name FROM accounts");

            while (resultSet.next()) {
                String accountName = resultSet.getString("account_name");
                accountList.add(accountName);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    private void setupAccountDeletionUI() {
        fetchAccountNamesFromDatabase();
        if (accountList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No accounts to delete.");
            return;
        }

        String[] accountArray = accountList.toArray(new String[0]);
        JComboBox<String> accountComboBox = new JComboBox<>(accountArray);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Account to Delete:"));
        panel.add(accountComboBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Delete Account", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedAccount = (String) accountComboBox.getSelectedItem();
            deleteAccount(selectedAccount);
        }
    }




    private void deleteAccount(String accountName) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Delete account from 'accounts' table
            String deleteQuery = "DELETE FROM accounts WHERE account_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, accountName);

                // Execute the delete query
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Account '" + accountName + "' deleted successfully.");
                    // Update account list
                    accountList.remove(accountName);
                } else {
                    JOptionPane.showMessageDialog(frame, "No account found with the specified name.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();

            }
        });

    }

    private BigDecimal getCurrentBalance() {

        return new BigDecimal("1000.00");
    }

    private void performPinChange() {
        String accountName = JOptionPane.showInputDialog(frame, "Enter your account name:");
        String currentPin = JOptionPane.showInputDialog(frame, "Enter your current PIN:");
        String newPin = JOptionPane.showInputDialog(frame, "Enter your new PIN:");

        // Validate the account using the stored PINs
        if (validateAccount(accountName, currentPin)) {
            // Account validation successful, update the PIN
            boolean pinChanged = updatePinInDatabase(accountName, newPin);
            if (pinChanged) {
                JOptionPane.showMessageDialog(frame, "PIN changed successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to change PIN. Please try again later.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid account name or current PIN. Please try again.");
        }

        // Reset the visibility of buttons after performing the PIN change
        resetButtonVisibility();
    }

    private boolean updatePinInDatabase(String accountName, String newPin) {
        String url = "jdbc:mysql://localhost:3306/ATM";
        String username = "root";
        String password = "Gauri@2004";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Prepare SQL statement to update PIN for the given account
            String sql = "UPDATE accounts SET pin = ? WHERE account_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newPin);
                statement.setString(2, accountName);

                // Execute the update
                int rowsUpdated = statement.executeUpdate();

                // Return true if update was successful
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to update PIN in the database: " + e.getMessage());
            return false;
        }
    }
    private void showMiniStatement() {
        String accountName = JOptionPane.showInputDialog(frame, "Enter your account name:");
        String pin = JOptionPane.showInputDialog(frame, "Enter your PIN:");

        // Validate the account using the stored PINs
        if (validateAccount(accountName, pin)) {
            try {
                // Establish database connection
                Connection connection = DriverManager.getConnection(url, username, password);

                // Prepare the SQL statement to fetch transaction history
                String sql = "SELECT transaction_type, amount, timestamp FROM transaction_history WHERE account_name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, accountName);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Process the result set
                StringBuilder statement = new StringBuilder("Mini Statement:\n");
                while (resultSet.next()) {
                    String transactionType = resultSet.getString("transaction_type");
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    Timestamp timestamp = resultSet.getTimestamp("timestamp");
                    statement.append(transactionType).append(": Rs").append(amount).append(" (").append(timestamp).append(")\n");
                }

                // Close resources
                resultSet.close();
                preparedStatement.close();
                connection.close();

                // Show the mini statement
                JOptionPane.showMessageDialog(frame, statement.toString());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Error fetching mini statement: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid account name or PIN. Please try again.");
        }

        // Reset the visibility of buttons after displaying the mini statement
        resetButtonVisibility();
    }

    private void setupCurrencyConversionUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel amountLabel = new JLabel("Enter Amount:");
        JTextField amountField = new JTextField();
        JLabel toCurrencyLabel = new JLabel("To Currency:");
        JComboBox<String> currencyComboBox = new JComboBox<>(new String[]{"USD", "Euro"});
        JButton convertButton = new JButton("Convert");
        convertButton.addActionListener(e -> performCurrencyConversion(amountField.getText(), (String) currencyComboBox.getSelectedItem()));

        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(toCurrencyLabel);
        panel.add(currencyComboBox);
        panel.add(convertButton);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Currency Conversion", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
        }
    }

    private void performCurrencyConversion(String amountString, String toCurrency) {
        System.out.println("Performing Currency Conversion");
        try {
            BigDecimal amount = new BigDecimal(amountString);

            if ("USD".equals(toCurrency)) {
                BigDecimal amountInUSD = convertToDollar(amount);
                JOptionPane.showMessageDialog(frame, "Converted amount: " + amountInUSD + " USD");
            } else if ("Euro".equals(toCurrency)) {
                BigDecimal amountInEuro = convertToEuro(amount);
                JOptionPane.showMessageDialog(frame, "Converted amount: " + amountInEuro + " Euro");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid currency selection.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.");
        }
    }

    private BigDecimal convertToEuro(BigDecimal amountInINR) {
        BigDecimal eurToInrRate = new BigDecimal("88.50");

        return amountInINR.divide(eurToInrRate, 2, BigDecimal.ROUND_HALF_UP);
    }


    private BigDecimal convertToDollar(BigDecimal amountInINR) {
        // Assuming this is the current exchange rate (for example purposes)
        BigDecimal usdToInrRate = new BigDecimal("75.00");

        return amountInINR.divide(usdToInrRate, 2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == transferButton) {

            performTransfer();
            resetButtonVisibility();

        } else if (e.getSource() == withdrawButton) {

            performWithdraw();
            resetButtonVisibility();

        } else if (e.getSource() == cashDepositButton) {

            performCashDeposit();
            resetButtonVisibility();

        }  else if (e.getSource() == createabutton) {
            int choice = showCreateAccountDialog();

            if (choice == JOptionPane.YES_OPTION) {
                setupAccountCreationUI();
            } else {
                System.exit(0);
            }

            frame.setVisible(true);


            label.setText("here you can create account");
            createabutton.setVisible(false);
            deleteabutton.setVisible(false);
            backButton.setVisible(false);
        } else if (e.getSource() == deleteabutton) {
            setupAccountDeletionUI();
        }
        if (e.getSource() == bankingButton) {
            label.setText("For banking services, click one of the options below.");
            bankingButton.setVisible(false);
            ministatementButton.setVisible(false);
            cuurencycoversionButton.setVisible(false);
            donationButton.setVisible(false);
            exitButton.setVisible(false);
            pinchangeButton.setVisible(false);
            emergencyserviceButton.setVisible(false);
            transferButton.setVisible(true);
            withdrawButton.setVisible(true);
            cashDepositButton.setVisible(true);
            balanceButton.setVisible(true);
            backButton.setVisible(true);
            accountButton.setVisible(false);
            useridButton.setVisible(false);

        } else if (e.getSource() == balanceButton) {

            transferButton.setVisible(false);
            withdrawButton.setVisible(false);
            cashDepositButton.setVisible(false);
            balanceButton.setVisible(false);
            backButton.setVisible(true);
            accountButton.setVisible(false);
            performBalanceInquiry();
            resetButtonVisibility();
        } else if (e.getSource() == backButton) {
            // to go back to the main Screen
            label.setText("Thank you for visiting ATM MACHINE");
            bankingButton.setVisible(true);
            accountButton.setVisible(true);
            ministatementButton.setVisible(true);
            cuurencycoversionButton.setVisible(true);
            donationButton.setVisible(true);
            pinchangeButton.setVisible(true);
            exitButton.setVisible(true);
            emergencyserviceButton.setVisible(true);
            transferButton.setVisible(false);
            withdrawButton.setVisible(false);
            cashDepositButton.setVisible(false);
            balanceButton.setVisible(false);
            backButton.setVisible(false);
            createabutton.setVisible(false);
            deleteabutton.setVisible(false);
            thanksButton.setVisible(false);
            contact.setVisible(false);


        } else if (e.getSource() == accountButton) {
            label.setText("For Account services, click one of the options below.");
            ministatementButton.setVisible(false);
            cuurencycoversionButton.setVisible(false);
            donationButton.setVisible(false);
            exitButton.setVisible(false);
            pinchangeButton.setVisible(false);
            emergencyserviceButton.setVisible(false);
            createabutton.setVisible(true);
            deleteabutton.setVisible(true);
            backButton.setVisible(true);
            bankingButton.setVisible(false);
            accountButton.setVisible(false);
            useridButton.setVisible(false);
        } else if (e.getSource() == createabutton) {
            resetButtonVisibility();
        } else if (e.getSource() == deleteabutton) {
            resetButtonVisibility();

        } else if (e.getSource() == ministatementButton) {

            showMiniStatement();
            resetButtonVisibility();

        } else if (e.getSource()==emergencyserviceButton) {
            contact.setVisible(true);
            bankingButton.setVisible(false);
            ministatementButton.setVisible(false);
            cuurencycoversionButton.setVisible(false);
            donationButton.setVisible(false);
            exitButton.setVisible(false);
            pinchangeButton.setVisible(false);
            emergencyserviceButton.setVisible(false);
            backButton.setVisible(true);
            accountButton.setVisible(false);
            useridButton.setVisible(false);

        }
        else if (e.getSource() == cuurencycoversionButton) {
            System.out.println("Currency Conversion Button Clicked");
            setupCurrencyConversionUI();
        }
        else if (e.getSource() == exitButton) {
            bankingButton.setVisible(false);
            accountButton.setVisible(false);
            ministatementButton.setVisible(false);
            cuurencycoversionButton.setVisible(false);
            donationButton.setVisible(false);
            exitButton.setVisible(false);
            pinchangeButton.setVisible(false);
            emergencyserviceButton.setVisible(false);
            useridButton.setVisible(false);
            thanksButton.setVisible(true);
            backButton.setVisible(true);
            label.setVisible(false);
        } else if (e.getSource()==pinchangeButton) {
            performPinChange();
            resetButtonVisibility();

        }
    }

}