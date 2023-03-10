package com.udacity.project4.locationreminders.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.locationreminders.geofence.GeofenceTransitionsJobIntentService.Companion.FENCE_ID
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment.Companion.ACTION_GEOFENCE_EVENT

/**
 * Triggered by the Geofence.  Since we can have many Geofences at once, we pull the request
 * ID from the first Geofence, and locate it within the cached data in our Room DB
 *
 * Or users can add the reminders and then close the app, So our app has to run in the background
 * and handle the geofencing in the background.
 * To do that you can use https://developer.android.com/reference/android/support/v4/app/JobIntentService to do that.
 *
 */

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)


            if (geofencingEvent!!.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
                && geofencingEvent.triggeringGeofences!!.isNotEmpty()
            ) {
                geofencingEvent.triggeringGeofences!!.forEach { geofence ->
                    val fenceId = geofence.requestId
                    val geofenceServiceIntent =
                        Intent(context, GeofenceTransitionsJobIntentService::class.java)
                    geofenceServiceIntent.putExtra(FENCE_ID, fenceId)
                    GeofenceTransitionsJobIntentService.enqueueWork(context, geofenceServiceIntent)
                }
            }
        }
    }


}
