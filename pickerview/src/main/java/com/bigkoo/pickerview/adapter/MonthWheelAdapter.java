package com.bigkoo.pickerview.adapter;


import com.contrarywind.adapter.WheelAdapter;

import java.util.List;

/**
 * Numeric Wheel adapter.
 */
public class MonthWheelAdapter implements WheelAdapter {
	private String[] list = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

	private int minValue;
	private int maxValue;

	/**
	 * Constructor
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public MonthWheelAdapter(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	public Object getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			int value = minValue + index;
			return value;
		}
		return 1;
	}

	@Override
	public int getItemsCount() {
		return maxValue - minValue + 1;
	}
	
	@Override
	public int indexOf(Object o){
		try {
			return (int)o - minValue;
		} catch (Exception e) {
			return -1;
		}

	}
}
