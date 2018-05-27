package org.cse.gobinda;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;

import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea txtrForMoreVideos;
	private JTextField textField;

	private static Voice voice;

	private static boolean soundSet = false;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {

		setTitle("Text To Speech");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 578, 367);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnPlay = new JButton("play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				new Thread(new Runnable() {
					public void run() {
						if (soundSet == false) {
							voice = VoiceManager.getInstance().getVoice("kevin");
							voice.allocate();
						}

						voice.speak(txtrForMoreVideos.getText());
					}
				}).start();

			}
		});
		btnPlay.setBounds(10, 250, 95, 80);
		contentPane.add(btnPlay);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "kevin", "kevin16", "alan" }));
		comboBox.setBounds(350, 250, 212, 36);
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == 1) {
					soundSet = true;
					voice = VoiceManager.getInstance().getVoice((String) e.getItem());
					voice.allocate();
				}

			}
		});
		contentPane.add(comboBox);

		JButton btnStop = new JButton("save");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (soundSet == false) {
					voice = VoiceManager.getInstance().getVoice("kevin");
					voice.allocate();
				}
				String fileLocation = textField.getText();
				if(fileLocation.equals("select directory where to save the file..")){
					JOptionPane.showMessageDialog(null, "select a location first!!");
					return;
				}
				AudioPlayer audioPlayer = new SingleFileAudioPlayer(fileLocation,
						javax.sound.sampled.AudioFileFormat.Type.WAVE);
				voice.setAudioPlayer(audioPlayer);
				voice.speak(txtrForMoreVideos.getText());
				voice.deallocate();
				audioPlayer.close();
			}
		});
		btnStop.setBounds(115, 250, 95, 80);
		contentPane.add(btnStop);

		JLabel lblNewLabel = new JLabel("select sound type..");
		lblNewLabel.setBorder(new LineBorder(Color.BLACK));
		lblNewLabel.setBounds(220, 250, 120, 36);
		contentPane.add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 552, 228);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		txtrForMoreVideos = new JTextArea();
		txtrForMoreVideos.setText("I love the new sofa. It's really comfortable and stylish, and the service was great too. It arrived earlier than expected!\r\n\r\nWe absolutely love our new sofa! From order to delivery - everything was straight forward and easy.\r\n\r\nThe table is of great quality and super easy to assemble!");
		scrollPane.setViewportView(txtrForMoreVideos);

		textField = new JTextField();
		textField.setBounds(220, 297, 298, 33);
		textField.setText("select directory where to save the file..");
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
					return;
				}
				textField.setText("select directory first where to save the file..");
			}
		});
		btnNewButton.setBounds(524, 297, 38, 33);
		contentPane.add(btnNewButton);
	}
}
