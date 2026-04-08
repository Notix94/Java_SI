package exercice1_p2;

import exercice6.*;
import graphicLayer.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientIHM extends JFrame {

    private static final String HOST = "localhost";
    private static final int PORT = 9876; // ⚠️ même port que serveur

    private JTextField inputField;
    private JButton sendButton;
    private JList<String> historyList;
    private DefaultListModel<String> historyModel;
    private JLabel statusLabel;

    private GSpace space;
    private GRect robi;
    private Environment env;

    public ClientIHM() {
        super("Client S-Expression");

        space = new GSpace("Rendu Client", new Dimension(200, 100));
        robi = new GRect();
        space.addElement(robi);
        space.open();

        env = buildEnv(space, robi);

        buildUI();
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // ===== HAUT =====
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        inputField = new JTextField("(robi translate 50 0)");
        inputField.setFont(new Font("Consolas", Font.PLAIN, 14));

        sendButton = new JButton("Envoyer ▶");

        topPanel.add(inputField, BorderLayout.CENTER);
        topPanel.add(sendButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ===== CENTRE =====
        historyModel = new DefaultListModel<>();
        historyList = new JList<>(historyModel);
        historyList.setFont(new Font("Consolas", Font.PLAIN, 12));

        // 🔥 historique cliquable
        historyList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selected = historyList.getSelectedValue();
                if (selected != null && selected.startsWith("→ ")) {
                    inputField.setText(selected.substring(2));
                }
            }
        });

        add(new JScrollPane(historyList), BorderLayout.CENTER);

        // ===== BAS =====
        JPanel bottomPanel = new JPanel(new BorderLayout());

        statusLabel = new JLabel("Prêt.");
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        // 🔥 boutons rapides
        JPanel quickPanel = new JPanel();

        JButton btnRight = new JButton("→");
        btnRight.addActionListener(e -> inputField.setText("(robi translate 20 0)"));

        JButton btnLeft = new JButton("←");
        btnLeft.addActionListener(e -> inputField.setText("(robi translate -20 0)"));

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> inputField.setText("(robi setPosition 0 0)"));

        JButton btnColor = new JButton("Rouge");
        btnColor.addActionListener(e -> inputField.setText("(robi setColor red)"));

        quickPanel.add(btnLeft);
        quickPanel.add(btnRight);
        quickPanel.add(btnReset);
        quickPanel.add(btnColor);

        bottomPanel.add(quickPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== ACTIONS =====
        sendButton.addActionListener(e -> sendScript());
        inputField.addActionListener(e -> sendScript());

        setVisible(true);
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
                    statusLabel.setText("Exécuté ✔");
                    sendButton.setEnabled(true);
                });

                executeFromJson(json, env);

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Erreur ❌");
                    sendButton.setEnabled(true);
                    JOptionPane.showMessageDialog(this,
                            "Erreur connexion serveur",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void executeFromJson(String json, Environment env) {
        json = json.trim().substring(1, json.length() - 1);
        String[] nodeStrs = json.split("\\],\\[");

        for (String nodeStr : nodeStrs) {
            nodeStr = nodeStr.replaceAll("[\\[\\]]", "");
            String[] parts = nodeStr.split(",");

            List<String> tokens = new ArrayList<>();
            for (String p : parts)
                tokens.add(p.replaceAll("\"", "").trim());

            executeTokens(tokens, env);
        }
    }

    private void executeTokens(List<String> tokens, Environment env) {
        Reference ref = env.getReferenceByName(tokens.get(0));
        if (ref == null) return;

        Command cmd = ref.getCommandByName(tokens.get(1));
        if (cmd == null) return;

        cmd.run(ref, buildSNode(tokens), env);
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

            public java.util.List<stree.parser.SNode> children() {
                List<stree.parser.SNode> list = new ArrayList<>();
                for (String t : tokens) {
                    list.add(new stree.parser.SNode() {
                        public Boolean isLeaf() { return true; }
                        public String contents() { return t; }
                        public java.util.List<stree.parser.SNode> children() { return Collections.emptyList(); }
                        public void setContents(String c) {}
                        public void addToContents(Character c) {}
                        public void setParent(stree.parser.SNode p) {}
                        public void addChild(stree.parser.SNode c) {}
                        public stree.parser.SNode parent() { return null; }
                        public int quote() { return 0; }
                        public void quote(int q) {}
                        public Object alien() { return null; }
                        public void setAlien(Object o) {}
                    });
                }
                return list;
            }

            public void setContents(String c) {}
            public void addToContents(Character c) {}
            public void setParent(stree.parser.SNode p) {}
            public void addChild(stree.parser.SNode c) {}
            public stree.parser.SNode parent() { return null; }
            public int quote() { return 0; }
            public void quote(int q) {}
            public Object alien() { return null; }
            public void setAlien(Object o) {}
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientIHM::new);
    }
}