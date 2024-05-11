package text_editor;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.Color;
import java.awt.Dimension;



public class TE extends JFrame implements ActionListener{
	JTextArea textArea;
	JLabel label,flabel;
	JScrollPane scrollPane;
	JSpinner fontSizeSpinner;
	JButton colorButton;
	JComboBox fontSelect;
	
	JMenuBar menuBar;
	JMenu fileItem;
	JMenuItem newItem, openItem, saveItem,copyPathItem, exitItem;

	JFileChooser fileChooser;
	
	TE(){
		 
		
		// (1) Insert Frame
		this.setTitle("Text Editor");
		this.setSize(750,770);
		this.getContentPane().setBackground(new Color(51,51,51));
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null);
		
		// (2) MenuBar
		menuBar = new JMenuBar();
		fileItem = new JMenu("File");
		
		newItem = new JMenuItem("New");
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		copyPathItem = new JMenuItem("Copy Path");
		exitItem = new JMenuItem("Exit");
		
		// (3) MenuItems
		fileItem.add(newItem);
		fileItem.addSeparator();
		fileItem.add(openItem);
		fileItem.add(saveItem);
		fileItem.addSeparator();
		fileItem.add(copyPathItem);
		fileItem.addSeparator();
		fileItem.add(exitItem);
		menuBar.add(fileItem);
		
		// (4) Menu Action Listener
		
		newItem.addActionListener(this);
		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		copyPathItem.addActionListener(this);
		exitItem.addActionListener(this);
		
		// (5) file chooser
		fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt")); 
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Files", "pdf")); 
        
        
		// (6) Text Style
		textArea = new JTextArea();
//		textArea.setPreferredSize(new Dimension(720,700));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Times New Roman",Font.PLAIN,20));
		
		
		//Text Area
		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(720,650));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		label = new JLabel("Font Size: ");
		label.setForeground(new Color(250,250,250));
		
		flabel = new JLabel("@Ashray Khosin");
		flabel.setForeground(new Color(250,250,250));
		
		// (7) Select Size
		fontSizeSpinner = new JSpinner();
		fontSizeSpinner.setPreferredSize(new Dimension(40,20));
		fontSizeSpinner.setValue(10);
		fontSizeSpinner.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSizeSpinner.getValue()));
			}
		});
		
		
		// (8) Color Chooser
		colorButton = new JButton("Color");
		colorButton.addActionListener(this);
		
		// (9) Select font
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		fontSelect = new JComboBox(fonts);
		fontSelect.addActionListener(this);
		fontSelect.setSelectedItem("Times New Roman");
		
		this.setJMenuBar(menuBar);
		this.add(label);
		this.add(fontSizeSpinner);
		this.add(fontSelect);
		
		
		
//		(10) adding all
		//this.add(textArea);
		this.add(colorButton);
		
		this.add(scrollPane);
		this.add(flabel);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
	
	//Actions
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		switch(command) {
		case "New":
			textArea.setText("");
			break;
		case "Open":
			openFile();
			break;
		case "Save":
			saveFile();
			break;
		case "Copy Path":
		    copyFilePath();
		    break;

		case "Exit":
			System.exit(0);
			break;
		}
		
		//Selecting Color
		if(e.getSource()==colorButton) {
			JColorChooser colorChooser = new JColorChooser();
			Color color = colorChooser.showDialog(null, "Select Color",Color.BLACK );
			textArea.setForeground(color);
		}
		
		//Selecting Font
		if(e.getSource()==fontSelect) {
			textArea.setFont(new Font((String)fontSelect.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
		}
	}
	
	public void openFile() {
		fileChooser.setCurrentDirectory(new File("."));
		int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
	}
	
	public void saveFile() {
		fileChooser.setCurrentDirectory(new File("."));
		int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textArea.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
	}
	
	public void copyFilePath() {
	    File currentFile = fileChooser.getSelectedFile();
	    if (currentFile != null) {
	        String filePath = currentFile.getAbsolutePath();
	        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(filePath), null);
	        JOptionPane.showMessageDialog(this, "File path copied to clipboard:\n" + filePath, "File Path Copied", JOptionPane.INFORMATION_MESSAGE);
	    } else {
	        JOptionPane.showMessageDialog(this, "No file is currently open.", "No File Open", JOptionPane.WARNING_MESSAGE);
	    }
	}
}
