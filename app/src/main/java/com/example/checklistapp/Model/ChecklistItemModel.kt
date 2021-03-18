package com.example.checklistapp.Model

class ChecklistItemModel(
    val id:Int,
    val nama:String,
    var status:Boolean
) {

    fun setStatus(){
        this.status = true
    }
}