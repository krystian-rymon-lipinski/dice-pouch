package com.krystianrymonlipinski.dicepouch.viewmodels

import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.capture
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {

    private lateinit var testObj: SettingsViewModel

    @Mock
    lateinit var settingsLocalDataSource: SettingsLocalDataSourceImpl

    @Captor
    lateinit var savedSettingsCaptor: ArgumentCaptor<RollingSettings>

    @Before
    fun setUp() {
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        testObj = SettingsViewModel(settingsLocalDataSource)
    }


    @Test
    fun saveSettings() = runTest {
        val settingsToSave = RollingSettings()

        testObj.saveSettings(settingsToSave)
        verify(settingsLocalDataSource).saveRollingSettings(capture(savedSettingsCaptor))
        assertEquals(settingsToSave, savedSettingsCaptor.value)
    }

    @Test
    fun retrieveSettings() = runTest {
        val settingsToRetrieve = RollingSettings()
            .setIsRollPopupAutodismissOn(true)
            .setSingleThrowTimeMillis(2400)
        whenever(settingsLocalDataSource.retrieveRollingSettings()).thenReturn(flowOf(settingsToRetrieve))

        val settingsRetrieved = testObj.retrieveSettings()
        assertEquals(settingsToRetrieve, settingsRetrieved)
    }
}