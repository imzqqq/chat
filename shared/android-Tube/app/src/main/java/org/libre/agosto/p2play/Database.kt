package org.libre.agosto.p2play

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import org.libre.agosto.p2play.models.TokenModel
import org.libre.agosto.p2play.models.UserModel

class Database(context:Context): SQLiteOpenHelper(context,"p2play",null,1) {
    val dbName = "p2play"

    private val dbUsers = "CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, uuid INTEGER, username varchar(30), " +
            "nsfw INTEGER, email string, followers INTEGER, avatar string, status integer)"
    private val dbTokens = "CREATE TABLE tokens(id INTEGER PRIMARY KEY AUTOINCREMENT, token STRING, refresh_token STRING, status INTEGER)"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(dbUsers)
        db?.execSQL(dbTokens)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE users")
        db?.execSQL("DROP TABLE tokens")
        onCreate(db)
    }

    fun newToken(token: TokenModel): Boolean {
        val db = writableDatabase
        this.closeTokens()
        val newToken=ContentValues()
        newToken.put("token", token.token)
        newToken.put("refresh_token", token.refresh_token)
        newToken.put("status", token.status)

        db.insert("tokens",null,newToken)

        return true
    }

    fun newUser(user: UserModel): Boolean {
        val db = writableDatabase
        this.closeUsers()
        val newUser=ContentValues()
        newUser.put("uuid", user.uuid)
        newUser.put("username", user.username)
        newUser.put("email", user.email)
        newUser.put("nsfw", user.nsfw)
        newUser.put("followers", user.followers)
        newUser.put("avatar", user.avatar)
        newUser.put("status", user.status)

        db.insert("users",null, newUser)

        return true
    }

    fun getToken(): TokenModel{
        val db = writableDatabase
        var token = TokenModel()

        try {
            var cursor= db.rawQuery("SELECT * FROM tokens WHERE status=1 ORDER BY id DESC LIMIT 1",null)

            if(cursor.count != 0){
                cursor.moveToFirst()

                token.token = cursor.getString(cursor.getColumnIndex("token")).toString()
                token.refresh_token = cursor.getString(cursor.getColumnIndex("refresh_token")).toString()
                token.status = cursor.getString(cursor.getColumnIndex("status")).toInt()
            }
            cursor.close()

            return token

        }catch (e:SQLiteException){
            db?.execSQL(dbTokens)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return token
    }

    fun getUser(): UserModel{
        val db = writableDatabase
        var user = UserModel()

        try {
            var cursor= db.rawQuery("SELECT * FROM users WHERE status=1 ORDER BY id DESC LIMIT 1",null)

            if(cursor.count != 0){
                cursor.moveToFirst()

                user.uuid = cursor.getString(cursor.getColumnIndex("uuid")).toInt()
                user.username = cursor.getString(cursor.getColumnIndex("username")).toString()
                user.email = cursor.getString(cursor.getColumnIndex("email")).toString()
                user.nsfw = cursor.getString(cursor.getColumnIndex("nsfw")).toBoolean()
                user.followers = cursor.getString(cursor.getColumnIndex("followers")).toInt()
                user.avatar = cursor.getString(cursor.getColumnIndex("avatar")).toString()
                user.status = cursor.getString(cursor.getColumnIndex("status")).toInt()
            }

            cursor.close()

            return user

        }catch (e:SQLiteException){
            db?.execSQL(dbTokens)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return user
    }

    private fun closeTokens(){
        val db = writableDatabase
        db.execSQL("UPDATE tokens SET status=-1 WHERE 1")
    }

    private fun closeUsers(){
        val db = writableDatabase
        db.execSQL("UPDATE users SET status=-1 WHERE 1")
    }

    fun logout(){
        closeUsers()
        closeTokens()
    }

}