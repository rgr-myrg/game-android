package net.usrlib.gobirdie.util;

import java.util.Random;

public final class NumUtil {
	public static final Random RAND = new Random();

	public static final int getRandInt(int min, int max) {
		return RAND.nextInt((max - min) + 1) + min;
	}

	public static final int getRandIntFromList(int[] list) {
		int min = 0;
		int max = list.length - 1;

		return list[getRandInt(min, max)];
	}
}
