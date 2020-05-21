/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response


    private var viewModelJob = Job()


    //The Dispatchers.Main dispatcher uses the UI thread for its work.
    // Because Retrofit does all its work on a background thread,
    // there's no reason to use any other thread for the scope.
    // This allows you to easily update the value of the MutableLiveData when you get a result.
    private val coroutineScope = CoroutineScope(
            viewModelJob + Dispatchers.Main )


    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    //This is the method where i'll call the Retrofit service and handle the returned JSON string.
    private fun getMarsRealEstateProperties() {
        coroutineScope.launch {
            var getPropertiesDeferred =
                    MarsApi.retrofitService.getProperties()
            try {
                //Calling await() on the Deferred object returns the result from the network call when the value is ready.
                //The await() method is non-blocking, so the Mars API service retrieves the data from the network
                // without blocking the current thread—which is important
                // because we're in the scope of the UI thread. Once the task is done,
                // your code continues executing from where it left off
                var listResult = getPropertiesDeferred.await()
                _response.value =
                        "Success: ${listResult.size} Mars properties retrieved"
              // handle the failure response:
            } catch (e: Exception) {

            }

        }

    }
    //Loading data should stop when the ViewModel is destroyed,
    // because the OverviewFragment that uses this ViewModel will be gone.
    // To stop loading when the ViewModel is destroyed, you override onCleared() to cancel the job.
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}
