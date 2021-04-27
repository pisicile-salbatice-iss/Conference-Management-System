package domain

import java.sql.Time

data class Session (val sessionId: Int,val confereceId: Int ,val topic: String, val hour:Time){
    override fun toString(): String {
        return topic
    }
}