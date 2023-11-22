/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzycmeans;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Nobita-Kun
 */
public class FuzzyCMeans implements FCMInterface{

    /**
     * @param args the command line arguments
     */
    public static double FCMOBJECTIV_VALUE;
    //////////////////////////// SHOW METHOD //////////////////////////////
    // method untuk menampilkan matrix partisinya.
    public void showMatrixPartition (double [][] matrix)
    { 
        int DATA_SIZE = matrix.length;
        for ( int row = 0; row<DATA_SIZE;row++)
        {
            System.out.print("\t"+(row+1)+".)");
            for (int col = 0; col<CLUSTER_SIZE ; col++)
            {
                System.out.print("["+matrix[row][col]+"] ");
            }
            System.out.println("");
        }
        
    }
    
    
    // method untuk menampilkan datanya.
    public void showData (ArrayList<DataSet> theData)
    {
        int DATA_SIZE = theData.size();
        for ( int row = 0; row<DATA_SIZE;row++)
        {
            System.out.print("\t"+(row+1)+".) : ");
            for (int col = 0; col<DATA_ATTR ; col++)
            {
                System.out.print(" ["+theData.get(row).getData(col)+"]");
            }
            System.out.print(" Cluster - "+theData.get(row).getCluster()+" , ");
            System.out.println(" Index VM = "+theData.get(row).getIndexVM()+".");
            
        }
        
    }
    
    public void showDataRAW (ArrayList<DataSet> theData)
    {
        int rsize = theData.size();
        int csize = theData.get(0).getSizeData();
        for ( int row = 0; row<rsize;row++)
        {
            for (int col = 0; col<csize ; col++)
            {
                System.out.print(theData.get(row).getData(col)+" ");
            }
            System.out.println("");
        }
        
    }
    
    public ArrayList<DataSet> groupDataCluster (ArrayList<DataSet> theData, int index)
    {
        ArrayList<DataSet> dataResult = new ArrayList();
        
        for (int i = 0; i<theData.size();i++)
        {
            if (theData.get(i).getCluster() == index)
            {
                dataResult.add(theData.get(i));
                //dataResult.dataResult.add(new DataSet(theData.get(i).getData() ));
                //dataResult.get(i).setCluster(theData.get(i).getCluster());
                //dataResult.get(i).setIndexVM(theData.get(i).getIndexVM());
                
            }
        }
        
        return dataResult;
    }
    
     
    /////////////////////////// PROCESS PERHITUNGAN PADA FCM //////////////////////////////////
    
    public double getRandomRange (int min , int max)
    {
        double hasil = (Math.random() * max + min); 
        
        while (hasil > max || hasil < min)
        {
            hasil = (Math.random() * max + min) % max;
        }
        
        return hasil;
    }
    
    public double getAverageColumn (ArrayList<DataSet> theData , int indexCol)
    {
        
        double sumData = 0;
        double result = 0;
        int rowSize = theData.size();
        
        for (int i=0 ; i<rowSize ; i++)
        {
            sumData +=Double.parseDouble(theData.get(i).getData(indexCol).toString());
        }
        
        if (rowSize == 0)
        {
            result = 0;
        }
        else
        {
            result = sumData / rowSize;
        }
        return result;
    }
   
    // method untuk mencari jarak antara titik data dengan centroid
    // dengan rumus ecludian.
    public double Distance2 (DataSet theData , double [] theCentroid)
    {
        double sumPow = 0; // var temporary untuk menampung hasil pangkatnya
        double range = 0; // var temporary untuk nampung hasil pengurangan
        
        // loop dilakukan sesuai dengan jumlah kolomnya.
        for (int i=0;i<DATA_ATTR;i++)
        {
            // Parsing Double digunakan karena tipe datanya masih objek , sehingga harus di konversi ke double agar dapat dihitung.
            range = Double.parseDouble(theData.getData(i).toString()) - theCentroid[i];
            sumPow = sumPow + Math.pow(range,2); // setelah dikurngai dipangkatkan dengan 2.
        }
        
        return (sumPow); // hasil akhirnya semua dijumlahkan lalu di akar.
    }
    
