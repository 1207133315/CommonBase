package com.example.userinfo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.liningkang.common.bean.UserInfo
import org.jetbrains.annotations.NotNull

@Dao
interface UserDao {
    @Query("SELECT * FROM UserInfo WHERE userId = (:id)")
    fun getUserInfoById(id: String): UserInfo?

    @Insert
    fun insertUserInfo(userInfo: UserInfo?)

    @Update
    fun update(userInfo: UserInfo?)

    @Delete
    fun delete(userInfo: UserInfo?)
}