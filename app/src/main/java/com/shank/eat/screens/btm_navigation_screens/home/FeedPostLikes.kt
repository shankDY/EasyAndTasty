package com.shank.eat.screens.btm_navigation_screens.home

//отдельный класс, для подсчета лайков. первый параметр количество лайков,
// второй поставлен лайк или нет
data class FeedPostLikes(val likesCount: Int, val likedByUser: Boolean)