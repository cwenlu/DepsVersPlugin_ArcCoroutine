package com.cwl.arccoroutine.jectpack.room

import androidx.lifecycle.LiveData
import androidx.room.*

/**

 * @Author cwl

 * @Date 2020/12/21 16:56

 */
@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)
}

@Dao
interface ShoeDao {
    // 增加一双鞋子,冲突策略
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoe(shoe: Shoe)

    // 增加多双鞋子
    // 除了List之外，也可以使用数组
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoes(shoes: List<Shoe>)

    @Delete
    fun deleteShoe(shoe: Shoe)

    @Update
    fun updateShoe(shoe: Shoe)

    @Query("select * from shoe where id=:id")
    fun queryShoeById(id:Long):Shoe?

    @Query("select * from shoe where shoe_name like :name order by shoe_brand asc")
    fun queryShoesByName(name:String):List<Shoe>

    @Query("select * from shoe")
    fun queryShoes2LiveData():LiveData<List<Shoe>>

    @Query(
        "SELECT shoe.id,shoe.shoe_name,shoe.shoe_description,shoe.shoe_price,shoe.shoe_brand,shoe.shoe_imgUrl " +
                "FROM shoe " +
                "INNER JOIN fav_shoe ON fav_shoe.shoe_id = shoe.id " +
                "WHERE fav_shoe.user_id = :userId"
    )
    fun queryShoesByUserId(userId:Long):List<Shoe>
}