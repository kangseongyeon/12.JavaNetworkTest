package kr.or.ddit.basic;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressTest {

	public static void main(String[] args) throws UnknownHostException {
		// INetAddress 클래스 => IP 주소 정보를 다루기 위한 클래스
		/*
		 * getByName()은 www.namer.com 또는 SEM-PC 등과 같은 머신이름이나 IP 주소를 통해 유효한 InetAddress
		 * 객체를 제공한다. IP 주소 자체를 넣으면 주소 구성 자체의 유효성 정도만 체크해 준다
		 */

		/*
		 * HostName : 도메인 네임을 의미 
		 * 			    도메인 네임이 없을 경우 => ip 주소가 HostName이 되기도 한다.
		 */
		
		// 네이버 사이트의 IP 정보 가져오기
		InetAddress naverIp = InetAddress.getByName("www.naver.com");
		// InetAddress naverIp = InetAddress.getByName("192.168.36.107");
		System.out.println("Host Name => " + naverIp.getHostName());		// 도메인 이름을 알고 싶을 때
		System.out.println("Host Address => " + naverIp.getHostAddress());	// IP 주소를 알고 싶을 때
		System.out.println();

		// getLocalHost : 자기 자신의 IP주소 정보 가져오기
		InetAddress localIp = InetAddress.getLocalHost();
		System.out.println("내 컴퓨터의 Host Name => " + localIp.getHostName());
		System.out.println("내 컴퓨터의 Host Address => " + localIp.getHostAddress());
		System.out.println();

		// IP 주소가 여러개인 호스트의 정보 가져오기
		// getAllByName : 모든 서버들을 InetAddress 객체 배열로 리턴
		InetAddress[] naverIps = InetAddress.getAllByName("www.naver.com");
		for (InetAddress iAddr : naverIps) {
			System.out.println(iAddr.toString());
		}

	}
}
