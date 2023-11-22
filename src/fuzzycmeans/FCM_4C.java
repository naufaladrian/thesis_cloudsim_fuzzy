/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzycmeans;

import static fuzzycmeans.FCMInterface.DATA_ATTR;
import static fuzzycmeans.FCMInterface.ERR;
import static fuzzycmeans.FCMInterface.MAX_ITER;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;

/**
 *
 * @author Nobita-Kun
 */
public class FCM_4C extends  PowerVmSelectionPolicy{
   
    Set<Integer> setHost = new HashSet<Integer>();
    ArrayList<DataSet> dataVM = new ArrayList<>();
    
    ArrayList<DataSet> DataKluster1 = new ArrayList();
    ArrayList<DataSet> DataKluster2 = new ArrayList();
    ArrayList<DataSet> DataKluster3 = new ArrayList();
    ArrayList<DataSet> DataKluster4 = new ArrayList();
  
   
    double [][] MATRIX_PARTITION ;
    double [] datanya = new double[DATA_ATTR]; 
    int ITERASI = 0;
    double [] OBJECTIVE_VALUE = new double[MAX_ITER+1];
    double selisihObjecP = 0;
    int prevID = -10;
    int currentCluster = 1;
    int moveVMID;
    boolean alreadygetVM ;
    FuzzyCMeans fcm = new FuzzyCMeans();
    
    public void showAllSet ()
    {
        System.out.println(" ================ SHOW ================");
        for (Integer loopSet : setHost)
        {
            System.out.println("Host id = "+loopSet);
        }
        System.out.println("ukuran Set = "+setHost.size());
    }
    
    
    public void resetAll ()
    {
       
        dataVM.clear();
        
        DataKluster1.clear();
        DataKluster2.clear();
        DataKluster3.clear();
        DataKluster4.clear();
       
        
        ITERASI = 0;
        OBJECTIVE_VALUE = new double[MAX_ITER +1];
        selisihObjecP = 0;
        currentCluster = 1;
        moveVMID = -1;
        alreadygetVM = false;
    }
    
    public void showEachCLuster ()
    {
        System.out.println("----- DATA MASING-MASING KLUSTER -----");
        if (!DataKluster1.isEmpty())
        {
            System.out.println("===== DATA PADA KLUSTER 1 =====");
            fcm.showData(DataKluster1);
            System.out.println("\tRata2 Kluter 1 = ["+fcm.getAverageColumn(DataKluster1,0)+"] , ["+fcm.getAverageColumn(DataKluster1,1)+"]");
        }

        if (!DataKluster2.isEmpty())
        {
            System.out.println("===== DATA PADA KLUSTER 2 =====");
            fcm.showData(DataKluster2);
            System.out.println("\tRata2 Kluter 2 = ["+fcm.getAverageColumn(DataKluster2,0)+"] , ["+fcm.getAverageColumn(DataKluster2,1)+"]");     
        }

        if (!DataKluster3.isEmpty())
        {
            System.out.println("===== DATA PADA KLUSTER 3 =====");
            fcm.showData(DataKluster3);
            System.out.println("\tRata2 Kluter 3 = ["+fcm.getAverageColumn(DataKluster3,0)+"] , ["+fcm.getAverageColumn(DataKluster3,1)+"]");
        }  
        
        if (!DataKluster4.isEmpty())
        {
            System.out.println("===== DATA PADA KLUSTER 4 =====");
            fcm.showData(DataKluster4);
            System.out.println("\tRata2 Kluter 4 = ["+fcm.getAverageColumn(DataKluster4,0)+"] , ["+fcm.getAverageColumn(DataKluster4,1)+"]");
        }  
      
    }
    
