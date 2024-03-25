package com.example.userinfo

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.liningkang.base.BaseApplication
import com.liningkang.common.bean.UserInfo

@Database(entities = [UserInfo::class], version = 4, exportSchema = true)
@TypeConverters(Converters::class)
abstract class UserInfoDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "UserInfo.db"
        var instance: UserInfoDatabase =
            Room.databaseBuilder(
                BaseApplication.context!!,
                UserInfoDatabase::class.java,
                DATABASE_NAME
            )
                 .allowMainThreadQueries()// 允许在主线程操作数据库
                //  .addMigrations(MIGRATION_1_2)// 数据库升级时
                .fallbackToDestructiveMigration()// 数据库版本异常时 会清空原来的数据  然后转到目前版本
                //  .createFromAsset("资源文件中的初始数据库")// 预填充数据库  初始数据
                .build();

    }

    abstract fun userDao(): UserDao

}