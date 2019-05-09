package com.cwgis;

import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;

public class testInsertPointCopy {
    public static void main()
    {
        Byte[] testSide=new Byte[]{30,31};
        int testLastIndex = testSide.length;
        Byte tierLeft=15;
        int firstGreater= Arrays.binarySearch(testSide,tierLeft);
        System.out.println("testLastIndex="+testLastIndex);
        System.out.println("firstGreater="+firstGreater);
        int insertionPoint = Math.abs(firstGreater);
        if (insertionPoint >= testLastIndex) {
            // Not present in array, and none greater than this value
            System.out.println("InsertPoint>=lastIndex");
        }
        HashSet<Byte> higherTiers = Sets.newHashSet(Arrays.copyOfRange(
                testSide,
                insertionPoint,
                testLastIndex));
        System.out.print("result=");
        for(Byte t :higherTiers)
        {
            System.out.print(t+" ");
        }
    }
}
