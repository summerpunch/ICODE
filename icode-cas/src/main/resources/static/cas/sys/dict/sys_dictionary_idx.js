var tableIndex;
$(function () {

    getDictionaryTree();

    /**
     * Title: 默认按 ParentId 为 1 查询 所属字典列表<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 19:17<br>
     */
    getdictData({id:1});

    /**
     * Title: 点击字典树节点<br>
     * Description: 按当前节点查询所属字典<br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 19:16<br>
     */
    $('#dictJson').on('nodeSelected', function (event, data) {
        getdictData({id:data.id});
    });

    /**
     * Title: 获取当前选中Table的行的下标<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Date: 2018/8/14 10:07<br>
     */
    $('#exampleTableToolbar').on("click-row.bs.table",function(e, row, $element) {
        tableIndex = $element.data('index');
    });

    /**
     * Title: 移除模态框<br>
     * Description: modal页面加载$()错误,
     *              由于移除缓存时加载到<div class="modal-content"></div>未移除的数据<
     *              手动移除加载的内容br>
     * Author: XiaChong<br>
     * Date: 2018/8/13 19:15<br>
     */
    $("#_pop_add_v_edit_dictionary").on("hidden.bs.modal", function () {
        $(this).removeData("bs.modal");
        $(this).find(".modal-content").children().remove();
    });
});

/**
 * Title: 新增数据字典页面<br>
 * Description: 弹窗<br>
 * Author: XiaChong<br>
 * Date: 2018/8/13 19:15<br>
 */
$("#_addDictionary").on("click", function () {
    var jsonData = getTableData($('#exampleTableToolbar'));
    if (isEmptyObject(jsonData)) {
        var parentId = jsonData.id;
        var url = '/casSysDictionary/pop_add_v_edit_dictionary?parentId=' + parentId;
        $("#_pop_add_v_edit_dictionary").modal({
            remote: url,
            backdrop: "static",
            keyboard: true,
        })
    } else {
        parent.layer.msg('请选择一项作为父节点 . ', {icon: 6});
    }
});

/**
 * Title: 编辑数据字典页面<br>
 * Description: 弹窗<br>
 * Author: XiaChong<br>
 * Date: 2018/8/13 19:15<br>
 */
$("#_editDictionary").click(function () {
    var jsonData = getTableData($('#exampleTableToolbar'));
    if (isEmptyObject(jsonData)) {
        var id = jsonData.id;
        var url = '/casSysDictionary/pop_add_v_edit_dictionary?id=' + id;
        $("#_pop_add_v_edit_dictionary").modal({
            remote: url,
            backdrop: "static",
            keyboard: true,
        })
    } else {
        parent.layer.msg('请选择需要编辑的节点 . ', {icon: 6});
    }
});

/**
 * Title: 删除数据字典<br>
 * Description: 逻辑 or 物理 <br>
 * Author: XiaChong<br>
 * Date: 2018/8/13 19:14<br>
 */
$("#_delDictionary").click(function () {
    var jsonData = getTableData($('#exampleTableToolbar'));
    if (isEmptyObject(jsonData)) {
        layer.confirm('确定要禁用【 ' + jsonData.itemNamecn + ' 】这条数据吗 ?', {
            btn: ['确定','取消'] //按钮
        }, function(){
            var url = "/casSysDictionary/do_delete_v_forbidden_dictionary";
            var params = {
                id:jsonData.id,
                type:'forbidden' // 参数: forbidden 禁用 (逻辑) , delete 删除 (物理)
            }
            ajaxRequest(url, params, function () {
                refreshBootstrapTable($("#exampleTableToolbar"));
                refreshDelTreeview($('#dictJson'));
                layer.closeAll('dialog'); //关闭信息框
            });
        }, function(){
            parent.layer.msg('已取消 . ', {icon: 6});
        });
    } else {
        parent.layer.msg('请选择需要禁用的节点 . ', {icon: 6});
    }
});

/**
 * Title: 获取字典树结构树<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Date: 2018/8/13 19:14<br>
 */
function getDictionaryTree() {
    var json = new Array();
    $.ajax({
        type: 'POST',
        url: '/casSysDictionary/ajax/dictionary_tree',
        async: false,
        dataType: 'json',
        success: function (data) {
            json = data.data;
        }
    });
    $('#dictJson').treeview({
        data: json,
        levels: 2,
        showCheckbox: true, //不显示单选
    });
}

/**
 * Title: 按条件查询<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Date: 2018/8/13 19:13<br>
 */
function dictSearch() {
    var treeData = getTreeData($("#dictJson"))
    var params = serializeObject($("#_dictFormSearch"));
    if( treeData.length == 0 || treeData.length == 1 ){
        if( treeData.length == 1 ){
            params.id = treeData[0].id;
        }
        getdictData(params);//数据转为json对象，调用搜索方法
    }else{
        parent.layer.msg("搜索节点选项不能大于一条", {icon: 3});
    }


}

/**
 * Title: 查询数据字典列表<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Date: 2018/8/13 19:13<br>
 */
function getdictData(params) {
    /**
     * 解决多次请求无效的问题
     * 在初始化table之前，要将table销毁，否则会保留上次加载的内容
     */
    console.info(params);
    $("#exampleTableToolbar").bootstrapTable('destroy');
    $('#exampleTableToolbar').bootstrapTable(BootstrapTableUtil.getAjaxPagingOptions({
        ajaxUrl: '/casSysDictionary/doDictionaryListById',
        data: params,
        colums: [{
            checkbox: true
        }, {
            field: 'id',
            title: 'id'
        }, {
            field: 'itemNamecn',
            title: '中文名称'
        }, {

            field: 'itemKey',
            title: '键名称'
        }, {

            field: 'status',
            title: '状态'
        }]
    }));
}