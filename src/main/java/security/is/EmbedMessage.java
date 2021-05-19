package security.is;

import java.awt.image.*;
import javax.swing.*;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.file.FileTypeDirectory;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;

public class EmbedMessage extends JFrame implements ActionListener {
	// buttons
	JButton open = new JButton("Open");
	JButton embed = new JButton("Embed");
	JButton save = new JButton("Save into new file");
	JButton reset = new JButton("Reset");

	// textarea
	JTextArea message = new JTextArea(10, 3);
	String metaString = "";

	// images
	BufferedImage sourceImage = null;
	BufferedImage embeddedImage = null;
	BufferedImage embeddedImage1 = null;
	// splitpane
	JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	JScrollPane originalPane = new JScrollPane();
	JScrollPane embeddedPane = new JScrollPane();

	// cons
	public EmbedMessage() {
		// imagess
		sp.setLeftComponent(originalPane);
		sp.setRightComponent(embeddedPane);
		originalPane.setBorder(BorderFactory.createTitledBorder("Original Image"));
		embeddedPane.setBorder(BorderFactory.createTitledBorder("Steganographed Image"));

		this.getContentPane().add(sp, BorderLayout.CENTER);

		originalPane.setBackground(Color.WHITE);
		originalPane.getViewport().setBackground(Color.WHITE);

		embeddedPane.setBackground(Color.WHITE);
		embeddedPane.getViewport().setBackground(Color.WHITE);
		// message area
		JPanel p = new JPanel(new GridLayout(1, 1));

		// buttons
		p = new JPanel(new FlowLayout());
		p.setBackground(Color.WHITE);
		open.setBackground(Color.BLACK);
		open.setForeground(Color.WHITE);

		open.setFont(new Font("Arial", Font.BOLD, 20));

		embed.setBackground(Color.BLACK);
		embed.setForeground(Color.WHITE);

		embed.setFont(new Font("Arial", Font.BOLD, 20));
		save.setBackground(Color.BLACK);
		save.setForeground(Color.WHITE);

		save.setFont(new Font("Arial", Font.BOLD, 20));
		reset.setBackground(Color.BLACK);
		reset.setForeground(Color.WHITE);

		reset.setFont(new Font("Arial", Font.BOLD, 20));

		p.add(open);
		p.add(embed);
		p.add(save);
		p.add(reset);
		this.getContentPane().add(p, BorderLayout.SOUTH);
		open.addActionListener(this);
		embed.addActionListener(this);
		save.addActionListener(this);
		reset.addActionListener(this);

		// FRAME

		setTitle("Steganography Program");
		this.setSize(1200, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		sp.setDividerLocation(0.5);
		this.validate();
	}

	public void actionPerformed(ActionEvent ae) {
		String s1 = ae.getActionCommand();
		if (s1.equals("Open"))
			openImage();
		if (s1.equals("Embed"))
			embedMessage();
		if (s1.equals("Save into new file"))
			saveImage();
		if (s1.equals("Reset"))
			resetInterface();
	}

	// OPENING THE IMAGE
	private java.io.File showFileDialog(final boolean open) {
		JFileChooser fc = new JFileChooser("Open an image");
		javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
			public boolean accept(java.io.File f) {
				String name = f.getName();
				if (open)
					return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")
							|| name.endsWith(".gif") || name.endsWith(".tiff") || name.endsWith(".bmp")
							|| name.endsWith(".dib");
				return f.isDirectory() || name.endsWith(".png") || name.endsWith(".bmp");
			}

			public String getDescription() {
				if (open)
					return "Image (*.jpg, *.jpeg, *.png, *.gif, *.tiff, *.bmp, *.dib)";
				return "Image (*.png, *.bmp)";
			}
		};
		fc.addChoosableFileFilter(ff);

		java.io.File f = null;
		if (open && fc.showOpenDialog(this) == fc.APPROVE_OPTION)
			f = fc.getSelectedFile();
		else if (!open && fc.showSaveDialog(this) == fc.APPROVE_OPTION)
			f = fc.getSelectedFile();
		return f;
	}

	private void openImage() {
		java.io.File f = showFileDialog(true);
		try {
			String fName = f.getName() + "IS";
	    	String fileSize = f.length() + "IS";
			Metadata metadata = ImageMetadataReader.readMetadata(f);
			FileTypeDirectory FLType = metadata.getFirstDirectoryOfType(FileTypeDirectory.class);
			String fLType = FLType.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_LONG_NAME) + "IS";
		/*	System.out.println(fLType);
			System.out.println(fileSize);
		    System.out.println(fName);
			
			System.out.println(Hashs.sha256(fLType+"IS"));
	    	System.out.println(Hashs.sha256(fileSize+"IS"));
	    	System.out.println(Hashs.sha256(fName+"IS")); */
			
			metaString = Hashs.sha256(fLType) + " " + Hashs.sha256(fileSize) + " " + Hashs.sha256(fName);
			
			sourceImage = ImageIO.read(f);
			JLabel l = new JLabel(new ImageIcon(sourceImage));
			originalPane.getViewport().add(l);
			this.validate();
		} catch (Exception ex) {
		}
	}

	// RESET ALL THE PANELSS
	private void resetInterface() {
		message.setText("");
		originalPane.getViewport().removeAll();
		embeddedPane.getViewport().removeAll();
		sourceImage = null;
		embeddedImage = null;
		sp.setDividerLocation(0.5);
		this.validate();
	}

	// SAVING THE IMAGE
	private void saveImage() {
		if (embeddedImage == null) {
			JOptionPane.showMessageDialog(this, "No message has been embedded!", "Nothing to save",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		java.io.File f = showFileDialog(false);
		String name = f.getName();
		String ext = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
		if (!ext.equals("png") && !ext.equals("bmp") && !ext.equals("dib")) {
			ext = "png";
			f = new java.io.File(f.getAbsolutePath() + ".png");
		}
		try {
			if (f.exists())
				f.delete();
			ImageIO.write(embeddedImage, ext.toUpperCase(), f);
		} catch (Exception ex) {
		}
	}

	// message encryption
	private void embedMessage() {
		String mess = message.getText();
		embeddedImage = sourceImage.getSubimage(0, 0, sourceImage.getWidth(), sourceImage.getHeight());

		SteganoImgProcess sip = new SteganoImgProcess();
		embeddedImage1 = sip.encode(sourceImage, embeddedImage, sourceImage.getWidth(), sourceImage.getHeight(), metaString);
		try {
			JLabel l = new JLabel(new ImageIcon(embeddedImage1));
			embeddedPane.getViewport().add(l);
			this.validate();
		} catch (Exception ex) {
		}
	}

	public static void main(String arg[]) {
		new EmbedMessage();
	}

}