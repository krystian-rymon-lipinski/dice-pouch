package com.krystianrymonlipinski.dicepouch.model

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RollShortcutTest {

    private lateinit var testObj: RollShortcut

    @Before
    fun setUp() {
        testObj = RollShortcut(
            timestampId = 0L,
            name = "a_name",
            setting = RollSetting(Die(6))
        )
    }

    @Test
    fun changeName() {
        testObj = testObj.changeName("new_name")
        assertEquals("new_name", testObj.name)
    }

    @Test
    fun changeSetting() {
        val newSetting = RollSetting(Die(20), 3, -2)
        testObj = testObj.changeSetting(newSetting = newSetting)
        assertEquals(newSetting, testObj.setting)
    }
}