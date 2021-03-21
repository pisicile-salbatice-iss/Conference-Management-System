package domain

enum class ROLE {
    AUTHOR,
    CHAIR,
    CO_CHAIR,
    REVIEWER,
    PC_MEMBER,
    SC_MEMBER,
    SESSION_CHAR,
    LISTENER,
    SPEAKER
}

data class UserConference(val userId: Int, val conferenceId: Int, val roles: List<ROLE>, val paid: Boolean)