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

package com.example.android.marsrealestate.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = " https://android-kotlin-fun-mars-server.appspot.com/"

//use a Retrofit builder to create a Retrofit object
private val retrofit = Retrofit.Builder()
        //The converter tells Retrofit what do with the data it gets back from the web service.

        //Retrofit has a ScalarsConverter that supports strings and other primitive types.

       //In this case, i want Retrofit to fetch a JSON response from the web service,

      // and return it as a String.
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()


// define an interface that defines how Retrofit talks to the web server using HTTP requests.

//the goal is to get the JSON response string from the web service,
// and i only need one method to do that: getProperties().
// To tell Retrofit what this method should do, i use a @GET annotation and specify the path,
// or endpoint, for that web service method. In this case the endpoint is called realestate. When the getProperties() method is invoked.

//Retrofit appends the endpoint realestate to the base URL (which i defined in the Retrofit builder),
// creates a Call object. That Call object is used to start the request.
interface MarsApiService {
    @GET("realestate")
    fun getProperties():
            Call<String>
}

//The Retrofit create() method creates the Retrofit service itself with the MarsApiService interface.
// Because this call is expensive, and the app only needs one Retrofit service instance,
// you expose the service to the rest of the app using a public object called MarsApi,
// and lazily initialize the Retrofit service there.

//Now that all the setup is done, each time the app calls MarsApi.retrofitService,
// it will get a singleton Retrofit object that implements MarsApiService.

//define a public object called MarsApi to initialize the Retrofit service.
object MarsApi {
    val retrofitService : MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java) }
}
