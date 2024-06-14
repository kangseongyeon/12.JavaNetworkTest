package kr.or.ddit.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionTest {
	public static void main(String[] args) throws IOException {
		// URLConnection => 애플리케이션과 URL 간의 통신 연결을 위한 추상 클래스
		
		// 특정 서버 (예: 네이버)의 리소스 접근해 가져오기
		URL url = new URL("https://www.naver.com/index.html");
		
		// Header 정보 가져오기
		URLConnection urlConn = url.openConnection();
		
		System.out.println("Content-Type : " + urlConn.getContentType());	// Content-Type : text/html; charset=UTF-8
		System.out.println("Encoding : " + urlConn.getContentEncoding());	// Encoding : null
		System.out.println("Content : " + urlConn.getContent());			// Content : sun.net.www.protocol.http.HttpURLConnection$HttpInputStream@5056dfcb
			
		///////////////////////////////////////////////////////
		
		// 문자기반으로 읽음
		InputStream is = (InputStream)urlConn.getContent();
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		
		String temp = "";
		while((temp = br.readLine()) != null) {
			System.out.println(temp);
		}
		br.close();		// 스트림 닫기
	}
}
