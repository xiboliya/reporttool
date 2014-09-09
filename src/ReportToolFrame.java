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
 * ������ݲ�ѯ���ܹ���
 * 
 * @author ��ԭ
 * 
 */
public class ReportToolFrame extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L; // ���л�����ʱʹ�õ�һ���汾�ţ����뵱ǰ�����л��������
  private JToolBar tlbMain = new JToolBar(); // ��ʾ���ð�ť�Ĺ��������
  private JTable tabMain = null; // ��ʾ���ݵı�����
  private JScrollPane spnMain = null;
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menuOperation = new JMenu("����(P)");
  private JMenuItem itemNew = new JMenuItem("���(N)", 'N');
  private JMenuItem itemView = new JMenuItem("�鿴(V)", 'V');
  private JMenuItem itemSearch = new JMenuItem("����(S)...", 'S');
  private JMenuItem itemDelete = new JMenuItem("ɾ��(D)", 'D');
  private JMenuItem itemImport = new JMenuItem("����(I)...", 'I');
  private JMenuItem itemExport = new JMenuItem("����(X)...", 'X');
  private JMenuItem itemRefresh = new JMenuItem("ˢ��(R)", 'R');
  private JMenuItem itemListAll = new JMenuItem("���ȫ��(L)", 'L');
  private JMenu menuHelp = new JMenu("����(H)");
  private JMenuItem itemAbout = new JMenuItem("����(A)", 'A');

  private JPopupMenu popMenuTabbed = new JPopupMenu();
  private JMenuItem itemPopCloseCurrent = new JMenuItem("�رյ�ǰ(C)", 'C');
  private JMenuItem itemPopCloseOthers = new JMenuItem("�ر�����(O)", 'O');
  private JMenuItem itemPopSave = new JMenuItem("����(S)", 'S');
  private JMenuItem itemPopSaveAs = new JMenuItem("���Ϊ(A)...", 'A');
  private JMenuItem itemPopReName = new JMenuItem("������(N)...", 'N');
  private JMenuItem itemPopDelFile = new JMenuItem("ɾ���ļ�(D)", 'D');
  private JMenuItem itemPopReOpen = new JMenuItem("��������(R)", 'R');

  private LinkedList<AbstractButton> toolButtonList = new LinkedList<AbstractButton>(); // ��Ź����������а�ť������
  private BaseDefaultTableModel baseDefaultTableModel = null;
  private String[] arrViewItem = new String[Util.TABLE_COLUMN]; // ���ڴ�ŵ�ǰ�����ѡ�������ݵ�����
  private String sql = "select * from " + Util.TABLE_NAME;
  private Vector<Vector> cells = new Vector<Vector>();
  private Vector<String> cellsTitle = new Vector<String>();
  private Vector<String[]> cellsImport = new Vector<String[]>();
  private Vector<String> cellsSql = new Vector<String>();

  private Connection connection = null; // �����ݿ�����Ӷ���
  private Statement statement = null; // ����ִ�о�̬SQL��䲢�����������ɽ���Ķ���
  private ResultSet resultSet = null; // ���ݿ���������

  private AboutDialog aboutDialog = null; // ���ڶԻ���
  private EditItemDialog editItemDialog = null; // ��ӱ�����ݶԻ���
  private SearchItemDialog searchItemDialog = null; // ����������ݶԻ���
  private SaveFileChooser saveFileChooser = null; // "����"�ļ�ѡ����
  private OpenFileChooser openFileChooser = null; // "����"�ļ�ѡ����

  /**
   * ���췽�� ���ڳ�ʼ�����������
   */
  public ReportToolFrame() {
    this.setTitle(Util.SOFTWARE);
    this.setSize(700, 600);
    this.setMinimumSize(new Dimension(600, 500)); // �������������С�ߴ�
    this.setLocationRelativeTo(null); // ʹ���ھ�����ʾ
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // ����Ĭ�Ϲرղ���Ϊ�գ��Ա���Ӵ��ڼ����¼�
    this.init();
    this.setIcon();
    this.setVisible(true);
  }

  /**
   * �����Զ���Ĵ���ͼ��
   */
  private void setIcon() {
    try {
      this.setIconImage(Util.SW_ICON.getImage());
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  /**
   * ��ʼ���������Ӽ�����
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
   * ���������ӹ�������ͼ
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
   * ���������Ӳ˵���
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
   * ��ʼ����ݲ˵�
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
    popSize.width += popSize.width / 5; // Ϊ�����ۣ��ʵ��ӿ�˵�����ʾ
    this.popMenuTabbed.setPopupSize(popSize);
  }

  /**
   * ���������ӱ����ͼ
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
   * ��ȡ���ݱ����ض������µ�������
   * 
   * @param sql
   *          ������SQL���
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
   * Ϊ���˵����������Ƿ��Ϳ�ݼ�
   */
  private void setMenuMnemonic() {
    this.menuOperation.setMnemonic('P');
    this.menuHelp.setMnemonic('H');
    this.itemNew.setAccelerator(KeyStroke.getKeyStroke('N',
        InputEvent.CTRL_DOWN_MASK)); // ��ݼ���Ctrl+N
    this.itemView.setAccelerator(KeyStroke.getKeyStroke('O',
        InputEvent.CTRL_DOWN_MASK)); // ��ݼ���Ctrl+O
    this.itemSearch.setAccelerator(KeyStroke.getKeyStroke('F',
        InputEvent.CTRL_DOWN_MASK)); // ��ݼ���Ctrl+F
    this.itemImport.setAccelerator(KeyStroke.getKeyStroke('I',
        InputEvent.CTRL_DOWN_MASK)); // ��ݼ���Ctrl+I
    this.itemExport.setAccelerator(KeyStroke.getKeyStroke('E',
        InputEvent.CTRL_DOWN_MASK)); // ��ݼ���Ctrl+E
    this.itemListAll.setAccelerator(KeyStroke.getKeyStroke('L',
        InputEvent.CTRL_DOWN_MASK)); // ��ݼ���Ctrl+L
    this.itemDelete.setAccelerator(KeyStroke
        .getKeyStroke(KeyEvent.VK_DELETE, 0)); // ��ݼ���Delete
    this.itemRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0)); // ��ݼ���F5
    this.itemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)); // ��ݼ���F1
  }

  /**
   * ��Ӹ�������¼�������
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
    // Ϊ��������¼�������
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        exit();
      }
    });
  }

  /**
   * "�˳�"�Ĵ�����
   */
  private void exit() {
    boolean toExit = true;
    if (toExit) {
      System.exit(0);
    }
  }

  /**
   * ��ȡ��ǰ�������ѡ�������ID
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
      JOptionPane.showMessageDialog(this, "��ѡ��һ�����ݣ�", "�鿴ʧ�ܣ�",
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
   * "����"�Ĵ�����
   */
  private void showAbout() {
    if (this.aboutDialog == null) {
      final String strBaiduSpace = "http://hi.baidu.com/xiboliya";
      final String strEmail = "chenzhengfeng@163.com";
      String[] arrStrLabel = new String[] {
          "������ƣ�" + Util.SOFTWARE,
          "����汾��" + Util.VERSION,
          "������ߣ���ԭ",
          "<html>�ٶȿռ䣺<a href='" + strBaiduSpace + "'>" + strBaiduSpace
              + "</a></html>",
          "<html>�������䣺<font color=red>" + strEmail + "</font></html>",
          "�����Ȩ����ѭGNU GPL�����濪Դ���Э����������" };
      this.aboutDialog = new AboutDialog(this, true, arrStrLabel, Util.SW_ICON);
      this.aboutDialog.addLinkByIndex(3, strBaiduSpace);
      this.aboutDialog.pack(); // �Զ��������ڴ�С������Ӧ�����
    }
    this.aboutDialog.setVisible(true);
  }

  /**
   * "���"�Ĵ�����
   */
  private void openEditItemDialog(int id, String[] arrViewItem) {
    if (this.editItemDialog == null) {
      this.editItemDialog = new EditItemDialog(this, false, id, arrViewItem); // ��2����������Ϊfalse�������޷��������е�������
    } else {
      this.editItemDialog.setCurrentId(id);
      this.editItemDialog.setViewItems(arrViewItem);
      this.editItemDialog.setVisible(true);
    }
  }

  /**
   * "����"�Ĵ�����
   */
  private void openSearchItemDialog() {
    if (this.searchItemDialog == null) {
      this.searchItemDialog = new SearchItemDialog(this, true);
    } else {
      this.searchItemDialog.setVisible(true);
    }
  }

  /**
   * ��ˢ�¡��Ĵ�����
   */
  public void refresh() {
    this.getCells(this.sql);
    this.tabMain.updateUI();
  }

  /**
   * ���õ�ǰ��SQL���
   * 
   * @param sql
   *          ������SQL���
   */
  public void setSql(String sql) {
    this.sql = sql;
  }

  /**
   * ��ɾ�����Ĵ�����
   */
  private void delete() {
    int id = this.getCurrentItemId();
    if (id < 0) {
      JOptionPane.showMessageDialog(this, "��ѡ��һ�����ݣ�", "ɾ��ʧ�ܣ�",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    int result = JOptionPane.showConfirmDialog(this, "��Ҫɾ�����Ϊ��" + id
        + " �����ݣ��Ƿ������", Util.SOFTWARE, JOptionPane.YES_NO_CANCEL_OPTION);
    if (result != JOptionPane.YES_OPTION) {
      return;
    }
    try {
      this.connection = Util.getConnection();
      this.statement = this.connection.createStatement();
      this.statement.execute("delete from " + Util.TABLE_NAME + " where id="
          + id);
      JOptionPane.showMessageDialog(this, "�ɹ�ɾ�����ݣ�", "ɾ���ɹ���",
          JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException x) {
      JOptionPane.showMessageDialog(this, "����δ֪����ɾ��ʧ�ܣ�", "ɾ��ʧ�ܣ�",
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
   * �����ȫ�����Ĵ�����
   */
  private void listAll() {
    this.getCells("select * from " + Util.TABLE_NAME);
    this.tabMain.updateUI();
  }

  /**
   * ���������Ĵ�����
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
    this.saveFileChooser.setDialogTitle("��������");
    this.saveFileChooser.setSelectedFile(null);
    if (JFileChooser.APPROVE_OPTION != this.saveFileChooser
        .showSaveDialog(this)) {
      return;
    }
    File file = this.saveFileChooser.getSelectedFile();
    Util.exportToXLS(this, cellsItems, file);
  }

  /**
   * �����롱�Ĵ�����
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
      JOptionPane.showMessageDialog(this, "�ļ���" + file
          + " ����ʧ�ܣ�", Util.SOFTWARE, JOptionPane.ERROR_MESSAGE);
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
      JOptionPane.showMessageDialog(this, "�ɹ�����" + n + "�����ݣ�",
          "����ɹ���", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException x) {
      x.printStackTrace();
      JOptionPane.showMessageDialog(this, "���뷢��δ֪�����ѳɹ�����" + n + "�����ݣ�",
          "���ֵ���ɹ���", JOptionPane.INFORMATION_MESSAGE);
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
   * Ϊ���˵�������¼��Ĵ�����
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
