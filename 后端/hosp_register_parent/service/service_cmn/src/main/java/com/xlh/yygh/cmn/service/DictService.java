package com.xlh.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xlh.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(long id);

    //导出
    void exportDictData(HttpServletResponse httpServletResponse);

    //导入
    void importDictData(MultipartFile file);

    //获取名称
    String getName(String dictCode, String value);

    //根据dictCode查询下级节点
    List<Dict> findByDictCode(String dictCode);
}
