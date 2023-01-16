package com.udacity.project4.locationreminders.data.local

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

class FakeDataSource : ReminderDataSource {


    val reminder1 = ReminderDTO("title1", "description1", "location1", 0.0, 0.0, "1")
    val reminder2 = ReminderDTO("title2", "description2", "location2", 0.0, 0.0, "2")
    val reminder3 = ReminderDTO("title3", "description3", "location3", 0.0, 0.0, "3")
    val reminder4 = ReminderDTO("title4", "description4", "location4", 0.0, 0.0, "4")
    var showLoading = true

    var errorMsg: String = ""

    var listReminder = mutableListOf(reminder1, reminder2, reminder3, reminder4)
//    TODO: Create a fake data source to act as a double to the real data source

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (showLoading && errorMsg == "") {
            return Result.Success(listReminder)
        } else {
            return Result.Error("Failed")
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        listReminder.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        for (reminder in listReminder) {
            if (reminder.id == id)
                return Result.Success(reminder)
        }
        return Result.Error("can't find reminder with this id")

    }

    override suspend fun deleteAllReminders() {
        listReminder.clear()
    }


}