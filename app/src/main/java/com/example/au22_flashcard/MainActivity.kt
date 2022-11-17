package com.example.au22_flashcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope{

    lateinit var wordView : TextView
    var currentWord : Word? = null
    //val wordList = WordList()
    private lateinit var job: Job
    var showList = mutableListOf<Word>()
    var usedWords = mutableListOf<Word>()

    override val coroutineContext: CoroutineContext
    get()= Dispatchers.Main+job

    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job=Job()

        var addWordBnt=findViewById<Button>(R.id.addingBtn)
        wordView = findViewById(R.id.wordTextView)

        db = AppDatabase.getInstance(this)

        showNewWord()

        wordView.setOnClickListener {
            revealTranslation()
        }
        addWordBnt.setOnClickListener {
            val intent= Intent(this, AddWord::class.java)
            startActivity(intent)
        }

    }
    fun loadAllWords(): Deferred<List<Word>> =
        async (Dispatchers.IO){
            db.wordDao.getAllWords()
        }

    fun revealTranslation() {
        wordView.text = currentWord?.english
    }
    fun showNewWord() {
        val list=loadAllWords()
        launch {
            val wordList=list.await()
            for (word in wordList){
                showList.add(word)
                Log.d("!!!","item ${word.swedish}")
            }
            currentWord=getNewWord(wordList)
            wordView.text = currentWord?.swedish
        }
        showList.clear()
        Log.d("!!!","currentWord: ${currentWord?.english}")
        Log.d("!!!","list contains: ${showList.size}")
    }
    fun getNewWord(list:List<Word>) : Word {
        if (list.size == usedWords.size) {
            usedWords.clear()
        }
        Log.d("!!!","used list contains: ${usedWords.size}")
        var word : Word? = null
        do {
            val rnd = (0 until list.size).random()
            word = list[rnd]
        } while(usedWords.contains(word))
        usedWords.add(word!!)

        return word
    }
    fun delete(word: Word){
        launch(Dispatchers.IO){
            db.wordDao.deleteWord(word)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            showNewWord()
        }
        return true
    }


}


//att göras:

//1. new activity where a new word can be written
//2. save that new word in database
//3.in mainAct reads all words from database

//use coroutines when you read and write, examples from before
//Vad ska göras:

//1. skapa en ny aktivitet där ett nytt ord får skrivas in
//2. spara det nya ordet i databasen.

//3. I main activity läs in alla ord från databasen

// (anväd coroutiner när ni läser och skriver till databasen se tidigare exempel)