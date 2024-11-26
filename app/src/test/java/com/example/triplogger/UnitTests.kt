package com.example.triplogger

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.triplogger.utilities.NetworkUtils
import com.example.triplogger.view.MapViewActivity
import com.example.triplogger.view.PhotoAdapter
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTests {
    @Test
    fun `isNetworkAvailable returns true when network is connected`() {
        val mockContext = mockk<Context>()
        val mockConnectivityManager = mockk<ConnectivityManager>()
        val mockNetworkInfo = mockk<NetworkInfo>()

        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        every { mockConnectivityManager.activeNetworkInfo } returns mockNetworkInfo
        every { mockNetworkInfo.isConnected } returns true

        val result = NetworkUtils.isNetworkAvailable(mockContext)

        assertTrue(result)
    }

    @Test
    fun `isNetworkAvailable returns false when network is disconnected`() {
        val mockContext = mockk<Context>()
        val mockConnectivityManager = mockk<ConnectivityManager>()
        val mockNetworkInfo = mockk<NetworkInfo>()

        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        every { mockConnectivityManager.activeNetworkInfo } returns mockNetworkInfo
        every { mockNetworkInfo.isConnected } returns false

        val result = NetworkUtils.isNetworkAvailable(mockContext)

        assertFalse(result)
    }

    @Test
    fun `test item count in photoAdapter`() {
        val simulatedPhotoList = mutableListOf(
            "content://some/uri/photo1",
            "content://some/uri/photo2",
            "content://some/uri/photo3"
        )

        val adapter = PhotoAdapter(simulatedPhotoList) {  }

        val expectedItemCount = simulatedPhotoList.size
        val actualItemCount = adapter.itemCount

        assertEquals(expectedItemCount, actualItemCount)
    }
}