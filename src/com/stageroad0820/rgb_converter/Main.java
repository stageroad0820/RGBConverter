package com.stageroad0820.rgb_converter;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Main extends JFrame{
	String filePath = null;
	String fileName = null;
	
	public void onClickLoad() {
		JFileChooser load = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("사용 가능한 이미지 파일 ", "jpg", "jpeg", "png", "bmp");
		
		load.setFileFilter(filter);
		load.setDialogTitle("사진 파일 불러오기");
		load.setPreferredSize(new Dimension(800, 450));
		
		int event = load.showOpenDialog(this);
		
		if (event == JFileChooser.APPROVE_OPTION) {
			int confirm = JOptionPane.showConfirmDialog(null
					, "다음 파일을 열려고 합니다. 맞는지 확인해 주세요.\n\n- 파일 이름: " + load.getSelectedFile().getName() + "\n- 파일 위치: " + load.getSelectedFile().getPath()
					, "파일 확인"
					, JOptionPane.OK_CANCEL_OPTION);
			
			if (confirm == JOptionPane.OK_OPTION) {
				String filePath = load.getSelectedFile().getPath();
				String fileName = load.getSelectedFile().getName();
				
				try {
					this.getImage(filePath, fileName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if (confirm == JOptionPane.CANCEL_OPTION) {
				this.onClickLoad();
			}
			else {
				System.err.println("Error!");
			}
		}
		else if (event == JFileChooser.CANCEL_OPTION) {
			msgbox("error", "사용자 취소", "파일 열기를 취소하셨습니다. 파일을 열려면 [사진 불러오기] 버튼을 눌러주세요.");
		}
		else {
			System.err.println("Error!");
		}
	}
	
	public void getImage(String filePath, String fileName) throws IOException {
		try {
			File file = new File(filePath);
			BufferedImage bf_img = ImageIO.read(file);
			FileWriter output = new FileWriter("rgb_converter-" + fileName + ".txt");
			
			Desktop dt = Desktop.getDesktop();
			
			int height = bf_img.getHeight();
			int width = bf_img.getWidth();
			
			int alpha, red, green, blue, pixel;
			
			int confirm = JOptionPane.showConfirmDialog(null
					, "파일 입력을 시작하기 전 아래의 내용을 확인하시고 진행해 주세요.\n파일의 크기가 클 수록 파일 입력은 오래걸립니다.\n\n- 이미지 높이: " + height + " px\n- 이미지 너비: " + width + " px"
					, "파일 입력 시작 - 사용자 확인"
					, JOptionPane.OK_CANCEL_OPTION);
			
			if (confirm == JOptionPane.OK_OPTION) {
				msgbox("info", "파일 입력 시작", "파일 입력을 시작합니다. 파일 크기에 따라 시간이 다소 걸릴 수 있습니다.");
				
				for (int i = 0; i <= (height - 1); i++) {
					for (int j = 0; j <= (width - 1); j++) {
						pixel = bf_img.getRGB(i, j);
						alpha = (pixel >> 24) & 0xff;
						red = (pixel >> 16) & 0xff;
						green = (pixel >> 8) & 0xff;
						blue = pixel & 0xff;
						
						String contents = "- Pixel: " + i + ", " + j + " > [" + alpha + ", " + red + ", " + green + ", " + blue + "]\r\n";
						System.out.println(contents);
						output.write(contents);
					}
				}
				
				output.close();
				
				msgbox("info", "파일 입력 성공", "파일 입력이 완료되었습니다. 파일 이름은 다음과 같습니다.\n\n- 파일 이름: rgb_converter-" + fileName + ".txt");
				
				File temp = new File("rgb_converter-" + fileName + ".txt");
				
				dt.open(temp);
			}
			else if (confirm == JOptionPane.CANCEL_OPTION) {
				msgbox("error", "파일 입력 실패 - 사용자 취소", "파일 입력을 취소하셨습니다. 파일을 다시 열어주세요.");
			}
			else {
				System.err.println("Error!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void msgbox(String type, String title, String message) {
		if (type.equalsIgnoreCase("error")) {
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
		}
		else if (type.equalsIgnoreCase("info")) {
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			System.err.println("잘못된 메세지 박스 타입입니다.");
		}
	}
	
	public static void main(String[] args) {
		Main mainsc = new Main();
		JFrame fmain = new JFrame();
		JButton btn_load = new JButton();
		JPanel pmain = new JPanel();
		
		fmain.setTitle("RGB Converter v0.0.1 [Alpha]");
		fmain.setSize(800, 450);
		
		btn_load.setText("사진 불러오기");
		btn_load.setSize(200, 112);
		
		btn_load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					mainsc.onClickLoad();
				} catch (Exception e1) {
					System.err.println("Error!");
					e1.printStackTrace();
				}
			}
		});
		
		pmain.add(btn_load);
		
		fmain.add(pmain);
		fmain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fmain.setVisible(true);
	}
}