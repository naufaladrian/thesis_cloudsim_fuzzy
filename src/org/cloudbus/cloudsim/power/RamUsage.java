package org.cloudbus.cloudsim.power;


import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Encapsulate Fuzzification of RamUsage. 
 */
public class RamUsage {

	/**
	 * "Derajat keanggotaan" or Membership degree.
	 */
	public final double membershipDegree;
	
	/**
	 * "Kategory keanggotaan"
	 */
	public final ERamUsageCategory category;
	
	private RamUsage(double membershipDegree, ERamUsageCategory category) {
		this.membershipDegree = membershipDegree;
		this.category = category;
	}
	
	public static RamUsage[] doFuzzification(double ramUsage) {				
		ERamUsageCategory[] categories = ERamUsageCategory.getCategories(ramUsage);		
		RamUsage[] retvals = new RamUsage[categories.length];
		for (int c = 0; c < categories.length; c++) {
			double membershipDegree = categories[c].getMembershipDegree(ramUsage);
			retvals[c] = new RamUsage(membershipDegree, categories[c]);
		}		
		return retvals;
		
	}
	
	/**
	 *	Has membership function curve as Triangle ({@link EMembershipFunction#TRIANGLE}). 			
	 */
	public static enum ERamUsageCategory {
		LOW	( 7,EMembershipFunction.TRAPEZIUM, Double.NaN,	  0,  800, 1565),
		MEDIUM	(11,EMembershipFunction.TRAPEZIUM, 1065, 1565, 2530, 3030),
		HIGH	(13,EMembershipFunction.TRAPEZIUM, 2530, 3030, Double.MAX_VALUE, Double.NaN);
		
		public final int code;
		private final EMembershipFunction fuctionType;
		private final double[] bound;
		private final double min, max;		
	
		private ERamUsageCategory(int code, EMembershipFunction type, double ... bound) {
			this.code = code;
			this.fuctionType = type;
			this.bound = bound;
			min = Double.isNaN(bound[0]) ? bound[1] : bound[0];
			max = Double.isNaN(bound[bound.length-1]) ? bound[bound.length-2] : bound[bound.length-1];
		}
		
		public final double getMembershipDegree(double val) {
			switch(fuctionType) {
				case LINEAR_DOWN:
					return EMembershipFunction.linearDown(bound[0], bound[1], val);							
				case LINEAR_UP:
					return EMembershipFunction.linearUp(bound[0], bound[1], val);
				case TRAPEZIUM:
					return EMembershipFunction.trapezium(bound[0], bound[1], bound[2], bound[3],val);
				case TRIANGLE:
					return EMembershipFunction.triangle(bound[0], bound[1], bound[2],val);							
				default:
					throw new NotImplementedException();				
			}
		}
		
		/**
		 * Get the fuzzication enum from the cpuUsage value.
		 * @param ramUsage
		 * @return The {@link ERamUsageCategory}
		 */			
		private static ERamUsageCategory[] getCategories(double ramUsage) {
			List<ERamUsageCategory> retval = new ArrayList<>();
			if (ramUsage >= LOW.min && ramUsage <= LOW.max) {
				retval.add(LOW);
			}
			if (ramUsage >= MEDIUM.min && ramUsage <= MEDIUM.max) {
				retval.add(MEDIUM);
			}
			if (ramUsage >= HIGH.min && ramUsage <= HIGH.max) {
				retval.add(HIGH);
			}
			return (ERamUsageCategory[])retval.toArray(new ERamUsageCategory[retval.size()]);
		}
	}			
}