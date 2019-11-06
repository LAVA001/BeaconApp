package com.example.beaconapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import org.altbeacon.beacon.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(), BeaconConsumer {
    private val beaconManager: BeaconManager by lazy { BeaconManager.getInstanceForApplication(this) }
    private var count = 0
    private var beaconDatas: MutableList<BeaconData> = mutableListOf()        //javaで言うArrayList

    companion object{
        const val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"      //javaで言うstatic変数
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //beaconManagerの初期化は上のby lazyのおかげで初めてgetter呼ばれたときに設定される
        //kotlinではgetterアクセスを変数アクセスのように書けるらしい あとnullableなので!!をつける
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))
    }

    override fun onResume() {
        super.onResume()
        beaconManager.bind(this)      //開始
    }

    override fun onPause() {
        super.onPause()
        beaconManager.unbind(this)        //終了
    }

    override fun onBeaconServiceConnect() {
        val mRegion = Region("iBeacon", null, null, null)
        beaconManager.addMonitorNotifier(object: MonitorNotifier {
            override fun didEnterRegion(region: Region?) {
                //領域への入場検知
                Log.d("iBeacon", "Enter region")
            }

            override fun didExitRegion(region: Region?) {
                //領域からの退場検知
                Log.d("iBeacon", "Exit Region")
            }

            override fun didDetermineStateForRegion(i: Int, region: Region?) {
                //領域の入退場のステータス変化
                Log.d("MyActivity", "Determine State $i")
            }
        })

        try {
            //Beacon情報の監視を開始
            beaconManager.startMonitoringBeaconsInRegion(mRegion)
        } catch (e: RemoteException){
            e.printStackTrace()
        }

        //kotlinでは無名クラスの実装すべきメソッドが一つの時はラムダ式を使ってより簡潔に書ける あと使わない引数は_で受け取る
        beaconManager.addRangeNotifier{ beacons: MutableCollection<Beacon>?, _: Region? ->
            beaconDatas = mutableListOf()       //サンプルのやつデータクラスあんのになんで個別に定義した？
            beacons?.forEach { beacon ->
                Log.d("My Activity", "UUID;${beacon.id1}, major:${beacon.id2}, minor:${beacon.id3}"
                        + "RSSI: ${beacon.rssi}, TxPower: ${beacon.distance}")

                beaconDatas.add(BeaconData(beacon.id1, beacon.id2, beacon.id3, beacon.rssi, beacon.txPower, beacon.distance))
            }
            count = beaconDatas.size    //countは0から増えていくわけじゃないから上のcount=0は冗長
            Log.d("Activity", "total:" + count + "台")
        }
        try {
            beaconManager.startRangingBeaconsInRegion(mRegion)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun onUpdateButtonClick(view: View){
        val textView: TextView = this.result    //kotlin の findViewById を使わない書き方
        textView.text = "$count"     //kotlin では(返り値なしの？)setter呼び出しを代入文風に書けるらしい

        //サンプルの一番最後 この辺かなり適当
        val beaconList: ListView = this.beacons     //これもfindViewByIdを使わない書き方
        val beaconAdapter = ArrayAdapter<BeaconData>(this, android.R.layout.simple_list_item_1, beaconDatas)
        beaconList.adapter = beaconAdapter     //これもsetter呼び出しを代入文風に書くやつ

        Log.d("ButtonPush", "BeaconManager: $beaconManager")
    }

}
