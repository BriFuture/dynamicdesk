#说明
夭折的项目，貌似最后又在我的电脑上改了点东西，过几天加上来。

然后就不管了=、=

##bug
<b>哦对了，这个显示动态图的时候，明明内存占用并不多，但是用几分钟之后就会出现闪烁的情况，然而我并没有继续弄了</b>
--
#如何使用
使用时打包成jar包放在桌面上，把res文件夹一起放在桌面上，后期会实现自定义GIF和PNG图片组->,->

## 使用Rainmeter 实现
只需要在 [Rainmeter](https://www.rainmeter.net/) 的皮肤路径下面新建一个文件夹，放入图片文件，然后用下面这个配置

```
[Metadata]
Name=
Config=
Description=
Instructions=
Version=
Tags=
License=
Variant=
Preview=

;End of added Metadata


[Rainmeter]
Author=future
AppVersion=001
DynamicWindowSize=1
Update=180
BackgroundMode=1


[Variables]
PNGPATH="demos.png"

=== calc number ===
[MeasureNum]
Measure=Calc
Formula=Counter%27+1


=== get the png ===
[MeasureQuote]
Measure=Plugin
Plugin=Plugins\QuotePlugin.dll
PathName="#PNGPATH#\"
FileFilter=*.png
============================================

[DrawPic]
Meter=IMAGE
MeasureName=MeasureQuote
X=50
Y=0
W=124
H=183

```
然后就能实现同样的功能，并且 rm 的内存管理机制要好很多，不会出现图片闪烁的情况。

