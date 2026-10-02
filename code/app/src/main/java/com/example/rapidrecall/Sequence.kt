package com.example.rapidrecall

import kotlin.math.pow
import kotlin.random.Random

class Sequence (val numDigits: Double) {

    private val generatedInt = 55555
    /*
    private val generatedInt = Random.nextInt(
        from = 0+(10.0.pow(numDigits - 1)).toInt(), until = (10.0.pow(numDigits)).toInt())
    */
    val newSequence : String
        get() = generatedInt.toString()

}