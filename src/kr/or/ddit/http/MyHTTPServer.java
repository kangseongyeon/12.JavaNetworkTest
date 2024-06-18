package kr.or.ddit.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.StringTokenizer;

/**
 * 간단한 웹서버 예제
 */
public class MyHTTPServer {
	private final int PORT = 80;
	private final String ENCODING = "UTF-8";

	public void start() {
		System.out.println("HTTP 서버가 시작되었습니다...");

		ServerSocket serverSocket = null;

		try {
			// ServerSocket 객체를 생성하여 지정된 포트에서 클라이언트 연결 대기
			serverSocket = new ServerSocket(this.PORT);
			while (true) {
				// 메서드 호출 시 클라이언트 연결 요청이 들어오면 Socket 객체 반환
				Socket socket = serverSocket.accept();

				// 클라이언트 연결이 들어오면 HttpHandler 객체를 생성하고..
				HttpHandler handler = new HttpHandler(socket);
				// start() 메서드를 호출해서 새로운 스레드 실행
				handler.start();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * HTTP 요청 처리를 위한 스레드
	 */
	class HttpHandler extends Thread {
		private Socket socket;

		public HttpHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			BufferedOutputStream out = null;
			BufferedReader br = null;

			try {
				out = new BufferedOutputStream(socket.getOutputStream());
				// 소켓으로부터 오는 정보들을 문자열이라고 판단하고 문자열을 가지고 request 메세지에 readLine을 읽기 위함
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// 요청 헤더정보 파싱하기

				// Request Line
				// 요청방식 (GET, POST 등), 요청경로, HTTP 버전 정보 포함
				String reqLine = br.readLine(); // 첫줄은 요청라인

				System.out.println("Request Line : " + reqLine);
				
				String reqPath = "";		// 요청경로
				
				// 요청 페이지 정보 가져오기
				// StringTokenizer를 사용하여 요청 라인을 토큰으로 분리함
				// 요청 라인을 공백 문자를 기준으로 토큰화함
				StringTokenizer st = new StringTokenizer(reqLine);
				
				// 반복문을 통해 토큰을 하나씩 확인
				while (st.hasMoreElements()) {
					// 현재 토근을 가져옴
					String token = st.nextToken();
					
					// "/"으로 시작하는 토큰이 있다면.. reqPath에 저장
					if (token.startsWith("/")) {
						reqPath = token;
						break;
					}
				}
				
				// URL 디코딩 처리(한글 깨짐문제 처리)
				reqPath = URLDecoder.decode(reqPath, ENCODING);
				
				String filePath = "./WebContent" + reqPath;
				
				// 해당 파일이름을 이용하여 Content-Type 정보 추출하기
				String contentType = URLConnection.getFileNameMap().getContentTypeFor(filePath);
				
				// CSS 파일인 경우 인식이 안되서 추가함
				if (contentType == null && filePath.endsWith(".css")) contentType = "text/css";
				
				System.out.println("Content-Type : " + contentType);
				
				File file = new File(filePath);
				if (!file.exists()) {
					// 에러 페이지 출력하기...
					return;
				}
				
				// 파일이 존재하는 경우, makeResponseBody() 메서드를 호출하여 파일 내용을 byte 배열로 만듦
				byte[] body = makeResponseBody(filePath);
				
				// makeResponseHeader() 메서드를 호출하여 HTTP 응답 헤더를 byte 배열로 만듦
				byte[] header = makeResponseHeader(body.length, contentType);
				
				///////////////////////////////////////////////////////
				
				// 응답 헤더정보 보내기
				out.write(header);
				
				// 응답내용 보내기 전 반드시 Empty Line 보내기..
				out.write("\r\n\r\n".getBytes());
				
				// 응답 내용 보내기
				out.write(body);
				
				out.flush(); // 강제 방출..

			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					socket.close(); // 소켓연결 끊기
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * 응답 헤더정보 생성하기
	 * @param contentLength 응답내용 크기
	 * @param mimeType 컨텐츠 타입정보
	 * @return 헤더내용 바이트배열
	 */
	//  contentLength와 mimeType을 입력받아 HTTP 응답 헤더를 생성
	private byte[] makeResponseHeader(int contentLength, String mimeType) {
		String header = "HTTP/1.1 200 OK\r\n"			// HTTP 응답 상태 확인
				+ "Server: MyHTTPServer 1.0]\r\n"		// Server 헤더 필드
				+ "Content-Length: " + contentLength + "\r\n"	// Content-Length 헤더 필드
				+ "Content-Type: " + mimeType + "; charset = " + this.ENCODING; // Content-Type 헤더 필드
		
		System.out.println("header => " + header);
		
		return header.getBytes();		// 헤더 문자열을 바이트 배열로 변환하여 반환
	}
	
	/**
	 * 응답내용 생성하기
	 * @param filePath	응답으로 사용할 파일경로
	 * @return 바이트 배열 데이터
	 */
	private byte[] makeResponseBody(String filePath) {
		byte[] data = null;
		
		FileInputStream fis = null;
		
		try {
			File file = new File(filePath);
			data = new byte[(int) file.length()];
			
			fis = new FileInputStream(file);
			fis.read(data);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	public static void main(String[] args) {
		new MyHTTPServer().start();
	}
}
