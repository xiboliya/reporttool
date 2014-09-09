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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * "�༭�������"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class EditItemDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlContent = new JPanel();
  private JPanel pnlBtn = new JPanel();
  private JPanel pnlBusinessEntity = new JPanel();
  private JPanel pnlLinkman = new JPanel();
  private JPanel pnlRatio = new JPanel();
  private JPanel pnlYear = new JPanel();

  private JLabel lblFactoryName = new JLabel("��λ����:");
  private JLabel lblFactoryAddress = new JLabel("��λ��ַ:");
  private JLabel lblFoundDate = new JLabel("����ʱ��:");
  private JLabel lblFactoryProperty = new JLabel("��ҵ����:");
  private JLabel lblMainBusinessSphere = new JLabel("��Ҫ��Ӫ��Χ:");
  private JLabel lblFactoryEmail = new JLabel("��ϵ����:");
  private JLabel lblBusinessEntity = new JLabel("��ҵ����:");
  private JLabel lblBusinessEntityPhone = new JLabel("�绰:");
  private JLabel lblBusinessEntityHandset = new JLabel("�ֻ�:");
  private JLabel lblLinkman = new JLabel("��ϵ��:");
  private JLabel lblLinkmanPhone = new JLabel("�绰:");
  private JLabel lblLinkmanHandset = new JLabel("�ֻ�:");
  private JLabel lblInsideRatio = new JLabel("��������:");
  private JLabel lblOutsideRatio = new JLabel("��������:");
  private JLabel lblTradeType = new JLabel("��ҵ����ʶ��:");
  private JLabel lblCountry = new JLabel("����:");
  private JLabel lblBuyOrLease = new JLabel("���/���޳���:");
  private JLabel lblStaffQuantity = new JLabel("Ա������:");
  private JLabel lblRegisteredCapital = new JLabel("ע���ʱ�(��Ԫ):");
  private JLabel lblFactorySynopsis = new JLabel("��ҵ���:");
  private JLabel lblFactoryProblemOrNeeds1 = new JLabel("��ҵ��������");
  private JLabel lblFactoryProblemOrNeeds2 = new JLabel("����������:");
  private JLabel lblExpositionNeeds = new JLabel("չ������:");
  private JLabel lblOtherItems1 = new JLabel("������Ҫ��¼");
  private JLabel lblOtherItems2 = new JLabel("����:");
  private JLabel lblYearNum = new JLabel("���:");
  private JLabel lblYearSalesVolume = new JLabel("���۶�(��Ԫ):");
  private JLabel lblYearTaxationOfProfit = new JLabel("��˰(��Ԫ):");
  private JLabel lblYearForeignExchangeEarning = new JLabel("���ڴ���(����Ԫ):");
  private JLabel lblInterviewDate = new JLabel("�߷�ʱ��:");
  private JLabel lblInterviewPersonnel = new JLabel("�߷���Ա:");
  private JLabel lblCurrentId = new JLabel();

  private BaseTextField txtFactoryName = new BaseTextField();
  private BaseTextField txtFactoryAddress = new BaseTextField();
  private BaseTextField txtFoundDate = new BaseTextField();
  private BaseTextField txtFactoryProperty = new BaseTextField();
  private BaseTextField txtFactoryEmail = new BaseTextField();
  private BaseTextField txtBusinessEntity = new BaseTextField();
  private BaseTextField txtBusinessEntityPhone = new BaseTextField();
  private BaseTextField txtBusinessEntityHandset = new BaseTextField();
  private BaseTextField txtLinkman = new BaseTextField();
  private BaseTextField txtLinkmanPhone = new BaseTextField();
  private BaseTextField txtLinkmanHandset = new BaseTextField();
  private BaseTextField txtInsideRatio = new BaseTextField();
  private BaseTextField txtOutsideRatio = new BaseTextField();
  private BaseTextField txtTradeType = new BaseTextField();
  private BaseTextField txtCountry = new BaseTextField();
  private BaseTextField txtBuyOrLease = new BaseTextField();
  private BaseTextField txtStaffQuantity = new BaseTextField();
  private BaseTextField txtRegisteredCapital = new BaseTextField();
  private BaseTextField txtYearNum = new BaseTextField();
  private BaseTextField txtYearSalesVolume = new BaseTextField();
  private BaseTextField txtYearTaxationOfProfit = new BaseTextField();
  private BaseTextField txtYearForeignExchangeEarning = new BaseTextField();
  private BaseTextField txtInterviewDate = new BaseTextField();
  private BaseTextField txtInterviewPersonnel = new BaseTextField();

  private BaseTextArea txaMainBusinessSphere = new BaseTextArea();
  private BaseTextArea txaFactorySynopsis = new BaseTextArea();
  private BaseTextArea txaFactoryProblemOrNeeds = new BaseTextArea();
  private BaseTextArea txaExpositionNeeds = new BaseTextArea();
  private BaseTextArea txaOtherItems = new BaseTextArea();

  private JPanel pnlMainBusinessSphere = new JPanel(new BorderLayout());
  private JPanel pnlFactorySynopsis = new JPanel(new BorderLayout());
  private JPanel pnlFactoryProblemOrNeeds = new JPanel(new BorderLayout());
  private JPanel pnlExpositionNeeds = new JPanel(new BorderLayout());
  private JPanel pnlOtherItems = new JPanel(new BorderLayout());

  private JButton btnSave = new JButton("����");
  private JButton btnDelete = new JButton("ɾ��");
  private JButton btnNew = new JButton("�½�");
  private JButton btnExport = new JButton("����");
  private JButton btnCancel = new JButton("ȡ��");

  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Connection connection = null; // �����ݿ�����Ӷ���
  private Statement statement = null; // ����ִ�о�̬SQL��䲢�����������ɽ���Ķ���
  private ResultSet resultSet = null; // ���ݿ���������
  private int currentId = -1; // ��ǰ�����������ݿ����е����
  private String sql = null;
  private String[] arrViewItem = null;
  private SaveFileChooser saveFileChooser = null; // "����"�ļ�ѡ����
  private Vector<Vector> cells = new Vector<Vector>();

  public EditItemDialog(JFrame owner, boolean modal, int currentId,
      String[] arrViewItem) {
    super(owner, modal);
    this.currentId = currentId;
    this.arrViewItem = arrViewItem;
    this.init();
    this.addListeners();
    this.setSize(880, 700);
    this.setVisible(true);
  }

  /**
   * ��д����ķ��������ñ������Ƿ�ɼ�
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.fillContents();
    }
    super.setVisible(visible);
  }

  /**
   * ��ʼ������
   */
  private void init() {
    this.setTitle("�༭�������");
    this.pnlMain.setLayout(null);
    this.pnlContent.setLayout(null);
    this.pnlContent.setBorder(new TitledBorder("��Ϣ"));
    this.pnlContent.setBounds(0, 0, 730, 670);
    this.lblFactoryName.setBounds(10, 15, 60, Util.VIEW_HEIGHT);
    this.txtFactoryName.setBounds(70, 15, 290, Util.INPUT_HEIGHT);
    this.lblFactoryAddress.setBounds(370, 15, 60, Util.VIEW_HEIGHT);
    this.txtFactoryAddress.setBounds(430, 15, 290, Util.INPUT_HEIGHT);
    this.pnlContent.add(this.lblFactoryName);
    this.pnlContent.add(this.txtFactoryName);
    this.pnlContent.add(this.lblFactoryAddress);
    this.pnlContent.add(this.txtFactoryAddress);

    this.lblFoundDate.setBounds(10, 45, 60, Util.VIEW_HEIGHT);
    this.txtFoundDate.setBounds(70, 45, 100, Util.INPUT_HEIGHT);
    this.lblFactoryProperty.setBounds(185, 45, 65, Util.VIEW_HEIGHT);
    this.txtFactoryProperty.setBounds(250, 45, 100, Util.INPUT_HEIGHT);
    this.lblTradeType.setBounds(360, 45, 85, Util.VIEW_HEIGHT);
    this.txtTradeType.setBounds(445, 45, 100, Util.INPUT_HEIGHT);
    this.lblFactoryEmail.setBounds(555, 45, 65, Util.VIEW_HEIGHT);
    this.txtFactoryEmail.setBounds(620, 45, 100, Util.INPUT_HEIGHT);
    this.pnlContent.add(this.lblFoundDate);
    this.pnlContent.add(this.txtFoundDate);
    this.pnlContent.add(this.lblFactoryProperty);
    this.pnlContent.add(this.txtFactoryProperty);
    this.pnlContent.add(this.lblTradeType);
    this.pnlContent.add(this.txtTradeType);
    this.pnlContent.add(this.lblFactoryEmail);
    this.pnlContent.add(this.txtFactoryEmail);

    this.lblCountry.setBounds(10, 75, 30, Util.VIEW_HEIGHT);
    this.txtCountry.setBounds(45, 75, 100, Util.INPUT_HEIGHT);
    this.lblRegisteredCapital.setBounds(155, 75, 95, Util.VIEW_HEIGHT);
    this.txtRegisteredCapital.setBounds(250, 75, 100, Util.INPUT_HEIGHT);
    this.lblStaffQuantity.setBounds(360, 75, 60, Util.VIEW_HEIGHT);
    this.txtStaffQuantity.setBounds(420, 75, 100, Util.INPUT_HEIGHT);
    this.lblBuyOrLease.setBounds(530, 75, 90, Util.VIEW_HEIGHT);
    this.txtBuyOrLease.setBounds(620, 75, 100, Util.INPUT_HEIGHT);
    this.pnlContent.add(this.lblCountry);
    this.pnlContent.add(this.txtCountry);
    this.pnlContent.add(this.lblRegisteredCapital);
    this.pnlContent.add(this.txtRegisteredCapital);
    this.pnlContent.add(this.lblStaffQuantity);
    this.pnlContent.add(this.txtStaffQuantity);
    this.pnlContent.add(this.lblBuyOrLease);
    this.pnlContent.add(this.txtBuyOrLease);

    this.pnlBusinessEntity.setLayout(null);
    this.pnlBusinessEntity.setBorder(new EtchedBorder());
    this.pnlBusinessEntity.setBounds(10, 110, 480, 40);
    this.lblBusinessEntity.setBounds(10, 6, 60, Util.VIEW_HEIGHT);
    this.txtBusinessEntity.setBounds(70, 6, 100, Util.INPUT_HEIGHT);
    this.lblBusinessEntityPhone.setBounds(185, 6, 35, Util.VIEW_HEIGHT);
    this.txtBusinessEntityPhone.setBounds(220, 6, 100, Util.INPUT_HEIGHT);
    this.lblBusinessEntityHandset.setBounds(330, 6, 35, Util.VIEW_HEIGHT);
    this.txtBusinessEntityHandset.setBounds(365, 6, 100, Util.INPUT_HEIGHT);
    this.pnlBusinessEntity.add(this.lblBusinessEntity);
    this.pnlBusinessEntity.add(this.txtBusinessEntity);
    this.pnlBusinessEntity.add(this.lblBusinessEntityPhone);
    this.pnlBusinessEntity.add(this.txtBusinessEntityPhone);
    this.pnlBusinessEntity.add(this.lblBusinessEntityHandset);
    this.pnlBusinessEntity.add(this.txtBusinessEntityHandset);
    this.pnlContent.add(this.pnlBusinessEntity);

    this.pnlLinkman.setLayout(null);
    this.pnlLinkman.setBorder(new EtchedBorder());
    this.pnlLinkman.setBounds(10, 155, 480, 40);
    this.lblLinkman.setBounds(10, 6, 60, Util.VIEW_HEIGHT);
    this.txtLinkman.setBounds(70, 6, 100, Util.INPUT_HEIGHT);
    this.lblLinkmanPhone.setBounds(185, 6, 35, Util.VIEW_HEIGHT);
    this.txtLinkmanPhone.setBounds(220, 6, 100, Util.INPUT_HEIGHT);
    this.lblLinkmanHandset.setBounds(330, 6, 35, Util.VIEW_HEIGHT);
    this.txtLinkmanHandset.setBounds(365, 6, 100, Util.INPUT_HEIGHT);
    this.pnlLinkman.add(this.lblLinkman);
    this.pnlLinkman.add(this.txtLinkman);
    this.pnlLinkman.add(this.lblLinkmanPhone);
    this.pnlLinkman.add(this.txtLinkmanPhone);
    this.pnlLinkman.add(this.lblLinkmanHandset);
    this.pnlLinkman.add(this.txtLinkmanHandset);
    this.pnlContent.add(this.pnlLinkman);

    this.pnlRatio.setLayout(null);
    this.pnlRatio.setBorder(new EtchedBorder());
    this.pnlRatio.setBounds(520, 110, 190, 85);
    this.lblInsideRatio.setBounds(10, 12, 60, Util.VIEW_HEIGHT);
    this.txtInsideRatio.setBounds(70, 12, 100, Util.INPUT_HEIGHT);
    this.lblOutsideRatio.setBounds(10, 47, 60, Util.VIEW_HEIGHT);
    this.txtOutsideRatio.setBounds(70, 47, 100, Util.INPUT_HEIGHT);
    this.pnlRatio.add(this.lblInsideRatio);
    this.pnlRatio.add(this.txtInsideRatio);
    this.pnlRatio.add(this.lblOutsideRatio);
    this.pnlRatio.add(this.txtOutsideRatio);
    this.pnlContent.add(this.pnlRatio);

    this.pnlYear.setLayout(null);
    this.pnlYear.setBorder(new TitledBorder("�����Ϣͳ��"));
    this.pnlYear.setBounds(10, 200, 710, 60);
    this.lblYearNum.setBounds(10, 20, 35, Util.VIEW_HEIGHT);
    this.txtYearNum.setBounds(45, 20, 90, Util.INPUT_HEIGHT);
    this.lblYearSalesVolume.setBounds(145, 20, 85, Util.VIEW_HEIGHT);
    this.txtYearSalesVolume.setBounds(230, 20, 90, Util.INPUT_HEIGHT);
    this.lblYearTaxationOfProfit.setBounds(330, 20, 70, Util.VIEW_HEIGHT);
    this.txtYearTaxationOfProfit.setBounds(400, 20, 90, Util.INPUT_HEIGHT);
    this.lblYearForeignExchangeEarning
        .setBounds(500, 20, 107, Util.VIEW_HEIGHT);
    this.txtYearForeignExchangeEarning
        .setBounds(607, 20, 90, Util.INPUT_HEIGHT);
    this.pnlYear.add(this.lblYearNum);
    this.pnlYear.add(this.txtYearNum);
    this.pnlYear.add(this.lblYearSalesVolume);
    this.pnlYear.add(this.txtYearSalesVolume);
    this.pnlYear.add(this.lblYearTaxationOfProfit);
    this.pnlYear.add(this.txtYearTaxationOfProfit);
    this.pnlYear.add(this.lblYearForeignExchangeEarning);
    this.pnlYear.add(this.txtYearForeignExchangeEarning);
    this.pnlContent.add(this.pnlYear);

    this.lblMainBusinessSphere.setBounds(10, 290, 90, Util.VIEW_HEIGHT);
    this.txaMainBusinessSphere.setLineWrap(true);
    this.pnlMainBusinessSphere.setBounds(100, 270, 610, 60);
    this.pnlMainBusinessSphere.add(new JScrollPane(this.txaMainBusinessSphere),
        BorderLayout.CENTER);

    this.lblFactorySynopsis.setBounds(10, 360, 90, Util.VIEW_HEIGHT);
    this.txaFactorySynopsis.setLineWrap(true);
    this.pnlFactorySynopsis.setBounds(100, 340, 610, 60);
    this.pnlFactorySynopsis.add(new JScrollPane(this.txaFactorySynopsis),
        BorderLayout.CENTER);

    this.lblFactoryProblemOrNeeds1.setBounds(10, 420, 90, Util.VIEW_HEIGHT);
    this.lblFactoryProblemOrNeeds2.setBounds(10, 420 + Util.VIEW_HEIGHT, 90,
        Util.VIEW_HEIGHT);
    this.txaFactoryProblemOrNeeds.setLineWrap(true);
    this.pnlFactoryProblemOrNeeds.setBounds(100, 410, 610, 60);
    this.pnlFactoryProblemOrNeeds.add(new JScrollPane(
        this.txaFactoryProblemOrNeeds), BorderLayout.CENTER);

    this.lblExpositionNeeds.setBounds(10, 500, 90, Util.VIEW_HEIGHT);
    this.txaExpositionNeeds.setLineWrap(true);
    this.pnlExpositionNeeds.setBounds(100, 480, 610, 60);
    this.pnlExpositionNeeds.add(new JScrollPane(this.txaExpositionNeeds),
        BorderLayout.CENTER);

    this.lblOtherItems1.setBounds(10, 560, 90, Util.VIEW_HEIGHT);
    this.lblOtherItems2.setBounds(10, 560 + Util.VIEW_HEIGHT, 90,
        Util.VIEW_HEIGHT);
    this.txaOtherItems.setLineWrap(true);
    this.pnlOtherItems.setBounds(100, 550, 610, 60);
    this.pnlOtherItems.add(new JScrollPane(this.txaOtherItems),
        BorderLayout.CENTER);

    this.pnlContent.add(this.lblMainBusinessSphere);
    this.pnlContent.add(this.pnlMainBusinessSphere);
    this.pnlContent.add(this.lblFactorySynopsis);
    this.pnlContent.add(this.pnlFactorySynopsis);
    this.pnlContent.add(this.lblFactoryProblemOrNeeds1);
    this.pnlContent.add(this.lblFactoryProblemOrNeeds2);
    this.pnlContent.add(this.pnlFactoryProblemOrNeeds);
    this.pnlContent.add(this.lblExpositionNeeds);
    this.pnlContent.add(this.pnlExpositionNeeds);
    this.pnlContent.add(this.lblOtherItems1);
    this.pnlContent.add(this.lblOtherItems2);
    this.pnlContent.add(this.pnlOtherItems);

    this.lblInterviewPersonnel.setBounds(130, 625, 60, Util.VIEW_HEIGHT);
    this.txtInterviewPersonnel.setBounds(190, 625, 150, Util.INPUT_HEIGHT);
    this.lblInterviewDate.setBounds(380, 625, 60, Util.VIEW_HEIGHT);
    this.txtInterviewDate.setBounds(440, 625, 150, Util.INPUT_HEIGHT);
    this.lblCurrentId.setBounds(610, 625, 100, Util.INPUT_HEIGHT);
    this.lblCurrentId.setBorder(new EtchedBorder());
    this.pnlContent.add(this.lblInterviewPersonnel);
    this.pnlContent.add(this.txtInterviewPersonnel);
    this.pnlContent.add(this.lblInterviewDate);
    this.pnlContent.add(this.txtInterviewDate);
    this.pnlContent.add(this.lblCurrentId);

    this.pnlMain.add(this.pnlContent);

    this.pnlBtn.setLayout(null);
    this.pnlBtn.setBorder(new TitledBorder("����"));
    this.pnlBtn.setBounds(745, 0, 120, 670);
    this.btnSave.setBounds(10, 20, 100, Util.BUTTON_HEIGHT);
    this.btnDelete.setBounds(10, 80, 100, Util.BUTTON_HEIGHT);
    this.btnNew.setBounds(10, 140, 100, Util.BUTTON_HEIGHT);
    this.btnExport.setBounds(10, 200, 100, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(10, 260, 100, Util.BUTTON_HEIGHT);
    this.pnlBtn.add(this.btnSave);
    this.pnlBtn.add(this.btnDelete);
    this.pnlBtn.add(this.btnNew);
    this.pnlBtn.add(this.btnExport);
    this.pnlBtn.add(this.btnCancel);

    this.pnlMain.add(this.pnlBtn);
  }

  /**
   * ����¼�������
   */
  private void addListeners() {
    this.txtFactoryName.addKeyListener(this.keyAdapter);
    this.txtFactoryAddress.addKeyListener(this.keyAdapter);
    this.txtFoundDate.addKeyListener(this.keyAdapter);
    this.txtFactoryProperty.addKeyListener(this.keyAdapter);
    this.txtFactoryEmail.addKeyListener(this.keyAdapter);
    this.txtBusinessEntity.addKeyListener(this.keyAdapter);
    this.txtBusinessEntityPhone.addKeyListener(this.keyAdapter);
    this.txtBusinessEntityHandset.addKeyListener(this.keyAdapter);
    this.txtLinkman.addKeyListener(this.keyAdapter);
    this.txtLinkmanPhone.addKeyListener(this.keyAdapter);
    this.txtLinkmanHandset.addKeyListener(this.keyAdapter);
    this.txtInsideRatio.addKeyListener(this.keyAdapter);
    this.txtOutsideRatio.addKeyListener(this.keyAdapter);
    this.txtTradeType.addKeyListener(this.keyAdapter);
    this.txtCountry.addKeyListener(this.keyAdapter);
    this.txtBuyOrLease.addKeyListener(this.keyAdapter);
    this.txtStaffQuantity.addKeyListener(this.keyAdapter);
    this.txtRegisteredCapital.addKeyListener(this.keyAdapter);
    this.txtYearNum.addKeyListener(this.keyAdapter);
    this.txtYearSalesVolume.addKeyListener(this.keyAdapter);
    this.txtYearTaxationOfProfit.addKeyListener(this.keyAdapter);
    this.txtYearForeignExchangeEarning.addKeyListener(this.keyAdapter);
    this.txtInterviewDate.addKeyListener(this.keyAdapter);
    this.txtInterviewPersonnel.addKeyListener(this.keyAdapter);
    this.txaMainBusinessSphere.addKeyListener(this.keyAdapter);
    this.txaFactorySynopsis.addKeyListener(this.keyAdapter);
    this.txaFactoryProblemOrNeeds.addKeyListener(this.keyAdapter);
    this.txaExpositionNeeds.addKeyListener(this.keyAdapter);
    this.txaOtherItems.addKeyListener(this.keyAdapter);

    this.btnSave.addKeyListener(this.buttonKeyAdapter);
    this.btnDelete.addKeyListener(this.buttonKeyAdapter);
    this.btnNew.addKeyListener(this.buttonKeyAdapter);
    this.btnExport.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);

    this.btnSave.addActionListener(this);
    this.btnDelete.addActionListener(this);
    this.btnNew.addActionListener(this);
    this.btnExport.addActionListener(this);
    this.btnCancel.addActionListener(this);
  }

  /**
   * Ϊ���������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnSave.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnDelete.equals(e.getSource())) {
      this.deleteItem();
    } else if (this.btnNew.equals(e.getSource())) {
      this.createNewItem();
    } else if (this.btnExport.equals(e.getSource())) {
      this.exportItem();
    }
  }

  /**
   * ��ʾ���������������
   */
  private void viewCurrentId() {
    if (this.currentId >= 0) {
      this.lblCurrentId.setText("���:" + this.currentId);
      this.lblCurrentId.setVisible(true);
      this.btnDelete.setEnabled(true);
      this.btnExport.setEnabled(true);
    } else {
      this.lblCurrentId.setVisible(false);
      this.btnDelete.setEnabled(false);
      this.btnExport.setEnabled(false);
    }
  }

  /**
   * ���õ�ǰ���������
   * 
   * @param currentId
   *          ��ǰ���������
   */
  public void setCurrentId(int currentId) {
    this.currentId = currentId;
  }

  /**
   * ���õ�ǰ��������������
   * 
   * @param arrViewItem
   *          ��ǰ��������������
   */
  public void setViewItems(String[] arrViewItem) {
    this.arrViewItem = arrViewItem;
  }

  /**
   * ��ʾ����������������ı�
   */
  private void fillContents() {
    this.viewCurrentId();
    if (this.arrViewItem != null) {
      this.txtFactoryName.setText(this.arrViewItem[1]);
      this.txtFactoryAddress.setText(this.arrViewItem[2]);
      this.txtFoundDate.setText(this.arrViewItem[3]);
      this.txtFactoryProperty.setText(this.arrViewItem[4]);
      this.txaMainBusinessSphere.setText(this.arrViewItem[5]);
      this.txtFactoryEmail.setText(this.arrViewItem[6]);
      this.txtBusinessEntity.setText(this.arrViewItem[7]);
      this.txtBusinessEntityPhone.setText(this.arrViewItem[8]);
      this.txtBusinessEntityHandset.setText(this.arrViewItem[9]);
      this.txtLinkman.setText(this.arrViewItem[10]);
      this.txtLinkmanPhone.setText(this.arrViewItem[11]);
      this.txtLinkmanHandset.setText(this.arrViewItem[12]);
      this.txtInsideRatio.setText(this.arrViewItem[13]);
      this.txtOutsideRatio.setText(this.arrViewItem[14]);
      this.txtTradeType.setText(this.arrViewItem[15]);
      this.txtCountry.setText(this.arrViewItem[16]);
      this.txtBuyOrLease.setText(this.arrViewItem[17]);
      this.txtStaffQuantity.setText(this.arrViewItem[18]);
      this.txtRegisteredCapital.setText(this.arrViewItem[19]);
      this.txaFactorySynopsis.setText(this.arrViewItem[20]);
      this.txaFactoryProblemOrNeeds.setText(this.arrViewItem[21]);
      this.txaExpositionNeeds.setText(this.arrViewItem[22]);
      this.txaOtherItems.setText(this.arrViewItem[23]);
      this.txtYearNum.setText(this.arrViewItem[24]);
      this.txtYearSalesVolume.setText(this.arrViewItem[25]);
      this.txtYearTaxationOfProfit.setText(this.arrViewItem[26]);
      this.txtYearForeignExchangeEarning.setText(this.arrViewItem[27]);
      this.txtInterviewDate.setText(this.arrViewItem[28]);
      this.txtInterviewPersonnel.setText(this.arrViewItem[29]);
    } else {
      this.txtFactoryName.setText("");
      this.txtFactoryAddress.setText("");
      this.txtFoundDate.setText("");
      this.txtFactoryProperty.setText("");
      this.txaMainBusinessSphere.setText("");
      this.txtFactoryEmail.setText("");
      this.txtBusinessEntity.setText("");
      this.txtBusinessEntityPhone.setText("");
      this.txtBusinessEntityHandset.setText("");
      this.txtLinkman.setText("");
      this.txtLinkmanPhone.setText("");
      this.txtLinkmanHandset.setText("");
      this.txtInsideRatio.setText("");
      this.txtOutsideRatio.setText("");
      this.txtTradeType.setText("");
      this.txtCountry.setText("");
      this.txtBuyOrLease.setText("");
      this.txtStaffQuantity.setText("");
      this.txtRegisteredCapital.setText("");
      this.txaFactorySynopsis.setText("");
      this.txaFactoryProblemOrNeeds.setText("");
      this.txaExpositionNeeds.setText("");
      this.txaOtherItems.setText("");
      this.txtYearNum.setText("");
      this.txtYearSalesVolume.setText("");
      this.txtYearTaxationOfProfit.setText("");
      this.txtYearForeignExchangeEarning.setText("");
      this.txtInterviewDate.setText("");
      this.txtInterviewPersonnel.setText("");
    }
  }

  /**
   * ���½����Ĵ�����
   */
  private void createNewItem() {
    this.setCurrentId(-1);
    this.setViewItems(null);
    this.fillContents();
  }

  /**
   * ��ɾ�����Ĵ�����
   */
  private void deleteItem() {
    if (this.currentId < 0) {
      JOptionPane.showMessageDialog(this, "��������δ�����ݱ��У�ɾ��ʧ�ܣ�", "ɾ��ʧ�ܣ�",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    int result = JOptionPane.showConfirmDialog(this, "��Ҫɾ�����Ϊ��"
        + this.currentId + " �����ݣ��Ƿ������", Util.SOFTWARE,
        JOptionPane.YES_NO_CANCEL_OPTION);
    if (result != JOptionPane.YES_OPTION) {
      return;
    }
    this.sql = "delete from " + Util.TABLE_NAME + " where id=" + this.currentId;
    int n = 0;
    try {
      this.connection = Util.getConnection();
      this.statement = this.connection.createStatement();
      n = this.statement.executeUpdate(this.sql);
      if (n == 1) {
        JOptionPane.showMessageDialog(this, "�ɹ�ɾ�����Ϊ��" + this.currentId
            + " �������", "ɾ���ɹ���", JOptionPane.INFORMATION_MESSAGE);
      }
      this.createNewItem();
    } catch (SQLException x) {
      x.printStackTrace();
    } finally {
      try {
        this.statement.close();
        this.connection.close();
      } catch (SQLException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * ����ǰ���ݲ��뵽���ݱ�
   */
  private void insertItem() {
    String strFactoryName = this.txtFactoryName.getText();
    if (strFactoryName.trim().isEmpty()) {
      JOptionPane.showMessageDialog(this, "��λ���Ʋ���Ϊ�գ�����д��", "����ʧ�ܣ�",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    this.sql = "insert into "
        + Util.TABLE_NAME
        + " (factoryName,factoryAddress,foundDate,factoryProperty,mainBusinessSphere,factoryEmail,businessEntity,businessEntityPhone,businessEntityHandset,linkman,linkmanPhone,linkmanHandset,insideRatio,outsideRatio,tradeType,country,buyOrLease,staffQuantity,registeredCapital,factorySynopsis,factoryProblemOrNeeds,expositionNeeds,otherItems,yearNum,yearSalesVolume,yearTaxationOfProfit,yearForeignExchangeEarning,interviewDate,interviewPersonnel) values ('"
        + strFactoryName + "','" + this.txtFactoryAddress.getText() + "','"
        + this.txtFoundDate.getText() + "','"
        + this.txtFactoryProperty.getText() + "','"
        + this.txaMainBusinessSphere.getText() + "','"
        + this.txtFactoryEmail.getText() + "','"
        + this.txtBusinessEntity.getText() + "','"
        + this.txtBusinessEntityPhone.getText() + "','"
        + this.txtBusinessEntityHandset.getText() + "','"
        + this.txtLinkman.getText() + "','" + this.txtLinkmanPhone.getText()
        + "','" + this.txtLinkmanHandset.getText() + "','"
        + this.txtInsideRatio.getText() + "','"
        + this.txtOutsideRatio.getText() + "','" + this.txtTradeType.getText()
        + "','" + this.txtCountry.getText() + "','"
        + this.txtBuyOrLease.getText() + "','"
        + this.txtStaffQuantity.getText() + "','"
        + this.txtRegisteredCapital.getText() + "','"
        + this.txaFactorySynopsis.getText() + "','"
        + this.txaFactoryProblemOrNeeds.getText() + "','"
        + this.txaExpositionNeeds.getText() + "','"
        + this.txaOtherItems.getText() + "','" + this.txtYearNum.getText()
        + "','" + this.txtYearSalesVolume.getText() + "','"
        + this.txtYearTaxationOfProfit.getText() + "','"
        + this.txtYearForeignExchangeEarning.getText() + "','"
        + this.txtInterviewDate.getText() + "','"
        + this.txtInterviewPersonnel.getText() + "')";
    try {
      this.connection = Util.getConnection();
      this.statement = this.connection.createStatement();
      this.statement.executeUpdate(this.sql);
      this.sql = "select id from " + Util.TABLE_NAME + " order by id desc"; // ��ѯ�ղųɹ���������������
      this.resultSet = this.statement.executeQuery(this.sql);
      if (this.resultSet.next()) {
        this.currentId = this.resultSet.getInt(1);
      }
      JOptionPane.showMessageDialog(this, "�ɹ��������ݣ�\n���Ϊ��" + this.currentId,
          "����ɹ���", JOptionPane.INFORMATION_MESSAGE);
      this.viewCurrentId();
      ((ReportToolFrame) this.getOwner()).refresh();
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
   * ���µ�ǰ������
   */
  private void updateItem() {
    String strFactoryName = this.txtFactoryName.getText();
    if (strFactoryName.trim().isEmpty()) {
      JOptionPane.showMessageDialog(this, "��λ���Ʋ���Ϊ�գ�����д��", "����ʧ�ܣ�",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    this.sql = "update " + Util.TABLE_NAME + " set factoryName='"
        + strFactoryName + "',factoryAddress='"
        + this.txtFactoryAddress.getText() + "',foundDate='"
        + this.txtFoundDate.getText() + "',factoryProperty='"
        + this.txtFactoryProperty.getText() + "',mainBusinessSphere='"
        + this.txaMainBusinessSphere.getText() + "',factoryEmail='"
        + this.txtFactoryEmail.getText() + "',businessEntity='"
        + this.txtBusinessEntity.getText() + "',businessEntityPhone='"
        + this.txtBusinessEntityPhone.getText() + "',businessEntityHandset='"
        + this.txtBusinessEntityHandset.getText() + "',linkman='"
        + this.txtLinkman.getText() + "',linkmanPhone='"
        + this.txtLinkmanPhone.getText() + "',linkmanHandset='"
        + this.txtLinkmanHandset.getText() + "',insideRatio='"
        + this.txtInsideRatio.getText() + "',outsideRatio='"
        + this.txtOutsideRatio.getText() + "',tradeType='"
        + this.txtTradeType.getText() + "',country='"
        + this.txtCountry.getText() + "',buyOrLease='"
        + this.txtBuyOrLease.getText() + "',staffQuantity='"
        + this.txtStaffQuantity.getText() + "',registeredCapital='"
        + this.txtRegisteredCapital.getText() + "',factorySynopsis='"
        + this.txaFactorySynopsis.getText() + "',factoryProblemOrNeeds='"
        + this.txaFactoryProblemOrNeeds.getText() + "',expositionNeeds='"
        + this.txaExpositionNeeds.getText() + "',otherItems='"
        + this.txaOtherItems.getText() + "',yearNum='"
        + this.txtYearNum.getText() + "',yearSalesVolume='"
        + this.txtYearSalesVolume.getText() + "',yearTaxationOfProfit='"
        + this.txtYearTaxationOfProfit.getText()
        + "',yearForeignExchangeEarning='"
        + this.txtYearForeignExchangeEarning.getText() + "',interviewDate='"
        + this.txtInterviewDate.getText() + "',interviewPersonnel='"
        + this.txtInterviewPersonnel.getText() + "' where id=" + this.currentId;
    try {
      this.connection = Util.getConnection();
      this.statement = this.connection.createStatement();
      this.statement.executeUpdate(this.sql);
      JOptionPane.showMessageDialog(this, "�ɹ��������ݣ�", "����ɹ���",
          JOptionPane.INFORMATION_MESSAGE);
      ((ReportToolFrame) this.getOwner()).refresh();
    } catch (SQLException x) {
      x.printStackTrace();
    } catch (Exception x) {
      x.printStackTrace();
    } finally {
      try {
        this.statement.close();
        this.connection.close();
      } catch (SQLException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * ���������Ĵ�����
   */
  private void exportItem() {
    if (this.currentId < 0) {
      JOptionPane.showMessageDialog(this, "���ȱ���������Ȼ�����ԣ�", "����ʧ�ܣ�",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    this.sql = "select * from " + Util.TABLE_NAME + " where id="
        + this.currentId;
    this.cells.clear();
    Vector<String> cellsLine = null;
    int n = 2;
    try {
      this.connection = Util.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(sql);
      while (this.resultSet.next()) {
        cellsLine = new Vector<String>();
        cellsLine.add(String.valueOf(this.resultSet.getInt(1)));
        for (n = 2; n <= Util.TABLE_COLUMN; n++) {
          cellsLine.add(this.resultSet.getString(n));
        }
        this.cells.add(cellsLine);
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
    Util.exportToXLS(this, this.cells, file);
  }

  /**
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    if (this.currentId < 0) {
      this.insertItem();
    } else {
      this.updateItem();
    }
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.dispose();
  }
}
