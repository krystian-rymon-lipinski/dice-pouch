package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.Before

open class BaseDaoTest {

    protected lateinit var db: AppDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
    }
}