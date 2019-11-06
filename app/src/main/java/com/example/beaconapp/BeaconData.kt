package com.example.beaconapp

import org.altbeacon.beacon.Identifier

//javaでgetterとsetterを列挙するだけのデータクラスはkotlinではdata classと定義するだけで作れる
data class BeaconData(
    val uuid:Identifier,
    val major: Identifier,
    val minor: Identifier,
    val rssi: Int,
    val txPower: Int,
    val distance: Double
)