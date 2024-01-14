package  com.blueray.Kanz.hlsdemo.common.utils

interface ResponseListener<T> {
    fun onResponse(response: T)
}