/**
 * Copyright (C) 2014 ��ԭ
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
 * ʵ�ù����࣬���������õĸ������Ժͷ��������Ϊfinal���ͣ�ʹ���಻�ɱ��̳�
 * 
 * @author ��ԭ
 * 
 */
public final class Util {
  public static final String SOFTWARE = "��Ϣ���ܹ���"; // �������
  public static final String VERSION = "V1.0"; // ����汾��
  public static final String OS_NAME = System.getProperty("os.name", "Windows"); // ��ǰ����ϵͳ������
  public static final String DATABASE_NAME = "Factory"; // ���ݿ�����
  public static final String TABLE_NAME = "factory"; // �������
  public static final String CTRL_C = "Ctrl+C"; // ��ϼ�Ctrl+C���ַ���
  public static final String CTRL_H = "Ctrl+H"; // ��ϼ�Ctrl+H���ַ���
  public static final String CTRL_V = "Ctrl+V"; // ��ϼ�Ctrl+V���ַ���
  public static final String CTRL_X = "Ctrl+X"; // ��ϼ�Ctrl+X���ַ���
  public static final String CTRL_Y = "Ctrl+Y"; // ��ϼ�Ctrl+Y���ַ���
  public static final String CTRL_Z = "Ctrl+Z"; // ��ϼ�Ctrl+Z���ַ���
  public static final String SYSTEM_LOOK_AND_FEEL_CLASS_NAME = UIManager
      .getSystemLookAndFeelClassName(); // ��ǰϵͳĬ����۵���������
  public static final String[] TOOL_TEXTS = new String[] { "����", "���", "�鿴", "����",
      "ɾ��", "����", "ˢ��", "���ȫ��" }; // ��������ʾ��Ϣ
  public static final String[] TABLE_TITLE_TEXTS = new String[] { "���", "��λ����",
      "��λ��ַ", "��Ӫ��Χ", "��ϵ��", "��ϵ���ֻ�" }; // �����������
  public static final String[] TABLE_COLUMN_NAMES = new String[] { "id",
      "factoryName", "factoryAddress", "foundDate", "factoryProperty",
      "mainBusinessSphere", "factoryEmail", "businessEntity",
      "businessEntityPhone", "businessEntityHandset", "linkman",
      "linkmanPhone", "linkmanHandset", "insideRatio", "outsideRatio",
      "tradeType", "country", "buyOrLease", "staffQuantity",
      "registeredCapital", "factorySynopsis", "factoryProblemOrNeeds",
      "expositionNeeds", "otherItems", "yearNum", "yearSalesVolume",
      "yearTaxationOfProfit", "yearForeignExchangeEarning", "interviewDate",
      "interviewPersonnel" }; // ���ݿ��б�������
  public static final Font GLOBAL_FONT = new Font("����", Font.PLAIN, 12); // ȫ�ֵ�Ĭ������
  public static final int INPUT_HEIGHT = 25; // ���������ĸ߶�
  public static final int VIEW_HEIGHT = 22; // ��ǩ����ѡ��ť����ѡ��ĸ߶�
  public static final int BUTTON_HEIGHT = 40; // ��ť�ĸ߶�
  public static final int TABLE_COLUMN = 30; // �������������
  public static final int DEFAULT_UNDO_INDEX = 0; // ������ʶ����Ĭ��ֵ
  public static final int BUFFER_LENGTH = 1024; // �������Ĵ�С
  public static final ImageIcon SW_ICON = new ImageIcon(
      ClassLoader.getSystemResource("res/icon.png")); // ������ͼ��
  public static final ImageIcon[] TOOL_ICONS = new ImageIcon[] {
      new ImageIcon(ClassLoader.getSystemResource("res/tool_import.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_new.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_view.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_search.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_delete.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_export.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_refresh.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/tool_list.png")) }; // ��������ͼ��

  /**
   * ���ڴ���Ϊ�����࣬�ʽ����췽��˽�л�
   */
  private Util() {
  }

  /**
   * �޸����������Ĭ������
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
   * Ϊ�ļ�ѡ�������Ԥ������ļ�������
   * 
   * @param fileChooser
   *          Ҫ������ļ�ѡ����
   */
  public static void addChoosableFileFilters(JFileChooser fileChooser) {
    FileExt[] arrFileExt = FileExt.values(); // ��ȡ����ö�����г�Ա������
    BaseFileFilter fileFilter = null;
    BaseFileFilter defFileFilter = null; // Ĭ��ѡ����ļ�������
    for (FileExt fileExt : arrFileExt) { // ����ö�ٵ����г�Ա
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
   * ��ʽ����������ļ���
   * 
   * @param strFileName
   *          �ļ�������·��
   * @param fileFilter
   *          ��ǰ���ļ����͹�����
   * @param strExt
   *          �������ļ�����չ��
   * @return ��ʽ������ļ�
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
   * ��ȡ���ݿ�����
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
   * ��������������Excel����ļ�
   * 
   * @param container
   *          �����Ĵ��ڽ���
   * @param cells
   *          ��Ҫ�������ļ������ݼ���
   * @param file
   *          ������Ŀ��Excel����ļ�
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
      JOptionPane.showMessageDialog(container, "�ɹ������ļ���\n" + file, "�����ɹ���",
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
