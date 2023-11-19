package com.krystianrymonlipinski.dicepouch.data_layer

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import com.krystianrymonlipinski.dicepouch.data_store.CURRENT_SET_ID_KEY
import com.krystianrymonlipinski.dicepouch.data_store.DELAY_BETWEEN_THROWS_MILLIS_KEY
import com.krystianrymonlipinski.dicepouch.data_store.IS_ROLL_POPUP_AUTODISMISS_ON_KEY
import com.krystianrymonlipinski.dicepouch.data_store.IS_SOUND_ON_KEY
import com.krystianrymonlipinski.dicepouch.data_store.ROLL_POPUP_AUTODISMISS_TIME_MILLIS_KEY
import com.krystianrymonlipinski.dicepouch.data_store.SINGLE_THROW_TIME_MILLIS_KEY
import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class SettingsLocalDataSourceImplTest {

    private lateinit var testObj: SettingsLocalDataSourceImpl

    @Mock
    lateinit var appSettingsDataStore: DataStore<Preferences>


    @Before
    fun setUp() {
        testObj = SettingsLocalDataSourceImpl(appSettingsDataStore)
    }


    @Test
    fun retrieveCurrentSetId() = runTest {
        val setIdToRetrieve = 2
        whenever(appSettingsDataStore.data).thenReturn(flowOf(preferencesOf(
            CURRENT_SET_ID_KEY to 2
        )))

        val setIdRetrieved = testObj.retrieveCurrentSetId().take(1).singleOrNull()
        verify(appSettingsDataStore).data
        assertEquals(setIdRetrieved, setIdToRetrieve)
    }

    @Test
    fun retrieveSettings() = runTest {
        val settingsToRetrieve = RollingSettings()
        whenever(appSettingsDataStore.data).thenReturn(flowOf(preferencesOf(
            IS_SOUND_ON_KEY to false,
            SINGLE_THROW_TIME_MILLIS_KEY to 1000 ,
            DELAY_BETWEEN_THROWS_MILLIS_KEY to 500,
            IS_ROLL_POPUP_AUTODISMISS_ON_KEY to false,
            ROLL_POPUP_AUTODISMISS_TIME_MILLIS_KEY to 500
        )))

        val settingsRetrieved = testObj.retrieveRollingSettings().take(1).single()
        verify(appSettingsDataStore).data
        assertEquals(settingsToRetrieve, settingsRetrieved)
    }

}