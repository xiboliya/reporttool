
软件名称：信息汇总工具
软件作者：冰原
作者百度空间：http://hi.baidu.com/xiboliya

代码仓库（皆采用git进行管理）：
GitHub：https://github.com/xiboliya/reporttool
CSDN代码：https://code.csdn.net/chenzhengfeng/reporttool

软件版权：
本项目是一个开源项目，遵循GNU GPL第三版开源许可协议的相关条款。协议具体条款详见licenses/GPLv3.txt文件。

软件编译：
本项目采用ant进行编译和打包。ant是目前java环境下最好用的打包部署工具，其采用xml的格式进行编写，功能非常强大。
现介绍一下如何安装和使用ant进行java程序的编译打包。
1.下载并安装ant。
到官方主页http://ant.apache.org下载新版（目前为Ant1.8.1）的ant，得到的是一个apache-ant-1.8.1-bin.zip的压缩包。将其解压到你的硬盘上，例如：D:\apache-ant-1.8.1。
2.配置环境变量。
依次选择：我的电脑->属性->高级->环境变量->系统变量->Path->编辑->变量值，在最前面添加如下内容：
D:\apache-ant-1.8.1\bin;
然后点击“确定”，完成环境变量的配置。
3.验证ant。
为了验证ant是否成功安装，可以进行如下操作：
依次选择：开始->运行->cmd，输入如下命令：
ant
如果出现如下内容，说明安装成功：
Buildfile: build.xml does not exist!
Build failed
但如果出现如下内容，说明安装失败：（应重复前述步骤，直至安装成功。）
'ant' 不是内部或外部命令，也不是可运行的程序或批处理文件。
4.编译项目
假设本项目的路径为：D:\ReportTool，我们在命令行中cd到此路径下，然后输入如下命令：
ant
开始编译。编译完成后，当前目录下会产生一个ant目录。其中ant\bin目录存放编译产生的以及第三方jar包的class文件和所需的资源文件（图片和配置文件等）。而ant\jar\ReportTool.jar文件即是我们需要的可运行的jar包。此jar文件可以直接双击运行，或者使用命令行：java -jar ant\jar\ReportTool.jar来运行。

*****************

注：
此软件是用java语言开发，所以需要在电脑上安装java才可以运行。
ReportTool.jar是程序主文件，直接双击就可以运行。Factory是数据库文件夹，用于保存数据。
上述的ReportTool.jar和Factory必须放到同一文件夹下，并且在其完整路径中不能含有空格和汉字。比如以下几种情况的路径，都将导致程序无法保存和读取数据：
C:\Program Files\ReportTool
C:\Program Files\MyProject\ReportTool
C:\MyProject\汇总工具\ReportTool
C:\汇总工具\MyProject\ReportTool
