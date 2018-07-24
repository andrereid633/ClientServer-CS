package com.finaltry.ap;

import java.awt.Desktop.Action;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SendAndGet extends JFrame implements Runnable{

	private JPanel contentPane;
	private JTextField textField;
	private final SwingAction action = new SwingAction();
	Socket socket;
	ObjectInputStream is = null;
	ObjectOutputStream os = null;
	JTextArea textArea = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SendAndGet frame = new SendAndGet();
					frame.setVisible(true);
					new Thread(frame).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SendAndGet() {
		
		try {
			socket = new Socket("localhost", 4300);
			is = new ObjectInputStream(socket.getInputStream());
			os = new ObjectOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		textArea = new JTextArea();
		textArea.setColumns(4);
		contentPane.add(textArea);
		textArea.setRows(5);

		JButton btnNewButton = new JButton("Send Messgae");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// send stream to server
				try {
					os.writeObject("SendMessage");
					os.writeObject(textField.getText());
					System.out.println("TextFieldText: " + textField.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setAction(action);
		contentPane.add(btnNewButton);

		textField = new JTextField();
		contentPane.add(textField);
		textField.setColumns(10);
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}

	@Override
	public void run() {
		while(true){
			try {
				is = new ObjectInputStream(socket.getInputStream());
				String msg = (String) is.readObject();
				
				if(!msg.equals("")){
					textArea.append("\n"+msg);
				}
			} catch (ClassNotFoundException e) {
				System.err.println("SendAndGet: " + e.getMessage());
			} catch (IOException e) {
				System.err.println("SendAndGet2: " + e.getMessage());
			}
		}
	}
}
