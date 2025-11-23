package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private RengasdengklokGame mainApp;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    
    // Color palette
    private final Color BACKGROUND_COLOR = new Color(0xF5, 0xED, 0xE0); // Cream vintage
    private final Color ACCENT_COLOR = new Color(0x8B, 0x45, 0x1E);     // Dark brown vintage
    private final Color TEXT_COLOR = new Color(0x5D, 0x30, 0x3C);       // Deep brown
    private final Color INPUT_BACKGROUND = new Color(0xFA, 0xF3, 0xE8); // Light cream
    private final Color BUTTON_BACKGROUND = new Color(0xA0, 0x6C, 0x4F); // Medium brown
    
    public LoginPanel(RengasdengklokGame mainApp) {
        this.mainApp = mainApp;
        setLayout(null);
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(800, 600));
        initializeUI();
    }
    
    private void initializeUI() {
        // Title dengan font yang lebih vintage
        JLabel titleLabel = new JLabel("Rengasdengklok: Jangan Salah Culik!", JLabel.CENTER);
        titleLabel.setBounds(100, 80, 600, 50);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_COLOR);
        add(titleLabel);
        
        // Subtitle biar estetik ky film
        JLabel subtitleLabel = new JLabel("Telltale Petualangan Sejarah 1945", JLabel.CENTER);
        subtitleLabel.setBounds(100, 120, 600, 30);
        subtitleLabel.setFont(new Font("Serif", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(0x7D, 0x5A, 0x45)); // Brown medium
        add(subtitleLabel);
        
        // Username Label
        JLabel usernameLabel = new JLabel("Username:", JLabel.RIGHT);
        usernameLabel.setBounds(50, 180, 150, 30);
        usernameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        usernameLabel.setForeground(TEXT_COLOR);
        add(usernameLabel);
        
        // Username Field 
        usernameField = new JTextField(20);
        usernameField.setBounds(220, 180, 350, 35);
        usernameField.setFont(new Font("Serif", Font.PLAIN, 14));
        usernameField.setBackground(INPUT_BACKGROUND);
        usernameField.setForeground(TEXT_COLOR);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x8B, 0x45, 0x1E), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        add(usernameField);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:", JLabel.RIGHT);
        passwordLabel.setBounds(50, 230, 150, 30);
        passwordLabel.setFont(new Font("Serif", Font.BOLD, 16));
        passwordLabel.setForeground(TEXT_COLOR);
        add(passwordLabel);
        
        // Password Field 
        passwordField = new JPasswordField(20);
        passwordField.setBounds(220, 230, 350, 35);
        passwordField.setFont(new Font("Serif", Font.PLAIN, 14));
        passwordField.setBackground(INPUT_BACKGROUND);
        passwordField.setForeground(TEXT_COLOR);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x8B, 0x45, 0x1E), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        add(passwordField);
        
        // Login Button 
        loginButton = new JButton("Login");
        loginButton.setBounds(220, 300, 350, 40);
        loginButton.setBackground(BUTTON_BACKGROUND);
        loginButton.setForeground(ACCENT_COLOR);
        loginButton.setFont(new Font("Serif", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x8B, 0x45, 0x1E), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Efek hover 
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0x8B, 0x45, 0x1E)); // Darker brown 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(BUTTON_BACKGROUND); // semula
            }
        });
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        add(loginButton);
        
        // Register Button 
        registerButton = new JButton("Register");
        registerButton.setBounds(220, 355, 350, 40);
        registerButton.setBackground(BUTTON_BACKGROUND);
        registerButton.setForeground(ACCENT_COLOR);
        registerButton.setFont(new Font("Serif", Font.BOLD, 16));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x8B, 0x45, 0x1E), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Efek hover 
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(0x8B, 0x45, 0x1E)); // Darker brown 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(BUTTON_BACKGROUND); // semula
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegister();
            }
        });
        add(registerButton);
        
        // Tambahin garis estetij
        addDecorativeElements();
    }
    
    private void addDecorativeElements() {
        // Garis dekoratif atas
        JSeparator topSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        topSeparator.setBounds(100, 70, 600, 2);
        topSeparator.setForeground(new Color(0x8B, 0x45, 0x1E));
        topSeparator.setBackground(new Color(0x8B, 0x45, 0x1E));
        add(topSeparator);
        
        // Garis dekoratif bawah
        JSeparator bottomSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        bottomSeparator.setBounds(100, 410, 600, 2);
        bottomSeparator.setForeground(new Color(0x8B, 0x45, 0x1E));
        bottomSeparator.setBackground(new Color(0x8B, 0x45, 0x1E));
        add(bottomSeparator);
        
        // Cornernya 
        JLabel cornerTL = createCornerDecoration("╔", 80, 55);
        JLabel cornerTR = createCornerDecoration("╗", 710, 55);
        JLabel cornerBL = createCornerDecoration("╚", 80, 400);
        JLabel cornerBR = createCornerDecoration("╝", 710, 400);
        
        add(cornerTL);
        add(cornerTR);
        add(cornerBL);
        add(cornerBR);
    }
    
    private JLabel createCornerDecoration(String symbol, int x, int y) {
        JLabel corner = new JLabel(symbol);
        corner.setBounds(x, y, 20, 20);
        corner.setFont(new Font("Monospaced", Font.BOLD, 18));
        corner.setForeground(new Color(0x8B, 0x45, 0x1E));
        return corner;
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = mainApp.getDbManager().authenticateUser(username, password);
        if (user != null) {
            // Login berhasil
            mainApp.setCurrentUser(user);
            
            // Load profiles untuk user ini
            java.util.List<PlayerProfile> profiles = mainApp.getDbManager().getUserProfiles(user.getUserId());
            if (!profiles.isEmpty()) {
                mainApp.setCurrentProfile(profiles.get(0));
            } else {
                // Buat profile default klo blum ad
                boolean profileCreated = mainApp.getDbManager().createProfile(
                    user.getUserId(), 
                    user.getUsername(), 
                    "Male" // default gender
                );
                if (profileCreated) {
                    profiles = mainApp.getDbManager().getUserProfiles(user.getUserId());
                    if (!profiles.isEmpty()) {
                        mainApp.setCurrentProfile(profiles.get(0));
                    }
                }
            }
            
            JOptionPane.showMessageDialog(this, "Login berhasil! Selamat datang " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
            mainApp.showPanel("MAIN_MENU");
            
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username minimal 5 karakter!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password.length() < 3) {
            JOptionPane.showMessageDialog(this, "Password minimal 5 karakter!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = mainApp.getDbManager().createUser(username, password);
        if (success) {
            JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            reset();
        } else {
            JOptionPane.showMessageDialog(this, "Username sudah digunakan!", "Registrasi Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        
        usernameField.requestFocusInWindow();
    }

}