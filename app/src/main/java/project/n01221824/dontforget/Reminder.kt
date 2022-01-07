package project.n01221824.dontforget

class Reminder {
    var id:Int = 0
    var reminderName: String? = null
    var reminderDate: String? = null
    var reminderDesc: String? = null

    constructor(name: String, date: String, desc: String){
        this.reminderName = name
        this.reminderDate = date
        this.reminderDesc = desc
    }


}