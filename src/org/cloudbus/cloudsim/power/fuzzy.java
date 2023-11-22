/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.power;

import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;

/**
 * The Static Threshold (THR) VM allocation policy.
 * 
 * If you are using any algorithms, policies or workload included in the power package, please cite
 * the following paper:
 * 
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 * 
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 3.0
 */
public class fuzzy extends PowerVmAllocationPolicyMigrationAbstract {

	/** The utilization threshold. */
	private double utilizationThreshold = 0.9;
        private PowerVmAllocationPolicyMigrationAbstract fallbackVmAllocationPolicy;                
      
	/**
	 * Instantiates a new power vm allocation policy migration mad.
	 * 
	 * @param hostList the host list
	 * @param vmSelectionPolicy the vm selection policy
	 * @param utilizationThreshold the utilization threshold
	 */
	public fuzzy(
			List<? extends Host> hostList,
			PowerVmSelectionPolicy vmSelectionPolicy,
			double utilizationThreshold,
                        PowerVmAllocationPolicyMigrationAbstract fallbackVmAllocationPolicy) 
        {
		super(hostList, vmSelectionPolicy);
                this.fallbackVmAllocationPolicy = fallbackVmAllocationPolicy;
		setUtilizationThreshold(utilizationThreshold);
	}

	/**
	 * Checks if is host over utilized.
	 * 
	 * @param _host the _host
	 * @return true, if is host over utilized
	 */
	@Override
	protected boolean isHostOverUtilized(PowerHost host) 
        {
               // System.out.println("ID host = "+host.getId());
		addHistoryEntry(host, getUtilizationThreshold());
		double totalRequestedMips = 0;
		double totalRequestedRam = 0;
                
		for (Vm vm : host.getVmList()) {
                       
			totalRequestedMips += vm.getCurrentRequestedTotalMips();
			totalRequestedRam += vm.getCurrentRequestedRam();
                      
		}
                
               // System.out.println("RAM = "+totalRequestedRam+" ,CPU = "+totalRequestedMips);
       
		CpuUsage[] cpuUsages = CpuUsage.doFuzzification(totalRequestedMips);
		RamUsage[] ramUsage = RamUsage.doFuzzification(totalRequestedRam);
		if (cpuUsages == null || ramUsage == null || cpuUsages.length == 0 || ramUsage.length == 0 ) 
                {
                    return this.fallbackVmAllocationPolicy.isHostOverUtilized(host);                   
		}
                //System.out.println("id = "+host.getId()+" ukuran = "+cpuUsages.length+", ram = "+ramUsage.length);
		double utilization = MamdaniEngine.applyRules(cpuUsages, ramUsage);
		utilization /= 100;		
		//System.out.println("host ke - "+host.getId()+", hasil = "+(utilization > getUtilizationThreshold())+" utiliz = "+utilization);
                return utilization > getUtilizationThreshold();
	}

	/**
	 * Sets the utilization threshold.
	 * 
	 * @param utilizationThreshold the new utilization threshold
	 */
	protected void setUtilizationThreshold(double utilizationThreshold) {
		this.utilizationThreshold = utilizationThreshold;
	}

	/**
	 * Gets the utilization threshold.
	 * 
	 * @return the utilization threshold
	 */
	protected double getUtilizationThreshold() {
		return utilizationThreshold;
	}	
        
        public PowerVmAllocationPolicyMigrationAbstract getFallbackVmAllocationPolicy() {
            return this.fallbackVmAllocationPolicy;
        }
        
        public void setFallbackVmAllocationPolicy(PowerVmAllocationPolicyMigrationAbstract fallbackVmAllocationPolicy) {
            this.fallbackVmAllocationPolicy = fallbackVmAllocationPolicy;
        }
	
}
