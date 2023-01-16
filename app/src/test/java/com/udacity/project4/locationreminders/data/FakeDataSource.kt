package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {


    val reminder1 = ReminderDTO("title", "description", "location", 0.0, 0.0, "1")
    val reminder2 = ReminderDTO("title", "description", "location", 0.0, 0.0, "2")
    val reminder3 = ReminderDTO("title", "description", "location", 0.0, 0.0, "3")
    val reminder4 = ReminderDTO("title", "description", "location", 0.0, 0.0, "4")

    var errorMsg: String = ""
    var shouldReturnError = false

    var listReminder = mutableListOf<ReminderDTO>(reminder1, reminder2, reminder3, reminder4)
//    TODO: Create a fake data source to act as a double to the real data source

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return try {

            if (!shouldReturnError) {
                if (errorMsg == "") {
                    return Result.Success(listReminder)
                } else {
                    return Result.Error(errorMsg)
                }
            } else {
                return Result.Error("Failed To load data")
            }
        } catch (e: Exception) {
            return Result.Error(e.localizedMessage)

        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        listReminder.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {

        return try {
            if (!shouldReturnError) {

                for (reminder in listReminder) {
                    if (reminder.id == id)
                        return Result.Success(reminder)
                }
            } else {
                return Result.Error("Failed To load data")

            }
            return Result.Error("can't find reminder with this id")
        } catch (e: Exception) {
            return Result.Error(e.localizedMessage)
        }

    }

    override suspend fun deleteAllReminders() {
        listReminder.clear()
    }


}