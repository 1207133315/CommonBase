package com.liningkang.utils

import kotlin.math.roundToInt

object UnitConverterUtils {
    fun kgToLbs(kg: Int): Int {
        val lbsConversionFactor = 2.20462
        val lbs = kg * lbsConversionFactor
        return lbs.roundToInt()
    }

    fun lbsToKg(lbs: Int): Int {
        val kgConversionFactor = 0.453592
        val kg = lbs * kgConversionFactor
        return kg.roundToInt()
    }

}