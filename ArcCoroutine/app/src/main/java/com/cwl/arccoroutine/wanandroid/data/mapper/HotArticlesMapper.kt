package com.cwl.arccoroutine.wanandroid.data.mapper

import com.cwl.arccoroutine.wanandroid.data.dto.hotarticles.HotArticlesDto
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo
import org.mapstruct.Mapper
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers

/**

 * @Author cwl

 * @Date 2021/1/5 11:39

 */
@Mapper
interface HotArticlesMapper {

    @Mappings
    fun toHotArticlesVo(hotArticlesDto: HotArticlesDto.Data.DataX): HotArticlesVo

    @Mappings
    fun toHotArticlesVoList(hotArticlesDtoList: List<HotArticlesDto.Data.DataX>): List<HotArticlesVo>
}

//Mappers.getMapper(HotArticlesMapper::class.java)
//HotArticlesMapperImpl() //编译后才有