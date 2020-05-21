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
        MarsApi.retrofitService.getProperties().enqueue(
                object: Callback<List<MarsProperty>> {
                    //The onFailure() callback is called when the web service response fails.
                    //F For this response, i set the _response status to "Failure: " concatenated with the message from the Throwable argument.
                    override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable)  {
                        _response.value = "Failure: " + t.message
                    }

                    //The onResponse() callback is called when the request is successful and the web service returns a response.
                    override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
                        _response.value =
                                "Success: ${response.body()?.size} Mars properties retrieved"

                    }
                })
    }
}
