package domain

import java.sql.Time

data class Session (val sessionId: Int, val conferenceId: Int, val topic: String, val hour:Time){
    override fun toString(): String {
        return topic
    }
}