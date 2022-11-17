package com.example.au22_flashcard

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.time.chrono.HijrahChronology.INSTANCE

@Database(entities = [Word::class], version= 1)
abstract class AppDatabase : RoomDatabase() {
    abstract  val wordDao: WordDao

    companion object{
        //var INSTANCE : AppDatabase? = null
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "word_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}