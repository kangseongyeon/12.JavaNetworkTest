package kr.or.ddit.udp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FileSender {
	private DatagramSocket ds;
	private DatagramPacket dp;
	
	private InetAddress receiveAddr;
	private int port;
	private File file;
	
	public FileSender(String receiverIp) {
		try {
			ds = new DatagramSocket();
			this.port = port;
			receiveAddr = InetAddress.getByName(receiverIp);
			file = new File("d:/D_Other/aaa.jpg");
			
			if (!file.exists()) {
				System.out.println("파일이 존재하지 않습니다.");
				System.exit(0);
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void start() {
		long fileSize = file.length();
		long totalReadBytes = 0;
		
		long startTime = System.currentTimeMillis();
		
		try {
		 	sendData("start".getBytes());	// 전송 시작을 알려주기 위한 문자열 전송
		 	
		 	sendData(file.getName().getBytes());	// 파일 전송
		 	
		 	sendData(String.valueOf(fileSize).getBytes());	// 총 파일 크기 전송 (bytes)
		 	
		 	FileInputStream fis = new FileInputStream(file);
		 	
		 	byte[] buffer = new byte[1000];
		 	
		 	while(true) {
		 		
		 		Thread.sleep(10);	// 패킷 전송 간의 시간 간격을 쥑 위해서..
		 		
		 		int readBytes = fis.read(buffer, 0, buffer.length);
		 		
		 		if (readBytes == -1) {	// 파일 내용을 다 읽은 경우...
		 			break;
		 		}
		 		sendData(buffer, readBytes);	// 읽어온 파일내용 전송
		 		
		 		totalReadBytes += readBytes;
		 		
		 		System.out.println("진행 상태 : " + totalReadBytes + "/" + fileSize + " byte(s) ("
		 				+ (totalReadBytes * 100 / fileSize) + "%)");
		 	}
		 	
		 	long endTime = System.currentTimeMillis();
		 	long diffTime = endTime - startTime;
		 	double transferSpeed = fileSize / diffTime;
		 	
		 	System.out.println("걸린 시간 : " + diffTime + " (ms)");
		 	System.out.println("평균 전송속도 : " + transferSpeed + " Bytes/ms");
		 	
		 	System.out.println("전송 완료...");
		 	
		 	fis.close();
		 	ds.close();
		 	
		 	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 바이트 배열 데이터 전송하기
	 * @param data 보낼 바이트배열 데이터
	 * @param length 보낼 바이트배열 크기
	 * @throws IOException
	 */
	public void sendData(byte[] data, int length) throws IOException {
		dp = new DatagramPacket(data, length, receiveAddr, port);
		ds.send(dp);
	}

	/**
	 * 바이트 배열 데이터 전송하기
	 * @param data
	 * @throws IOException
	 */
	public void sendData(byte[] data) throws IOException {
		sendData(data, data.length);
	}
}
