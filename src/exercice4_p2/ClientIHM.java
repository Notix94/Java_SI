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
	private JLabel labelArg1, labelArg2;
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

		// ✅ NOUVEAU : toutes les commandes disponibles
		commandBox = new JComboBox<>(new String[] {
			"translate", "setColor", "setPosition", "setDim",
			"sleep", "clear", "del", "add",
			"save", "load", "screenshot"
		});
		commandBox.addActionListener(e -> updateArgsPanel());

		arg1 = new JTextField("20");
		arg2 = new JTextField("0");
		labelArg1 = new JLabel("Arg 1 :");
		labelArg2 = new JLabel("Arg 2 :");

		argsPanel = new JPanel(new GridLayout(2, 2, 4, 4));
		argsPanel.add(labelArg1);
		argsPanel.add(arg1);
		argsPanel.add(labelArg2);
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
		fileMenu.addSeparator();
		fileMenu.add(menuItem("🗑 Vider l'écran", "(space clear)"));  // ✅ NOUVEAU

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

	// ✅ MISE À JOUR : gestion des labels et visibilité selon la commande choisie
	private void updateArgsPanel() {
		String cmd = (String) commandBox.getSelectedItem();

		// Par défaut tout visible
		labelArg1.setVisible(true);
		labelArg2.setVisible(true);
		arg1.setVisible(true);
		arg2.setVisible(true);

		switch (cmd) {
			case "translate":
				labelArg1.setText("dx (horizontal) :");
				labelArg2.setText("dy (vertical) :");
				arg1.setText("20");
				arg2.setText("0");
				break;

			case "setColor":
				labelArg1.setText("Couleur (ex: red) :");
				arg1.setText("red");
				labelArg2.setVisible(false);
				arg2.setVisible(false);
				break;

			case "setPosition":
				labelArg1.setText("x :");
				labelArg2.setText("y :");
				arg1.setText("0");
				arg2.setText("0");
				break;

			case "setDim": // ✅ NOUVEAU
				labelArg1.setText("Largeur :");
				labelArg2.setText("Hauteur :");
				arg1.setText("50");
				arg2.setText("50");
				break;

			case "sleep":
				labelArg1.setText("Durée (ms) :");
				arg1.setText("1000");
				labelArg2.setVisible(false);
				arg2.setVisible(false);
				break;

			case "clear": // ✅ NOUVEAU
				labelArg1.setVisible(false);
				arg1.setVisible(false);
				labelArg2.setVisible(false);
				arg2.setVisible(false);
				break;

			case "del": // ✅ NOUVEAU
				labelArg1.setText("Nom de l'élément :");
				arg1.setText("monRect");
				labelArg2.setVisible(false);
				arg2.setVisible(false);
				break;

			case "add": // ✅ NOUVEAU
				labelArg1.setText("Nom :");
				labelArg2.setText("Type (Rect/Oval) :");
				arg1.setText("monRect");
				arg2.setText("Rect");
				break;

			case "save":
			case "load":
				labelArg1.setText("Fichier :");
				arg1.setText("save_robi.json");
				labelArg2.setVisible(false);
				arg2.setVisible(false);
				break;

			case "screenshot":
				labelArg1.setVisible(false);
				arg1.setVisible(false);
				labelArg2.setVisible(false);
				arg2.setVisible(false);
				break;
		}
		argsPanel.revalidate();
		argsPanel.repaint();
	}

	// ✅ MISE À JOUR : construction de la commande selon le type
	private void buildAndSend() {
		String rec = (String) receiverBox.getSelectedItem();
		String cmd = (String) commandBox.getSelectedItem();
		String s;

		switch (cmd) {
			// Ces commandes s'appliquent toujours sur space
			case "screenshot":
			case "save":
			case "load":
			case "clear":
				s = "(space " + cmd;
				if (arg1.isVisible()) s += " " + arg1.getText();
				s += ")";
				break;

			// add a une syntaxe spéciale avec sous-expression
			case "add":
				s = "(space add " + arg1.getText() + " (" + arg2.getText() + " new))";
				break;

			// del s'applique toujours sur space
			case "del":
				s = "(space del " + arg1.getText() + ")";
				break;

			// Les autres commandes utilisent la cible sélectionnée
			default:
				s = "(" + rec + " " + cmd;
				if (arg1.isVisible()) s += " " + arg1.getText();
				if (arg2.isVisible()) s += " " + arg2.getText();
				s += ")";
				break;
		}

		quickSend(s);
	}

	private void quickSend(String script) {
		inputField.setText(script);
		sendScript();
	}

	private void sendScript() {
		String script = inputField.getText().trim();
		if (script.isEmpty()) return;

		new Thread(() -> {
			try (Socket socket = new Socket(HOST, PORT);
				 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

				out.println(script);
				String response = in.readLine();

				SwingUtilities.invokeLater(() -> {
					historyModel.addElement("→ " + script);
					if (response != null) {
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
		if (json == null || !json.startsWith("[")) return;
		String content = json.substring(1, json.length() - 1);

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
		if (tokens.size() < 2) return;
		Reference ref = env.getReferenceByName(tokens.get(0));
		if (ref == null) return;
		Command cmd = ref.getCommandByName(tokens.get(1));
		if (cmd != null) {
			cmd.run(ref, buildSNode(tokens), env);
		}
	}

	// ✅ MISE À JOUR : ajout de setDim, clear, del côté client
	private Environment buildEnv(GSpace space, GRect robi) {
		Environment env = new Environment();

		Reference spaceRef = new Reference(space);
		spaceRef.addCommand("setColor",    new SetColor());
		spaceRef.addCommand("sleep",       new Sleep());
		spaceRef.addCommand("clear",       new ClearCommand());           // ✅ NOUVEAU
		spaceRef.addCommand("del",         new DelElement(env));          // ✅ NOUVEAU
		spaceRef.addCommand("save",        (r, m, e) -> r);
		spaceRef.addCommand("load",        (r, m, e) -> r);
		spaceRef.addCommand("screenshot",  (r, m, e) -> r);
		env.addReference("space", spaceRef);

		Reference robiRef = new Reference(robi);
		robiRef.addCommand("setColor",    new SetColor());
		robiRef.addCommand("translate",   new Translate());
		robiRef.addCommand("setPosition", new SetPosition());
		robiRef.addCommand("setDim",      new SetDim());                  // ✅ NOUVEAU
		env.addReference("robi", robiRef);

		return env;
	}

	private void setupKeyboardShortcuts() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
			if (e.getID() != KeyEvent.KEY_PRESSED || inputField.isFocusOwner())
				return false;
			switch (e.getKeyCode()) {
				case KeyEvent.VK_RIGHT: quickSend("(robi translate 20 0)");  return true;
				case KeyEvent.VK_LEFT:  quickSend("(robi translate -20 0)"); return true;
				case KeyEvent.VK_UP:    quickSend("(robi translate 0 -20)"); return true;
				case KeyEvent.VK_DOWN:  quickSend("(robi translate 0 20)");  return true;
			}
			return false;
		});
	}

	private SNode buildSNode(List<String> tokens) {
		return new SNode() {
			public int size() { return tokens.size(); }
			public Boolean isLeaf() { return false; }
			public String contents() { return tokens.get(0); }
			public List<SNode> children() {
				List<SNode> list = new ArrayList<>();
				for (String t : tokens) list.add(leafNode(t));
				return list;
			}
			public SNode get(int i) { return children().get(i); }
			public void setContents(String c) {}
			public void addToContents(Character c) {}
			public void setParent(SNode p) {}
			public void addChild(SNode c) {}
			public SNode parent() { return null; }
			public int quote() { return 0; }
			public void quote(int q) {}
			public Object alien() { return null; }
			public void setAlien(Object o) {}
		};
	}

	private SNode leafNode(String val) {
		return new SNode() {
			public int size() { return 0; }
			public Boolean isLeaf() { return true; }
			public String contents() { return val; }
			public List<SNode> children() { return Collections.emptyList(); }
			public SNode get(int i) { return null; }
			public void setContents(String c) {}
			public void addToContents(Character c) {}
			public void setParent(SNode p) {}
			public void addChild(SNode c) {}
			public SNode parent() { return null; }
			public int quote() { return 0; }
			public void quote(int q) {}
			public Object alien() { return null; }
			public void setAlien(Object o) {}
		};
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(ClientIHM::new);
	}
}