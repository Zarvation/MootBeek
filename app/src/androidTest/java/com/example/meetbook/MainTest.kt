package com.example.meetbook

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.material.internal.ContextUtils.getActivity
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Pengujian dengan menggunakan Android JUnit4
@RunWith(AndroidJUnit4::class)
class MainTest{
    // Mendeklarasi pengujian / rule di Java Virtual Machine
    @Rule @JvmField
    // Tentukan Activity yang akan diuji
    var activitytest = ActivityTestRule(MainActivity::class.java)

    // Testing untuk mengecek apakah username kosong
    @Test
    fun usernameShouldNotEmpty(){
        // ketik client pada edittext username
        onView(withId(R.id.LUsername)).perform(typeText("client"), closeSoftKeyboard())
        // Lakukan pengecekan
        onView(withId(R.id.LUsername)).check(matches(not(withText(""))))
    }

    // Testing untuk mengecek apakah password kosong
    @Test
    fun passwordShouldNotEmpty(){
        // ketik 123 pada edittext password
        onView(withId(R.id.LPassword)).perform(typeText("123"), closeSoftKeyboard())
        // Lakukan pengecekan
        onView(withId(R.id.LPassword)).check(matches(not(withText(""))))
    }

    // Pengecekan apakah Toast dapat ditampilkan bila username kosong
    @Test
    fun toastCheckUsername(){
        // Buat Username dan password kosong
        onView(withId(R.id.LUsername)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.LPassword)).perform(typeText(""), closeSoftKeyboard())
        // Klik login
        onView(withId(R.id.LoginButton)).perform(click())
        // pengecekan apakah Toast yang ditampilkan memiliki text yang sama
        onView(withText("Username Belum Diisi")).inRoot(withDecorView(not(activitytest.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()))
    }

    // Pengecekan Toast Bila Password dikosongkan
    @Test
    fun toastCheckPassword(){
        // Buat Password kosong
        onView(withId(R.id.LUsername)).perform(typeText("client"), closeSoftKeyboard())
        onView(withId(R.id.LPassword)).perform(typeText(""), closeSoftKeyboard())
        // Klik login
        onView(withId(R.id.LoginButton)).perform(click())
        // pengecekan apakah Toast yang ditampilkan memiliki text yang sama
        onView(withText("Password Belum Diisi")).inRoot(withDecorView(not(activitytest.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()))
    }

    // Mempersiapkan semua yang diperlukan sebelum menjalankan test
    @Before
    fun setUp(){
        // Persiapan untuk menjalankan Intent
        Intents.init()
    }

    // Test untuk bisa menjalankan intent google search
    @Test
    fun launchBrowser(){
        var expectedIntent : org.hamcrest.Matcher<Intent>? = allOf(
                hasAction(Intent.ACTION_VIEW), //  Intent memliki aksi ACTION_VIEW
                hasData(Uri.parse("https://www.google.com/search?q=supportus")) // Intent memiliki data Uri https://www.google.com/search?q=supportus
        )
        // Klik tombol support
        onView(withId(R.id.SupportText)).perform(click())
        // Jalankan Intent
        intended(expectedIntent)
    }

    // Test untuk mengecekan login dengan username dan password yang kosong tidak dapat berjalan
    @Test
    fun usernameAndPasswordMustNotEmpty(){
        // Kosongkan Username dan Password
        onView(withId(R.id.LUsername)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.LPassword)).perform(typeText(""), closeSoftKeyboard())
        // Buat Object Client dengan Username dan Password yang kosong
        var client = Client("","")

        // Klik Login
        onView(withId(R.id.LoginButton)).perform(click())
        // Jalankan intent dengan component class yang dituju adalah HomeActivtiy dengan package com.example.meetbook
        // dan memiliki Extra berisi Client
        // Gunakan "not" untuk apabila intent tidak dapat dijalankan (username dan password kosong), maka test berhasil
        not(intending(allOf(
                hasComponent(hasShortClassName(".HomeActivity")),
                toPackage("com.example.meetbook"),
                hasExtra("CLIENTDATA123",client))
        ))
    }

    // Test untuk pengecekan keberhasilan login bila username dan password diisi
    @Test
    fun loginIntentHome(){
        // Isi username dan password
        onView(withId(R.id.LUsername)).perform(typeText("client"), closeSoftKeyboard())
        onView(withId(R.id.LPassword)).perform(typeText("123"), closeSoftKeyboard())
        // Buat Object Client dengan Username dan Password
        var client = Client("client","123")

        // Klik Login
        onView(withId(R.id.LoginButton)).perform(click())
        // Jalankan Intent dengan component class yang dituju adalah HomeActivtiy dengan package com.example.meetbook
        // dan memiliki Extra berisi Client
        intending(allOf(
                hasComponent(hasShortClassName(".HomeActivity")),
                toPackage("com.example.meetbook"),
                hasExtra("CLIENTDATA123",client))
        )
    }

    // Pengecekan Intent menuju Register
    @Test
    fun registerIntent(){
        // Klik Register
        onView(withId(R.id.RegisText)).perform(click())
        // Jalankan Intent dengan component class yang dituju adalah RegisterActivtiy dengan package com.example.meetbook
        intending(allOf(
                hasComponent(hasShortClassName(".RegisterActivity")),
                toPackage("com.example.meetbook")
        ))
    }

    // Setelah Test selesai
    @After
    // Untuk penghematan memori, release intent yang sudah tidak digunakan
    fun tearDown(){
        Intents.release()
    }
}