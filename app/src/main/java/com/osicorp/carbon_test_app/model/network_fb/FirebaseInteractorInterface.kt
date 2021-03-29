package com.osicorp.carbon_test_app.model.network_fb

import com.osicorp.carbon_test_app.model.Post
import kotlinx.coroutines.flow.Flow

interface FirebaseInteractorInterface {

   fun firstPageFirstLoad(): Flow<List<Post>>
    fun loadMorePost():Flow<List<Post>>
}