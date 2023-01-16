package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private lateinit var fakeDataSource: FakeDataSource

    private lateinit var invalidTitleReminder: ReminderDataItem
    private lateinit var invalidlatLngReminder: ReminderDataItem
    private lateinit var validReminder: ReminderDataItem

    @Before
    fun setup() {
//       val reminderViewModel :RemindersListViewModel= RemindersListViewModel()
        fakeDataSource = FakeDataSource()
        saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)

        invalidlatLngReminder = ReminderDataItem(
            "title", "description", "location", null, null, "1"
        )
        invalidTitleReminder = ReminderDataItem(
            null, "description", "location", 0.0, 0.0, "2"
        )

        validReminder = ReminderDataItem(
            "Title", "description", "location", 0.0, 0.0, "3"
        )
    }

    @Test
    fun validateReminder_invalidReminderTitle_false() {
        val result = saveReminderViewModel.validateEnteredData(invalidTitleReminder)
        assertThat(result, `is`(false))

    }

    @Test
    fun validateReminder_invalidReminderLatLng_false() {
        val result = saveReminderViewModel.validateEnteredData(invalidlatLngReminder)
        assertThat(result, `is`(false))

    }

    @Test
    fun validateReminder_validReminder_true() {
        val result = saveReminderViewModel.validateEnteredData(validReminder)
        assertThat(result, `is`(true))

    }
    //TODO: provide testing to the SaveReminderView and its live data objects


}