    // method untuk menghitung matriks partisinya , saat langkah awal / inisiailisasi
    // menggunakan bilangan random
    // per kolomnya aturannya yaitu jika dijumlahkan
    // hasilnya pasti 1.
    public double [][] matrixPartition (int row,int col)
    {
        double [][] res_val = new double[row][col];
        double angka;
        double total = 0;
        
        int baris;
        int kolom;
        Random rnd = new Random(10);
        
        // pembangkitan bilangan random dengan nilai max 1.
        for (baris = 0; baris <row ; baris++)
        {
            for (kolom = 0 ; kolom < col ; kolom++)
            {
                res_val[baris][kolom] = rnd.nextDouble() ;
            }
        }
        
        // proses normalisasi supaya jumlah kolomnya pada matrix partisi
        // jika di total akan bernilai 1.
        for (baris = 0; baris <row ; baris++)
        {
            // perulangan yang ini untuk ngitung totalnya pada setiap kolom
            for (kolom = 0 ; kolom < col ; kolom++)
            {
                total = total + res_val[baris][kolom];
            }
            
            // yang ini untuk proses normalisasinya
            // jadi, setiap sell dibagi dengan jumlah total kolomnya.
            for (kolom = 0 ; kolom < col ; kolom++)
            {
                res_val[baris][kolom] = res_val[baris][kolom] / total;
            }
            total = 0;
        }
        
        
        return res_val;
    }
    
    
        
     
    // method untuk melakukan alokasi kluster pada tiap datanya dengan berdasarkan bobot
    // nilai / derajat keanggotaan dari matriks partisi yang terbesar.
    public void setDataCluster (ArrayList<DataSet> theData , double [][] MatrixPart)
    {
        int max = 0;
        int DATA_SIZE = theData.size();
        // mencari nilai max pada setiap barisnya.
        // index dari max itulah nantinya sebagai index kluster.
        for (int row = 0 ; row<DATA_SIZE ; row++)
        {
            for (int col = 0 ; col<CLUSTER_SIZE ; col++)
            {
                if (MatrixPart[row][max] <= MatrixPart[row][col])
                {
                    max = col;
                }
            }
            // max +1 , karena nilai max nya diawali dari 0 sesuai index array.
            // sehingga harus di + 1 supaya seusuai dengan index klusternya.
            theData.get(row).setCluster(max+1);
            max = 0;
        }
        
        
    }
    
    // method bantuan untuk menghitung centroidnya.
    // segala proses perhitungannya dilakukan pada method ini.
    // sehingga pada method fussionNewmatriksP , pada bagian loopnya
    // hanya menampung nilai hasilnya saja.
    public double getCentroidPoint (double [][]matrPartition , ArrayList<DataSet> theData,int idxMatr,int idxData)
    {
        
        double sumUIK = 0;
        double sumPoint = 0;
        double result = 0;
        int DATA_SIZE = theData.size();
        double UikSqr = 0; 
        
        for (int i = 0; i<DATA_SIZE ; i++)
        {
            UikSqr = Math.pow(matrPartition[i][idxMatr] , WEIGHT);
            sumUIK = sumUIK + UikSqr;
            sumPoint = sumPoint + (UikSqr * Double.parseDouble(theData.get(i).getData(idxData).toString()));
        }
        
        if (sumUIK != 0)
        {
            result = sumPoint / sumUIK;
        }
        return result;
    }
    
   
    