    public void clusterSelection ()
    {
        while (!alreadygetVM)
        {
            if (currentCluster==1 && !DataKluster1.isEmpty())
            {
                if (FCMInterface.SHOW_DEBUG)
                {
                    System.out.println("\n > VM dengan ID = "+DataKluster1.get(0).getIndexVM()+" , pada Kluster 1 telah dipindahkan");
                }
                
                moveVMID = DataKluster1.get(0).getIndexVM();
                DataKluster1.remove(0);
                alreadygetVM = true;
            }
            else if (currentCluster==2 && !DataKluster2.isEmpty())
            {
                if (FCMInterface.SHOW_DEBUG)
                {
                    System.out.println("\n > VM dengan ID = "+DataKluster2.get(0).getIndexVM()+" , pada Kluster 2 telah dipindahkan");
                }
                
                moveVMID = DataKluster2.get(0).getIndexVM();
                DataKluster2.remove(0);
                alreadygetVM = true;
            }
            else if (currentCluster==3 && !DataKluster3.isEmpty())
            {
                if (FCMInterface.SHOW_DEBUG)
                {
                    System.out.println("\n > VM dengan ID = "+DataKluster3.get(0).getIndexVM()+" , pada Kluster 3 telah dipindahkan");
                }
                
                moveVMID = DataKluster3.get(0).getIndexVM();
                DataKluster3.remove(0);
                alreadygetVM = true;
            }
            else if (currentCluster==4 && !DataKluster4.isEmpty())
            {
                if (FCMInterface.SHOW_DEBUG)
                {
                    System.out.println("\n > VM dengan ID = "+DataKluster4.get(0).getIndexVM()+" , pada Kluster 4 telah dipindahkan");
                }
                
                moveVMID = DataKluster4.get(0).getIndexVM();
                DataKluster4.remove(0);
                alreadygetVM = true;
            }
         
            
            currentCluster = (currentCluster + 1) % 5;
            if (currentCluster==0)
            {
                currentCluster++;
            }
        }
    }
    
