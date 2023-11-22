package org.cloudbus.cloudsim.power;


import java.util.ArrayList;
import java.util.List;

public class MamdaniEngine {
	
	public static double applyRules(CpuUsage[] cpuUsage, RamUsage[] ramUsage) {		
		if (cpuUsage == null || ramUsage == null) {
			System.err.println("There are null value in cpuUsage[] and ramUsage[]");
		}		
		List<HostUsage> clusterOverload = new ArrayList<>();			
		List<HostUsage> clusterNonOverload = new ArrayList<>();			
		
		//implikasi MIN
                for (int c = 0; c < cpuUsage.length; c++) {
			for (int d = 0; d < ramUsage.length; d++) {
				int code = cpuUsage[c].category.code * ramUsage[d].category.code;
				boolean isHostOverloaded = false;
				for (int e = 0; e < HostUsage.OVERLOAD_FLAG_LIST.length; e++) {						
					if (code == HostUsage.OVERLOAD_FLAG_LIST[e]) 
                                        {
						isHostOverloaded = true;
                                                //System.out.println("===bener===");
					}						
				}					
				double membershipDegree = Math.min(cpuUsage[c].membershipDegree, ramUsage[d].membershipDegree);
				if (isHostOverloaded) {
                                    //System.out.println("===overload ===");
					clusterOverload.add(new HostUsage(code, membershipDegree, HostUsage.EHostUsageCategory.OVERLOAD));
				} else {
                                    
					clusterNonOverload.add(new HostUsage(code, membershipDegree, HostUsage.EHostUsageCategory.NORMAL));
				}
			}				
		}
		/*
                System.out.println("==============Result============");
		info(clusterNonOverload);
		info(clusterOverload);
		System.out.println("==============+++++++============");
		          System.out.println("get max nya");
		System.out.println(getMax(clusterNonOverload));
		System.out.println(getMax(clusterOverload));
                System.out.println("----------------------------------------");
		*/
                if (clusterNonOverload.isEmpty() && clusterOverload.isEmpty()) {
			System.err.println(cpuUsage.length + "<>" + ramUsage.length);
			System.err.println("There are null value in clusterOverload and clusterNonOverload");
		}
                double hasil = defuzzify(getMax(clusterOverload), getMax(clusterNonOverload)); 
               
                return hasil;
	}		
	
	/**
	 * Do deffuzification for the graph(s) resulted from the applying fuzzy rules.
	 * @param hostUsageNormal 
	 * @param hostUsageOverload
	 * @return Crisp value ("Bilangan Tegas")
	 */
	public static double defuzzify(HostUsage hostUsageNormal, HostUsage hostUsageOverload) {	
		if (hostUsageNormal == null && hostUsageOverload == null) {
			System.err.println("There are null value in hostUsageNormal and hostUsageOverload");
		}
		if (hostUsageNormal == null) {
			hostUsageNormal = hostUsageOverload;
		}
		if (hostUsageOverload == null) {
			hostUsageOverload = hostUsageNormal;
		}
		double presisi = 250;
		double minimum = hostUsageNormal.category.getMinbound();
		double maximum = hostUsageOverload.category.getMaxbound();
        double dx = (maximum - minimum) / presisi;	// untuk presisi, higher--better.
        double x, y;
        double area = 0, xcentroid = 0, ycentroid = 0;
        for (int i = 0; i < presisi; ++i) {
            x = minimum + (i + 0.5) * dx;
            // Get appropriate HostUsage graph to calculate "membership degree" 
            // according to the x position
            HostUsage targetGraph = x > hostUsageNormal.category.getMaxbound() ? hostUsageOverload : hostUsageNormal;            		
            y = targetGraph.category.getMembershipDegree(x);
            xcentroid += y * x;
            ycentroid += y * y;
            area += y;
        }
        xcentroid /= area;
        ycentroid /= 2 * area;
        area *= dx; //total area... unused, but for future reference.
        return xcentroid;
    }
	//komposisi aturan menggunakan MAX
	private static HostUsage getMax(List<HostUsage> list) {
		if (list.isEmpty()) {
			return null;
		}		
		int len = list.size();				
		HostUsage max = list.get(0);
		for (int c = 1; c < len; c++) {
			HostUsage other = list.get(c);
			if (max.membershipDegree < other.membershipDegree ) {
				HostUsage temp = max;
				max = other;
				other = temp;
			}
		}			
		return max;
	}
	
	private static void info(List<HostUsage> list) {
		if (list.isEmpty()) {
			return ;
		}		
		System.out.println("List content :");
		int len = list.size();		
		for (int c = 0; c < len; c++) {
			System.out.println(list.get(c));
		}		
	}	
}	