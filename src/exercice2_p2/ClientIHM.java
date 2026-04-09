package exercice2_p2;

import exercice6.*;
import graphicLayer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class ClientIHM extends JFrame {

    private static final String HOST = "localhost";
    private static final int PORT = 9876;

    private JTextField inputField;
    private JButton sendButton;
    private JList<String> historyList;
    private DefaultListModel<String> historyModel;
    private JLabel statusLabel;

    private GSpace space;
    private GRect robi;
    private Environment env;

    // Panneau visuel
    private JComboBox<String> receiverBox;
    private JComboBox<String> commandBox;
    private JPanel argsPanel;
    private JTextField arg1, arg2;

    public ClientIHM() {
        super("Interpréteur S-Expression — Client");
        space = new GSpace("Rendu Client", new Dimension(200, 100));
        robi = new GRect();
        space.addElement(robi);
        space.open();
        env = buildEnv(space, robi);
        buildUI();
        buildMenu();
        setupKeyboardShortcuts();
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // ===== HAUT : saisie texte =====
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("S-Expression (saisie directe)"));
        inputField = new JTextField("(robi translate 50 0)");
        inputField.setFont(new Font("Consolas", Font.PLAIN, 13));
        sendButton = new JButton("Envoyer ▶");
        topPanel.add(inputField, BorderLayout.CENTER);
        topPanel.add(sendButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ===== CENTRE : historique =====
        historyModel = new DefaultListModel<>();
        historyList = new JList<>(historyModel);
        historyList.setFont(new Font("Consolas", Font.PLAIN, 11));
        historyList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String sel = historyList.getSelectedValue();
                if (sel != null && sel.startsWith("→ "))
                    inputField.setText(sel.substring(2));
            }
        });
        JScrollPane scroll = new JScrollPane(historyList);
        scroll.setBorder(BorderFactory.createTitledBorder("Historique (clic pour réutiliser)"));
        add(scroll, BorderLayout.CENTER);

        // ===== DROITE : constructeur visuel =====
        JPanel visualPanel = new JPanel();
        visualPanel.setLayout(new BoxLayout(visualPanel, BoxLayout.Y_AXIS));
        visualPanel.setBorder(BorderFactory.createTitledBorder("Constructeur visuel"));
        visualPanel.setPreferredSize(new Dimension(200, 0));

        receiverBox = new JComboBox<>(new String[]{"robi", "space"});
        commandBox  = new JComboBox<>(new String[]{"translate", "setColor", "setPosition", "sleep"});

        arg1 = new JTextField("0");
        arg2 = new JTextField("0");
        argsPanel = new JPanel(new GridLayout(2, 2, 4, 4));
        argsPanel.add(new JLabel("Arg 1:"));
        argsPanel.add(arg1);
        argsPanel.add(new JLabel("Arg 2:"));
        argsPanel.add(arg2);

        // Quand on change de commande, adapter les args
        commandBox.addActionListener(e -> updateArgsPanel());

        JButton buildBtn = new JButton("Construire & Envoyer");
        buildBtn.addActionListener(e -> buildAndSend());

        visualPanel.add(new JLabel("Récepteur :"));
        visualPanel.add(receiverBox);
        visualPanel.add(Box.createVerticalStrut(8));
        visualPanel.add(new JLabel("Commande :"));
        visualPanel.add(commandBox);
        visualPanel.add(Box.createVerticalStrut(8));
        visualPanel.add(argsPanel);
        visualPanel.add(Box.createVerticalStrut(8));
        visualPanel.add(buildBtn);
        visualPanel.add(Box.createVerticalStrut(8));

        // Boutons rapides de déplacement
        visualPanel.add(new JSeparator());
        visualPanel.add(new JLabel("Déplacement rapide :"));
        JPanel arrows = new JPanel(new GridLayout(3, 3, 2, 2));
        arrows.add(new JLabel()); // vide
        JButton up = new JButton("↑"); up.addActionListener(e -> quickSend("(robi translate 0 -20)"));
        arrows.add(up);
        arrows.add(new JLabel());
        JButton left = new JButton("←"); left.addActionListener(e -> quickSend("(robi translate -20 0)"));
        arrows.add(left);
        JButton reset = new JButton("⌂"); reset.addActionListener(e -> quickSend("(robi setPosition 0 0)"));
        arrows.add(reset);
        JButton right = new JButton("→"); right.addActionListener(e -> quickSend("(robi translate 20 0)"));
        arrows.add(right);
        arrows.add(new JLabel());
        JButton down = new JButton("↓"); down.addActionListener(e -> quickSend("(robi translate 0 20)"));
        arrows.add(down);
        arrows.add(new JLabel());
        visualPanel.add(arrows);

        add(visualPanel, BorderLayout.EAST);

        // ===== BAS : status =====
        statusLabel = new JLabel("Prêt. | Raccourcis : flèches = déplacer robi");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        add(statusLabel, BorderLayout.SOUTH);

        // Actions
        sendButton.addActionListener(e -> sendScript());
        inputField.addActionListener(e -> sendScript());

        updateArgsPanel();
        setVisible(true);
    }

    private void buildMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Couleurs
        JMenu colorMenu = new JMenu("Couleur");
        for (String color : new String[]{"red", "blue", "green", "yellow", "white", "black"}) {
            JMenuItem item = new JMenuItem(color);
            item.addActionListener(e -> quickSend("(robi setColor " + color + ")"));
            colorMenu.add(item);
        }

        // Menu Actions
        JMenu actionMenu = new JMenu("Actions");
        actionMenu.add(menuItem("Robi → droite",  "(robi translate 20 0)"));
        actionMenu.add(menuItem("Robi → gauche",  "(robi translate -20 0)"));
        actionMenu.add(menuItem("Robi → haut",    "(robi translate 0 -20)"));
        actionMenu.add(menuItem("Robi → bas",     "(robi translate 0 20)"));
        actionMenu.addSeparator();
        actionMenu.add(menuItem("Reset position", "(robi setPosition 0 0)"));
        actionMenu.add(menuItem("Fond blanc",     "(space setColor white)"));

        // Menu Scripts
        JMenu scriptMenu = new JMenu("Scripts");
        scriptMenu.add(menuItem("Tour complet", 
            "(space setColor white) (robi setColor red) " +
            "(robi translate 180 0) (space sleep 500) " +
            "(robi translate 0 80) (space sleep 500) " +
            "(robi translate -180 0) (space sleep 500) " +
            "(robi translate 0 -80)"));

        menuBar.add(colorMenu);
        menuBar.add(actionMenu);
        menuBar.add(scriptMenu);
        setJMenuBar(menuBar);
    }

    private JMenuItem menuItem(String label, String script) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(e -> quickSend(script));
        return item;
    }

    private void setupKeyboardShortcuts() {
        // Flèches clavier pour déplacer robi
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(e -> {
                if (e.getID() != KeyEvent.KEY_PRESSED) return false;
                if (inputField.isFocusOwner()) return false; // ne pas intercepter si on tape
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT: quickSend("(robi translate 20 0)");  return true;
                    case KeyEvent.VK_LEFT:  quickSend("(robi translate -20 0)"); return true;
                    case KeyEvent.VK_UP:    quickSend("(robi translate 0 -20)"); return true;
                    case KeyEvent.VK_DOWN:  quickSend("(robi translate 0 20)");  return true;
                }
                return false;
            });
    }

    private void updateArgsPanel() {
        String cmd = (String) commandBox.getSelectedItem();
        argsPanel.setVisible(true);
        switch (cmd) {
            case "setColor":
                arg1.setText("red");
                arg2.setVisible(false);
                argsPanel.getComponent(2).setVisible(false); // label arg2
                break;
            case "sleep":
                arg1.setText("500");
                arg2.setVisible(false);
                argsPanel.getComponent(2).setVisible(false);
                break;
            default: // translate, setPosition
                arg1.setText("20");
                arg2.setText("0");
                arg2.setVisible(true);
                argsPanel.getComponent(2).setVisible(true);
        }
        argsPanel.revalidate();
    }

    private void buildAndSend() {
        String receiver = (String) receiverBox.getSelectedItem();
        String command  = (String) commandBox.getSelectedItem();
        String a1 = arg1.getText().trim();
        String a2 = arg2.getText().trim();

        String script;
        if (!arg2.isVisible() || a2.isEmpty()) {
            script = "(" + receiver + " " + command + " " + a1 + ")";
        } else {
            script = "(" + receiver + " " + command + " " + a1 + " " + a2 + ")";
        }
        quickSend(script);
    }

    private void quickSend(String script) {
        inputField.setText(script);
        sendScript();
    }

    private void sendScript() {
        String script = inputField.getText().trim();
        if (script.isEmpty()) return;

        statusLabel.setText("Connexion...");
        sendButton.setEnabled(false);

        new Thread(() -> {
            try (Socket socket = new Socket(HOST, PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(script);
                String json = in.readLine();

                SwingUtilities.invokeLater(() -> {
                    historyModel.addElement("→ " + script);
                    historyModel.addElement("← " + json);
                    historyModel.addElement("---");
                    historyList.ensureIndexIsVisible(historyModel.size() - 1);
                    statusLabel.setText("Exécuté ✔ | Raccourcis : flèches = déplacer robi");
                    sendButton.setEnabled(true);
                });

                executeFromJson(json, env);

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Erreur connexion ❌");
                    sendButton.setEnabled(true);
                    JOptionPane.showMessageDialog(this, "Serveur inaccessible.", "Erreur", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void executeFromJson(String json, Environment env) {
        json = json.trim().substring(1, json.length() - 1);
        for (String nodeStr : json.split("\\],\\[")) {
            nodeStr = nodeStr.replaceAll("[\\[\\]]", "");
            List<String> tokens = new ArrayList<>();
            for (String p : nodeStr.split(","))
                tokens.add(p.replaceAll("\"", "").trim());
            executeTokens(tokens, env);
        }
    }

    private void executeTokens(List<String> tokens, Environment env) {
        Reference ref = env.getReferenceByName(tokens.get(0));
        if (ref == null) return;
        String methodName = tokens.get(1);
        if (methodName.equals("sleep")) {
            try { Thread.sleep(Long.parseLong(tokens.get(2))); }
            catch (InterruptedException e) { e.printStackTrace(); }
            return;
        }
        Command cmd = ref.getCommandByName(methodName);
        if (cmd != null) cmd.run(ref, buildSNode(tokens), env);
    }

    private Environment buildEnv(GSpace space, GRect robi) {
        Environment env = new Environment();
        Reference spaceRef = new Reference(space);
        spaceRef.addCommand("setColor", new SetColor());
        spaceRef.addCommand("sleep", new Sleep());
        env.addReference("space", spaceRef);
        Reference robiRef = new Reference(robi);
        robiRef.addCommand("setColor", new SetColor());
        robiRef.addCommand("translate", new Translate());
        robiRef.addCommand("setPosition", new SetPosition());
        env.addReference("robi", robiRef);
        return env;
    }

    private stree.parser.SNode buildSNode(List<String> tokens) {
        return new stree.parser.SNode() {
            public Boolean isLeaf() { return false; }
            public String contents() { return tokens.get(0); }
            public List<stree.parser.SNode> children() {
                List<stree.parser.SNode> list = new ArrayList<>();
                for (String t : tokens) list.add(leafNode(t));
                return list;
            }
            public void setContents(String c) {} public void addToContents(Character c) {}
            public void setParent(stree.parser.SNode p) {} public void addChild(stree.parser.SNode c) {}
            public stree.parser.SNode parent() { return null; }
            public int quote() { return 0; } public void quote(int q) {}
            public Object alien() { return null; } public void setAlien(Object o) {}
        };
    }

    private stree.parser.SNode leafNode(String val) {
        return new stree.parser.SNode() {
            public Boolean isLeaf() { return true; }
            public String contents() { return val; }
            public List<stree.parser.SNode> children() { return Collections.emptyList(); }
            public void setContents(String c) {} public void addToContents(Character c) {}
            public void setParent(stree.parser.SNode p) {} public void addChild(stree.parser.SNode c) {}
            public stree.parser.SNode parent() { return null; }
            public int quote() { return 0; } public void quote(int q) {}
            public Object alien() { return null; } public void setAlien(Object o) {}
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientIHM::new);
    }
}