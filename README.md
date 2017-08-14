# calendarSchedule
日程表，仿滴答清单，包含日视图、三日视图、周视图、月视图、列表视图


此项目是仿滴答清单做的一个日程表，现包括的功能有：日视图、三日视图、周视图、月视图、列表视图（周月可自由切换，左右滑动切换周或月份）。

1、 网络请求功能完善； 
~~2、 同步手机的日程表； (已完成)~~
~~3、 可以增加日历事件，并可选择是否同步到手机本身日程表；(已完成) ~~
4、 可设置提醒事件功能； 
5、 日历事件保存到数据库；
6、 同步google邮件账号或是其他黄历事件； 
7､ 事件功能分类展示；
8、 暂进还没想到功能完善。

此工程是根据以下两个项目改进的项目： 
依赖的工程分别是 java-week-view项目，地址是：https://github.com/alamkanak/Android-Week-View 
android-collapse-calendar-view项目，地址是：https://github.com/blazsolar/android-collapse-calendar-view 
由于这两个项目都不能满足我的需求，且这两个项目开发人员近期都不维护了，上一次更新还是两年前，还存在一些bug。所以就在他们项目代码基础上，重新编码，实现我想要的效果。 古人云，不要重复造轮子，况且开发人员把代码公布出来，应该不会涉及到侵权问题吧，如果涉及到侵权，请及时联系我。 下图是展示日视图、三日视图、周视图的效果图 ：

![这里写图片描述](http://img.blog.csdn.net/20170726165503516?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc21pbGVpYW0=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

下图是展示月视图、日程列表的效果图 ：
日程列表，周月可自由切换。
![这里写图片描述](http://img.blog.csdn.net/20170726173508620?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc21pbGVpYW0=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
![日程列表月显示](https://github.com/xiewenfeng/calendarSchedule/blob/master/calendar_schedule/images/%E6%97%A5%E7%A8%8B%E5%88%97%E8%A1%A81.png)

![日程列表周显示](https://github.com/xiewenfeng/calendarSchedule/blob/master/calendar_schedule/images/%E6%97%A5%E7%A8%8B%E5%88%97%E8%A1%A82.png)
![读取手机日程]
(https://raw.githubusercontent.com/xiewenfeng/calendarSchedule/master/calendar_schedule/images/%E8%AF%BB%E5%8F%96%E6%89%8B%E6%9C%BA%E7%B3%BB%E7%BB%9F%E6%97%A5%E7%A8%8B.png)

![添加日程]
(https://github.com/xiewenfeng/calendarSchedule/blob/master/calendar_schedule/images/%E6%B7%BB%E5%8A%A0%E6%97%A5%E7%A8%8B.png)
如果有感兴趣的朋友，想要一起来开发或完善这个项目，小妹子非常欢迎您的加入。
