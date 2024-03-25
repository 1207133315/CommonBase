package com.liningkang.utils

/**
 * @Author ；Ningkang.Li
 * @Time ：2023/4/20日 16点
 * @Description ；---
 */
fun <E> MutableList<E>.addElement(element: E) {
    synchronized(this) {
        this.add(element)
    }
}
fun <E> MutableList<E>.setElement(index:Int,element: E) {
    synchronized(this) {
        this.set(index,element)
    }
}

fun <E> MutableList<E>.removeElement(element: E) {
    synchronized(this) {
        this.remove(element)
    }
}

fun <E> MutableList<E>.removeElement(index: Int) {
    synchronized(this) {
        this.removeAt(index)
    }
}

fun <E> MutableList<E>.getElement(index: Int):E {
    synchronized(this) {
       return this[index]
    }
}