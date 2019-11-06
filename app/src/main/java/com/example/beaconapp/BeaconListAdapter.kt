package com.example.beaconapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.beacon_list.* //なぜか使えなかった




class BeaconListAdapter(private val content: Context, private val resource: Int, private val beacons: MutableList<BeaconData>)
    : ArrayAdapter<BeaconData>(content, resource, beacons){

    private val inflater = content.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //kotlinにはエルビス演算子というらしいnullチェックがある javaではごつくなってたからうれしい
        val view: View = convertView ?: inflater.inflate(resource, null)

        //表示するbeaconデータの取得
        val beaconData: BeaconData = beacons[position]

        //各要素を設定 findViewById的のせいで同じようなこと6回書くのを回避する方法がわからなかった上にkotlin拡張のfindViewById回避にエラー吐かれた

        val uuid = view.findViewById(R.id.uuid) as TextView
        uuid.text = beaconData.uuid.toString()

        val major = view.findViewById(R.id.major) as TextView
        major.text = beaconData.major.toString()

        val minor = view.findViewById(R.id.minor) as TextView
        minor.text = beaconData.minor.toString()

        val rssi = view.findViewById(R.id.rssi) as TextView
        rssi.text = beaconData.rssi.toString()

        val txPower = view.findViewById(R.id.txPower) as TextView
        txPower.text = beaconData.txPower.toString()

        val distance = view.findViewById(R.id.distance) as TextView
        distance.text = beaconData.distance.toString()

        return view
    }

}