package com.blooburn.owere.user.item

//채팅방별 메시지 데이터
//@make@userId@time@~~~~ 채팅방ID
data class ChatItem(
    val profileImg: String,
    val message: String,
    val timestamp: String,
    val uid: String,
    val userName: String
){
    constructor() : this("","","","","")
}

/* RoomUser(채팅방 별 유저리스트)
    채팅방 ID
        userId1 true
        userId2 true

   UserRooms(
 */
