import javax.swing.*;
import java.awt.*;
import java.sql.*; // ← Add this for PostgreSQL

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

    private JPanel createHeader() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel title = new JLabel("Student Registration Form");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title);
        return panel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(5, 2, 10, 10));

        fieldsPanel.add(new JLabel("ID Number:"));
        idNumberField = new JTextField();
        fieldsPanel.add(idNumberField);

        fieldsPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        fieldsPanel.add(nameField);

        fieldsPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        fieldsPanel.add(courseField);

        fieldsPanel.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel();
        genderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup genderGroup = new ButtonGroup();
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        fieldsPanel.add(genderPanel);

        fieldsPanel.add(new JLabel("Year:"));
        String[] years = {"1st Year", "2nd Year", "3rd Year", "4th Year"};
        yearComboBox = new JComboBox<>(years);
        fieldsPanel.add(yearComboBox);

        mainPanel.add(fieldsPanel, BorderLayout.NORTH);
        return mainPanel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> handleSave());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> handleClear());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(saveButton);
        panel.add(clearButton);
        panel.add(exitButton);

        return panel;
    }

    // --- Updated Save Button Action with PostgreSQL ---
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

    private void handleClear() {
        idNumberField.setText("");
        nameField.setText("");
        courseField.setText("");
        maleRadio.setSelected(false);
        femaleRadio.setSelected(false);
        yearComboBox.setSelectedIndex(0);
    }
}