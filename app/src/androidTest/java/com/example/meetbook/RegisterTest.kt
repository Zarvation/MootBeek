package com.example.meetbook

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterTest{
    @Rule
    @JvmField
    // Tentukan Activity yang akan diuji
    var regisactivitytest = ActivityTestRule(RegisterActivity::class.java)

    // Pengecekan berfungsinya OnSaveInstance setelah register
    @Test
    fun registerStatusOnSaveInstance(){
        // Isi Username dan Password
        onView(withId(R.id.RUsername)).perform(typeText("client"), closeSoftKeyboard())
        onView(withId(R.id.RPassword)).perform(typeText("123"), closeSoftKeyboard())
        // Klik Register
        onView(withId(R.id.RegisButton)).perform(click())

        // Ubah orientasi mobile menjadi Landscape
        // Register Activity (yang diuji) dirotate
        regisactivitytest.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        // Kemudian mengecek apakah text pada RegisStatus masih ditampilkan sama
        onView(withId(R.id.RegisStatus)).check(matches(withText("User client has been created")))
        // Ulangi bila mobile dikembalikan menjadi Portrait
        regisactivitytest.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        onView(withId(R.id.RegisStatus)).check(matches(withText("User client has been created")))
    }

    // Pengecekan bila Username dan Password tidak boleh kosong ketika register
    @Test
    fun userPassMustNotEmpty(){
        onView(withId(R.id.RUsername)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.RPassword)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.RegisButton)).perform(click())

        onView(withId(R.id.RegisStatus)).check(matches(withText("Username and Password cannot be empty")))
    }

    // Persiapan Testing
    @Before
    fun setUp(){
        Intents.init()
    }

    // Menguji bila bisa kembali ke halaman login
    @Test
    fun backToLoginIntent(){
        // Klik Login
        onView(withId(R.id.LoginText)).perform(click())
        // Jalankan intent dengan component class yang dituju adalah LoginActivtiy dengan package com.example.meetbook
        Intents.intending(AllOf.allOf(
                IntentMatchers.hasComponent(ComponentNameMatchers.hasShortClassName(".LoginActivity")),
                IntentMatchers.toPackage("com.example.meetbook")
        ))
    }

    @After
    fun tearDown(){
        Intents.release()
    }
}