    // method untuk mencari nilai matriks partisi yang baru.
    // method ini lah yang nantinya dilakukan perulangan terus meneurs.
    // hingga kondisi berhenti terpenuhi
    // nilai matriks partisi akan selalu diperbarui pada setiap iterasinya
    // melalui method ini.
    public double [][] FusionNewMatrixP (double [][] MatrixPartition, ArrayList<DataSet> theData)
    {
        int DATA_SIZE = theData.size();
        // variabel untuk hasil akhirnya untuk menampung matriks partisi yang baru.
        double [][] result_new = new double[DATA_SIZE][CLUSTER_SIZE];
        
        // variabel untuk hasil centroidnya.
        double [][] resCentroid = new double[CLUSTER_SIZE][DATA_ATTR];
        
        
        double distanceTemp;
        double sumMatr ;
        
        // proses untuk ngitung centroidnya.
        for (int i = 0 ; i < CLUSTER_SIZE ; i++)
        {
            for (int j=0 ; j< DATA_ATTR ; j++)
            {
                resCentroid[i][j] = getCentroidPoint(MatrixPartition,theData,i,j);
            }
        }
        
        // variabel untuk menampung hasil fungsi objectivenya.
        FCMOBJECTIV_VALUE = 0;
        
       
        for (int i = 0; i<DATA_SIZE ; i++)
        {
             // proses yang ini untuk ngitung fungsi objectivenya
            sumMatr = 0;
            for (int j = 0; j<CLUSTER_SIZE ; j++)
            {
                 distanceTemp = Distance2(theData.get(i),resCentroid[j]);
                 FCMOBJECTIV_VALUE = FCMOBJECTIV_VALUE + (distanceTemp * Math.pow(MatrixPartition[i][j],WEIGHT)) ;
                 
                 if (distanceTemp!=0)
                 {
                     result_new[i][j] = (1/distanceTemp);
                     sumMatr = sumMatr + result_new[i][j];
                 }
                  
            }
            
            // proses yang ini untuk perhitungan matriks partisi yang baru.
            // sehingga nanti hasilnya dimasukan ke dalam tabel array result_new
            for (int k = 0; k<CLUSTER_SIZE ; k++)
            {
                if (sumMatr!=0)
                {
                    result_new[i][k] = result_new[i][k] / sumMatr;
                }
            }
        }
        
        
        return result_new;
    }
//    
//    public static void main(String[] args) 
//    {
//        FuzzyCMeans fcm = new FuzzyCMeans();
//        
//        ArrayList<DataSet> dataVM = new ArrayList<>();
//    
//        ArrayList<DataSet> DataKluster1 = new ArrayList();
//        ArrayList<DataSet> DataKluster2 = new ArrayList();
//        ArrayList<DataSet> DataKluster3 = new ArrayList();
//        
//        double [][] MATRIX_PARTITION ;
//        double [] datanya = new double[DATA_ATTR]; 
//        int ITERASI = 0;
//        double [] OBJECTIVE_VALUE = new double[MAX_ITER+1];
//        double selisihObjecP = 0;
//        
//        datanya[0] = 12; datanya[1] = 7; datanya[2] = 9;
//        dataVM.add(new DataSet(datanya , 1));
//        
//        datanya[0] = 8; datanya[1] = 11; datanya[2] = 5;
//        dataVM.add(new DataSet(datanya , 2));
//        
//        datanya[0] = 8; datanya[1] = 11; datanya[2] = 4;
//        dataVM.add(new DataSet(datanya , 3));
//        
//        datanya[0] = 12; datanya[1] = 7; datanya[2] = 9;
//        dataVM.add(new DataSet(datanya , 4));
//        
//        datanya[0] = 12; datanya[1] = 7; datanya[2] = 9;
//        dataVM.add(new DataSet(datanya , 5));
//        
//        MATRIX_PARTITION = fcm.matrixPartition(dataVM.size(), FCMInterface.CLUSTER_SIZE);
//       
//        
//        
//       System.out.println("========== Data Awalnya ========== = ");
//       fcm.setDataCluster(dataVM, MATRIX_PARTITION); // ngeset data sesuai kluster berdasarkan derajat keanggotaan di matrix partisi.
//       fcm.showData(dataVM); // buat nampilin datanya aja :D.
//       
//        // hanya menampilkan matrix partisinya saja.
//        System.out.println("========== Matrix Partisi AWAL ==========");
//        fcm.showMatrixPartition(MATRIX_PARTITION);
//        
//        //****************************** AREA PROSES FCM-NYA **********************************//
//                            // proses fuzzy c means strart here.
//                            do
//                            {
//                                // variabel iterasi untuk mencacah , prosesnya saat ini sampai pada iterasi ke berapa?
//                                ITERASI++; // iterasi nambah 1.
//
//                                System.out.println("\n******************** ITERASI KE "+ITERASI+"********************\n");
//                                // matriks partisi yang lama direplace dengan matriks partisi yang baru.
//                                // dengan method FusonNewMatrixP.
//                                // inputannya adalah datasetnya dengan matriks partisi yang lama.
//                                MATRIX_PARTITION = fcm.FusionNewMatrixP(MATRIX_PARTITION, dataVM);
//                                fcm.showMatrixPartition(MATRIX_PARTITION);
//
//                                // fungsi objektiv yang sudah dihitung ditampung ke dalam array
//                                // gunanya supaya bisa dibandingkan nilai iterase ke N dengan iterasi ke N-1
//                                OBJECTIVE_VALUE[ITERASI] = FuzzyCMeans.FCMOBJECTIV_VALUE;
//
//                                // nah selisish dari nilai objective functionnya di tampung dalam variabel
//                                // selisihObecjP.
//                                selisihObjecP =   Math.abs(OBJECTIVE_VALUE[ITERASI] - OBJECTIVE_VALUE[ITERASI-1]);  
//
//                                // hanya untuk menampilkan nilai objectivenya.
//                                System.out.println("\n\t********** P["+ITERASI+"] - P["+(ITERASI-1)+"] >>> ["+OBJECTIVE_VALUE[ITERASI]+"] - ["+OBJECTIVE_VALUE[ITERASI-1]+"] = "+selisihObjecP+" **********");
//                            }
//                            while (ITERASI < MAX_ITER && ( selisihObjecP > ERR));
//                            // proses fuzzy c means end here.
//                            
//                            
//                              // ini hasil akhir dari Fuzzy C meansnya.
//                             System.out.println("========== Data Akhirnya ==========");
//                            // pada akhirnya datasetnya dilakukan proses kluster melalui
//                            // matriks partisi yang sudah didapatkan pada proses akhirnya.
//                            // menggunakan method setDataCluster.
//                            fcm.setDataCluster(dataVM, MATRIX_PARTITION);
//
//                            // hanya menampilkan saja.
//                            fcm.showData(dataVM);
//
//                            DataKluster1 = fcm.groupDataCluster(dataVM,1);
//                            DataKluster2 = fcm.groupDataCluster(dataVM,2);
//                            DataKluster3 = fcm.groupDataCluster(dataVM,3);
//                            
//                            System.out.println("matriks partisi yg baru");
//                            fcm.showMatrixPartition(MATRIX_PARTITION);
//    }
//  
}
