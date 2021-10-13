package com.example.trackingmypantry.lib.adapters

data class IndexedArray<T>(
    val array: Array<T>,
    val index: Int
)

typealias ArgsCallback<A> = (
    A
) -> Unit

typealias IndexedArrayCallback<T> = (
    IndexedArray<T>
) -> Unit