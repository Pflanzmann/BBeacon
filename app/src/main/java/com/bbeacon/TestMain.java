package com.bbeacon;

import android.os.Build;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.RequiresApi;

public class TestMain {

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void main(String[] args) {

        List<Integer> liste = new ArrayList<>();
        liste.add(2);
        liste.add(0);
        liste.add(9);
        liste.add(6);
        liste.add(5);

                liste.sort((o1, o2) -> {
            if (o1 > o2)
                return 1;
            else
                return -1;
        });

        Iterator<Integer> iterator = liste.iterator();

        while(iterator.hasNext())
        {
            //System.out.println(itr.next());
            System.out.println(iterator.next());
        }
    }

}
