/** layuiAdmin.std-v2020.1.24 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;
    var $=layui.jquery;
    layui.form;
    i.render({
        elem: "#LAY-user-manage",
        url: layui.setter.base + "json/useradmin/webuser.js",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "id", width: 100, title: "ID", sort: !0}, {
            field: "username",
            title: "用户名",
            minWidth: 100
        }, {field: "avatar", title: "头像", width: 100, templet: "#imgTpl"}, {
            field: "phone",
            title: "手机"
        }, {field: "email", title: "邮箱"}, {field: "sex", width: 80, title: "性别"}, {
            field: "ip",
            title: "IP"
        }, {field: "jointime", title: "加入时间", sort: !0}, {
            title: "操作",
            width: 150,
            align: "center",
            fixed: "right",
            toolbar: "#table-useradmin-webuser"
        }]],
        page: !0,
        limit: 30,
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-user-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.prompt({formType: 1, title: "敏感操作，请验证口令"}, function (t, i) {
            layer.close(i), layer.confirm("真的删除行么", function (t) {
                e.del(), layer.close(t)
            })
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑用户",
                content: "../../../views/user/user/userform.html",
                maxmin: !0,
                area: ["500px", "500px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e], r = "LAY-user-front-submit",
                        n = t.find("iframe").contents().find("#" + r);
                    l.layui.form.on("submit(" + r + ")", function (t) {
                        t.field;
                        i.reload("LAY-user-front-submit"), layer.close(e)
                    }), n.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }), i.render({
        elem: "#LAY-user-back-manage",
        url: "/user/findEmployeeList"
        ,toolbar: 'default',
        cols: [[{type: "checkbox", fixed: "left"}
        , {field: "id", width: 60, title: "ID", sort: !0}
        , {field: "uuid",title: "职员编号"},
            {field: "avatar", title: "头像",templet: "#imgTpl"},
            {field: "username", title: "个人账号"},
            {field: "password", title: "密码"},
            {field: "name", title: "姓名"},
            {field: "position", title: "职位"},
            {field: "entrytime", title: "入职时间",templet:"<div>{{layui.util.toDateString(d.entrytime, \"yyyy-MM-dd HH:mm:ss\")}}</div>", minWidth: 80, align: "center"},
            {field: "resignationtime", title: "离职时间",templet:"<div>{{layui.util.toDateString(d.resignationtime, \"yyyy-MM-dd HH:mm:ss\")}}</div>", minWidth: 80, align: "center"},
            {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-useradmin-admin"}]],
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-user-back-manage)", function (e) {
        var data =e.data;
        if ("del" === e.event) layer.prompt({formType: 1, title: "敏感操作，请验证口令"}, function (t, i) {
            layer.close(i), layer.confirm("确定删除此员工？", function (t) {
                $.ajax({
                    url: "/user/deleteEmployee",
                    type: "POST",
                    data:{id:data.id},
                    dataType: 'json',
                    success: function (res) {
                        if (res.code ==2009) {
                            e.del();
                            layer.close(t)
                            layer.msg(res.msg);
                        } else {
                            layer.msg(res.msg);
                        }
                    }
                });
            })
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                shade: 0,
                title: "修改员工信息",
                content: "/user/updateEmplform?id=" +data.id,
                area: ["450px", "450px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e], r = "LAY-user-back-submit",
                        n = t.find("iframe").contents().find("#" + r);
                    l.layui.form.on("submit(" + r + ")", function (t) {
                       var data=t.field;
                        $.ajax({
                            url: "/user/updateEmployee",
                            type: "POST",
                            data:data,
                            dataType: 'json',
                            success: function (res) {
                                if (res.code ==2008) {
                                    i.reload("LAY-user-back-manage"),
                                        layer.close(e)
                                    layer.msg(res.msg);
                                } else {
                                    layer.msg(res.msg);
                                }
                            }
                        });
                    }), n.trigger("click")
                },
                success: function (e, t) {

                }
            })
        }
    }), i.render({
        elem: "#LAY-user-back-role",
        url: layui.setter.base + "json/useradmin/role.js",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "id", width: 80, title: "ID", sort: !0}, {
            field: "rolename",
            title: "角色名"
        }, {field: "limits", title: "拥有权限"}, {field: "descr", title: "具体描述"}, {
            title: "操作",
            width: 150,
            align: "center",
            fixed: "right",
            toolbar: "#table-useradmin-admin"
        }]],
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-user-back-role)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除此角色？", function (t) {
            e.del(), layer.close(t)
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑角色",
                content: "../../../views/user/administrators/roleform.html",
                area: ["450px", "450px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-user-role-submit");
                    l.layui.form.on("submit(LAY-user-role-submit)", function (t) {
                        t.field;
                        i.reload("LAY-user-back-role"), layer.close(e)
                    }), r.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }), e("useradmin", {})
});