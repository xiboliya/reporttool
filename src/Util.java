/**
 * Copyright (C) 2014 冰原
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xiboliya.reporttool;

import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * 实用工具类，包括可重用的各种属性和方法。设计为final类型，使本类不可被继承
 * 
 * @author 冰原
 * 
 */
public final class Util {
  public static final String SOFTWARE = "信息汇总工具"; // 软件名称
  public static final String VERSION = "V1.0"; // 软件版本号
  public static final String OS_NAME = System.getProperty("os.name", "Windows"); // 当前操作系统的名称
  public static final String DATABASE_NAME = "Factory"; // 数据库名称
  public static final String TABLE_NAME = "factory"; // 表格名称
  public static final String CTRL_C = "Ctrl+C"; // 组合键Ctrl+C的字符串
  public static final String CTRL_H = "Ctrl+H"; // 组合键Ctrl+H的字符串
  public static final String CTRL_V = "Ctrl+V"; // 组合键Ctrl+V的字符串
  public static final String CTRL_X = "Ctrl+X"; // 组合键Ctrl+X的字符串
  public static final String CTRL_Y = "Ctrl+Y"; // 组合键Ctrl+Y的字符串
  public static final String CTRL_Z = "Ctrl+Z"; // 组合键Ctrl+Z的字符串
  public static final String SYSTEM_LOOK_AND_FEEL_CLASS_NAME = UIManager
      .getSystemLookAndFeelClassName(); // 当前系统默认外观的完整类名
  public static final String[] TOOL_TEXTS = new String[] { "导入", "添加", "查看", "搜索",
      "删除", "导出", "刷新", "浏览全部" }; // 工具栏提示信息
  public static final String[] TABLE_TITLE_TEXTS = new String[] { "序号", "单位名称",
      "单位地址", "经营范围", "联系人", "联系人手机" }; // 主界面表格标题
  public static final String[] TABLE_COLUMN_NAMES = new String[] { "id",
      "factoryName", "factoryAddress", "foundDate", "factoryProperty",
      "mainBusinessSphere", "factoryEmail", "businessEntity",
      "businessEntityPhone", "businessEntityHandset", "linkman",
      "linkmanPhone", "linkmanHandset", "insideRatio", "outsideRatio",
      "tradeType", "country", "buyOrLease", "staffQuantity",
      "registeredCapital", "factorySynopsis", "factoryProblemOrNeeds",
      "expositionNeeds", "otherItems", "yearNum", "yearSalesVolume",
      "yearTaxationOfProfit", "yearForeignExchangeEarning", "interviewDate",
      "interviewPersonnel" }; // 数据库中表格的列名
  public static final Font GLOBAL_FONT = new Font("宋体", Font.PLAIN, 12); // 全局的默认字体
  public static final int INPUT_HEIGHT = 25; // 单行输入框的高度
  public static final int VIEW_HEIGHT = 22; // 标签、单选按钮、复选框的高度
  public static final int BUTTON_HEIGHT = 40; // 按钮的高度
  public static final int TABLE_COLUMN = 30; // 表格中数据列数
  public static final int DEFAULT_UNDO_INDEX = 0; // 撤销标识符的默认值
  public static final int BUFFER_LENGTH = 1024; // 缓冲区的大小
  public static final ImageIcon SW_ICON = new ImageIcon(
      ClassLoader.getSystemResource("res/icon.png")); // 主程序图标
  public static final ImageIcon[] TOOL_ICONS = new ImageIcon[] {
      new ImageIcon(ClassLoader.getSystemResource("res/tool_import.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_new.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_view.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_search.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_delete.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_export.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_refresh.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_list.png")) }; // 工具栏的图标

  /**
   * 由于此类为工具类，故将构造方法私有化
   */
  private Util() {
  }

