package com.icode.cas.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.icode.cas.common.response.ResponseData;
import com.icode.cas.common.response.ResponseVerifyData;
import com.icode.cas.common.response.tree.TreeDictionaryResponse;
import com.icode.cas.repository.dto.CasSysDictionaryDTO;
import com.icode.cas.repository.entity.CasSysDictionary;
import com.icode.cas.repository.qo.CasSysDictionaryQO;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author xiachong
 * @since 2018-07-08
 */
public interface ICasSysDictionaryService extends IService<CasSysDictionary> {

    /**
     * Title: 获取数据字典结构树<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:36<br>
     * Param: []<br>
     * Return: com.icode.cas.common.response.tree.TreeDictionaryResponse
     */
    TreeDictionaryResponse getDictionaryTree();


    /**
     * Title: 按条件查询数据字典列表<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:41<br>
     * Param: [qo]<br>
     * Return: com.baomidou.mybatisplus.plugins.Page<com.icode.cas.repository.dto.CasSysDictionaryDTO>
     */
    Page<CasSysDictionaryDTO> doDictionaryListById(CasSysDictionaryQO qo);

    /**
     * Title: 新增or编辑保存<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:42<br>
     * Param: [casSysDictionary]<br>
     * Return: java.lang.Boolean
     */
    Boolean saveOrUpdateDictionary(CasSysDictionary casSysDictionary);

    /**
     * Title: 验证数据字典key唯一性<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:36<br>
     * Param: [id, itemKey, fields]<br>
     * Return: com.icode.cas.common.response.ResponseVerifyData
     */
    ResponseVerifyData doVerifyDictionary(Integer id,String itemKey, String fields);

    /**
     * Title: 按字典id删除(逻辑or物理)<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:42<br>
     * Param: [id, type]<br>
     * Return: com.icode.cas.common.response.ResponseData
     */
    ResponseData doDeleteOrForbiddenDictionary(Integer id, String type);
}
