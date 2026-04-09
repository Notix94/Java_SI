package exercice4_p2;

import exercice6.*;

import graphicLayer.*;
import stree.parser.SNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class ClientIHM extends JFrame {

	private static final String HOST = "localhost";
	private static final int PORT = 9876;

	private JTextField inputField;
	private JButton sendButton;
	private JList<String> historyModelList;
	private DefaultListModel<String> historyModel;
	private JLabel statusLabel;

	private GSpace space;
	private GRect robi;
	private Environment env;

	private JComboBox<String> receiverBox;
	private JComboBox<String> commandBox;
	private JPanel argsPanel;
	private JTextField arg1, arg2;

	public ClientIHM() {
		super("Robi Control Center — Exercice 4");
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
		setSize(750, 550);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(8, 8));

		// ===== HAUT : Saisie =====
		JPanel topPanel = new JPanel(new BorderLayout(5, 5));
		topPanel.setBorder(BorderFactory.createTitledBorder("Commande S-Expression"));
		inputField = new JTextField("(robi translate 50 0)");
		sendButton = new JButton("Envoyer ▶");
		topPanel.add(inputField, BorderLayout.CENTER);
		topPanel.add(sendButton, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);

		// ===== CENTRE : Historique =====
		historyModel = new DefaultListModel<>();
		historyModelList = new JList<>(historyModel);
		historyModelList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String sel = historyModelList.getSelectedValue();
				if (sel != null && sel.startsWith("→ "))
					inputField.setText(sel.substring(2));
			}
		});
		add(new JScrollPane(historyModelList), BorderLayout.CENTER);

		// ===== DROITE : Outils =====
		JPanel visualPanel = new JPanel();
		visualPanel.setLayout(new BoxLayout(visualPanel, BoxLayout.Y_AXIS));
		visualPanel.setPreferredSize(new Dimension(220, 0));
		visualPanel.setBorder(BorderFactory.createTitledBorder("Outils"));

		receiverBox = new JComboBox<>(new String[] { "robi", "space" });
		commandBox = new JComboBox<>(
				new String[] { "translate", "setColor", "setPosition", "sleep", "save", "load", "screenshot" });
		commandBox.addActionListener(e -> updateArgsPanel());

		arg1 = new JTextField("20");
		arg2 = new JTextField("0");
		argsPanel = new JPanel(new GridLayout(2, 2, 4, 4));
		argsPanel.add(new JLabel("Arg 1:"));
		argsPanel.add(arg1);
		argsPanel.add(new JLabel("Arg 2:"));
		argsPanel.add(arg2);

		JButton buildBtn = new JButton("Exécuter");
		buildBtn.addActionListener(e -> buildAndSend());

		visualPanel.add(new JLabel("Cible :"));
		visualPanel.add(receiverBox);
		visualPanel.add(Box.createVerticalStrut(5));
		visualPanel.add(new JLabel("Action :"));
		visualPanel.add(commandBox);
		visualPanel.add(Box.createVerticalStrut(5));
		visualPanel.add(argsPanel);
		visualPanel.add(Box.createVerticalStrut(10));
		visualPanel.add(buildBtn);

		add(visualPanel, BorderLayout.EAST);

		statusLabel = new JLabel("Connecté à " + HOST + ":" + PORT);
		add(statusLabel, BorderLayout.SOUTH);

		sendButton.addActionListener(e -> sendScript());
		inputField.addActionListener(e -> sendScript());

		updateArgsPanel();
		setVisible(true);
	}

	private void buildMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Fichier");

		fileMenu.add(menuItem("Sauvegarder Scène", "(space save save_robi.json)"));
		fileMenu.add(menuItem("Charger Scène", "(space load save_robi.json)"));

		fileMenu.addSeparator();
		fileMenu.add(menuItem("📸 Capture d'écran", "(space screenshot)"));

		JMenu colorMenu = new JMenu("Couleurs");
		for (String c : new String[] { "red", "blue", "green", "yellow", "black" }) {
			colorMenu.add(menuItem(c, "(robi setColor " + c + ")"));
		}

		menuBar.add(fileMenu);
		menuBar.add(colorMenu);
		setJMenuBar(menuBar);
	}

	private JMenuItem menuItem(String label, String script) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(e -> quickSend(script));
		return item;
	}

	private void updateArgsPanel() {
		String cmd = (String) commandBox.getSelectedItem();
		arg1.setVisible(true);
		arg2.setVisible(true);
		argsPanel.getComponent(0).setVisible(true);
		argsPanel.getComponent(2).setVisible(true);

		if (cmd.equals("save") || cmd.equals("load")) {
			arg1.setText("save_robi.json");
			arg2.setVisible(false);
			argsPanel.getComponent(2).setVisible(false);
		} else if (cmd.equals("screenshot")) {
			arg1.setVisible(false);
			arg2.setVisible(false);
			argsPanel.getComponent(0).setVisible(false);
			argsPanel.getComponent(2).setVisible(false);
		} else if (cmd.equals("setColor") || cmd.equals("sleep")) {
			arg2.setVisible(false);
			argsPanel.getComponent(2).setVisible(false);
		}
		argsPanel.revalidate();
	}

	private void buildAndSend() {
		String rec = (String) receiverBox.getSelectedItem();
		String cmd = (String) commandBox.getSelectedItem();

		// SECURITÉ : Screenshot/Save/Load sont toujours pour 'space'
		if (cmd.equals("screenshot") || cmd.equals("save") || cmd.equals("load"))
			rec = "space";

		String s = "(" + rec + " " + cmd;
		if (arg1.isVisible())
			s += " " + arg1.getText();
		if (arg2.isVisible())
			s += " " + arg2.getText();
		s += ")";
		quickSend(s);
	}

	private void quickSend(String script) {
		inputField.setText(script);
		sendScript();
	}

	private void sendScript() {
		String script = inputField.getText().trim();
		if (script.isEmpty())
			return;

		new Thread(() -> {
			try (Socket socket = new Socket(HOST, PORT);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

				out.println(script);
				String response = in.readLine();

				SwingUtilities.invokeLater(() -> {
					historyModel.addElement("→ " + script);

					if (response != null) {
						// Detection Screenshot : pas de crochets et long
						if (!response.startsWith("[") && response.length() > 100) {
							showScreenshot(response);
						} else {
							executeFromJson(response, env);
						}
						statusLabel.setText("Réponse traitée ✅");
					}
				});
			} catch (Exception ex) {
				SwingUtilities.invokeLater(() -> statusLabel.setText("Erreur Serveur ❌"));
			}
		}).start();
	}

	private void showScreenshot(String base64) {
		try {
			byte[] imageBytes = Base64.getDecoder().decode(base64.trim());
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			if (img != null) {
				JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(img)), "Capture Serveur",
						JOptionPane.PLAIN_MESSAGE);
			}
		} catch (Exception e) {
			statusLabel.setText("Erreur décodage image");
			e.printStackTrace();
		}
	}

	private void executeFromJson(String json, Environment env) {
		if (json == null || !json.startsWith("["))
			return;
		// On nettoie les crochets extérieurs [[...],[...]] -> [...],[...]
		String content = json.substring(1, json.length() - 1);

		// Séparation des nœuds
		String[] nodeStrings = content.split("\\],\\[");
		for (String ns : nodeStrings) {
			String clean = ns.replace("[", "").replace("]", "");
			String[] parts = clean.split(",");
			List<String> tokens = new ArrayList<>();
			for (String p : parts)
				tokens.add(p.replace("\"", "").trim());

			executeTokens(tokens, env);
		}
	}

	private void executeTokens(List<String> tokens, Environment env) {
		if (tokens.size() < 2)
			return;
		Reference ref = env.getReferenceByName(tokens.get(0));
		if (ref == null)
			return;

		Command cmd = ref.getCommandByName(tokens.get(1));
		if (cmd != null) {
			cmd.run(ref, buildSNode(tokens), env);
		}
	}

	private Environment buildEnv(GSpace space, GRect robi) {
		Environment env = new Environment();
		Reference spaceRef = new Reference(space);
		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());
		spaceRef.addCommand("save", (r, m, e) -> r);
		spaceRef.addCommand("load", (r, m, e) -> r);
		spaceRef.addCommand("screenshot", (r, m, e) -> r);
		env.addReference("space", spaceRef);

		Reference robiRef = new Reference(robi);
		robiRef.addCommand("setColor", new SetColor());
		robiRef.addCommand("translate", new Translate());
		robiRef.addCommand("setPosition", new SetPosition());
		env.addReference("robi", robiRef);
		return env;
	}

	private void setupKeyboardShortcuts() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
			if (e.getID() != KeyEvent.KEY_PRESSED || inputField.isFocusOwner())
				return false;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				quickSend("(robi translate 20 0)");
				return true;
			case KeyEvent.VK_LEFT:
				quickSend("(robi translate -20 0)");
				return true;
			case KeyEvent.VK_UP:
				quickSend("(robi translate 0 -20)");
				return true;
			case KeyEvent.VK_DOWN:
				quickSend("(robi translate 0 20)");
				return true;
			}
			return false;
		});
	}

	private SNode buildSNode(List<String> tokens) {
		return new SNode() {
			public Boolean isLeaf() {
				return false;
			}

			public String contents() {
				return tokens.get(0);
			}

			public List<SNode> children() {
				List<SNode> list = new ArrayList<>();
				for (String t : tokens)
					list.add(leafNode(t));
				return list;
			}

			// AJOUT CRITIQUE POUR SetColor RGB
			public SNode get(int i) {
				return children().get(i);
			}

			public void setContents(String c) {
			}

			public void addToContents(Character c) {
			}

			public void setParent(SNode p) {
			}

			public void addChild(SNode c) {
			}

			public SNode parent() {
				return null;
			}

			public int quote() {
				return 0;
			}

			public void quote(int q) {
			}

			public Object alien() {
				return null;
			}

			public void setAlien(Object o) {
			}
		};
	}

	private SNode leafNode(String val) {
		return new SNode() {
			public Boolean isLeaf() {
				return true;
			}

			public String contents() {
				return val;
			}

			public List<SNode> children() {
				return Collections.emptyList();
			}

			public SNode get(int i) {
				return null;
			}

			public void setContents(String c) {
			}

			public void addToContents(Character c) {
			}

			public void setParent(SNode p) {
			}

			public void addChild(SNode c) {
			}

			public SNode parent() {
				return null;
			}

			public int quote() {
				return 0;
			}

			public void quote(int q) {
			}

			public Object alien() {
				return null;
			}

			public void setAlien(Object o) {
			}
		};
	}

	public static void main(String[] args) {
		try {
			// Force l'apparence native
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(ClientIHM::new);
	}
}