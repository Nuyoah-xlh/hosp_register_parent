package com.xlh.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.sun.org.apache.xml.internal.dtm.ref.DTMChildIterNodeList;
import com.sun.webkit.graphics.WCRenderQueue;
import com.xlh.yygh.cmn.listener.DictListener;
import com.xlh.yygh.cmn.mapper.DictMapper;
import com.xlh.yygh.cmn.service.DictService;
import com.xlh.yygh.common.result.Result;
import com.xlh.yygh.enums.DictEnum;
import com.xlh.yygh.model.cmn.Dict;
import com.xlh.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    //允许缓存
    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(long id) {
        QueryWrapper<Dict> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList=baseMapper.selectList(wrapper);
        for(Dict dict:dictList){
            Long dictId=dict.getId();
            boolean isChild=this.hasChildren(dictId);
            dict.setHasChildren(isChild);
        }
        return dictList;
    }

    //判断id下是否有子节点
    private boolean hasChildren(Long id){
        QueryWrapper<Dict> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count=baseMapper.selectCount(wrapper);
        return count>0;
    }

    //导出数据
    @Override
    public void exportDictData(HttpServletResponse httpServletResponse) {
        //设置下载信息
        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setCharacterEncoding("utf-8");
        String filename="dict";
        httpServletResponse.setHeader("Content-disposition","attachment;filename="+filename+".xlsx");
        //查询数据库
        List<Dict> dictList=baseMapper.selectList(null);
        //将Dict转为DictEeVo
        List<DictEeVo> dictEeVos=new ArrayList<>();
        for(Dict dict:dictList){
            DictEeVo dictEeVo=new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo);
            dictEeVos.add(dictEeVo);
        }
        //写操作
        try {
            EasyExcel.write(httpServletResponse.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictEeVos);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //导入数据
    @Override
    @CacheEvict(value = "dict", allEntries=true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getName(String dictCode, String value) {
        if(StringUtils.isEmpty(dictCode)){
            //只根据value查询
            QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("value",value);
            Dict dict=baseMapper.selectOne(queryWrapper);
            return dict.getName();
        }
        else{
            Dict dict=getDictByDictCode(dictCode);
            Long parent_id=dict.getId();
            Dict dictFinal=baseMapper.selectOne(new QueryWrapper<Dict>().eq("parent_id",parent_id).eq("value",value));
            return dictFinal.getName();
        }
    }

    private Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dict_code",dictCode);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dict获取对应行的数据
        Dict dict=this.getDictByDictCode(dictCode);
        //根据id获取子节点
        List<Dict> list=this.findChildData(dict.getId());
        return list;
    }
}
