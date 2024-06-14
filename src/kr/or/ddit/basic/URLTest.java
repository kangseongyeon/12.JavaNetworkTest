package kr.or.ddit.basic;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class URLTest {
	public static void main(String[] args) throws MalformedURLException, URISyntaxException {
		// URL : uniform resource locator (특정한 리소스의 위치 정보)
		// URL을 안다는 것은 ? => 특정한 리소스 (이미지, 동영상)을 가져올 수 있다는 의미
		
		// URL 클래스 => 인터넷에 존재하는 서버들의 자원에 접근하기 위한 주소를 관리하기 위한 클래스
		URL url = new URL("http", "ddit.or.kr", 80, "/main/index.html?ttt=123#kkk");
		
		System.out.println("전체 URL 주소 : " + url.toString());	// http://ddit.or.kr:80/main/index.html?ttt=123#kkk
		
		System.out.println("protocol : " + url.getProtocol());	// protocol : http
		System.out.println("host : " + url.getHost());			// host : ddit.or.kr
		System.out.println("port : " + url.getPort());			// port : 80
		
		// query : ? 다음에 나오는 필요한 정보 (ttt=123) => 쿼리를 통해서 의미있는 정보를 사용할 수 있음
		System.out.println("query : " + url.getQuery());	// query : ttt=123
		
		// file : 쿼리정보 포함
		System.out.println("file : " + url.getFile());		// file : /main/index.html?ttt=123	
		
		// path : 쿼리정보 미포함
		System.out.println("path : " + url.getPath());		// path : /main/index.html
		
		// ref : query(?) 다음에 나오는 #
		System.out.println("ref : " + url.getRef());		// ref : kkk
		
		System.out.println(url.toExternalForm());			// 외부형태로 출력
		System.out.println(url.toString());					// 문자열로 출력
		System.out.println(url.toURI().toString());			// URL을 URI로 바꾼 후, string으로 변환
		
	}
}
