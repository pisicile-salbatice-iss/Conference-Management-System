package domain

import java.sql.Date


data class Conference (val id: Int, val name: String, val date: Date, val attendancePrice: Int, val submitPaperDeadline: Date, val reviewPaperDeadline: Date, val biddingPhaseDeadline: Date) {
    override fun toString(): String {
        return "$name at $date costs $attendancePrice"
    }
}
