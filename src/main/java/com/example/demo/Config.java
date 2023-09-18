package com.example.demo;

public class Config {
	static final int KEY_ESC = 27;
	static final int KEY_UP = 57416;
	static final int KEY_DOWN = 57424;
	static final int KEY_LEFT = 57419;
	static final int KEY_RIGHT = 57421;
	static final int KEY_CTRL_UP = 57485;
	static final int KEY_CTRL_DOWN = 57489;
	static final int KEY_CTRL_LEFT = 57459;
	static final int KEY_CTRL_RIGHT = 57460;
	
	
	static String key2Str(int key){
		String s = "";
		switch(key){
		case KEY_UP:
			s = "UP";
			break;
		case KEY_DOWN:
			s = "DOWN";
			break;
		case KEY_LEFT:
			s = "LEFT";
			break;
		case KEY_RIGHT:
			s = "RIGHT";
			break;
		case KEY_CTRL_UP:
			s = "CTRL_UP";
			break;
		case KEY_CTRL_DOWN:
			s = "CTRL_DOWN";
			break;
		case KEY_CTRL_LEFT:
			s = "CTRL_LEFT";
			break;
		case KEY_CTRL_RIGHT:
			s = "CTRL_RIGHT";
			break;
		case KEY_ESC:
			s = "ESC";
			break;
		default:
			s = "UNKNOWN";
			break;
		}
		s += "(" + key + ")";
		return s;
	}
}
