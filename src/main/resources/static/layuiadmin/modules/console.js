/** layuiAdmin.std-v2020.1.24 LPPL License By https://www.layui.com/admin/ */
;layui.define(function (e) {
    layui.use(["admin", "carousel"], function () {
        var e = layui.$, t = (layui.admin, layui.carousel), a = layui.element, i = layui.device();
        e(".layadmin-carousel").each(function () {
            var a = e(this);
            t.render({
                elem: this,
                width: "100%",
                arrow: "none",
                interval: a.data("interval"),
                autoplay: a.data("autoplay") === !0,
                trigger: i.ios || i.android ? "click" : "hover",
                anim: a.data("anim")
            })
        }), a.render("progress")
    }), layui.use(["admin", "carousel", "echarts"], function () {
        var e = layui.$, t = layui.admin, a = layui.carousel, i = layui.echarts, l = [], n = [{
            title: {text: "最近一周新增的用户量", x: "center", textStyle: {fontSize: 14}},
            tooltip: {trigger: "axis", formatter: "{b}<br>新增用户：{c}"},
            xAxis: [{type: "category", data: ["11-07", "11-08", "11-09", "11-10", "11-11", "11-12", "11-13"]}],
            yAxis: [{type: "value"}],
            series: [{type: "line", data: [200, 300, 400, 610, 150, 270, 380]}]
        }], r = e("#LAY-index-dataview").children("div"), o = function (e) {
            //实例化对象
            l[e] = i.init(r[e], layui.echartsTheme), l[e].setOption(n[e]), t.resize(function () {
                l[e].resize()
            })
        };
        if (r[0]) {
            o(0);
            var d = 0;
            a.on("change(LAY-index-dataview)", function (e) {
                o(d = e.index)
            }), layui.admin.on("side", function () {
                setTimeout(function () {
                    o(d)
                }, 300)
            }), layui.admin.on("hash(tab)", function () {
                layui.router().path.join("") || o(d)
            })
        }
    }),
    e("console", {})
});