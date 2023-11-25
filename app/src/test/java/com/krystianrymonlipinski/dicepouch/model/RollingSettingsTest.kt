package com.krystianrymonlipinski.dicepouch.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RollingSettingsTest {

    lateinit var testObj: RollingSettings

    @Before
    fun setUp() {
        testObj = RollingSettings(
            isSoundOn = false,
            singleThrowTimeMillis = 1000,
            delayBetweenThrowsTimeMillis = 500,
            isRollPopupAutodismissOn = false,
            rollPopupAutodismissTimeMillis = 300
        )
    }

    @Test
    fun changeIsSoundOn() {
        testObj = testObj.setIsSoundOn(isOn = true)
        assertTrue(testObj.isSoundOn)
    }

    @Test
    fun changeSingleThrowTimeMillis() {
        testObj = testObj.setSingleThrowTimeMillis(210)
        assertEquals(210, testObj.singleThrowTimeMillis)
    }

    @Test
    fun changeDelayBetweenThrowsTimeMillis() {
        testObj = testObj.setDelayBetweenThrowTimeMillis(32)
        assertEquals(32, testObj.delayBetweenThrowsTimeMillis)
    }

    @Test
    fun setIsRollPopupAutodismissOn() {
        testObj = testObj.setIsRollPopupAutodismissOn(true)
        assertTrue(testObj.isRollPopupAutodismissOn)
    }

    @Test
    fun setRollPopupAutodismissTimeMillis() {
        testObj = testObj.setRollPopupAutodismissTimeMillis(4502)
        assertEquals(4502, testObj.rollPopupAutodismissTimeMillis)
    }
}