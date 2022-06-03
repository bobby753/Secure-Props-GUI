

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mule.encryption.exception.MuleEncryptionException;
import com.mulesoft.tools.SecurePropertiesTool;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;

public class Gui extends JPanel implements ActionListener {
	private JLabel string_label;
	private JLabel input_label;
	private JTextField input_string;
	private JLabel alg_label;
	private JComboBox alg_combo;
	private JLabel mode_label;
	private JComboBox mode_combo;
	private JLabel random_iv_label;
	private JCheckBox iv_check_box;
	private JLabel key_label;
	private JPasswordField key_string;
	private JToggleButton key_show;
	private JButton enc_button;
	private JButton decrypt_button;
	private JLabel file_label;
	private JButton file_chooser_button;
	private JTextArea output_box;
	private JLabel output_label;

	private String selected_file_path;
	private String input_string_val;
	private String alg_val;
	private String mode_val;
	private Boolean random_iv_val;
	private String key_val;

	private int t = 0;
	
	private SecurePropertiesTool sp_tool = new SecurePropertiesTool();

	public Gui() {
		// construct preComponents
		String[] alg_comboItems = { "AES", "Blowfish", "DES", "DESede", "RC2", "RCA" };
		String[] mode_comboItems = { "CBC", "CFB", "ECB", "OFB" };

		// construct components
		string_label = new JLabel("String");
		input_label = new JLabel("Input : ");
		input_string = new JTextField();
		alg_label = new JLabel("Algorithm : ");
		alg_combo = new JComboBox(alg_comboItems);
		mode_label = new JLabel("Mode : ");
		mode_combo = new JComboBox(mode_comboItems);
		random_iv_label = new JLabel("use Random IV's : ");
		iv_check_box = new JCheckBox("");
		key_label = new JLabel("Key : ");
		key_string = new JPasswordField(32);
		key_show = new JToggleButton("Show");
		enc_button = new JButton("Encrypt");
		decrypt_button = new JButton("Decrypt");
		file_label = new JLabel("File");
		file_chooser_button = new JButton("Select File");
		output_box = new JTextArea(6, 30);
		output_box.setLineWrap(true);
		output_box.setWrapStyleWord(true);
		output_label = new JLabel("Output : ");

		// adjust size and set layout
		setPreferredSize(new Dimension(790, 531));
		setLayout(null);

		// add components
		add(string_label);
		add(input_label);
		add(input_string);
		add(alg_label);
		add(alg_combo);
		add(mode_label);
		add(mode_combo);
		add(random_iv_label);
		add(iv_check_box);
		add(key_label);
		add(key_string);
		add(key_show);
		add(enc_button);
		add(decrypt_button);
		add(file_label);
		add(file_chooser_button);
		add(output_box);
		add(output_label);

		// set component bounds (only needed by Absolute Positioning)
		string_label.setBounds(180, 10, 100, 25);
		input_label.setBounds(55, 45, 100, 25);
		input_string.setBounds(125, 45, 202, 25);
		alg_label.setBounds(295, 85, 100, 25);
		alg_combo.setBounds(405, 85, 100, 25);
		mode_label.setBounds(315, 120, 100, 25);
		mode_combo.setBounds(405, 120, 100, 25);
		random_iv_label.setBounds(255, 155, 120, 25);
		iv_check_box.setBounds(400, 160, 100, 25);
		key_label.setBounds(330, 195, 100, 25);
		key_string.setBounds(400, 195, 213, 25);
		key_show.setBounds(645, 195, 80, 25);
		enc_button.setBounds(215, 420, 100, 25);
		decrypt_button.setBounds(425, 420, 100, 25);
		file_label.setBounds(645, 10, 100, 25);
		file_chooser_button.setBounds(540, 45, 200, 25);
		output_box.setBounds(395, 265, 280, 110);
		output_label.setBounds(310, 310, 100, 25);

		// set functions

		file_chooser_button.addActionListener(this);
		enc_button.addActionListener(this);
		decrypt_button.addActionListener(this);
		input_string.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!input_string.toString().isEmpty())
					input_string.selectAll();
			}
		});
		output_box.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				String out_text = output_box.getText();
				StringSelection stringSelection = new StringSelection(out_text.toString());
				clpbrd.setContents(stringSelection, null);
				if (!(out_text.isEmpty() || out_text.toString().equals(" ")))
					Toast.showToast(output_box, "   Copied to Clipboard !!", null, 800);

			}
		});

		key_show.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// show
					key_string.setEchoChar((char) 0);
					key_show.setText("Hide");
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					// hide
					key_string.setEchoChar('\u2022');
					key_show.setText("Show");
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == file_chooser_button) {
				JFileChooser file_chooser = new JFileChooser();
				file_chooser.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator")+"Desktop"));
				file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				file_chooser.setFileFilter(new FileNameExtensionFilter("YAML Files", "yaml") );
				int resp = file_chooser.showOpenDialog(null);
				if (resp == JFileChooser.APPROVE_OPTION) {
					selected_file_path = file_chooser.getSelectedFile().getAbsolutePath();
					// System.out.println(selected_file_path);
				}
			} else {
				//System.out.println(selected_file_path);
				alg_val = alg_combo.getSelectedItem().toString();
				mode_val = mode_combo.getSelectedItem().toString();
				input_string_val = input_string.getText().toString();
				key_val = key_string.getText().toString();
				random_iv_val = iv_check_box.isSelected();
				// System.out.println(alg_val + "\t" + mode_val+ "\t" + input_string_val+ "\t" +
				// key_val+ "\t" + random_iv_val);

				// encrypt button

				if (e.getSource() == enc_button) {
					if (input_string_val.isEmpty() && !(selected_file_path.isEmpty())) {
						SecurePropertiesTool.applyOverFile(SecurePropertiesTool.ENCRYPTION_ACTION, alg_val, mode_val,
								key_val, random_iv_val, selected_file_path, (System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "out" + (t) + ".yaml"));
						
						Desktop.getDesktop().open(new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "out" + (t++) + ".yaml"));
					} else {

						output_box.setText(
								"![" + (SecurePropertiesTool.applyOverString(SecurePropertiesTool.ENCRYPTION_ACTION,
										alg_val, mode_val, key_val, random_iv_val, input_string_val)) + "]");

					}

				}
				// decrypt button
				else if (e.getSource() == decrypt_button) {

					if (input_string_val.isEmpty() && !(selected_file_path.isEmpty())) {
						SecurePropertiesTool.applyOverFile(SecurePropertiesTool.DECRYPTION_ACTION, alg_val, mode_val,
								key_val, random_iv_val, selected_file_path, (System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "out" + t + ".yaml"));
						Desktop.getDesktop().open(new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "out" + (t++) + ".yaml"));
					} else {
						if (input_string_val.startsWith("![") && input_string_val.endsWith("]"))
							input_string_val = input_string_val.substring(2, input_string_val.length() - 1);
						output_box.setText((SecurePropertiesTool.applyOverString(SecurePropertiesTool.DECRYPTION_ACTION,
								alg_val, mode_val, key_val, random_iv_val, input_string_val)));
					}

				}

			}
		} catch (Exception e1) {
			//System.out.println(e1.getMessage());
			Toast.showToast(output_box,e1.getMessage() , null, 2000);
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Secure Props GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new Gui());
		frame.pack();
		frame.setVisible(true);
		
	}
}
