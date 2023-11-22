package org.cloudbus.cloudsim.power;


import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Encapsulate Fuzzification of CpuUsage. 
 */
public class CpuUsage {
	
	/**
	 * "Derajat keanggotaan" or Membership degree.
	 */
	public final double membershipDegree ;
	
	/**
	 * "Kategory keanggotaan"
	 */
	public final ECpuUsageCategory category;
	
	private CpuUsage(double membershipDegree, ECpuUsageCategory category) {
		this.membershipDegree = membershipDegree;
		this.category = category;
	}
	
	public static CpuUsage[] doFuzzification(double cpuUsage) 
        {				
		ECpuUsageCategory[] categories = ECpuUsageCategory.getCategories(cpuUsage);
		CpuUsage[] retvals = new CpuUsage[categories.length];
		for (int c = 0; c < categories.length; c++) {
			double membershipDegree = categories[c].getMembershipDegree(cpuUsage);
			retvals[c] = new CpuUsage(membershipDegree, categories[c]);
		}
		return retvals;
		
	}
	
	/**
	 *	Has membership function curve as Triangle ({@link EMembershipFunction#TRIANGLE}). 			
	 */
	public static enum ECpuUsageCategory {
		LOW	(1, EMembershipFunction.TRAPEZIUM, Double.NaN, 0, 800, 1973),
		MEDIUM	(3, EMembershipFunction.TRAPEZIUM, 1273, 1973, 	 3346, 4046),
		HIGH	(5, EMembershipFunction.TRAPEZIUM, 3346, 4500, 5320, Double.NaN);
		
		public final int code;
		private final EMembershipFunction fuctionType;
		private final double[] bound;
		private final double min, max;
		private ECpuUsageCategory(int code, EMembershipFunction type, double ... bound) {
			this.code = code;
			this.fuctionType = type;
			this.bound = bound;
			min = Double.isNaN(bound[0]) ? bound[1] : bound[0];
			max = Double.isNaN(bound[bound.length-1]) ? bound[bound.length-2] : bound[bound.length-1];
		}
		
		private final double getMembershipDegree(double val) {
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
		 * @param cpuUsage
		 * @return The {@link ECpuUsageCategory}
		 */			
		public static ECpuUsageCategory[] getCategories(double cpuUsage) {
			List<ECpuUsageCategory> retval = new ArrayList<>();
			if (cpuUsage >= LOW.min && cpuUsage <= LOW.max) {
				retval.add(LOW);
			}
			if (cpuUsage >= MEDIUM.min && cpuUsage <= MEDIUM.max) {
				retval.add(MEDIUM);
			}
			if (cpuUsage >= HIGH.min && cpuUsage <= HIGH.max) {
				retval.add(HIGH);
			}
			return (ECpuUsageCategory[])retval.toArray(new ECpuUsageCategory[retval.size()]);
		}
	}			
}
