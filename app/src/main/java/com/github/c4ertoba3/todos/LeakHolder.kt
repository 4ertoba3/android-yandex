package com.github.c4ertoba3.todos

import android.app.Activity

object LeakHolder {
    val leaked = mutableListOf<Activity>()
}