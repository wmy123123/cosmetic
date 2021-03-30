/** layuiAdmin.std-v2020.1.24 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;
    var $ = layui.jquery;
    var form = layui.form;
    i.render({
        elem: "#LAY-product-back-manage"
        , toolbar: '#toolbarProduct',
        url: "/product/getProductList",
        cols: [[
            {field: "product_id", width: 120, title: "商品编号", sort: !0},
            {field: "product_name", width: 160, title: "商品名称"},
            {field: "product_type", width: 100, title: "商品类别", templet: "#producttype"},
            {field: "product_price", width: 100, title: "商品价格"},
            {field: "product_account", width: 100, title: "库存数量"},
            {field: "product_message", title: "商品简介"},
            {
                title: "操作", width: 160, align: "center", fixed: "right", toolbar: "#table-useradmin-webuser"
            }]],
        page: !0,
        limit: 10,
        text: "数据为空"
    }), i.on("tool(LAY-product-back-manage)", function (e) {
        var data = e.data;
        if ("del" === e.event)
            layer.confirm("真的删除此商品？", {title: false, anim: 3}, function (t) {
                $.ajax({
                    url: "/product/deleteProduct",
                    type: "POST",
                    data: {product_id: data.product_id},
                    dataType: 'json',
                    success: function (res) {
                        if (res.code == 0) {
                            e.del();
                            layer.close(t)
                            layer.msg(res.msg, {icon: 6, anim: 3});
                        } else {
                            layer.msg(res.msg, {icon: 5, anim: 3});
                        }
                    }
                });
            })
        else if ("edit" === e.event) {
            t(e.tr);
            var ii = layer.load();
            setTimeout(function () {
                layer.close(ii);
                layer.open({
                    type: 2,
                    title: "编辑商品",
                    content: "/product/findProduct?product_id=" + data.product_id,
                    area: ["800px", "500px"],
                    btn: ["确定", "取消"],
                    yes: function (e, t) {
                        var l = window["layui-layer-iframe" + e], r = "LAY-product-update-submit",
                            n = t.find("iframe").contents().find("#" + r);
                        l.layui.form.on("submit(" + r + ")", function (t) {
                            var data = t.field;
                            $.ajax({
                                url: "/product/updateProduct",
                                type: "POST",
                                data: data,
                                dataType: 'json',
                                success: function (res) {
                                    if (res.code == 0) {
                                        i.reload("LAY-product-back-manage"),
                                            layer.close(e)
                                        layer.msg(res.msg, {icon: 6, anim: 3});
                                    } else {
                                        layer.msg(res.msg, {icon: 6, anim: 3});
                                    }
                                }
                            });
                        }), n.trigger("click")
                    },
                    success: function (e, t) {
                    }
                })
            }, 500);
        }
    }),
        i.render({
            elem: "#LAY-productType-back-manage"
            , toolbar: '#toolbarProductType',
            url: "/product/productTypeList",
            cols: [[
                {field: "typeid", title: "商品类别编号"},
                {field: "typename", title: "商品类别名称"},
                {
                    title: "操作", width: 160, align: "center", fixed: "right", toolbar: "#table-useradmin-webuser"
                }]],
        }), i.on("tool(LAY-productType-back-manage)", function (e) {
        var data = e.data;
        if ("del" === e.event)
            layer.confirm("确定删除此商品类别？", {title: false, anim: 3}, function (t) {
                $.ajax({
                    url: "/product/deleteProductType",
                    type: "POST",
                    data: {id: data.typeid},
                    dataType: 'json',
                    success: function (res) {
                        if (res.code === 0) {
                            e.del();
                            layer.close(t)
                            layer.msg(res.msg, {icon: 6, anim: 3});
                        } else {
                            layer.msg(res.msg, {icon: 5, anim: 3});
                        }
                    }
                });
            })
        else if ("edit" === e.event) {
            t(e.tr);
            var ii = layer.load();
            setTimeout(function () {
                layer.close(ii);
                layer.open({
                    type: 2,
                    title: false,
                    content: "/page/updateProductType?id=" + data.typeid,
                    area: ['350px', '150px'],
                    btn: ["确定", "取消"],
                    yes: function (e, t) {
                        var l = window["layui-layer-iframe" + e], r = "LAY-productType-update-submit",
                            n = t.find("iframe").contents().find("#" + r);
                        l.layui.form.on("submit(" + r + ")", function (t) {
                            var data = t.field;
                            $.ajax({
                                url: "/product/updateProductType",
                                type: "POST",
                                data: data,
                                dataType: 'json',
                                success: function (res) {
                                    if (res.code == 0) {
                                        i.reload("LAY-productType-back-manage"),
                                            layer.close(e)
                                        layer.msg(res.msg, {icon: 6, anim: 3});
                                    } else {
                                        layer.msg(res.msg, {icon: 6, anim: 3});
                                    }
                                }
                            });
                        }), n.trigger("click")
                    },
                    success: function (e, t) {
                    }
                })
            }, 500);
        }
    }),
        i.render({
            elem: "#LAY-user-back-manage",
            url: "/user/findEmployeeList"
            , toolbar: '#toolbarAdmin',
            // ,defaultToolbar: ['',{title:'批量导入',layEvent: 'import',icon: 'layui-icon-upload'},
            //     {title:'批量导出',layEvent: 'export',icon: 'layui-icon-export'}], //这里在右边显示
            cols: [[{type: "checkbox", fixed: "left"}
                , {field: "id", width: 60, title: "ID", sort: !0}
                , {field: "uuid", title: "编号"},
                {field: "avatar", title: "照片", width: 70, templet: "#imgTpl"},
                {field: "username", title: "账号"},
                {field: "password", title: "密码"},
                {field: "name", title: "姓名"},
                {field: "sex", title: "性别", width: 70},
                {field: "position", title: "职位"},
                {field: "status", width: 120, title: "状态", templet: '#checkboxTpl'},
                {field: "salary", title: "工资"},
                {
                    field: "entrytime",
                    title: "入职时间",
                    templet: "<div>{{layui.util.toDateString(d.entrytime, \"yyyy-MM-dd HH:mm:ss\")}}</div>",
                    minWidth: 80,
                    align: "center"
                },
                {
                    field: "resignationtime",
                    title: "离职时间",
                    templet: "<div>{{layui.util.toDateString(d.resignationtime, \"yyyy-MM-dd HH:mm:ss\")}}</div>",
                    minWidth: 80,
                    align: "center"
                },
                {title: "操作", width: 160, align: "center", fixed: "right", toolbar: "#table-useradmin-admin"}]],
            page: !0,
            limit: 10,
            text: "数据为空"
        }), i.on("tool(LAY-user-back-manage)", function (e) {
        var data = e.data;
        if ("del" === e.event)
            layer.confirm("确定删除此员工？", {title: false, anim: 3}, function (t) {
                $.ajax({
                    url: "/user/deleteEmployee",
                    type: "POST",
                    data: {id: data.id},
                    dataType: 'json',
                    success: function (res) {
                        if (res.code === 0) {
                            e.del();
                            layer.close(t)
                            layer.msg(res.msg, {icon: 6, anim: 3});
                        } else {
                            layer.msg(res.msg, {icon: 5, anim: 3});
                        }
                    }
                });
            }); else if ("edit" === e.event) {
            t(e.tr);
            var ii = layer.load();
            setTimeout(function () {
                layer.close(ii);
                layer.open({
                    type: 2,
                    title: "修改员工信息",
                    content: "/user/updateEmplform?id=" + data.id,
                    area: ["800px", "500px"],
                    btn: ["确定", "取消"],
                    yes: function (e, t) {
                        var l = window["layui-layer-iframe" + e], r = "LAY-user-back-submit",
                            n = t.find("iframe").contents().find("#" + r);
                        l.layui.form.on("submit(" + r + ")", function (t) {
                            var data = t.field;
                            $.ajax({
                                url: "/user/updateEmployee",
                                type: "POST",
                                data: data,
                                dataType: 'json',
                                success: function (res) {
                                    if (res.code === 0) {
                                        i.reload("LAY-user-back-manage"),
                                            layer.close(e)
                                        layer.msg(res.msg, {icon: 6, anim: 3});
                                    } else {
                                        layer.msg(res.msg, {icon: 5, anim: 3});
                                    }
                                }
                            });
                        }), n.trigger("click")
                    },
                    success: function (e, t) {

                    }
                })
            }, 500);
        }
    }), i.render({
        elem: "#LAY-order-manage",
        url: "/order/findOrderForm",
        cols: [[
            {field: "orderid", width: 300, title: "订单编号"},
            {field: "payment", title: "支付金额",},
            {field: "status", title: "支付状态", templet: '#paystatus'},
            {
                field: "createdt",
                title: "开启时间",
                templet: "<div>{{layui.util.toDateString(d.createdt, \"yyyy-MM-dd HH:mm:ss\")}}</div>",
                minWidth: 80,
                align: "center"
            },
            {
                field: "enddt",
                title: "结束时间",
                templet: "<div>{{layui.util.toDateString(d.enddt, \"yyyy-MM-dd HH:mm:ss\")}}</div>",
                minWidth: 80,
                align: "center"
            },
            {title: "操作", width: 160, align: "center", fixed: "right", toolbar: "#table-order"}]],
        page: !0,
        limit: 11,
    }), i.on("tool(LAY-order-manage)", function (e) {
        var data = e.data;
        if ("del" === e.event)
            layer.confirm("确定删除此订单？", {title: false, anim: 3}, function (t) {
                $.ajax({
                    url: "/order/deleteOrderForm",
                    type: "POST",
                    data: {orderid: data.orderid},
                    dataType: 'json',
                    success: function (res) {
                        if (res.code == 0) {
                            e.del();
                            layer.close(t)
                            layer.msg(res.msg, {icon: 6, anim: 3});
                        } else {
                            layer.msg(res.msg, {icon: 5, anim: 3});
                        }
                    }
                });
            })
        else if ("edit" === e.event) {
            t(e.tr);
            var ii = layer.load();
            setTimeout(function () {
                layer.close(ii);
                layer.open({
                    type: 2,
                    title: "订单详情",
                    content: "/order/findOrderItem?orderid=" + data.orderid,
                    area: ["800px", "500px"],
                    btn: ["确定", "取消"],
                    yes: function (e, t) {
                        var l = window["layui-layer-iframe" + e], r = "LAY-user-back-submit",
                            n = t.find("iframe").contents().find("#" + r);
                        l.layui.form.on("submit(" + r + ")", function (t) {
                            var data = t.field;
                            $.ajax({
                                url: "/user/updateEmployee",
                                type: "POST",
                                data: data,
                                dataType: 'json',
                                success: function (res) {
                                    if (res.code == 0) {
                                        i.reload("LAY-user-back-manage"),
                                            layer.close(e)
                                        layer.msg(res.msg, {icon: 6, anim: 3});
                                    } else {
                                        layer.msg(res.msg, {icon: 6, anim: 3});
                                    }
                                }
                            });
                        }), n.trigger("click")
                    },
                    success: function (e, t) {

                    }
                })
            }, 500)
        }
    }), i.render({
        elem: "#LAY-user-back-role",
        url: "/user/roleList",
        toolbar: "#toolbarRole",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "rolid", width: 80, title: "ID", sort: !0}, {
            field: "rolna",
            title: "角色名"
        }, {
            field: "createdt", title: "创建时间",
            templet: "<div>{{layui.util.toDateString(d.createdt, \"yyyy-MM-dd HH:mm:ss\")}}</div>",
        }, {field: "description", title: "具体描述"}, {
            title: "操作",
            width: 165,
            align: "center",
            fixed: "right",
            toolbar: "#table-useradmin-admin"
        }]],
        text: "数据为空"
    }), i.on("tool(LAY-user-back-role)", function (e) {
        var data = e.data;
        if ("del" === e.event)
            layer.confirm("确定删除此角色？", {title: false, anim: 3}, function (t) {
                $.ajax({
                    url: "/permission/deleteRole?rolid=" + data.rolid,
                    type: "POST",
                    data: {orderid: data.orderid},
                    dataType: 'json',
                    success: function (res) {
                        if (res.code == 0) {
                            e.del();
                            layer.close(t)
                            layer.msg(res.msg, {icon: 6, anim: 3});
                        } else {
                            layer.msg(res.msg, {icon: 5, anim: 3});
                        }
                    }
                });
            }); else if ("edit" === e.event) {
            t(e.tr);
            var ii = layer.load();
            setTimeout(function () {
                layer.close(ii);
                layer.open({
                    type: 2,
                    title: "编辑角色",
                    content: "/page/updateRole?rolid=" + data.rolid + "&rolna=" + data.rolna + "&description=" + data.description,
                    area: ["800px", "520px"],
                    yes: function (e, t) {
                        var l = window["layui-layer-iframe" + e], r = "LAY-user-back-submit",
                            n = t.find("iframe").contents().find("#" + r);
                        l.layui.form.on("submit(" + r + ")", function (t) {
                            var data = t.field;
                            $.ajax({
                                url: "/user/updateEmployee",
                                type: "POST",
                                data: data,
                                dataType: 'json',
                                success: function (res) {
                                    if (res.code == 0) {
                                        i.reload("LAY-user-back-manage"),
                                            layer.close(e)
                                        layer.msg(res.msg, {icon: 6, anim: 3});
                                    } else {
                                        layer.msg(res.msg, {icon: 6, anim: 3});
                                    }
                                }
                            });
                        }), n.trigger("click")
                    },
                })
            }, 500);
        }
    }), e("useradmin", {})
});