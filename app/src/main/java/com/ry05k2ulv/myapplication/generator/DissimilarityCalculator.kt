package com.ry05k2ulv.myapplication.generator

import android.graphics.Bitmap

interface DissimilarityCalculator{

    // return the list which contains the position of grid should be replaced
    fun calc(material: Bitmap): List<Pair<Int, Int>>
}