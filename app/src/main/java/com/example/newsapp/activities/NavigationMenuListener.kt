package com.example.newsapp.activities

import android.content.Intent
import com.example.newsapp.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

object NavigationMenuListener {

    fun setListener(navView: NavigationView, activity: NewsActivity) {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val homeIntent = Intent(activity, NewsActivity::class.java)
                    activity.startActivity(homeIntent)
                    true
                }
                R.id.nav_profile -> {
                    true
                }
                R.id.nav_settings -> {
                    true
                }
                R.id.nav_logout -> {
                    val logoutIntent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(logoutIntent)

                    Firebase.auth.signOut()
                    activity.finish() // Signs user out and finishes the current active activity

                    true
                }

                else -> true
            }
        }
    }
}