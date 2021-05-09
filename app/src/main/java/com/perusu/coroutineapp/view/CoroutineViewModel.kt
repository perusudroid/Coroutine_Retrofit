package com.perusu.coroutineapp.view

import LoginResponse
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.perusu.coroutineapp.common.CoroutineApp
import com.perusu.coroutineapp.data.domain.IRepo
import com.perusu.coroutineapp.data.model.ContactModel
import com.perusu.coroutineapp.data.model.Dog
import com.perusu.coroutineapp.data.model.GeneralResult
import com.perusu.coroutineapp.data.model.ResultOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.collections.set
import kotlin.coroutines.CoroutineContext


open class CoroutineViewModel(
    private val iRepo: IRepo
) : ViewModel(), CoroutineScope {

    var flowCount = 0
    var obsCount = 0
    private val parentJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    val mutableStateFlow = MutableStateFlow(0)
    var timer = MutableLiveData<Int>()
    val obTopTwoDogs = MutableLiveData<ResultOf<List<Dog>>>()
    val obDogList = MutableLiveData<ResultOf<List<Dog>>>()
    val obLogin = MutableLiveData<ResultOf<LoginResponse>>()


    fun setMutableStateValue() {
        launch {
            (1..5).forEach {
                delay(500)
                mutableStateFlow.value = it
            }
        }
    }

    fun startCoroutineTimer(
        delayMillis: Long = 0,
        repeatMillis: Long = 0
    ): Flow<Int> = flow {
        delay(delayMillis)
        if (repeatMillis > 0) {
            while (true) {
                emit(flowCount)
                flowCount++
                delay(repeatMillis)
            }
        } else {
            emit(flowCount)
            flowCount++
        }
    }


    fun flowWithMap(): Flow<Dog> = flow {
        (1..5).forEach {
            delay(500)
            emit(Dog("", ""))
        }
    }.filter { it.breed == "1" }


    val obFlowDogList: LiveData<ResultOf<List<Dog>>> = liveData {
        emit(ResultOf.Progress(true))
        val start = System.currentTimeMillis()
        iRepo.getDogListFlow()
            .catch { emit(ResultOf.Failure(it.message, it)) }
            .collect {
                emit(it)
                Log.d("CoroutineViewModel", "getDogListByFlow: ${getTimeDifference(start)}")
            }
        emit(ResultOf.Progress(false))
    }

    val flowWithBuilder: LiveData<Dog> = liveData {
        flowSample()
            .collect {
                emit(it)
            }
    }

    val flowWithoutLiveData: LiveData<Dog> = liveData {
        flowSample()
            .collect {
                emit(it)
            }
    }

    val obRandomTwoDogs = liveData {
        emit(GeneralResult.Progress(true))
        emitSource(getRandomTwoDogs())
    }

    private fun getRandomTwoDogs(): LiveData<GeneralResult> = liveData {
        while (true) {
            delay(10000)
            val topTwoDogsResult = iRepo.getRandomTwoDogs()
            emit(topTwoDogsResult)
        }
    }

    fun getTopDogList() {
        parentJob.cancelChildren()
        launch {
            obTopTwoDogs.value = ResultOf.Progress(true)
            val result = runCatching { iRepo.getTopTwoDogs() }
            result.onSuccess {
                obTopTwoDogs.value = it
            }.onFailure {
                obTopTwoDogs.value = ResultOf.Failure(it.message, it)
            }
            obTopTwoDogs.value = ResultOf.Progress(false)
        }
    }

    fun getDogList() {
        parentJob.cancelChildren()
        launch {
            obDogList.value = ResultOf.Progress(true)
            val start = System.currentTimeMillis()
            val result = runCatching { iRepo.getDogList() }
            result.onSuccess {
                obDogList.value = it
                Log.d("CoroutineViewModel", "getDogList: ${getTimeDifference(start)}")
            }.onFailure {
                obDogList.value = ResultOf.Failure(it.message, it)
            }
            obDogList.value = ResultOf.Progress(false)
        }
    }

    fun getDogListByFlow() {
        parentJob.cancelChildren()
        launch {
            val start = System.currentTimeMillis()
            iRepo.getDogListFlow()
                .catch { obDogList.value = ResultOf.Failure(it.message, it) }
                .collect {
                    obDogList.value = it
                    Log.d("CoroutineViewModel", "getDogListByFlow: ${getTimeDifference(start)}")
                }
            obDogList.value = ResultOf.Progress(false)
        }
    }

    private fun getTimeDifference(start: Long): String {
        val difference = System.currentTimeMillis() - start
        return buildString {
            append(difference / 1000)
            append(" seconds")
        }
    }

    fun startObsTimer() {
        launch {
            while (true) {
                withContext(Dispatchers.IO) {
                    delay(1000)
                }
                timer.value = obsCount
                obsCount++
            }
        }
    }

    fun flowSample(): Flow<Dog> = flow {
        emit(execute("German shepard"))
        emit(execute("Laprador"))
        emit(execute("Dobber man"))
    }.flowOn(Dispatchers.IO)

    private suspend fun execute(text: String): Dog {
        delay(500)
        return Dog(text, null)
    }


    fun fetchContacts(): Flow<ContactModel?> = flow {


        CoroutineApp.createCursor().run {

            moveToFirst()
            val contactsMap = HashMap<Long, ContactModel>()

            while (!isAfterLast) {
                val longId = getLong(getColumnIndex(ContactsContract.Data.CONTACT_ID))
                var contactItem = contactsMap[longId]

                if (contactItem == null) {
                    contactItem = ContactModel().apply {
                        id = longId
                        inVisibleGroup =
                            getInt(getColumnIndex(ContactsContract.Data.IN_VISIBLE_GROUP))
                        name = getString(getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY))
                            ?: ""
                        starred = getInt(getColumnIndex(ContactsContract.Data.STARRED)) != 0
                        photo = getString(getColumnIndex(ContactsContract.Data.PHOTO_URI))
                        thumbNail =
                            getString(getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI))
                    }
                    contactsMap[longId] = contactItem
                }
                parseEmailAndPhone(
                    this,
                    getColumnIndex(ContactsContract.Data.MIMETYPE),
                    getColumnIndex(ContactsContract.Data.DATA1),
                    contactItem
                )
                moveToNext()
            }
            close()

            val sorted = contactsMap.toList()
                .sortedBy { (_, value) -> value }
                .toMap()

            for (key in sorted.keys) {
                emit(contactsMap[key])
            }

        }
    }.flowOn(Dispatchers.IO)
        .filter { it?.inVisibleGroup == 1 }


    private fun parseEmailAndPhone(
        cursor: Cursor,
        mimeTypeColumnIndex: Int,
        dataColumnIndex: Int,
        contactModel: ContactModel
    ) {

        when (cursor.getString(mimeTypeColumnIndex)) {
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> {

                with(contactModel) {
                    if (!cursor.getString(dataColumnIndex).isNullOrEmpty()) {
                        if (emails == null)
                            emails = mutableSetOf()
                        emails?.add(cursor.getString(dataColumnIndex))
                    }
                }
            }
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> {

                with(contactModel) {

                    var phoneNumber = cursor.getString(dataColumnIndex)
                    if (!phoneNumber.isNullOrEmpty()) {
                        if (phone == null)
                            phone = mutableSetOf()
                        phoneNumber = phoneNumber.replace("\\s+".toRegex(), "")
                        phone?.add(phoneNumber)
                    }
                }
            }
        }
    }


    fun doLogin(request: HashMap<String, String?>) {
        launch {
            obLogin.value = ResultOf.Progress(true)
            val loginResult = runCatching { iRepo.login(request) }
            loginResult.onSuccess {
                obLogin.value = it
            }.onFailure {
                obLogin.value = ResultOf.Failure(it.message, it)
            }
            obLogin.value = ResultOf.Progress(false)
        }

    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancelChildren()
    }


}