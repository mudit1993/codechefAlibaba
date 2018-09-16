package com.codechef.cah01.model;

import java.util.Arrays;
import java.util.Optional;

public enum Months {

	JAN(1), FEB(2), MARCH(3), APRIL(4), MAY(5), JUNE(6), JULY(7), AUG(8), SEPT(9), OCT(10), NOV(11), DEC(12);

	private int value;

	private Months(int value) {
		this.value = value;
	}

	public static Optional<Months> valueOf(int value) {
		return Arrays.stream(values()).filter(month -> month.value == value).findFirst();
	}
}
