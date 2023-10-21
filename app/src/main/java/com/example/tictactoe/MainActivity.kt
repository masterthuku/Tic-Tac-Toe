package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

var singleUser = false
class MainActivity : AppCompatActivity() {

    lateinit var singlePlayerBtn:Button
    lateinit var multiPlayerBtn:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        singlePlayerBtn = findViewById(R.id.btnSinglePlayer)
        multiPlayerBtn = findViewById(R.id.btnMultiPlayer)

        singlePlayerBtn.setOnClickListener {
            singleUser = true
            startActivity(Intent(this,GamePlayActivity::class.java))
        }
        multiPlayerBtn.setOnClickListener {
            singleUser = false
            startActivity(Intent(this,MultiPlayerSelection::class.java))
        }
    }
}