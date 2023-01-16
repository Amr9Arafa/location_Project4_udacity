package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.FakeDataSource
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito.*


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {


    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var context: Application

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        //stop original koin to inject fake repository
        stopKoin()
        context = getApplicationContext()
        fakeDataSource = FakeDataSource()

        val myModule = module {
            viewModel {
                RemindersListViewModel(context, fakeDataSource)
            }
            viewModel {
                SaveReminderViewModel(context, get() as ReminderDataSource)
            }
            single { fakeDataSource as ReminderDataSource }

        }
        startKoin {
            modules(listOf(myModule))

        }

//        //Get our real repository
//        repository= get()
//
//        //clear the data to start fresh
//        runBlocking {
//            repository.deleteAllReminders()
//        }
    }

    @Test
    fun navigateToSaveReminderScreen() = mainCoroutineRule.runBlockingTest {


        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        val navigationController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navigationController)
        }


        onView(withId(R.id.addReminderFAB))
            .perform(click())



        verify(navigationController).navigate(ReminderListFragmentDirections.toSaveReminder())

    }


    @Test
    fun showErrorMsg() = mainCoroutineRule.runBlockingTest {
        fakeDataSource.errorMsg = "Failed"
        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Failed")))


    }


    @Test
    fun showAllListMsg() {
        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        Thread.sleep(6000)
        for (reminder in fakeDataSource.listReminder) {
            onView(withText(reminder.title)).check(matches(isDisplayed()))
            onView(withText(reminder.description)).check(matches(isDisplayed()))

        }


    }


    @Test
    fun showNoData() = mainCoroutineRule.runBlockingTest {
        fakeDataSource.deleteAllReminders()
        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        Thread.sleep(6000)
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))


    }

    @After
    fun tearDown() {
        stopKoin()
    }


//    TODO: test the navigation of the fragments.
//    TODO: test the displayed data on the UI.
//    TODO: add testing for the error messages.
}