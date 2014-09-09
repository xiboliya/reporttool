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

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Vector;

/**
 * 表格数据查询汇总工具
 * 
 * @author 冰原
 * 
 */
public class ReportToolFrame extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L; // 序列化运行时使用的一个版本号，以与当前可序列化类相关联
  private JToolBar tlbMain = new JToolBar(); // 显示常用按钮的工具栏组件
  private JTable tabMain = null; // 显示数据的表格组件
  private JScrollPane spnMain = null;
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menuOperation = new JMenu("操作(P)");
  private JMenuItem itemNew = new JMenuItem("添加(N)", 'N');
  private JMenuItem itemView = new JMenuItem("查看(V)", 'V');
  private JMenuItem itemSearch = new JMenuItem("搜索(S)...", 'S');
  private JMenuItem itemDelete = new JMenuItem("删除(D)", 'D');
  private JMenuItem itemImport = new JMenuItem("导入(I)...", 'I');
  private JMenuItem itemExport = new JMenuItem("导出(X)...", 'X');
  private JMenuItem itemRefresh = new JMenuItem("刷新(R)", 'R');
  private JMenuItem itemListAll = new JMenuItem("浏览全部(L)", 'L');
  private JMenu menuHelp = new JMenu("帮助(H)");
  private JMenuItem itemAbout = new JMenuItem("关于(A)", 'A');

  private JPopupMenu popMenuTabbed = new JPopupMenu();
  private JMenuItem itemPopCloseCurrent = new JMenuItem("关闭当前(C)", 'C');
  private JMenuItem itemPopCloseOthers = new JMenuItem("关闭其它(O)", 'O');
  private JMenuItem itemPopSave = new JMenuItem("保存(S)", 'S');
  private JMenuItem itemPopSaveAs = new JMenuItem("另存为(A)...", 'A');
  private JMenuItem itemPopReName = new JMenuItem("重命名(N)...", 'N');
  private JMenuItem itemPopDelFile = new JMenuItem("删除文件(D)", 'D');
  private JMenuItem itemPopReOpen = new JMenuItem("重新载入(R)", 'R');

  private LinkedList<AbstractButton> toolButtonList = new LinkedList<AbstractButton>(); // 存放工具栏中所有按钮的链表
  private BaseDefaultTableModel baseDefaultTableModel = null;
  private String[] arrViewItem = new String[Util.TABLE_COLUMN]; // 用于存放当前表格中选择项数据的数组
  private String sql = "select * from " + Util.TABLE_NAME;
  private Vector<Vector> cells = new Vector<Vector>();
  private Vector<String> cellsTitle = new Vector<String>();
  private Vector<String[]> cellsImport = new Vector<String[]>();
  private Vector<String> cellsSql = new Vector<String>();

  private Connection connection = null; // 与数据库的连接对象
  private Statement statement = null; // 用于执行静态SQL语句并返回它所生成结果的对象
  private ResultSet resultSet = null; // 数据库结果集对象

  private AboutDialog aboutDialog = null; // 关于对话框
  private EditItemDialog editItemDialog = null; // 添加表格数据对话框
  private SearchItemDialog searchItemDialog = null; // 搜索表格数据对话框
  private SaveFileChooser saveFileChooser = null; // "保存"文件选择器
  private OpenFileChooser openFileChooser = null; // "导入"文件选择器

  /**
   * 构造方法 用于初始化界面和设置
   */
  public ReportToolFrame() {
    this.setTitle(Util.SOFTWARE);
    this.setSize(700, 600);
    this.setMinimumSize(new Dimension(600, 500)); // 设置主界面的最小尺寸
    this.setLocationRelativeTo(null); // 使窗口居中显示
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 设置默认关闭操作为空，以便添加窗口监听事件
    this.init();
    this.setIcon();
    this.setVisible(true);
  }

  /**
   * 设置自定义的窗口图标
   */
  private void setIcon() {
    try {
      this.setIconImage(Util.SW_ICON.getImage());
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  /**
   * 初始化界面和添加监听器
   */
  private void init() {
    this.addToolBar();
    this.addMenuItem();
    this.addPopMenu();
    this.addTable();
    this.setMenuMnemonic();
    this.addListeners();
  }

  /**
   * 主面板上添加工具栏视图
   */
  private void addToolBar() {
    this.getContentPane().add(this.tlbMain, BorderLayout.NORTH);
    for (int i = 0; i < Util.TOOL_ICONS.length; i++) {
      AbstractButton btnTool = new JButton(Util.TOOL_ICONS[i]);
      btnTool.setText(Util.TOOL_TEXTS[i]);
      btnTool.setFocusable(false);
      btnTool.addActionListener(this);
      this.tlbMain.add(btnTool);
      this.tlbMain.addSeparator();
      this.toolButtonList.add(btnTool);
    }
  }

  /**
   * 主面板上添加菜单栏
   */
  private void addMenuItem() {
    this.setJMenuBar(this.menuBar);
    this.menuBar.add(this.menuOperation);
    this.menuOperation.add(this.itemNew);
    this.menuOperation.add(this.itemView);
    this.menuOperation.add(this.itemSearch);
    this.menuOperation.add(this.itemDelete);
    this.menuOperation.add(this.itemRefresh);
    this.menuOperation.add(this.itemListAll);
    this.menuOperation.addSeparator();
    this.menuOperation.add(this.itemImport);
    this.menuOperation.add(this.itemExport);
    this.menuBar.add(this.menuHelp);
    this.menuHelp.add(this.itemAbout);
  }

  /**
   * 初始化快捷菜单
   */
  private void addPopMenu() {
    this.popMenuTabbed.add(this.itemPopCloseCurrent);
    this.popMenuTabbed.add(this.itemPopCloseOthers);
    this.popMenuTabbed.addSeparator();
    this.popMenuTabbed.add(this.itemPopSave);
    this.popMenuTabbed.add(this.itemPopSaveAs);
    this.popMenuTabbed.add(this.itemPopReName);
    this.popMenuTabbed.add(this.itemPopDelFile);
    this.popMenuTabbed.add(this.itemPopReOpen);
    this.popMenuTabbed.addSeparator();
    Dimension popSize = this.popMenuTabbed.getPreferredSize();
    popSize.width += popSize.width / 5; // 为了美观，适当加宽菜单的显示
    this.popMenuTabbed.setPopupSize(popSize);
  }

  /**
   * 主面板上添加表格视图
   */
  private void addTable() {
    for (String title : Util.TABLE_TITLE_TEXTS) {
      this.cellsTitle.add(title);
    }
    this.baseDefaultTableModel = new BaseDefaultTableModel(this.cells,
        this.cellsTitle);
    this.tabMain = new JTable(this.baseDefaultTableModel);
    this.spnMain = new JScrollPane(this.tabMain);
    this.tabMain.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.getContentPane().add(this.spnMain, BorderLayout.CENTER);
  }

  /**
   * 获取数据表中特定条件下的数据项
   * 
   * @param sql
   *          给定的SQL语句
   */
  private void getCells(String sql) {
    this.cells.clear();
    Vector<String> cellsLine = null;
    try {
      this.connection = Util.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(sql);
      while (this.resultSet.next()) {
        cellsLine = new Vector<String>();
        cellsLine.add(String.valueOf(this.resultSet.getInt(1)));
        cellsLine.add(this.resultSet.getString(2));
        cellsLine.add(this.resultSet.getString(3));
        cellsLine.add(this.resultSet.getString(6));
        cellsLine.add(this.resultSet.getString(11));
        cellsLine.add(this.resultSet.getString(13));
        this.cells.add(cellsLine);
      }
    } catch (SQLException x) {
      x.printStackTrace();
    } finally {
      try {
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
      } catch (SQLException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * 为各菜单项设置助记符和快捷键
   */
  private void setMenuMnemonic() {
    this.menuOperation.setMnemonic('P');
    this.menuHelp.setMnemonic('H');
    this.itemNew.setAccelerator(KeyStroke.getKeyStroke('N',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+N
    this.itemView.setAccelerator(KeyStroke.getKeyStroke('O',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+O
    this.itemSearch.setAccelerator(KeyStroke.getKeyStroke('F',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+F
    this.itemImport.setAccelerator(KeyStroke.getKeyStroke('I',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+I
    this.itemExport.setAccelerator(KeyStroke.getKeyStroke('E',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+E
    this.itemListAll.setAccelerator(KeyStroke.getKeyStroke('L',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+L
    this.itemDelete.setAccelerator(KeyStroke
        .getKeyStroke(KeyEvent.VK_DELETE, 0)); // 快捷键：Delete
    this.itemRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0)); // 快捷键：F5
    this.itemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)); // 快捷键：F1
  }

  /**
   * 添加各组件的事件监听器
   */
  private void addListeners() {
    this.itemNew.addActionListener(this);
    this.itemView.addActionListener(this);
    this.itemSearch.addActionListener(this);
    this.itemDelete.addActionListener(this);
    this.itemImport.addActionListener(this);
    this.itemExport.addActionListener(this);
    this.itemRefresh.addActionListener(this);
    this.itemListAll.addActionListener(this);
    this.itemAbout.addActionListener(this);
    // 为窗口添加事件监听器
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        exit();
      }
    });
  }

  /**
   * "退出"的处理方法
   */
  private void exit() {
    boolean toExit = true;
    if (toExit) {
      System.exit(0);
    }
  }

  /**
   * 获取当前表格中所选数据项的ID
   */
  private int getCurrentItemId() {
    int id = -1;
    int currentRow = this.tabMain.getSelectedRow();
    if (currentRow < 0) {
      return id;
    }
    String strId = this.tabMain.getValueAt(currentRow, 0).toString();
    try {
      id = Integer.parseInt(strId);
    } catch (NumberFormatException x) {
      x.printStackTrace();
    }
    return id;
  }

  private void viewCurrentItem() {
    int id = this.getCurrentItemId();
    if (id < 0) {
      JOptionPane.showMessageDialog(this, "请选择一条数据！", "查看失败！",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      this.connection = Util.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery("select * from "
          + Util.TABLE_NAME + " where id=" + id);
      int n = 1;
      if (this.resultSet.next()) {
        this.arrViewItem[0] = String.valueOf(id);
        for (; n < Util.TABLE_COLUMN; n++) {
          this.arrViewItem[n] = this.resultSet.getString(n + 1);
        }
      }
      this.openEditItemDialog(id, this.arrViewItem);
    } catch (SQLException x) {
      x.printStackTrace();
    } finally {
      try {
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
      } catch (SQLException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * "关于"的处理方法
   */
  private void showAbout() {
    if (this.aboutDialog == null) {
      final String strBaiduSpace = "http://hi.baidu.com/xiboliya";
      final String strEmail = "chenzhengfeng@163.com";
      String[] arrStrLabel = new String[] {
          "软件名称：" + Util.SOFTWARE,
          "软件版本：" + Util.VERSION,
          "软件作者：冰原",
          "<html>百度空间：<a href='" + strBaiduSpace + "'>" + strBaiduSpace
              + "</a></html>",
          "<html>作者邮箱：<font color=red>" + strEmail + "</font></html>",
          "软件版权：遵循GNU GPL第三版开源许可协议的相关条款" };
      this.aboutDialog = new AboutDialog(this, true, arrStrLabel, Util.SW_ICON);
      this.aboutDialog.addLinkByIndex(3, strBaiduSpace);
      this.aboutDialog.pack(); // 自动调整窗口大小，以适应各组件
    }
    this.aboutDialog.setVisible(true);
  }

  /**
   * "添加"的处理方法
   */
  private void openEditItemDialog(int id, String[] arrViewItem) {
    if (this.editItemDialog == null) {
      this.editItemDialog = new EditItemDialog(this, false, id, arrViewItem); // 第2个参数必须为false，否则无法更新已有的数据项
    } else {
      this.editItemDialog.setCurrentId(id);
      this.editItemDialog.setViewItems(arrViewItem);
      this.editItemDialog.setVisible(true);
    }
  }

  /**
   * "搜索"的处理方法
   */
  private void openSearchItemDialog() {
    if (this.searchItemDialog == null) {
      this.searchItemDialog = new SearchItemDialog(this, true);
    } else {
      this.searchItemDialog.setVisible(true);
    }
  }

  /**
   * “刷新”的处理方法
   */
  public void refresh() {
    this.getCells(this.sql);
    this.tabMain.updateUI();
  }

  /**
   * 设置当前的SQL语句
   * 
   * @param sql
   *          给定的SQL语句
   */
  public void setSql(String sql) {
    this.sql = sql;
  }

  /**
   * “删除”的处理方法
   */
  private void delete() {
    int id = this.getCurrentItemId();
    if (id < 0) {
      JOptionPane.showMessageDialog(this, "请选择一条数据！", "删除失败！",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    int result = JOptionPane.showConfirmDialog(this, "将要删除序号为：" + id
        + " 的数据，是否继续？", Util.SOFTWARE, JOptionPane.YES_NO_CANCEL_OPTION);
    if (result != JOptionPane.YES_OPTION) {
      return;
    }
    try {
      this.connection = Util.getConnection();
      this.statement = this.connection.createStatement();
      this.statement.execute("delete from " + Util.TABLE_NAME + " where id="
          + id);
      JOptionPane.showMessageDialog(this, "成功删除数据！", "删除成功！",
          JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException x) {
      JOptionPane.showMessageDialog(this, "发生未知错误，删除失败！", "删除失败！",
          JOptionPane.ERROR_MESSAGE);
      x.printStackTrace();
    } finally {
      try {
        this.statement.close();
        this.connection.close();
      } catch (SQLException x) {
        // x.printStackTrace();
      }
    }
    this.getCells(this.sql);
    this.tabMain.updateUI();
  }

  /**
   * “浏览全部”的处理方法
   */
  private void listAll() {
    this.getCells("select * from " + Util.TABLE_NAME);
    this.tabMain.updateUI();
  }

  /**
   * “导出”的处理方法
   */
  private void exportItems() {
    Vector<Vector> cellsItems = new Vector<Vector>();
    Vector<String> cellsItem = null;
    int n = 2;
    try {
      this.connection = Util.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(this.sql);
      while (this.resultSet.next()) {
        cellsItem = new Vector<String>();
        cellsItem.add(String.valueOf(this.resultSet.getInt(1)));
        for (n = 2; n <= Util.TABLE_COLUMN; n++) {
          cellsItem.add(this.resultSet.getString(n));
        }
        cellsItems.add(cellsItem);
      }
    } catch (SQLException x) {
      x.printStackTrace();
    } catch (Exception x) {
      x.printStackTrace();
    } finally {
      try {
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
      } catch (SQLException x) {
        // x.printStackTrace();
        return;
      }
    }
    if (this.saveFileChooser == null) {
      this.saveFileChooser = new SaveFileChooser();
    }
    this.saveFileChooser.setDialogTitle("导出多项");
    this.saveFileChooser.setSelectedFile(null);
    if (JFileChooser.APPROVE_OPTION != this.saveFileChooser
        .showSaveDialog(this)) {
      return;
    }
    File file = this.saveFileChooser.getSelectedFile();
    Util.exportToXLS(this, cellsItems, file);
  }

  /**
   * “导入”的处理方法
   */
  private void importItems() {
    if (this.openFileChooser == null) {
      this.openFileChooser = new OpenFileChooser();
    }
    this.openFileChooser.setSelectedFile(null);
    if (JFileChooser.APPROVE_OPTION != this.openFileChooser
        .showOpenDialog(this)) {
      return;
    }
    File file = this.openFileChooser.getSelectedFile();
    if (file == null || !file.exists()) {
      return;
    }
    InputStreamReader inputStreamReader = null;
    BufferedReader reader = null;
    String strBase = "";
    try {
      inputStreamReader = new InputStreamReader(new FileInputStream(file), "GB18030");
      reader = new BufferedReader(inputStreamReader);
      String line = reader.readLine();
      this.cellsImport.clear();
      this.cellsImport.add(line.split("\t", -1));
      while (line != null) {
        line = reader.readLine();
        this.cellsImport.add(line.split("\t", -1));
      }
    } catch (Exception x) {
      x.printStackTrace();
    } finally {
      try {
        inputStreamReader.close();
      } catch (IOException x) {
        x.printStackTrace();
      }
    }
    if (this.cellsImport == null) {
      JOptionPane.showMessageDialog(this, "文件：" + file
          + " 导入失败！", Util.SOFTWARE, JOptionPane.ERROR_MESSAGE);
      return;
    }
    this.cellsSql.clear();
    for (String[] arrItem : this.cellsImport) {
      try {
        Integer.parseInt(arrItem[0]);
      } catch (NumberFormatException x) {
        continue;
      }
      String value = "";
      for (int i = 1; i < arrItem.length; i++) {
        value += ",'" + arrItem[i] + "'";
      }
      System.out.println("length:"+arrItem.length);
      value = value.substring(1);
      this.cellsSql.add("insert into " + Util.TABLE_NAME 
      + " (factoryName,foundDate,interviewDate,factoryAddress,factoryProperty,factoryEmail,businessEntity,businessEntityPhone,businessEntityHandset,linkman,linkmanPhone,linkmanHandset,insideRatio,outsideRatio,tradeType,staffQuantity,buyOrLease,mainBusinessSphere,registeredCapital,country,yearNum,yearSalesVolume,yearTaxationOfProfit,yearForeignExchangeEarning,factorySynopsis,factoryProblemOrNeeds,otherItems,interviewPersonnel,expositionNeeds) values ("+value+")");
    }
    int n = 0;
    try {
      this.connection = Util.getConnection();
      this.statement = this.connection.createStatement();
      for (String strSql : this.cellsSql) {
        this.statement.executeUpdate(strSql);
        n++;
      }
      JOptionPane.showMessageDialog(this, "成功导入" + n + "行数据！",
          "导入成功！", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException x) {
      x.printStackTrace();
      JOptionPane.showMessageDialog(this, "导入发生未知错误！已成功导入" + n + "行数据！",
          "部分导入成功！", JOptionPane.INFORMATION_MESSAGE);
    } finally {
      try {
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
      } catch (SQLException x) {
        // x.printStackTrace();
      }
    }
    this.refresh();
  }
  
  /**
   * 为各菜单项添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.itemAbout.equals(e.getSource())) {
      this.showAbout();
    } else if (this.itemImport.equals(e.getSource())
        || this.toolButtonList.get(0).equals(e.getSource())) {
      this.importItems();
    } else if (this.itemNew.equals(e.getSource())
        || this.toolButtonList.get(1).equals(e.getSource())) {
      this.openEditItemDialog(-1, null);
    } else if (this.itemView.equals(e.getSource())
        || this.toolButtonList.get(2).equals(e.getSource())) {
      this.viewCurrentItem();
    } else if (this.itemSearch.equals(e.getSource())
        || this.toolButtonList.get(3).equals(e.getSource())) {
      this.openSearchItemDialog();
    } else if (this.itemDelete.equals(e.getSource())
        || this.toolButtonList.get(4).equals(e.getSource())) {
      this.delete();
    } else if (this.itemExport.equals(e.getSource())
        || this.toolButtonList.get(5).equals(e.getSource())) {
      this.exportItems();
    } else if (this.itemRefresh.equals(e.getSource())
        || this.toolButtonList.get(6).equals(e.getSource())) {
      this.refresh();
    } else if (this.itemListAll.equals(e.getSource())
        || this.toolButtonList.get(7).equals(e.getSource())) {
      this.listAll();
    }
  }

}
