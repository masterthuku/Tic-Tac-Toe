package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.TextSnapshot
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.dynamic.IFragmentWrapper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay

var isCodeMaker = true;
var code = "null"
var codeFound = false
var checkTemp = true
var keyValue : String = "null"

class OnlineCodeGeneratorActivity : AppCompatActivity() {

    lateinit var codeEdt : EditText
    lateinit var createCodeBtn : Button
    lateinit var headTv : TextView
    lateinit var joinCodeBtn : Button
    lateinit var loadingPb : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_code_generator)

        codeEdt  = findViewById(R.id.EdtCode)
        headTv = findViewById(R.id.HeadTv)
        createCodeBtn = findViewById(R.id.BtnCreate)
        joinCodeBtn = findViewById(R.id.BtnJoin)
        loadingPb = findViewById(R.id.PbLoading)

        createCodeBtn.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = codeEdt.text.toString()
            createCodeBtn.visibility = View.GONE
            joinCodeBtn.visibility = View.GONE
            headTv.visibility = View.GONE
            codeEdt.visibility = View.GONE
            loadingPb.visibility = View.VISIBLE

            if (code!="null" && code!=""){
                isCodeMaker = true
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var check = isValueAvailable(snapshot, code)
                        Handler().postDelayed({
                            if (check==true){
                                createCodeBtn.visibility = View.VISIBLE
                                codeEdt.visibility = View.VISIBLE
                                joinCodeBtn.visibility = View.VISIBLE
                                headTv.visibility = View.VISIBLE
                                loadingPb.visibility = View.GONE
                            }else{
                                FirebaseDatabase.getInstance().reference.child("codes").push().setValue(code)
                                isValueAvailable(snapshot, code)
                                checkTemp = false
                                Handler().postDelayed({
                                    accepted()
                                    Toast.makeText(this@OnlineCodeGeneratorActivity,"Please don't go back", Toast.LENGTH_SHORT).show()
                                },300)
                            }

                        },2000)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }else{
                createCodeBtn.visibility = View.VISIBLE
                joinCodeBtn.visibility = View.VISIBLE
                createCodeBtn.visibility = View.VISIBLE
                headTv.visibility = View.VISIBLE
                loadingPb.visibility = View.GONE

                Toast.makeText(this,"Please enter a valid code",Toast.LENGTH_SHORT).show()
            }
        }

        joinCodeBtn.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = codeEdt.text.toString()
            if (code!="null" && code!=""){
                createCodeBtn.visibility = View.GONE
                joinCodeBtn.visibility = View.GONE
                codeEdt.visibility = View.GONE
                headTv.visibility = View.GONE
                loadingPb.visibility = View.VISIBLE
            }else{
                Toast.makeText(this,"Please enter a a valid code",Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun accepted(){
        startActivity(Intent(this,OnlineMultiplayerGameActivity::class.java))
        createCodeBtn.visibility = View.VISIBLE
        joinCodeBtn.visibility = View.VISIBLE
        codeEdt.visibility = View.VISIBLE
        headTv.visibility = View.VISIBLE
        loadingPb.visibility = View.GONE

        isCodeMaker = false
        FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var data : Boolean = isValueAvailable(snapshot, code)
                Handler().postDelayed({
                    if (data==true){
                        codeFound = true
                        accepted()
                        createCodeBtn.visibility = View.VISIBLE
                        joinCodeBtn.visibility = View.VISIBLE
                        codeEdt.visibility = View.VISIBLE
                        headTv.visibility = View.VISIBLE
                        loadingPb.visibility = View.GONE

                    }else{
                        createCodeBtn.visibility = View.VISIBLE
                        joinCodeBtn.visibility = View.VISIBLE
                        codeEdt.visibility = View.VISIBLE
                        headTv.visibility = View.VISIBLE
                        loadingPb.visibility = View.GONE
                        Toast.makeText(this@OnlineCodeGeneratorActivity,"Invalid Code",Toast.LENGTH_SHORT).show()
                    }
                },2000
                )
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun isValueAvailable(snapshot: DataSnapshot, code : String): Boolean{
        var  data = snapshot.children
        data.forEach {
            var value = it.getValue().toString()
            if (value==code){
                keyValue = it.key.toString()
                return true
            }
        }
        return false
    }

}