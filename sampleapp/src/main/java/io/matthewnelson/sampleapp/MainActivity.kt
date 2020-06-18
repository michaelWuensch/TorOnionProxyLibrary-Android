package io.matthewnelson.sampleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import io.matthewnelson.topl_android_service.TorServiceController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonStart = findViewById<Button>(R.id.button_start)
        val buttonStop = findViewById<Button>(R.id.button_stop)
        val buttonRestart = findViewById<Button>(R.id.button_restart)
        val buttonNewId = findViewById<Button>(R.id.button_new_identity)
        TorServiceController.startTor()

        buttonStart.setOnClickListener {
            TorServiceController.startTor()
        }
        buttonStop.setOnClickListener {
            TorServiceController.stopTor()
        }
        buttonRestart.setOnClickListener {
            TorServiceController.restartTor()
        }
        buttonNewId.setOnClickListener {
            TorServiceController.newIdentity()
        }
    }
}
