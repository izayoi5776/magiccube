package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import biz.source_code.utils.RawConsoleInput;

/**
 * 魔方模拟
 */
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
	/**
	 * [面][行][列]
	 * 面0：前（远离自己），1：左，2：顶，3：右，4：后（靠近自己），5：底，6：底展开在3的右侧时
	 * 面展开时，第一行面0，第二行面1，2，3，第三行面4，第四行面5，
	 * 面展开后是2为中心，分别和0，1，3，4相接，4和5相接的十字形状
	 */
	char[][][] a = new char[7][3][3];
	int vert = 0;	// 当前垂直位置y
	int hori = 0;	// 当前水平位置x

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		init();
		System.out.println("" + atos());
		
		int x = 0;
		// 27:ESC
		while(x!=27) {
			x = RawConsoleInput.read(true);
			//x = Config.KEY_LEFT;
			System.out.println("input=" + x);
			//                Ctrl
			// left 57419     57459
			// right 57421    57460
			// up 57416       57485
			// down 57424     57489
			switch(x) {
			case Config.KEY_UP:
				moveVert(vert);
				break;
			case Config.KEY_DOWN:
				moveVert(vert);
				moveVert(vert);
				moveVert(vert);
				break;
			case Config.KEY_LEFT:
				moveHori(hori);
				break;
			case Config.KEY_RIGHT:
				moveHori(hori);
				moveHori(hori);
				moveHori(hori);
				break;
			case Config.KEY_CTRL_LEFT:
				vert+=2;
				vert = vert % 3;
				break;
			case Config.KEY_CTRL_RIGHT:
				vert++;
				vert = vert % 3;
				break;
			case Config.KEY_CTRL_UP:
				hori+=2;
				hori = hori % 3;
				break;
			case Config.KEY_CTRL_DOWN:
				hori++;
				hori = hori % 3;
				break;
			}

			System.out.println("vert=" + vert + " hori=" + hori);
			System.out.println("" + atos());
		}
		System.out.println("\nEOF\n");
	}

	/**
	 * 初始化
	 * 面0数字，1-3大写字母（加#号），4-5小写字母
	 */
	void init(){
		char c = '1';
		for(int i=0; i<6; i++) {
			for(int j=0; j<3; j++) {
				for(int k=0; k<3; k++) {
					a[i][j][k] = c;
					if(c=='9') {
						c = 'A';
					} else if(c=='Z') {
						c = '#';
					} else if(c=='#') {
						c = 'a';
					} else {
						c++;	
					}
				}
			}
		}
		// 底面5和底面6之间差180度
		copySide(a[5], a[6]);
		rotate(a[6], true);
		rotate(a[6], true);
	}
	
	// 面展开
	public String atos() {
		String s = "";
		s += "   |" + a[0][0][0] + a[0][0][1] + a[0][0][2] + "|\n";
		s += "   |" + a[0][1][0] + a[0][1][1] + a[0][1][2] + "|\n";
		s += "   |" + a[0][2][0] + a[0][2][1] + a[0][2][2] + "|\n";

		s += "---------------\n";
		s += "" + a[1][0][0] + a[1][0][1] + a[1][0][2];
		s += "|" + a[2][0][0] + a[2][0][1] + a[2][0][2];
		s += "|" + a[3][0][0] + a[3][0][1] + a[3][0][2];
		s += "|" + a[6][0][0] + a[6][0][1] + a[6][0][2] + "\n";

		s += "" + a[1][1][0] + a[1][1][1] + a[1][1][2];
		s += "|" + a[2][1][0] + a[2][1][1] + a[2][1][2];
		s += "|" + a[3][1][0] + a[3][1][1] + a[3][1][2];
		s += "|" + a[6][1][0] + a[6][1][1] + a[6][1][2] + "\n";
	
		s += "" + a[1][2][0] + a[1][2][1] + a[1][2][2];
		s += "|" + a[2][2][0] + a[2][2][1] + a[2][2][2];
		s += "|" + a[3][2][0] + a[3][2][1] + a[3][2][2];
		s += "|" + a[6][2][0] + a[6][2][1] + a[6][2][2] + "\n";

		s += "---------------\n";
		s += "   |" + a[4][0][0] + a[4][0][1] + a[4][0][2] + "|\n";
		s += "   |" + a[4][1][0] + a[4][1][1] + a[4][1][2] + "|\n";
		s += "   |" + a[4][2][0] + a[4][2][1] + a[4][2][2] + "|\n";
		
		s += "   ----\n";
		s += "   |" + a[5][0][0] + a[5][0][1] + a[5][0][2] + "|\n";
		s += "   |" + a[5][1][0] + a[5][1][1] + a[5][1][2] + "|\n";
		s += "   |" + a[5][2][0] + a[5][2][1] + a[5][2][2] + "|\n";

		return s;
	}
	/**
	 * 复制一个面
	 * @param from	源，不能为null
	 * @param to	目的，要求尺寸不比源小，不能为null
	 */
	void copySide(char[][] from, char[][] to) {
		for(int i=0; i<from.length; i++) {
			for(int j=0; j<from[0].length; j++) {
				to[i][j] = from[i][j];
			}
		}
	}
	
	
	/**
	 * 垂直移动
	 * 向上，第y列
	 * @param y 	第y列
	 */
	void moveVert(int y){
		char[][][] t = new char[1][1][3];
		// 0->t
		copy(a, 0, -1, y, t, 0, 0, -1);
		// 2->0
		copy(a, 2, -1, y, a, 0, -1, y);
		// 4->2
		copy(a, 4, -1, y, a, 2, -1, y);
		// 5->4
		copy(a, 5, -1, y, a, 4, -1, y);
		// t->5
		copy(t, 0, 0, -1, a, 5, -1, y);
		
		switch(y) {
		case 0:
			rotate(a[1], false);
			break;
		case 2:
			rotate(a[3], true);
			break;
		}
	}
	/**
	 * 水平移动
	 * 向左
	 * @param x		第x行
	 */
	void moveHori(int x) {
		char[][][] t = new char[1][1][3];
		// 1->t
		copy(a, 1, x, -1, t, 0, 0, -1);
		// 2->1
		copy(a, 2, x, -1, a, 1, x, -1);
		// 3->2
		copy(a, 3, x, -1, a, 2, x, -1);
		// 6->3
		copy(a, 6, x, -1, a, 3, x, -1);
		// t->6
		copy(t, 0, 0, -1, a, 6, x, -1);
		
		// 底面6复制回5
		copySide(a[6], a[5]);
		rotate(a[5], true);
		rotate(a[5], true);
		
		switch(x) {
		case 0:
			rotate(a[0], true);
			break;
		case 2:
			rotate(a[4], false);
			break;
		}
	}
	/**
	 * 旋转
	 * @param	a 
	 * @param	clockwise true:顺时针，false：逆时针
	 */
	void rotate(char[][] a, boolean clockwise) {
		char t = a[0][0];
		if(clockwise) {
			a[0][0] = a[2][0];
			a[2][0] = a[2][2];
			a[2][2] = a[0][2];
			a[0][2] = t;
			t = a[0][1];
			a[0][1] = a[1][0];
			a[1][0] = a[2][1];
			a[2][1] = a[1][2];
			a[1][2] = t;
		} else {
			a[0][0] = a[0][2];
			a[0][2] = a[2][2];
			a[2][2] = a[2][0];
			a[2][0] = t;
			t = a[0][1];
			a[0][1] = a[1][2];
			a[1][2] = a[2][1];
			a[2][1] = a[1][0];
			a[1][0] = t;
			
		}
	}
	/**
	 * 复制数组
	 * x, y是-1时表示指定全行或列
	 * @param from		from array
	 * @param fside		from side
	 * @param fx		from row
	 * @param fy		from col
	 * @param to		to array
	 * @param tside		to side
	 * @param tx		to row
	 * @param ty		to col
	 */
	void copy(char[][][] from, int fside, int fx, int fy, char[][][] to, int tside, int tx, int ty) {
		for(int i=0; i<3; i++) {
			to[tside][tx==-1?i:tx][ty==-1?i:ty] = from[fside][fx==-1?i:fx][fy==-1?i:fy];
		}
	}
}
