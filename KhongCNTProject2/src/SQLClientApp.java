/*
Name: Cody Khong
Course: CNT 4714 Summer 2021
Assignment title: Project 2 Two-Tier Client-Server Application Development With MySQL and JDBC”
Date: July 4, 2021
Class: SQLClientApp.java
*/
import com.mysql.cj.jdbc.MysqlDataSource;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class SQLClientApp  extends JFrame implements ActionListener {

	JFrame Frame;
	static final String DEFAULT_QUERY = "SELECT * FROM bikes";
	private static String username, password;
	private JTextField UserText, PasswordText;
	private static JTextArea StatusLabel, TextQuery;
	private static jdbc NEWjdbc = null;
	private static JButton ConnectButton, ExecuteButton, ClearQuery, ClearWindow;
	private ResultSetTableModel tableModel = null;
	private JTable resultTable;
	private boolean connected = false;
	String[] DriverItems = { "com.mysql.cj.jbdc.Driver" };
	String driver = "com.mysql.cj.jdbc.Driver";
	String[] UrlItems = { "jdbc:mysql://localhost:3306/project2?useTimezone=true&serverTimezone=UTC" };
	String URL = "jdbc:mysql://localhost:3306/project2?useTimezone=true&serverTimezone=UTC";
	String query = null;

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		MysqlDataSource dataSource = new MysqlDataSource();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SQLClientApp  window = new SQLClientApp ();
					window.Frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public SQLClientApp () {
		setup();
		action();
	}

	//Windowbuilder
	private void setup() {
		Frame = new JFrame();

		Frame.setTitle("SQL Client App");
		Frame.setBounds(100, 100, 1000, 800);
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		Frame.getContentPane().setLayout(springLayout);

		JPanel panelleft = new JPanel();
		springLayout.putConstraint(SpringLayout.WEST, panelleft, 10, SpringLayout.WEST, Frame.getContentPane());
		Frame.getContentPane().add(panelleft);

		JPanel panelright = new JPanel();
		springLayout.putConstraint(SpringLayout.EAST, panelleft, -43, SpringLayout.WEST, panelright);
		springLayout.putConstraint(SpringLayout.WEST, panelright, 417, SpringLayout.WEST, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panelright, -10, SpringLayout.EAST, Frame.getContentPane());

		GridBagLayout gbl_panelleft = new GridBagLayout();
		gbl_panelleft.columnWidths = new int[] { 65, 46, 0 };
		gbl_panelleft.rowHeights = new int[] { 13, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelleft.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelleft.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelleft.setLayout(gbl_panelleft);

		JLabel lblNewLabel_1 = new JLabel("JDBC Driver ");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1.setForeground(Color.BLACK);

		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		panelleft.add(lblNewLabel_1, gbc_lblNewLabel_1);
		Frame.getContentPane().add(panelright);

		JPanel panelbot = new JPanel();
		springLayout.putConstraint(SpringLayout.WEST, panelbot, 10, SpringLayout.WEST, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panelbot, -23, SpringLayout.EAST, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, panelbot, 349, SpringLayout.NORTH, Frame.getContentPane());

		Frame.getContentPane().add(panelbot);


		JLabel lblNewLabel = new JLabel("Enter Database Information");
		springLayout.putConstraint(SpringLayout.NORTH, panelleft, 6, SpringLayout.SOUTH, lblNewLabel);
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 10, SpringLayout.WEST, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblNewLabel, -442, SpringLayout.EAST, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 10, SpringLayout.NORTH, Frame.getContentPane());
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 16));
		Frame.getContentPane().add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel lblNewLabel_5 = new JLabel("Enter An SQL Command");
		springLayout.putConstraint(SpringLayout.NORTH, panelright, 0, SpringLayout.SOUTH, lblNewLabel_5);
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel_5, -5, SpringLayout.NORTH, lblNewLabel);
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_5, 28, SpringLayout.EAST, lblNewLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel_5, -706, SpringLayout.SOUTH, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblNewLabel_5, -113, SpringLayout.EAST, Frame.getContentPane());

		TextQuery = new JTextArea(13, 54);
		panelright.add(TextQuery);
		TextQuery.setWrapStyleWord(true);
		TextQuery.setLineWrap(true);
		lblNewLabel_5.setFont(new Font("Arial", Font.BOLD, 16));
		TextQuery.setWrapStyleWord(true);
		TextQuery.setLineWrap(true);
		Frame.getContentPane().add(lblNewLabel_5);

		ConnectButton = new JButton("Connect To Database");
		springLayout.putConstraint(SpringLayout.SOUTH, ConnectButton, -17, SpringLayout.NORTH, panelbot);
		springLayout.putConstraint(SpringLayout.EAST, ConnectButton, -394, SpringLayout.EAST, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panelright, -48, SpringLayout.NORTH, ConnectButton);
		ConnectButton.setEnabled(true);

		JComboBox cbdriver = new JComboBox(DriverItems);
		GridBagConstraints gbc_cbdriver = new GridBagConstraints();
		gbc_cbdriver.insets = new Insets(0, 0, 5, 0);
		gbc_cbdriver.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbdriver.gridx = 1;
		gbc_cbdriver.gridy = 0;
		panelleft.add(cbdriver, gbc_cbdriver);

		JLabel lblNewLabel_2 = new JLabel("Database URL");
		lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		panelleft.add(lblNewLabel_2, gbc_lblNewLabel_2);

		JComboBox cburl = new JComboBox(UrlItems);
		GridBagConstraints gbc_cburl = new GridBagConstraints();
		gbc_cburl.insets = new Insets(0, 0, 5, 0);
		gbc_cburl.fill = GridBagConstraints.HORIZONTAL;
		gbc_cburl.gridx = 1;
		gbc_cburl.gridy = 2;
		panelleft.add(cburl, gbc_cburl);

		JLabel lblNewLabel_3 = new JLabel("Username");
		lblNewLabel_3.setForeground(new Color(0, 0, 0));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_3.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 4;
		panelleft.add(lblNewLabel_3, gbc_lblNewLabel_3);

		UserText = new JTextField();
		GridBagConstraints gbc_UserText = new GridBagConstraints();
		gbc_UserText.insets = new Insets(0, 0, 5, 0);
		gbc_UserText.fill = GridBagConstraints.HORIZONTAL;
		gbc_UserText.gridx = 1;
		gbc_UserText.gridy = 4;
		panelleft.add(UserText, gbc_UserText);
		UserText.setColumns(10);
		username = UserText.getText();

		JLabel lblNewLabel_4 = new JLabel("Password");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_4.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 6;
		panelleft.add(lblNewLabel_4, gbc_lblNewLabel_4);

		PasswordText = new JPasswordField();
		((JPasswordField) PasswordText).setEchoChar('*');
		GridBagConstraints gbc_PasswordText = new GridBagConstraints();
		gbc_PasswordText.anchor = GridBagConstraints.NORTH;
		gbc_PasswordText.insets = new Insets(0, 0, 5, 0);
		gbc_PasswordText.fill = GridBagConstraints.HORIZONTAL;
		gbc_PasswordText.gridx = 1;
		gbc_PasswordText.gridy = 6;
		panelleft.add(PasswordText, gbc_PasswordText);
		PasswordText.setColumns(10);
		password = PasswordText.getText();
		ConnectButton.setBackground(Color.BLUE);
	    ConnectButton.setForeground(Color.YELLOW);
		Frame.getContentPane().add(ConnectButton);

		ClearQuery = new JButton("Clear SQL Command");
		springLayout.putConstraint(SpringLayout.NORTH, ClearQuery, 0, SpringLayout.NORTH, ConnectButton);
		springLayout.putConstraint(SpringLayout.WEST, ClearQuery, 18, SpringLayout.EAST, ConnectButton);
		ClearQuery.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			}
		});
		ClearQuery.setBackground(Color.RED);
	    ClearQuery.setForeground(Color.WHITE);
		Frame.getContentPane().add(ClearQuery);

		JLabel lblNewLabel_6 = new JLabel("SQL Execution Result Window");
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_6, 10, SpringLayout.WEST, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel_6, -18, SpringLayout.NORTH, panelbot);
		lblNewLabel_6.setFont(new Font("Arial", Font.BOLD, 16));
		Frame.getContentPane().add(lblNewLabel_6);

		ClearWindow = new JButton("Clear Result Window");
		springLayout.putConstraint(SpringLayout.WEST, ClearWindow, 10, SpringLayout.WEST, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panelbot, -6, SpringLayout.NORTH, ClearWindow);

		resultTable = new JTable();
		panelbot.add(new JScrollPane(this.resultTable), BorderLayout.NORTH);
		springLayout.putConstraint(SpringLayout.SOUTH, ClearWindow, -10, SpringLayout.SOUTH, Frame.getContentPane());
		ClearWindow.setBackground(Color.YELLOW);
		Frame.getContentPane().add(ClearWindow);

		StatusLabel = new JTextArea();
		springLayout.putConstraint(SpringLayout.SOUTH, panelleft, -24, SpringLayout.NORTH, StatusLabel);
		springLayout.putConstraint(SpringLayout.NORTH, StatusLabel, 272, SpringLayout.NORTH, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, StatusLabel, 0, SpringLayout.EAST, panelleft);
		springLayout.putConstraint(SpringLayout.WEST, StatusLabel, 10, SpringLayout.WEST, Frame.getContentPane());
		StatusLabel.setFont(new Font("Arial", Font.BOLD, 13));
		StatusLabel.setText("No Connection Now");
		StatusLabel.setColumns(40);
		StatusLabel.setEditable(false);
		Frame.getContentPane().add(StatusLabel);

		ExecuteButton = new JButton("Execute SQL Command");
		springLayout.putConstraint(SpringLayout.EAST, ClearQuery, -18, SpringLayout.WEST, ExecuteButton);
		springLayout.putConstraint(SpringLayout.WEST, ExecuteButton, 746, SpringLayout.WEST, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, ExecuteButton, -43, SpringLayout.EAST, Frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, ExecuteButton, 0, SpringLayout.NORTH, ConnectButton);
		Frame.getContentPane().add(ExecuteButton);
		ExecuteButton.setBackground(Color.GREEN);
		ExecuteButton.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}

	public void action() {
		ConnectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				NEWjdbc = new jdbc(URL, UserText.getText(), PasswordText.getText());
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
				} catch (ClassNotFoundException e1) {
					StatusLabel.setText("No Connection Now");
					e1.printStackTrace();

					resultTable.setModel(new DefaultTableModel());
					tableModel = null;
				}
				try {
					if (connected == true) {
						NEWjdbc.CloseConnection();
						StatusLabel.setText("No Longer Connected");
						connected = false;
						resultTable.setModel(new DefaultTableModel());
						tableModel = null;
					}
					NEWjdbc.EstablishConnection();
					StatusLabel.setText("Connected To " + URL);
					connected = true;
					ExecuteButton.setEnabled(true);
				} catch (SQLException e1) { // help
					StatusLabel.setText("No Connection");
					resultTable.setModel(new DefaultTableModel());
					tableModel = null;
					e1.printStackTrace();

				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		
		ExecuteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (connected == true && tableModel == null) {
					try {
						tableModel = new ResultSetTableModel(TextQuery.getText(), NEWjdbc.getConnection());
						resultTable.setModel(tableModel);
					} catch (ClassNotFoundException | SQLException e) {
						resultTable.setModel(new DefaultTableModel());
						tableModel = null;
						JOptionPane.showMessageDialog(null, e.getMessage(), "Database error",
								JOptionPane.ERROR_MESSAGE);


					}
				}
				else if (connected == true && tableModel != null) {
					query = TextQuery.getText();

					if (query.contains("select") || query.contains("SELECT")) {
						try {
							tableModel = new ResultSetTableModel(TextQuery.getText(), NEWjdbc.getConnection());
							tableModel.setQuery(query);
						} catch (IllegalStateException | SQLException | ClassNotFoundException e) {
							
							resultTable.setModel(new DefaultTableModel());
							tableModel = null;
							JOptionPane.showMessageDialog(null, e.getMessage(), "Database error",
									JOptionPane.ERROR_MESSAGE);
						} 
					}
					else {
						query = TextQuery.getText();
						try {
							tableModel.setUpdate(query);
							resultTable.setModel(new DefaultTableModel());
							tableModel = null;
						} catch (IllegalStateException | SQLException e) {

							resultTable.setModel(new DefaultTableModel());
							tableModel = null;
							JOptionPane.showMessageDialog(null, e.getMessage(), "Database error",
									JOptionPane.ERROR_MESSAGE);

						}
					}
				}
			}
		});

		ClearQuery.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				TextQuery.setText("");
			}
		});
		ClearWindow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resultTable.setModel(new DefaultTableModel());
				tableModel = null;
			}

		});
	}
} 
