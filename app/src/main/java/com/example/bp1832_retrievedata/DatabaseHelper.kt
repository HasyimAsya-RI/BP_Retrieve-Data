package com.example.bp1832_retrievedata

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.example.bp1832_retrievedata.model.MenuModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class DatabaseHelper( var context: Context ): SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    /**  Deklarasi Objek  **/
    companion object {
        private val DATABASE_NAME     = "bp1832_retrieveData"
        private val DATABASE_VERSION  = 1

        // Table and Column Accoount
        private val TABLE_ACCOUNT     = "account"
        private val COLUMN_EMAIL      = "email"
        private val COLUMN_NAME       = "name"
        private val COLUMN_LEVEL      = "level"
        private val COLUMN_PASSWORD   = "password"

        // Tabble and Column Menu
        private val TABLE_MENU        = "menu"
        private val COLUMN_ID_MENU    = "idMenu"
        private val COLUMN_NAMA_MENU  = "menuName"
        private val COLUMN_PRICE_MENU = "price"
        private val COLUMN_IMAGE      = "photo"
    }

    /**  Deklarasi SQL Query untuk Membuat dan Menghapus Tabel  **/
    // Create Table SQL Query
    private val CREATE_ACCOUNT_TABLE = (
            "CREATE TABLE " + TABLE_ACCOUNT + "("
                    + COLUMN_EMAIL      + " TEXT PRIMARY KEY, "
                    + COLUMN_NAME       + " TEXT, "
                    + COLUMN_LEVEL      + " TEXT, "
                    + COLUMN_PASSWORD   + " TEXT)"
            )
    private val CREATE_MENU_TABLE = (
            "CREATE TABLE " + TABLE_MENU + "("
                    + COLUMN_ID_MENU    + " INT PRIMARY KEY, "
                    + COLUMN_NAMA_MENU  + " TEXT, "
                    + COLUMN_PRICE_MENU + " INT, "
                    + COLUMN_IMAGE      + " BLOP)"
            )

    // Drop Table SQL Query
    private val DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS $TABLE_ACCOUNT"
    private val DROP_MENU_TABLE    = "DROP TABLE IF EXISTS $TABLE_MENU"

    /**  Baris Kode untuk Fungsi onCreate() dan onUpgrade()  **/
    override fun onCreate( p0: SQLiteDatabase? ) {
        p0?.execSQL( CREATE_ACCOUNT_TABLE )
        p0?.execSQL( CREATE_MENU_TABLE )
    }
    override fun onUpgrade( p0: SQLiteDatabase?, p1: Int, p2: Int ) {
        p0?.execSQL( DROP_ACCOUNT_TABLE )
        p0?.execSQL( DROP_MENU_TABLE )
        onCreate( p0 )
    }



    /**  CREATE  **/
    fun addAccount( email:String, name:String, level:String, password:String ) {
        val db     = this.writableDatabase

        // Input Data
        val values = ContentValues()
        values.put( COLUMN_EMAIL,    email )
        values.put( COLUMN_NAME,     name )
        values.put( COLUMN_LEVEL,    level )
        values.put( COLUMN_PASSWORD, password )

        val result = db.insert( TABLE_ACCOUNT, null, values )

        // Menampilkan Informasi
        if( result == (0).toLong() ) {
            Toast.makeText( context, "Register gagal!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Register berhasil. " + "Silakan masuk menggunakan akun baru Anda.", Toast.LENGTH_SHORT ).show()
        }
        db.close()
    }
    fun addMenu( menu:MenuModel ) {
        val db     = this.writableDatabase

        // Input Data
        val values = ContentValues()
        values.put( COLUMN_ID_MENU,    menu.id )
        values.put( COLUMN_NAMA_MENU,  menu.name )
        values.put( COLUMN_PRICE_MENU, menu.price )

        // Input Gambar
        val byteOutputStream = ByteArrayOutputStream()
        val imageInByte: ByteArray
        menu.image.compress( Bitmap.CompressFormat.JPEG, 100, byteOutputStream )
        imageInByte = byteOutputStream.toByteArray()
        values.put( COLUMN_IMAGE,      imageInByte )

        val result = db.insert( TABLE_MENU, null, values )

        // Menampilkan Informasi
        if( result == (0).toLong() ) {
            Toast.makeText( context, "Menu gagal ditambahkan!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Menu berhasil ditambahkan.", Toast.LENGTH_SHORT ).show()
        }
        db.close()
    }

    /**  READ  **/
    @SuppressLint( "Range" )
    fun showMenu(): ArrayList<MenuModel> {
        // Menampung Data
        val listMenu = ArrayList<MenuModel>()
        val db       = this.readableDatabase
        var cursor: Cursor?=null

        // Execute Query
        try {
            cursor = db.rawQuery( "SELECT * FROM " + TABLE_MENU, null )
        }
        catch( se: SQLiteException) {
            db.execSQL( CREATE_MENU_TABLE )
            return ArrayList()
        }

        var id:         Int
        var name:       String
        var price:      Int
        var imageArray: ByteArray
        var imageBmp:   Bitmap

        if( cursor.moveToFirst() ) {
            do {
                // Get Data Text
                id    = cursor.getInt( cursor.getColumnIndex( COLUMN_ID_MENU ) )
                name  = cursor.getString(  cursor.getColumnIndex( COLUMN_NAMA_MENU ) )
                price = cursor.getInt( cursor.getColumnIndex( COLUMN_PRICE_MENU ) )

                // Get Data Image
                imageArray = cursor.getBlob( cursor.getColumnIndex( COLUMN_IMAGE ) )

                // Convert ByteArray to Bitmap
                val byteInputStram = ByteArrayInputStream( imageArray )
                imageBmp           = BitmapFactory.decodeStream( byteInputStram )
                val model          = MenuModel( id = id, name = name, price = price, image = imageBmp )
                listMenu.add( model )
            }
            while( cursor.moveToNext() )
        }
        return  listMenu
    }

    /**  UPDATE  **/
    fun updateAccount( email:String, name:String, level:String, password:String ) {
        val db     = this.writableDatabase

        // Input Data
        val values = ContentValues()
        values.put( COLUMN_EMAIL,    email )
        values.put( COLUMN_NAME,     name )
        values.put( COLUMN_LEVEL,    level )
        values.put( COLUMN_PASSWORD, password )

        val result = db.update( TABLE_ACCOUNT, values, COLUMN_EMAIL + " = ? ", arrayOf( email ) ).toLong()

        // Menampilkan Informasi
        if( result == (0).toLong() ) {
            Toast.makeText( context, "Akun gagal diperbarui!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Akun berhasil diperbarui.", Toast.LENGTH_SHORT ).show()
        }
        db.close()
    }
    fun updateMenu( menu:MenuModel ) {
        val db     = this.writableDatabase

        // Input Data
        val values = ContentValues()
        values.put( COLUMN_ID_MENU,    menu.id )
        values.put( COLUMN_NAMA_MENU,  menu.name )
        values.put( COLUMN_PRICE_MENU, menu.price )

        // Input Gambar
        val byteOutputStream = ByteArrayOutputStream()
        val imageInByte: ByteArray
        menu.image.compress( Bitmap.CompressFormat.JPEG, 100, byteOutputStream )
        imageInByte = byteOutputStream.toByteArray()
        values.put( COLUMN_IMAGE,      imageInByte )

        val result = db.update( TABLE_MENU, values, COLUMN_ID_MENU + " = ? ", arrayOf( menu.id.toString() ) ).toLong()

        // Menampilkan Informasi
        if( result == (0).toLong() ) {
            Toast.makeText( context, "Menu gagal diperbarui!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Menu berhasil diperbarui.", Toast.LENGTH_SHORT ).show()
        }
        db.close()
    }

    /**  DELETE  **/
    fun deleteMenu( id:Int ) {
        val db     = this.writableDatabase

        val result = db.delete( TABLE_MENU, COLUMN_ID_MENU + " = ? ", arrayOf( id.toString() ) ).toLong()

        // Menampilkan Informasi
        if( result == (0).toLong() ) {
            Toast.makeText( context, "Menu gagal dihapus!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Menu berhasil dihapus.", Toast.LENGTH_SHORT ).show()
        }
        db.close()
    }



    /**  Fungsi untuk Mengecek Data  **/
    @SuppressLint("Range")
    fun checkData( email:String ): String {
        val colums        = arrayOf( COLUMN_NAME )
        val db            = this.readableDatabase
        val selection     = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf( email )
        var name: String  = ""

        val cursor = db.query(
            TABLE_ACCOUNT,     // Table to Query
            colums,        // Colums to Return
            selection,     // Colums for Where Clause
            selectionArgs, // The Values for Where Clause
            null,  // Group by Rows
            null,   // Filter by Row Groups
            null   // The Sort Order
        )

        if( cursor.moveToFirst() ) {
            name = cursor.getString( cursor.getColumnIndex( COLUMN_NAME ) )
        }
        cursor.close()
        return name
    }

    /**  Fungsi untuk Melakukan Validasi Login  **/
    @SuppressLint("Range")
    fun checkLogin(email:String, password:String): Boolean {
        val colums        = arrayOf( COLUMN_EMAIL, COLUMN_NAME, COLUMN_LEVEL, COLUMN_PASSWORD )
        val db            = this.readableDatabase
        val selection     = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf( email, password )

        val cursor        = db.query(
            TABLE_ACCOUNT,     // Table to Query
            colums,        // Colums to Return
            selection,     // Colums for Where Clause
            selectionArgs, // The Values for Where Clause
            null,  // Group by Rows
            null,   // Filter by Row Groups
            null   // The Sort Order
        )

        val cursorCount = cursor.count

        // Check Data Available or Not
        val result: Boolean
        if( cursorCount > 0 ) {
            result = true
            // Set Data
            if( cursor.moveToFirst() ) {
                FragmentProfile.email    = cursor.getString(cursor.getColumnIndex( COLUMN_EMAIL ) )
                FragmentProfile.name     = cursor.getString(cursor.getColumnIndex( COLUMN_NAME ) )
                FragmentProfile.level    = cursor.getString(cursor.getColumnIndex( COLUMN_LEVEL ) )
                FragmentProfile.password = cursor.getString(cursor.getColumnIndex( COLUMN_PASSWORD ) )
            }
        }
        else {
            return false
        }
        cursor.close()
        db.close()
        return result
    }
}