package com.company;

import java.util.ArrayList;

public class leaderboards {



    public static void mergeSort(ArrayList<Integer> array){
        int midPoint = (array.size() / 2);
        ArrayList<Integer> leftHalfList = new ArrayList<>();
        ArrayList<Integer> rightHalfList = new ArrayList<>();
        if (array.size() < 2) {
            return;
        }
        for (int i = 0; i < midPoint; i++) {
            leftHalfList.add(array.get(i));
        }
        for (int i = midPoint; i < array.size(); i++) {
            rightHalfList.add(array.get(i));
        }
        mergeSort(leftHalfList);
        mergeSort(rightHalfList);
        int leftPos = 0;
        int rightPos = 0;
        int sortedPos = 0;
        while (leftPos < leftHalfList.size() && rightPos < rightHalfList.size()){
            if (leftHalfList.get(leftPos) <= rightHalfList.get(rightPos)) {
                array.set(sortedPos, leftHalfList.get(leftPos));
                leftPos++;
            }else{
                array.set(sortedPos, rightHalfList.get(rightPos));
                rightPos++;
            }
            sortedPos++;
        }
        while (leftPos < leftHalfList.size()){
            array.set(sortedPos, leftHalfList.get(leftPos));
            leftPos++;
            sortedPos++;
        }
        while (rightPos < rightHalfList.size()){
            array.set(sortedPos, rightHalfList.get(rightPos));
            rightPos++;
            sortedPos++;
        }
    }




}
