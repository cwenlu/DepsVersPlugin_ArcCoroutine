package com.cwl.arccoroutine.test.room

import androidx.room.*
import java.util.*

/**

 * @Author cwl

 * @Date 2020/12/21 15:58

 */
//表名，列名在数据库里不区分大小写
@Entity(tableName = "user")//默认类名 //primaryKeys 复合主键  indices索引
data class User(
    @PrimaryKey(autoGenerate = true)
    var id:Long=0,
    //修改表中列名
    @ColumnInfo(name = "user_account") val account:String,
    val password:String,
    val name:String,
    //flat 字段到表中
    @Embedded val address:Address,
    //忽略，不存
    @Ignore val state: Int
)

data class Address(
    val street:String,val state:String,val city:String,@ColumnInfo(name = "post_code")val postCode:String
)

@Entity(tableName = "shoe")
data class Shoe(
    @ColumnInfo(name = "shoe_name") val name: String // 鞋名
    , @ColumnInfo(name = "shoe_description") val description: String// 描述
    , @ColumnInfo(name = "shoe_price") val price: Float // 价格
    , @ColumnInfo(name = "shoe_brand") val brand: String // 品牌
    , @ColumnInfo(name = "shoe_imgUrl") val imageUrl: String // 图片地址
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}

@Entity(
    tableName = "fav_shoe"
    , foreignKeys = [ForeignKey(entity = Shoe::class, /*from*/parentColumns = ["id"], /*to*/childColumns = ["shoe_id"])//onUpdate  onDelete 级联更新/删除
        , ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"])
    ],indices = [Index("shoe_id")]
)
data class FavouriteShoe(
    @ColumnInfo(name = "shoe_id") val shoeId: Long // 外键 鞋子的id
    , @ColumnInfo(name = "user_id") val userId: Long // 外键 用户的id
    , @ColumnInfo(name = "fav_date") val date: Date // 创建日期

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}