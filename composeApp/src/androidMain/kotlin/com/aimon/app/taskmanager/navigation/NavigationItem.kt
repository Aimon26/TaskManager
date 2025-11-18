package com.aimon.app.taskmanager.navigation

sealed class NavigateItem(
    var route:String,
    var title: String
){
    object taskList: NavigateItem(
        route ="taskList",
        title = "taskList"
    )
    object addTask: NavigateItem(
        route = "addTask",
        title ="addTask"
    )
}