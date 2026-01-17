package com.gadgeski.divchroma

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * DivChromaApplication
 * * Hiltの依存関係注入コンテナを生成するための基底クラスです。
 * Manifestの <application android:name="..."> に登録することで有効化されます。
 */
@HiltAndroidApp
class DivChromaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 将来的にTimberなどのログライブラリや
        // クラッシュレポートツールの初期化をここで行います。
    }
}