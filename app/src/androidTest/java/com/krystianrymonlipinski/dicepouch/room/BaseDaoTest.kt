package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.Before

open class BaseDaoTest {

    protected lateinit var db: AppDatabase
    protected lateinit var setDao: SetDao
    protected lateinit var dieDao: DieDao
    protected lateinit var shortcutDao: ShortcutDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        setDao = db.setDao()
        dieDao = db.dieDao()
        shortcutDao = db.shortcutDao()
    }


    protected fun createBasicDatabaseSetup() = SetWithDice(
        set = SetEntity(name = "first_set", diceSideColorArgb = 12, diceNumberColorArgb = 2),
        diceWithShortcuts = listOf(
            DieWithShortcuts(
                die = DieEntity(
                    timestampId = dieId,
                    setId = setId,
                    sides = 3,
                    sidesColorArgb = 23,
                    numberColorArgb = 19
                ),
                shortcuts = listOf(
                    ShortcutEntity(
                        timestampId = 10L,
                        name = "a_shortcut_name",
                        diceNumber = 2,
                        dieId = dieId,
                        modifier = 4,
                        mechanic = "NORMAL"
                    ),
                    ShortcutEntity(
                        timestampId = 19L,
                        name = "another_shortcut",
                        diceNumber = 7,
                        dieId = dieId,
                        modifier = -1,
                        mechanic = "DISADVANTAGE"
                    )
                )
            ),
            DieWithShortcuts(
                die = DieEntity(
                    timestampId = dieId2,
                    setId = setId,
                    sides = 10,
                    sidesColorArgb = 120,
                    numberColorArgb = 987
                ),
                shortcuts = listOf(
                    ShortcutEntity(
                        timestampId = 101L,
                        name = "random_name",
                        diceNumber = 4,
                        dieId = dieId2,
                        modifier = 2,
                        mechanic = "ADVANTAGE"
                    )
                )
            )
        )
    )

    protected fun insertBasicDatabaseSetup(setWithDice: SetWithDice) {
        setWithDice.apply {
            setDao.add(set)
            diceWithShortcuts.map { it.die }.forEach {
                dieDao.add(it)
            }
            diceWithShortcuts.flatMap { it.shortcuts }.forEach {
                shortcutDao.add(it)
            }
        }
    }

    companion object {
        @JvmStatic protected val setId = 1
        @JvmStatic protected val dieId = 1L
        @JvmStatic protected val dieId2 = 2L
    }

}