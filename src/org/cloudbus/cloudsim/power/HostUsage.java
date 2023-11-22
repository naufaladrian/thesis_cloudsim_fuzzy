package org.cloudbus.cloudsim.power;


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Encapsulate Fuzzification of HostUsage. 
 */
public class HostUsage {	
	
	/**
	 *	Has membership function curve as Linear down ({@link EMembershipFunction#LINEAR_DOWN}),
	 *	and Linear up ({@link EMembershipFunction#LINEAR_UP})			
	 */
	public static enum EHostUsageCategory {				
		NORMAL		(EMembershipFunction.TRAPEZIUM ,Double.NaN,  0,  40, 70),
		OVERLOAD	(EMembershipFunction.TRAPEZIUM ,60, 80, 100, Double.NaN);
		
		private final EMembershipFunction fuctionType;
		private final double[] bound;
		private final double min, max;			
		private EHostUsageCategory(EMembershipFunction type, double ... bound) {
			this.fuctionType = type;
			this.bound = bound;
			min = Double.isNaN(bound[0]) ? bound[1] : bound[0];
			max = Double.isNaN(bound[bound.length-1]) ? bound[bound.length-2] : bound[bound.length-1];
		}
		
		public final double getMinbound() {
			return min;				
		}
		public final double getMaxbound() {
			return max;
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
	}
	
	/**
	 * Contain all overload flag code.
	 */
	public static int[] OVERLOAD_FLAG_LIST = {
		CpuUsage.ECpuUsageCategory.HIGH.code   * RamUsage.ERamUsageCategory.HIGH.code,
		CpuUsage.ECpuUsageCategory.HIGH.code   * RamUsage.ERamUsageCategory.MEDIUM.code,
		CpuUsage.ECpuUsageCategory.MEDIUM.code * RamUsage.ERamUsageCategory.HIGH.code				
	} ;
	
	/**
	 * Not-overload (Normal) flag code.
	 */
	public static int[] NOT_OVERLOAD_FLAG_LIST = {
		CpuUsage.ECpuUsageCategory.LOW.code   	* RamUsage.ERamUsageCategory.LOW.code,
		CpuUsage.ECpuUsageCategory.LOW.code   	* RamUsage.ERamUsageCategory.MEDIUM.code,
		CpuUsage.ECpuUsageCategory.LOW.code   	* RamUsage.ERamUsageCategory.HIGH.code,
		CpuUsage.ECpuUsageCategory.MEDIUM.code 	* RamUsage.ERamUsageCategory.LOW.code,
		CpuUsage.ECpuUsageCategory.MEDIUM.code 	* RamUsage.ERamUsageCategory.MEDIUM.code,
		CpuUsage.ECpuUsageCategory.HIGH.code 	* RamUsage.ERamUsageCategory.LOW.code,
	};
	
	/**
	 * "Derajat keanggotaan" or Membership degree.
	 */
	public final double membershipDegree;
	
	/**
	 * "Kategory keanggotaan"
	 */
	public final EHostUsageCategory category;
	
	/**
	 * Determine the status of this HostUsage object. <br>
	 * You must use the flag {@link #NOT_OVERLOAD_FLAG_LIST} or {@link #OVERLOAD_FLAG_LIST}
	 * as status code.
	 */
	public final int statusCode;
	
	/**
	 * Encapsulate HostUsage.
	 * @param statusCode You must use the flag {@link HostUsage#NOT_OVERLOAD_FLAG_LIST} or 
	 * {@link HostUsage#OVERLOAD_FLAG_LIST} as status code.
	 * @param membershipDegree 
	 * @param category 
	 */
	public HostUsage(int statusCode, double membershipDegree, EHostUsageCategory category) {
		this.membershipDegree = membershipDegree;
		this.category = category;
		this.statusCode = statusCode;
	}
	
	@Override
	public String toString() {			
		return category + " - code : [" + statusCode + "] membership degree = " + membershipDegree;
	}
}