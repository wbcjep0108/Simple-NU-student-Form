import javax.swing.*;
import java.awt.*;
import java.sql.*; // ← Add this for PostgreSQL

// Custom rounded border class for buttons
class RoundedBorder extends javax.swing.border.AbstractBorder {
    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getBackground().darker());
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = insets.top = insets.bottom = radius / 2;
        return insets;
    }
}

public class MainApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentRegistrationFrame();
        });
    }
}

// Student Registration Window
class StudentRegistrationFrame extends JFrame {
    
    // Input fields
    private JTextField idNumberField;
    private JTextField nameField;
    private JTextField courseField;
    private JRadioButton maleRadio;
    private JRadioButton femaleRadio;
    private JComboBox<String> yearComboBox;

    /**
     * Creates a rounded text field for a cleaner modern look.
     */
    private JTextField createRoundedTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 35));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    field.setBackground(new Color(245, 245, 245)); // light gray
    return field;
    }

    public StudentRegistrationFrame() {
        setTitle("National University Student Form");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center screen

        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * HEADER DESIGN: Creates the top header section of the form
     * - Sets up a JPanel with empty border padding
     * - Displays a bold title label (Arial, 18pt)
     * - Used in BorderLayout.NORTH position
     */
    private JPanel createHeader() {
    JPanel panel = new JPanel(new BorderLayout()); // Use BorderLayout for left/right placement
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    panel.setBackground(new Color(173, 216, 230));
    panel.setOpaque(true);

    // Title on the left
    JLabel title = new JLabel("Student Registration Form");
    title.setFont(new Font("SansSerif", Font.BOLD, 18));
    title.setForeground(Color.BLACK);
    panel.add(title, BorderLayout.WEST);

    // Logo on the right, scaled smaller
    ImageIcon logo = new ImageIcon("nulogo.png");
    Image scaledLogo = logo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // smaller size
    JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
    panel.add(logoLabel, BorderLayout.EAST);

    return panel;
}

    /**
     * MAIN PANEL DESIGN: Creates the central form fields section
     * - Uses BorderLayout for the main panel
     * - Contains GridLayout (5 rows x 2 columns) for organized input fields
     * - Includes: ID Number, Name, Course, Gender (radio buttons), and Year (dropdown)
     * - Used in BorderLayout.CENTER position
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        //mainPanel.setBackground(Color.WHITE); // Set background color for contrast
        //mainPanel.setBackground(new Color(235, 218, 171));
        Color bgColor = new Color(235, 218, 171);
        mainPanel.setBackground(bgColor);
        mainPanel.setOpaque(true);
        

        // Grid panel for aligning labels and input fields in rows
        JPanel fieldsPanel = new JPanel();

        fieldsPanel.setLayout(new GridLayout(5, 2, 15, 15));
        fieldsPanel.setOpaque(false); // Make it transparent to show main panel's background

        // INPUT FIELD: ID Number
        fieldsPanel.add(new JLabel("ID Number:"));
        idNumberField = createRoundedTextField();
        idNumberField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        idNumberField.setForeground(new Color(80, 80, 80)); // soft gray
        fieldsPanel.add(idNumberField);

        // INPUT FIELD: Student Name
        fieldsPanel.add(new JLabel("Name:"));
        nameField = createRoundedTextField();
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        nameField.setForeground(new Color(80, 80, 80)); // soft gray
        fieldsPanel.add(nameField);

        // INPUT FIELD: Course
        fieldsPanel.add(new JLabel("Course:"));
        courseField = createRoundedTextField();
        courseField.setFont(new Font("SansSerif", Font.PLAIN, 13)); 
        courseField.setForeground(new Color(80, 80, 80)); // soft gray
        fieldsPanel.add(courseField);

        // RADIO BUTTON GROUP: Gender Selection
        fieldsPanel.add(new JLabel("Gender:"));

        // Panel for radio buttons
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        genderPanel.setBackground(bgColor); // Match main panel background
        genderPanel.setOpaque(true);

        // Create radio buttons
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");

        // Style (optional but nice)
        maleRadio.setFont(new Font("SansSerif", Font.PLAIN, 13));
        femaleRadio.setFont(new Font("SansSerif", Font.PLAIN, 13));

        maleRadio.setOpaque(false);
        femaleRadio.setOpaque(false);

        maleRadio.setFocusPainted(false);
        femaleRadio.setFocusPainted(false);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        // Add to panel
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);

        // Add panel to form
        fieldsPanel.add(genderPanel);

        // DROPDOWN MENU: Academic Year Selection
        fieldsPanel.add(new JLabel("Year:"));
        String[] years = {"1st Year", "2nd Year", "3rd Year", "4th Year"};
        yearComboBox = new JComboBox<>(years);
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        yearComboBox.setPreferredSize(new Dimension(200, 35));
        yearComboBox.setBackground(Color.WHITE);
        yearComboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        fieldsPanel.add(yearComboBox);

        mainPanel.add(fieldsPanel, BorderLayout.NORTH);
        return mainPanel;
    }

    /**
     * BUTTON PANEL DESIGN: Creates the bottom action buttons section
     * - Uses FlowLayout centered with 10px spacing between buttons
     * - Contains three buttons: Save, Clear, and Exit
     * - Used in BorderLayout.SOUTH position
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(235, 218, 171));
        panel.setOpaque(true);

        // SAVE BUTTON: Saves student data to database
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> handleSave());
        saveButton.setBackground(new Color(119, 221, 119));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Sanserif", Font.BOLD, 14));
        saveButton.setBorderPainted(false); // Remove outline
        saveButton.setBorder(new RoundedBorder(50)); // Almost circular corners
        saveButton.setPreferredSize(new Dimension(100, 40)); // Size
        saveButton.setOpaque(true); // Ensure background color shows

        

        // CLEAR BUTTON: Resets all input fields
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> handleClear());
        clearButton.setBackground(new Color(255, 239, 153));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setFont(new Font("Sanserif", Font.BOLD, 14));
        clearButton.setBorderPainted(false); // Remove outline
        clearButton.setBorder(new RoundedBorder(50)); // Almost circular corners
        clearButton.setPreferredSize(new Dimension(100, 40));
        clearButton.setOpaque(true); // Ensure background color shows

        // EXIT BUTTON: Closes the application
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.setBackground(new Color(255, 153, 153));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Sanserif", Font.BOLD, 14));
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false); // Remove outline
        exitButton.setBorder(new RoundedBorder(50)); // Almost circular corners
        exitButton.setPreferredSize(new Dimension(100, 40));
        exitButton.setOpaque(true); // Ensure background color shows

        panel.add(saveButton);
        panel.add(clearButton);
        panel.add(exitButton);

        return panel;
    }

    /**
     * SAVE BUTTON ACTION: Handles form submission and database insertion
     * - Validates that all required fields are filled
     * - Retrieves data from all input fields
     * - Establishes PostgreSQL database connection
     * - Inserts student record into database table
     * - Shows success/error dialog to user
     */
    private void handleSave() {
        String idNumber = idNumberField.getText();
        String name = nameField.getText();
        String course = courseField.getText();
        String gender = maleRadio.isSelected() ? "Male" : (femaleRadio.isSelected() ? "Female" : "Not selected");
        String year = (String) yearComboBox.getSelectedItem();

        if (idNumber.isEmpty() || name.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- PostgreSQL connection ---
        String url = "jdbc:postgresql://localhost:5432/nu_student"; // your DB
        String user = "postgres"; // your username
        String password = "010825"; // your password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO students (id_number, name, course, gender, year) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idNumber);
            pst.setString(2, name);
            pst.setString(3, course);
            pst.setString(4, gender);
            pst.setString(5, year);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            handleClear();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving to database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * CLEAR BUTTON ACTION: Resets all form fields to default values
     * - Clears all text input fields
     * - Deselects radio buttons
     * - Resets dropdown to first option
     */
    private void handleClear() {
        idNumberField.setText("");
        nameField.setText("");
        courseField.setText("");
        maleRadio.setSelected(false);
        femaleRadio.setSelected(false);
        yearComboBox.setSelectedIndex(0);
    }
}