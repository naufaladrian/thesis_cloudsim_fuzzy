/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzycmeans;

import java.util.ArrayList;

// NB : kelas untuk DataSet.
// pada kelas ini merepresentasikan untuk tiap 1 Baris = 1 kelas
// 1 baris ini terdiri dari beberapa kolom.
// kolom tersebutlah yang disimpan dalam ArrayList SomeData.
// untuk jumlah barisnya sudah di definisikan pada kelas FCMInterface.
public class DataSet 
{
    private int indexVM;
    ArrayList someData = new ArrayList(); // untuk menyimpan nilainya pada kolom
    private int indexCluster; // index klusternya.
    
    // konstruktor untuk memasukan datanya ke dalam ArrayList.
    // di sini value.length adalah ukuran kolomnya , bukan barisnya.
    public DataSet (double [] value , int idx)
    {
        for (int i=0;i<value.length;i++) 
        {
            someData.add(value[i]);
        }
        indexVM = idx;
    }
    
    public void setIndexVM (int idx)
    {
        indexVM = idx;
    }
    
    public int getIndexVM ()
    {
        return indexVM;
    }
    public DataSet(ArrayList data)
    {
        someData = data;
    }
    
    public void setData (ArrayList data)
    {
        someData = data;
    }
    
    public ArrayList getData ()
    {
        return someData;
    }
    
    // ukuran dari kolomnya. , bukan barisnya lho.
    public int getSizeData()
    {
        return someData.size();
    }
    
    // cuman ngambil datanya aja berdasarkan index.
    public Object getData (int index)
    {
        return someData.get(index);
    }
    
    // untuk mendaptkan data ini masuk ke kluster ke berapa?.
    public int getCluster ()
    {
        return indexCluster;
    }
    
    // untuk set klusternya berdasarkan nilai minimum jarak distancenya.
    public void setCluster (int val)
    {
        indexCluster = val;
    }  
    
    public void showData ()
    {
        
    }
}
