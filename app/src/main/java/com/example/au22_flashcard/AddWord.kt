package com.example.au22_flashcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddWord : AppCompatActivity(),CoroutineScope {

    private lateinit var db : AppDatabase
    lateinit var sweEditText: EditText
    lateinit var enEditText: EditText
    private lateinit var job: Job
    val wordList = WordList()

    override val coroutineContext: CoroutineContext
    get()= Dispatchers.Main+job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        sweEditText=findViewById(R.id.sweEdTxt)
        enEditText=findViewById(R.id.enEdTxt)
        var saveBtn=findViewById<Button>(R.id.saveBtn)
        var cancelBtn=findViewById<Button>(R.id.cancelBtn)

        job=Job()
        db= AppDatabase.getInstance(this)

        saveBtn.setOnClickListener {
            saveWord()
        }
        cancelBtn.setOnClickListener {
            finish()
        }

    }

    private fun saveWord() {
        val swedish= sweEditText.text.toString()
        val english= enEditText.text.toString()
        if(swedish!= "" && english!="") {
            val word = Word(0, swedish=swedish, english=english)
            //DataManager.students.add(student)
            insert(word)
            finish()
        }
    }
    fun insert(word:Word){
        launch(Dispatchers.IO){
            db.wordDao.insert(word)
        }
    }


}