package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.BuyRouterCreateDto;
import com.smile.eam.dto.BuyRouterDeleteQueryDto;
import com.smile.eam.dto.BuyRouterSearchDto;
import com.smile.eam.dto.BuyRouterUpdateDto;
import com.smile.eam.entity.BuyRouter;
import com.smile.eam.mapper.BuyRouterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BuyRouterService {

    @Resource
    BuyRouterMapper buyRouterMapper;

    /**
     * 模糊查找以及获取列表
     */
    public List<BuyRouter> getBuyRouterList(BuyRouterSearchDto buyRouterDto, Pagination pagination) {

        pagination.setTotal(buyRouterMapper.searchCount(buyRouterDto));
        List<BuyRouter> search = buyRouterMapper.search(buyRouterDto, pagination);

        return search;
    }

    /**
     * 新增购入途径
     */
    public int createBuyRouter(BuyRouterCreateDto buyRouterCreateDto) {

        //通过名字找ID判断是否已存在
        if (buyRouterMapper.findIdByName(buyRouterCreateDto.getName()) != null) {
            return -1;
        }
        return buyRouterMapper.createBuyRouter(buyRouterCreateDto);
    }

    /**
     * 删除购入途径
     */
    public void deleteBuyRouter(int id) {
        buyRouterMapper.deleteBuyRouter(id);
        buyRouterMapper.deleteBuyRouterBind(id);
    }

    /**
     * 编辑购入途径
     */
    public int updateBuyRouter(BuyRouterUpdateDto buyRouterUpdateDto) {

        //编辑购入途径名是判断是否已存在
        if ((buyRouterMapper.findIdByName(buyRouterUpdateDto.getName()) != null) &&
                (Integer.parseInt(buyRouterMapper.findIdByName(buyRouterUpdateDto.getName())) != buyRouterUpdateDto.getId())) {
            return -1;
        }
        return buyRouterMapper.updateBuyRouter(buyRouterUpdateDto);
    }

    /**
     * 购入途径详情
     */
    public BuyRouter buyRouterDetail(int id) {
        return buyRouterMapper.findDetailById(id);
    }

    /**
     * 购入途径批量删除
     */
    public void deleteBuyRouters(BuyRouterDeleteQueryDto buyRouterDeleteQueryDto) {
        buyRouterMapper.deleteBuyRoutersBind(buyRouterDeleteQueryDto.getIds());
        buyRouterMapper.deleteBuyRouters(buyRouterDeleteQueryDto.getIds());
    }

}
