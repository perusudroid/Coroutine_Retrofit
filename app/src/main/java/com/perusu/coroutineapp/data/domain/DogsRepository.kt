package com.perusu.coroutineapp.data.domain

import LoginResponse
import com.perusu.coroutineapp.data.model.Dog
import com.perusu.coroutineapp.data.model.GeneralResult
import com.perusu.coroutineapp.data.model.ResultOf
import com.perusu.coroutineapp.data.remote.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


class DogsRepository(private val api: ApiInterface) : IRepo {


    override suspend fun getRandomTwoDogs(): GeneralResult {

        return withContext(Dispatchers.IO) {

            val dogList = mutableListOf<Dog>()

            val dogBreedListDeferred = async {
                api.getBreedsListAsync().execute()
            }

            val dogBreedListResponse = dogBreedListDeferred.await()

            val dogBreedOneName = dogBreedListResponse.body()?.message?.keys?.toList()?.random()
            val dogBreedTwoName = dogBreedListResponse.body()?.message?.keys?.toList()?.random()

            val dogBreedOneImageDeferred = async {
                dogBreedOneName?.let { api.getImageByUrlAsync(it).execute() }
            }
            val dogBreedTwoImageDeferred = async {
                dogBreedTwoName?.let { api.getImageByUrlAsync(it).execute() }
            }

            val dogBreedOneImageResponse = dogBreedOneImageDeferred.await()
            val dogBreedTwoImageResponse = dogBreedTwoImageDeferred.await()

            dogList.add(Dog(dogBreedOneName, dogBreedOneImageResponse?.body()?.message))
            dogList.add(Dog(dogBreedTwoName, dogBreedTwoImageResponse?.body()?.message))

            GeneralResult.SuccessGeneric(dogList)

        }
    }

    override suspend fun getTopTwoDogs() = withContext(Dispatchers.IO) {

        val dogList = mutableListOf<Dog>()

        val dogBreedListDeferred = async {
            api.getBreedsListAsync().execute()
        }

        val dogBreedListResponse = dogBreedListDeferred.await()

        val dogBreedOneName = dogBreedListResponse.body()?.message?.keys?.toList()?.random()
        val dogBreedTwoName = dogBreedListResponse.body()?.message?.keys?.toList()?.random()

        val dogBreedOneImageDeferred = async {
            dogBreedOneName?.let { api.getImageByUrlAsync(it).execute() }
        }
        val dogBreedTwoImageDeferred = async {
            dogBreedTwoName?.let { api.getImageByUrlAsync(it).execute() }
        }

        val dogBreedOneImageResponse = dogBreedOneImageDeferred.await()
        val dogBreedTwoImageResponse = dogBreedTwoImageDeferred.await()

        dogList.add(Dog(dogBreedOneName, dogBreedOneImageResponse?.body()?.message))
        dogList.add(Dog(dogBreedTwoName, dogBreedTwoImageResponse?.body()?.message))

        if (dogList.isNullOrEmpty())
            ResultOf.Empty("List is empty")
        else
            ResultOf.Success(dogList)

    }

    override suspend fun getDogList(): ResultOf<List<Dog>> {

        val list = mutableListOf<Dog>()
        val dogBreedList = api.getBreedsList().message.keys.toList()

        withContext(Dispatchers.IO) {
            dogBreedList.map { async { api.getImageByUrl(it) } }.awaitAll().forEach {
                list.add(Dog(extractBreedName(it.message), it.message))
            }
        }
        /*  return when {
              list.size == 94 -> ResultOf.Failure("Size is 94", null)
              list.size > 90 -> ResultOf.Empty("List count is below 94")
              else -> return ResultOf.Success(list)
          }*/

        return ResultOf.Success(list)
    }

    override suspend fun getDogListFlow(): Flow<ResultOf<List<Dog>>> = flow {
        val list = mutableListOf<Dog>()
        val dogBreedList = api.getBreedsList().message.keys.toList()
        withContext(Dispatchers.IO) {
            dogBreedList.map { async { api.getImageByUrl(it) } }.awaitAll().forEach {
                list.add(Dog(extractBreedName(it.message), it.message))
            }
        }
        emit(ResultOf.Success(list))
    }.flowOn(Dispatchers.IO)


    private fun extractBreedName(message: String): String? {
        val breedName = message.substringAfter("breeds/").substringBefore("/")
        return breedName.replace(Regex("-"), " ").capitalize()
    }


    override suspend fun login(request: HashMap<String, String?>): ResultOf<LoginResponse> {
        val result = api.login(request)
        return ResultOf.Success(result)

    }

}