package membership;

class A {

	int num1;
	
	// 기본 생성자
	public A() {}

	// 인수 생성자
	public A(int num1) {
		this.num1 = num1;
	}
	/*
	 생성자를 정의하지 않으면, 자동으로 기본 생성자 추가.
	 생성자를 정의하면, 기본 생성자 추가 안됨.
	 인스턴스를 만들 때는 무조건 생성자를 통해서만 생성 가능.
	 */
	
}

public class Test {

	public static void main(String[] args) {

		A a1 = new A();
		A a2 = new A(10);

	}
}