    @Override
    public Vm getVmToMigrate(PowerHost host) 
    {
		
                List<PowerVm> migratableVms = getMigratableVms(host);
                Vm vmToMigrate = null;
               
                
                if (migratableVms.isEmpty()) 
                {
	           return null;
		}
                
                if (FCMInterface.SHOW_DEBUG)
                {
                    System.out.println("\n\n>>>>>>>>>>>>>>>>>>>>>>>> host id = "+host.getId()+" , ukuran vm = "+migratableVms.size());
                
                }
                    
                // kondisi ini jika host sudah pernah di kluster
                    if (setHost.contains(host.getId()))
                    {
                        alreadygetVM = false;
                        if (FCMInterface.SHOW_DEBUG)
                        {
                            System.out.println("Host masih overload ... !!");
                            showEachCLuster();
                        }
                        
                        // proses pemilihan VM nya di sini
                        clusterSelection();
                        for (Vm vm : migratableVms) 
                        {
                            
                                if (vm.isInMigration()) 
                                {
                                        continue;
                                }
                               
                                if (vm.getId() == moveVMID)
                                {
                                    vmToMigrate = vm;
                                    break;
                                }
                        }    
                        
                        
                    }
                    else // yang ini kalo di dalam host itu belum pernah dilakukan kluster
                    {
                        setHost.remove(prevID);
                        
                        if (FCMInterface.SHOW_DEBUG)
                        {
                            System.out.println("Host baru");
                        }
                        
                        
                        setHost.add(host.getId());
                        prevID = host.getId();
                        resetAll();
                        
                        for (Vm vm : migratableVms) 
                        {
                            
                                if (vm.isInMigration()) 
                                {
                                        continue;
                                }
                                
                                // alokasi datanya
                                datanya[0] = vm.getRam(); datanya[1] = vm.getMips();
                                dataVM.add(new DataSet(datanya , vm.getId()));       
                        }
                        
                            MATRIX_PARTITION = fcm.matrixPartition(dataVM.size(), FCMInterface.CLUSTER_SIZE);
             
               
                            // tampilkan data awalnya sblm diubek2.
                           if (FCMInterface.SHOW_DEBUG)
                           {
                               System.out.println("========== Data Awalnya ========== = ");
                           }
                           
                           
                           fcm.setDataCluster(dataVM, MATRIX_PARTITION); // ngeset data sesuai kluster berdasarkan derajat keanggotaan di matrix partisi.
                           
                           if (FCMInterface.SHOW_DEBUG)
                           {
                               fcm.showData(dataVM); // buat nampilin datanya aja :D.
                           }
                           


                           // hanya menampilkan matrix partisinya saja.
                           if (FCMInterface.SHOW_DEBUG)
                           {
                                System.out.println("========== Matrix Partisi AWAL ==========");
                                fcm.showMatrixPartition(MATRIX_PARTITION);
                           }
                          
                           
                          

//****************************** AREA PROSES FCM-NYA **********************************//
                            // proses fuzzy c means strart here.
                            do
                            {
                                // variabel iterasi untuk mencacah , prosesnya saat ini sampai pada iterasi ke berapa?
                                ITERASI++; // iterasi nambah 1.
                                
                                if (FCMInterface.SHOW_DEBUG)
                                {
                                    System.out.println("\n******************** ITERASI KE "+ITERASI+"********************\n");
                                }
                                
                                // matriks partisi yang lama direplace dengan matriks partisi yang baru.
                                // dengan method FusonNewMatrixP.
                                // inputannya adalah datasetnya dengan matriks partisi yang lama.
                                MATRIX_PARTITION = fcm.FusionNewMatrixP(MATRIX_PARTITION, dataVM);
                                
                                if (FCMInterface.SHOW_DEBUG)
                                {
                                    fcm.showMatrixPartition(MATRIX_PARTITION);
                                }
                                

                                // fungsi objektiv yang sudah dihitung ditampung ke dalam array
                                // gunanya supaya bisa dibandingkan nilai iterase ke N dengan iterasi ke N-1
                                OBJECTIVE_VALUE[ITERASI] = FuzzyCMeans.FCMOBJECTIV_VALUE;

                                // nah selisish dari nilai objective functionnya di tampung dalam variabel
                                // selisihObecjP.
                                selisihObjecP =   Math.abs(OBJECTIVE_VALUE[ITERASI] - OBJECTIVE_VALUE[ITERASI-1]);  

                                // hanya untuk menampilkan nilai objectivenya.
                                
                                if (FCMInterface.SHOW_DEBUG)
                                {
                                    System.out.println("\n\t********** P["+ITERASI+"] - P["+(ITERASI-1)+"] >>> ["+OBJECTIVE_VALUE[ITERASI]+"] - ["+OBJECTIVE_VALUE[ITERASI-1]+"] = "+selisihObjecP+" **********");
                                }
                                
                            }
                            while (ITERASI < MAX_ITER && ( selisihObjecP > ERR));
                            // proses fuzzy c means end here.


                            // ini hasil akhir dari Fuzzy C meansnya.
                            if (FCMInterface.SHOW_DEBUG)
                            {
                                System.out.println("========== Data Akhirnya ==========");
                            }
                            
                            // pada akhirnya datasetnya dilakukan proses kluster melalui
                            // matriks partisi yang sudah didapatkan pada proses akhirnya.
                            // menggunakan method setDataCluster.
                            fcm.setDataCluster(dataVM, MATRIX_PARTITION);

                            // hanya menampilkan saja.
                            if (FCMInterface.SHOW_DEBUG)
                            {
                                fcm.showData(dataVM);
                            }
                            

                            DataKluster1 = fcm.groupDataCluster(dataVM,1);
                            DataKluster2 = fcm.groupDataCluster(dataVM,2);
                            DataKluster3 = fcm.groupDataCluster(dataVM,3);
                            DataKluster4 = fcm.groupDataCluster(dataVM,4);
                           
                             if (FCMInterface.SHOW_DEBUG)
                             {
                                  System.out.println("matriks partisi yg baru");
                                  fcm.showMatrixPartition(MATRIX_PARTITION);
                                  showEachCLuster();
                             }
                        
                        // proses pemilihan VM nya disini
                        clusterSelection();
                             
                             for (Vm vm : migratableVms) 
                             {

                                    if (vm.isInMigration()) 
                                    {
                                            continue;
                                    }

                                    if (vm.getId() == moveVMID)
                                    {
                                        vmToMigrate = vm;
                                        break;
                                    }
                            }
                            
                             
                           
                            
                            
                    }

		return vmToMigrate;
	}
}
