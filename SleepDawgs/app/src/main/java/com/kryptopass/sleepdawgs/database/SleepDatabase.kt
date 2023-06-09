package com.kryptopass.sleepdawgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * A database that stores SleepNight information.
 * And a global method to get access to the database.
 * This pattern is pretty much same for any database, so you can reuse it.
 */
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val sleepDatabaseDao: SleepDatabaseDao

    /**
     * Define a companion object, this allows us to add functions on SleepDatabase class
     * i.e., clients can call `SleepDatabase.getInstance(context)` to instantiate new SleepDatabase
     */
    companion object {
        /**
         * INSTANCE will keep a reference to any database returned via getInstance.
         * This will help us avoid repeatedly initializing the database, which is expensive.
         * The value of a volatile variable will never be cached,
         * and all writes and reads will be done to and from the main memory.
         * It means that changes made by one thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: SleepDatabase? = null

        /**
         * Helper function to get the database.
         * If a database has already been retrieved, the previous database will be returned.
         * Otherwise, create a new database.
         * This function is threadsafe,
         * and callers should cache the result for multiple database calls to avoid overhead.
         * This is an example of a simple Singleton pattern
         * that takes another Singleton as an argument in Kotlin.
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getInstance(context: Context): SleepDatabase {
            // multiple threads can ask for database at same time,
            // ensure we only initialize it once by using synchronized
            // only one thread may enter a synchronized block at a time
            synchronized(this) {
                // copy current value of INSTANCE to a local variable so Kotlin can smart cast
                // smart cast is only available to local variables
                var instance = INSTANCE
                // if instance is `null` make a new database instance
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            SleepDatabase::class.java,
                            "sleep_history_database"
                    )
                            // wipes and rebuilds instead of migrating if no Migration object
                            // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}