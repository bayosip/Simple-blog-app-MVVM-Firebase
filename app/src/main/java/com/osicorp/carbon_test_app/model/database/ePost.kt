package com.osicorp.carbon_test_app.model.database

import com.osicorp.carbon_test_app.model.Post
import java.util.*

class ePost(db:DbPost): Post() {


    fun getDbObject():DbPost{
        return DbPost(this.postId!!, this.Title!!, this.Url, this.MediaType!!,
            this.TimeStamp!!.time)
    }
//    /*Comparator for sorting the list by roll no*/
//    var postComparator: Comparator<Post> =
//        object : Comparator<Post> {
//            override fun compare(o1: Post?, o2: Post?): Int {
////                TODO("Not yet implemented")
//                val time1: Long? = o1?.TimeStamp?.getTime()
//                val time2: Long? = o2?.TimeStamp?.getTime()
//                return if (time1!! > time2!!) 1
//                else if (time1 < time2) -1
//                else 0
//            } }

}