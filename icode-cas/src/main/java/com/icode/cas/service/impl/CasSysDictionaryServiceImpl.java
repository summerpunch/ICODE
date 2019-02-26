package com.icode.cas.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icode.cas.common.constant.DatabaseFinal;
import com.icode.cas.common.constant.ResponseFinal;
import com.icode.cas.common.response.ResponseData;
import com.icode.cas.common.response.ResponseUtil;
import com.icode.cas.common.response.ResponseVerifyData;
import com.icode.cas.common.response.tree.TreeDictionaryNode;
import com.icode.cas.common.response.tree.TreeDictionaryResponse;
import com.icode.cas.repository.dto.CasSysDictionaryDTO;
import com.icode.cas.repository.entity.CasSysDictionary;
import com.icode.cas.repository.mapper.CasSysDictionaryMapper;
import com.icode.cas.repository.qo.CasSysDictionaryQO;
import com.icode.cas.service.ICasSysDictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author xiachong
 * @since 2018-07-08
 */
@Service
public class CasSysDictionaryServiceImpl extends ServiceImpl<CasSysDictionaryMapper, CasSysDictionary> implements ICasSysDictionaryService {

    private List<CasSysDictionary> dictionaryList = null;
    private List<CasSysDictionary> removeDictionaryList = new ArrayList<>();

    @Override
    public TreeDictionaryResponse getDictionaryTree() {
        TreeDictionaryResponse treeDictionaryResponse = new TreeDictionaryResponse();
        EntityWrapper<CasSysDictionary> wrapper = new EntityWrapper<>();
       // TODO wrapper.eq(DatabaseFinal.CAS_SYS_DICTIONARY_V_STATUS,34);
        dictionaryList = selectList(wrapper);

        List<TreeDictionaryNode> nodes = new ArrayList<>();
        dictionaryList.stream().filter(cd -> !removeDictionaryList.contains(cd)).forEach(cd -> {
            nodes.add(facadeTree(cd));
        });

        treeDictionaryResponse.setData(nodes);
        return treeDictionaryResponse;
    }

    private TreeDictionaryNode facadeTree(CasSysDictionary dictionary) {
        TreeDictionaryNode node = new TreeDictionaryNode();
        List<TreeDictionaryNode> nodeList = new ArrayList<>();

        dictionaryList.stream().filter(cd -> dictionary.getId().equals(cd.getParentId())).forEach(cd -> {
            nodeList.add(facadeTree(cd));
            node.setNodes(nodeList);
            removeDictionaryList.add(cd);
        });

        node.setId(dictionary.getId());
        node.setText(dictionary.getItemNamecn());
        return node;
    }

    @Override
    public Page<CasSysDictionaryDTO> doDictionaryListById(CasSysDictionaryQO qo) {
        //搜索条件
        Map<String, Object> condition = new HashMap<>();
        EntityWrapper<CasSysDictionary> entityWrapper = new EntityWrapper<>();

        if (qo.getId() != null) {
            entityWrapper.eq(DatabaseFinal.CAS_SYS_DICTIONARY_V_PARENT_ID, qo.getId());
        }

        if (qo.getStatus() != null) {
            entityWrapper.eq(DatabaseFinal.CAS_SYS_DICTIONARY_V_STATUS, qo.getStatus());
        } else {
            // TODO entityWrapper.eq(DatabaseFinal.CAS_SYS_DICTIONARY_V_STATUS, 34);
        }

        if (StringUtils.isNotBlank(qo.getItemNamecn())) {
            entityWrapper.like(DatabaseFinal.CAS_SYS_DICTIONARY_V_ITEM_NAMECN, qo.getItemNamecn());
        }

        List<CasSysDictionaryDTO> resourceDTO = new ArrayList<>();
        Page<CasSysDictionary> page = new Page<>(qo.getPageIndex(), qo.getPageSize());

        page.setCondition(condition);
        page = selectPage(page, entityWrapper);

        for (CasSysDictionary cd : page.getRecords()) {
            CasSysDictionaryDTO dto = new CasSysDictionaryDTO();
            BeanUtils.copyProperties(cd, dto);
            resourceDTO.add(dto);
        }

        Page<CasSysDictionaryDTO> pageDto = new Page<>();
        BeanUtils.copyProperties(page, pageDto);
        pageDto.setRecords(resourceDTO);
        return pageDto;
    }

    @Override
    public Boolean saveOrUpdateDictionary(CasSysDictionary casSysDictionary) {
        if (casSysDictionary.getId() != null) {
            casSysDictionary.setAdminCreate(1);
            casSysDictionary.setUpdateTime(new Date());
        } else {
            casSysDictionary.setCreateTime(new Date());
            casSysDictionary.setAdminUpdate(1);
        }
        return insertOrUpdate(casSysDictionary);
    }

    @Override
    public ResponseVerifyData doVerifyDictionary(Integer id, String itemKey, String fields) {
        ResponseVerifyData responseVerifyData = new ResponseVerifyData();
        EntityWrapper<CasSysDictionary> dictCount = new EntityWrapper();
        dictCount.eq(DatabaseFinal.CAS_SYS_DICTIONARY_V_ITEM_KEY, itemKey.trim());
        if (id != null) {
            dictCount.notIn(DatabaseFinal.CAS_SYS_DICTIONARY_V_ID, id);
        }
        List<CasSysDictionary> list = selectList(dictCount);
        if (!list.isEmpty()) {
            responseVerifyData.setValid(false);
        } else {
            responseVerifyData.setValid(true);
        }
        return responseVerifyData;
    }

    @Override
    public ResponseData doDeleteOrForbiddenDictionary(Integer id, String type) {
        CasSysDictionary casSysDictionary = selectById(id);
        if (casSysDictionary != null) {
            switch (type) {
                case ResponseFinal.FORBIDDEN:
                    casSysDictionary.setStatus(35);
                    if (updateById(casSysDictionary)) {
                        return ResponseUtil.success(null, ResponseFinal.FORBIDDEN_OK);
                    } else {
                        return ResponseUtil.success(null, ResponseFinal.FORBIDDEN_COME_TO_NOTHING);
                    }
                case ResponseFinal.DELETE:
                    if (deleteById(id)) {
                        return ResponseUtil.success(null, ResponseFinal.DELETE_OK);
                    } else {
                        return ResponseUtil.success(null, ResponseFinal.DELETE_COME_TO_NOTHING);
                    }
                default:
                    return ResponseUtil.paramInvalidError(ResponseFinal.PARAMETER_ABNORMITY);
            }
        } else {
            return ResponseUtil.success(null, ResponseFinal.DATA_DOES_NOT_EXIST);
        }
    }

}
