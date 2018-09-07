package com.icode.cas.controller.system;


import com.baomidou.mybatisplus.plugins.Page;
import com.icode.cas.common.constant.PathFinal;
import com.icode.cas.common.constant.ResponseFinal;
import com.icode.cas.common.response.ResponseData;
import com.icode.cas.common.response.ResponseDataTable;
import com.icode.cas.common.response.ResponseUtil;
import com.icode.cas.common.response.ResponseVerifyData;
import com.icode.cas.common.response.tree.TreeDictionaryResponse;
import com.icode.cas.repository.dto.CasSysDictionaryDTO;
import com.icode.cas.repository.entity.CasSysDictionary;
import com.icode.cas.repository.qo.CasSysDictionaryQO;
import com.icode.cas.repository.vo.CasSysDictionaryVO;
import com.icode.cas.service.ICasSysDictionaryService;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author xiachong
 * @since 2018-07-08
 */
@Controller
@RequestMapping("/casSysDictionary")
public class CasSysDictionaryController {

    private static Logger log = LoggerFactory.getLogger(CasSysDictionaryController.class);

    @Autowired
    private ICasSysDictionaryService iCasSysDictionaryService;

    /**
     * Title: 跳转数据字典首页<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:37<br>
     * Param: []<br>
     * Return: java.lang.String
     */
    @RequestMapping("/sys_dictionary_idx")
    public String idx() {
        return PathFinal.PATH_PAGE_DICTIONARY_IDX;
    }

    /**
     * Title: 新增 or 编辑 弹窗<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:38<br>
     * Param: [request, parentId, id]<br>
     * Return: java.lang.String
     */
    @RequestMapping("/pop_add_v_edit_dictionary")
    public String popAddOrEditDictionary(HttpServletRequest request, Integer parentId, Integer id) {
        CasSysDictionary casSysDictionary = new CasSysDictionary();
        CasSysDictionary parentDict = new CasSysDictionary();
        if( parentId != null ){
            //新增
            parentDict = iCasSysDictionaryService.selectById(parentId);
            casSysDictionary.setParentId(parentId);
        } else {
            //编辑
            casSysDictionary = iCasSysDictionaryService.selectById(id);
            parentDict = iCasSysDictionaryService.selectById(casSysDictionary.getParentId());
        }
        request.setAttribute("parentDict",parentDict);
        request.setAttribute("casSysDictionary",casSysDictionary);
        return PathFinal.PATH_PAGE_POP_ADD_V_EDIT_DICTIONARY;
    }

    /**
     * Title: 新增or编辑保存<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:38<br>
     * Param: [vo, bindingResult]<br>
     * Return: ResponseData
     */
    @RequestMapping("/do_save_v_update_dictionary")
    @ResponseBody
    public ResponseData doSaveOrUpdateDictionary(@Valid CasSysDictionaryVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseUtil.paramInvalidError(bindingResult.getFieldErrors());
        } else {
            CasSysDictionary casSysDictionary = new CasSysDictionary();
            try {
                PropertyUtils.copyProperties(casSysDictionary, vo);
                Boolean boo = iCasSysDictionaryService.saveOrUpdateDictionary(casSysDictionary);
                if (boo) {
                    return ResponseUtil.success(casSysDictionary,ResponseFinal.SAVE_OK);
                }
            } catch (Exception e) {
                log.error("do_save_v_update_dictionary,err--{}", e);
                return ResponseUtil.businessError(ResponseFinal.SAVE_COME_TO_NOTHING);
            }
        }
        return null;
    }

    /**
     * Title: 校验数据唯一性<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:39<br>
     * Param: [id, itemKey, fields]<br>
     * Return: com.icode.cas.common.response.ResponseVerifyData
     */
    @RequestMapping("/do_verify_dictionary")
    @ResponseBody
    public ResponseVerifyData doVerifyDictionary(Integer id, String itemKey , String fields) {
        return iCasSysDictionaryService.doVerifyDictionary(id,itemKey,fields);
    } 

    /**
     * Title: 按字典id删除(逻辑or物理)<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:39<br>
     * Param: [id, type]<br>
     * Return: com.icode.cas.common.response.ResponseData
     */
    @RequestMapping("/do_delete_v_forbidden_dictionary")
    @ResponseBody
    public ResponseData doDeleteOrForbiddenDictionary(Integer id,String type) {
        return iCasSysDictionaryService.doDeleteOrForbiddenDictionary(id,type);
    }


    /**
     * Title: 获取数据字典结构树<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:40<br>
     * Param: []<br>
     * Return: com.icode.cas.common.response.tree.TreeDictionaryResponse
     */
    @RequestMapping(value = "ajax/dictionary_tree", produces = "application/json")
    @ResponseBody
    public TreeDictionaryResponse getDictionaryTree() {
        return iCasSysDictionaryService.getDictionaryTree();
    }


    /**
     * Title: 按条件查询数据字典列表<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 18:40<br>
     * Param: [qo]<br>
     * Return: com.icode.cas.common.response.ResponseDataTable
     */
    @RequestMapping(value = "doDictionaryListById")
    @ResponseBody
    public ResponseDataTable doDictionaryListById(CasSysDictionaryQO qo) {
        Page<CasSysDictionaryDTO> pageDto  = iCasSysDictionaryService.doDictionaryListById(qo);
        return new ResponseDataTable(true, pageDto);
    }
}

