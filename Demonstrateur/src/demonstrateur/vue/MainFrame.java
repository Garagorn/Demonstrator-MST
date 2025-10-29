package demonstrateur.vue;

import demonstrateur.controleur.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private GraphControleur controller;
    private JButton btnPause;
    private JButton btnStop;
    private JSlider speedSlider;
    private JLabel speedLabel;
    
    public MainFrame() {
        setTitle("Démonstrateur MST - Algorithmes de Prim & Kruskal");
        setSize(1400, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Création des panneaux de visualisation
        GraphPanel panelGraph = new GraphPanel();
        GraphPanel panelPrim = new GraphPanel();
        GraphPanel panelKruskal = new GraphPanel();
        
        panelGraph.setPreferredSize(new Dimension(450, 700));
        panelPrim.setPreferredSize(new Dimension(450, 700));
        panelKruskal.setPreferredSize(new Dimension(450, 700));
        
        // Panneau central avec les 3 graphes
        JPanel center = new JPanel(new GridLayout(1, 3, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        center.add(createPanelWithTitle(panelGraph, "Graphe Original"));
        center.add(createPanelWithTitle(panelPrim, "Algorithme de Prim"));
        center.add(createPanelWithTitle(panelKruskal, "Algorithme de Kruskal"));
        add(center, BorderLayout.CENTER);
        
        // Création du contrôleur
        controller = new GraphControleur(panelGraph, panelPrim, panelKruskal);
        
        // Panneau de contrôles en haut
        JPanel topPanel = createControlPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Panneau d'informations en bas
        JPanel bottomPanel = createInfoPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Générer un graphe initial
        controller.genererNouveauGraphe();
    }
    
    private JPanel createPanelWithTitle(JPanel panel, String title) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2), 
            title,
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 13)
        ));
        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }
    
    private JPanel createControlPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        // Première ligne : boutons principaux
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JButton btnGenerer = new JButton("🔄 Nouveau Graphe");
        JButton btnInstant = new JButton("⚡ Exécution Instantanée");
        JButton btnStepByStep = new JButton("▶️ Exécution Pas à Pas");
        
        styleButton(btnGenerer, new Color(100, 180, 100));
        styleButton(btnInstant, new Color(255, 140, 0));
        styleButton(btnStepByStep, new Color(70, 130, 180));
        
        btnGenerer.addActionListener(e -> {
            controller.genererNouveauGraphe();
            updateButtonStates();
        });
        
        btnInstant.addActionListener(e -> {
            controller.lancerInstantane();
            updateButtonStates();
        });
        
        btnStepByStep.addActionListener(e -> {
            controller.lancerPasAPas();
            updateButtonStates();
        });
        
        buttonPanel.add(btnGenerer);
        buttonPanel.add(btnInstant);
        buttonPanel.add(btnStepByStep);
        
        // Deuxième ligne : contrôles d'animation
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        btnPause = new JButton("⏸️ Pause");
        JButton btnResume = new JButton("▶️ Reprendre");
        btnStop = new JButton("⏹️ Arrêter");
        JButton btnReset = new JButton("🔄 Reset");
        
        styleButton(btnPause, new Color(255, 200, 50));
        styleButton(btnResume, new Color(100, 200, 100));
        styleButton(btnStop, new Color(220, 80, 80));
        styleButton(btnReset, new Color(150, 150, 150));
        
        btnPause.setEnabled(false);
        btnStop.setEnabled(false);
        
        btnPause.addActionListener(e -> {
            controller.pauseAnimations();
            updateButtonStates();
        });
        
        btnResume.addActionListener(e -> {
            controller.resumeAnimations();
            updateButtonStates();
        });
        
        btnStop.addActionListener(e -> {
            controller.stopAnimations();
            updateButtonStates();
        });
        
        btnReset.addActionListener(e -> {
            controller.resetAnimations();
            updateButtonStates();
        });
        
        controlPanel.add(btnPause);
        controlPanel.add(btnResume);
        controlPanel.add(btnStop);
        controlPanel.add(btnReset);
        
        // Troisième ligne : contrôle de vitesse
        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        speedPanel.add(new JLabel("Vitesse d'animation :"));
        
        speedSlider = new JSlider(JSlider.HORIZONTAL, 200, 3000, 1000);
        speedSlider.setMajorTickSpacing(700);
        speedSlider.setMinorTickSpacing(200);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(false);
        speedSlider.setPreferredSize(new Dimension(300, 40));
        
        speedLabel = new JLabel("1.0s");
        speedLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        speedLabel.setPreferredSize(new Dimension(60, 20));
        
        speedSlider.addChangeListener(e -> {
            int delay = speedSlider.getValue();
            controller.setDelayMs(delay);
            speedLabel.setText(String.format("%.1fs", delay / 1000.0));
        });
        
        JLabel fastLabel = new JLabel("Rapide");
        JLabel slowLabel = new JLabel("Lent");
        
        speedPanel.add(fastLabel);
        speedPanel.add(speedSlider);
        speedPanel.add(slowLabel);
        speedPanel.add(speedLabel);
        
        topPanel.add(buttonPanel);
        topPanel.add(controlPanel);
        topPanel.add(speedPanel);
        
        // Timer pour mettre à jour l'état des boutons
        Timer updateTimer = new Timer(100, e -> updateButtonStates());
        updateTimer.start();
        
        return topPanel;
    }
    
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(color.brighter());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }
    
    private void updateButtonStates() {
        boolean isAnimating = controller.isAnimating();
        boolean isPaused = controller.isPaused();
        
        btnPause.setEnabled(isAnimating && !isPaused);
        btnStop.setEnabled(isAnimating);
    }
    
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        JTextArea infoText = new JTextArea(3, 50);
        infoText.setEditable(false);
        infoText.setFont(new Font("Arial", Font.PLAIN, 11));
        infoText.setBackground(new Color(240, 240, 240));
        infoText.setText(
            "ℹ️ Instructions :\n" +
            "• Générez un graphe aléatoire avec 'Nouveau Graphe'\n" +
            "• Choisissez 'Exécution Instantanée' pour voir le résultat final immédiatement\n" +
            "• Choisissez 'Exécution Pas à Pas' pour visualiser l'algorithme étape par étape\n" +
            "• L'arête en cours d'ajout s'affiche en ORANGE, les arêtes du MST en ROUGE (Prim) ou BLEU (Kruskal)"
        );
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        
        infoPanel.add(new JScrollPane(infoText), BorderLayout.CENTER);
        
        return infoPanel;
    }
    
    public static void main(String[] args) {
        // Définir le Look and Feel du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setLocationRelativeTo(null); // Centrer la fenêtre
            frame.setVisible(true);
        });
    }
}