  /**
   * 修改整个程序的默认字体
   */
  public static void setDefaultFont() {
    FontUIResource fontRes = new FontUIResource(GLOBAL_FONT);
    Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value instanceof FontUIResource) {
        UIManager.put(key, fontRes);
      }
    }
  }

  /**
   * 为文件选择器添加预定义的文件过滤器
   * 
   * @param fileChooser
   *          要处理的文件选择器
   */
  public static void addChoosableFileFilters(JFileChooser fileChooser) {
    FileExt[] arrFileExt = FileExt.values(); // 获取包含枚举所有成员的数组
    BaseFileFilter fileFilter = null;
    BaseFileFilter defFileFilter = null; // 默认选择的文件过滤器
    for (FileExt fileExt : arrFileExt) { // 遍历枚举的所有成员
      fileFilter = new BaseFileFilter(fileExt.toString(),
          fileExt.getDescription());
      fileChooser.addChoosableFileFilter(fileFilter);
      if (fileExt.equals(FileExt.XLS)) {
        defFileFilter = fileFilter;
      }
    }
    if (defFileFilter != null) {
      fileChooser.setFileFilter(defFileFilter);
    }
  }

  /**
   * 格式化欲保存的文件名
   * 
   * @param strFileName
   *          文件的完整路径
   * @param fileFilter
   *          当前的文件类型过滤器
   * @param strExt
   *          欲保存文件的扩展名
   * @return 格式化后的文件
   */
  public static File checkFileName(String strFileName,
      BaseFileFilter fileFilter, String strExt) {
    if (strFileName == null || strFileName.isEmpty() || fileFilter == null
        || strExt == null || strExt.isEmpty()) {
      return null;
    }
    if (fileFilter.getExt().equalsIgnoreCase(strExt)) {
      if (!strFileName.toLowerCase().endsWith(strExt.toLowerCase())) {
        strFileName += strExt;
      }
    }
    return new File(strFileName);
  }

  /**
   * 获取数据库连接
   */
  public static Connection getConnection() {
    String dir = Util.class.getResource("").getPath();
    dir = dir.substring(5, dir.length() - 26);
    dir = new File(dir).getParent();
    Connection connection = null;
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      connection = DriverManager.getConnection("jdbc:derby:" + dir + "/"
          + DATABASE_NAME);
       /*connection=DriverManager.getConnection("jdbc:derby:/home/android/setup/ReportTool/"
       + DATABASE_NAME);*/
    } catch (ClassNotFoundException x) {
      x.printStackTrace();
    } catch (Exception x) {
      x.printStackTrace();
    }
    return connection;
  }

  /**
   * 将表格数据项导出到Excel表格文件
   * 
   * @param container
   *          操作的窗口界面
   * @param cells
   *          将要导出到文件的数据集合
   * @param file
   *          导出的目标Excel表格文件
   */
  public static void exportToXLS(Component container, Vector<Vector> cells,
      File file) {
    int size = cells.size();
    if (size <= 0) {
      return;
    }
    URL url = ClassLoader.getSystemResource("res/base.xml");
    BufferedReader reader = null;
    String strBase = "";
    String strTemplate = "";
    try {
      reader = new BufferedReader(new InputStreamReader(url.openStream(),
          "UTF-8"));
      String line = reader.readLine();
      while (line != null) {
        strBase += line + "\n";
        line = reader.readLine();
      }
      url = ClassLoader.getSystemResource("res/template.xml");
      reader = new BufferedReader(new InputStreamReader(url.openStream(),
          "UTF-8"));
      line = reader.readLine();
      while (line != null) {
        strTemplate += line + "\n";
        line = reader.readLine();
      }

    } catch (Exception x) {
      x.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (IOException x) {
        x.printStackTrace();
      }
    }
    String strCell = "";
    for (Vector<String> cell : cells) {
      String str = strTemplate;
      for (int n = 0; n < cell.size(); n++) {
        str = str.replace("{" + TABLE_COLUMN_NAMES[n] + "}", cell.get(n));
      }
      strCell += str;
    }
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(file);
      String strText = strBase + strCell + "\n</Workbook>";
      strText = strText.replace("\n", "\r\n");
      byte byteStr[];
      byteStr = strText.getBytes("UTF-8");
      fileOutputStream.write(byteStr);
      JOptionPane.showMessageDialog(container, "成功导出文件：\n" + file, "导出成功！",
          JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception x) {
      x.printStackTrace();
    } finally {
      try {
        fileOutputStream.flush();
        fileOutputStream.close();
      } catch (IOException x) {
        x.printStackTrace();
      }
    }
  }
}
