package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            AuthUI.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = RemindersLocalRepository(
            database.reminderDao(), Dispatchers.Main
        )
    }
//    TODO: Add testing implementation to the RemindersLocalRepository.kt

    @Test
    fun insertReminderAndGetById() = mainCoroutineRule.runBlockingTest {
        val reminder = ReminderDTO(
            "title", "description", "location", 0.0, 0.0, "1"
        )
        repository.saveReminder(reminder)

        val loaded = repository.getReminder(reminder.id)
        if (loaded is Result.Success<ReminderDTO>) {

            MatcherAssert.assertThat(loaded as Result<ReminderDTO>, CoreMatchers.notNullValue())
            MatcherAssert.assertThat(loaded.data.id, CoreMatchers.`is`(reminder.id))
            MatcherAssert.assertThat(loaded.data.title, CoreMatchers.`is`(reminder.title))
            MatcherAssert.assertThat(loaded.data.location, CoreMatchers.`is`(reminder.location))
            MatcherAssert.assertThat(loaded.data.latitude, CoreMatchers.`is`(reminder.latitude))
            MatcherAssert.assertThat(loaded.data.longitude, CoreMatchers.`is`(reminder.longitude))
            MatcherAssert.assertThat(
                loaded.data.description,
                CoreMatchers.`is`(reminder.description)
            )
        }

    }

    @Test
    fun reminderNotFoundError() = mainCoroutineRule.runBlockingTest {

        val errorMsg = repository.getReminder("-1") as Result.Error

        MatcherAssert.assertThat(errorMsg.message, `is`("Reminder not found!"))

    }

    @After
    fun closeDb() {
        database.close()
    }
}