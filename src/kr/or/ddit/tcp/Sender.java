package kr.or.ddit.tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Sender extends Thread{
	private Scanner scan;
	private String name;
	private DataOutputStream dos;
	
	public Sender(Socket socket) {
		name = "[" + socket.getLocalAddress() + " : " + socket.getLocalPort() + "]";
		
		scan = new Scanner(System.in);
		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void run() {
		while(dos != null) {
			try {
				dos.writeUTF(name + " >>> " + scan.nextLine());		// 입력 문자열 전송하기
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
