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

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * "��"�ļ�ѡ����
 * 
 * @author ��ԭ
 * 
 */
public class OpenFileChooser extends JFileChooser {
  private static final long serialVersionUID = 1L;

  public OpenFileChooser() {
    super();
  }

  /**
   * ���û�ȷ��ʱ�����ô˷���
   */
  public void approveSelection() {
    File file = this.getSelectedFile();
    FileFilter fileFilter = this.getFileFilter(); // ��ȡ��ǰ���ļ�������
    if (fileFilter instanceof BaseFileFilter) { // ���⵱ǰΪĬ�Ϲ�����ʱ���������쳣
      BaseFileFilter baseFileFilter = (BaseFileFilter) fileFilter;
      file = Util.checkFileName(file.getAbsolutePath(), baseFileFilter,
          baseFileFilter.getExt());
    }
    if (file != null && file.exists()) {
      this.setSelectedFile(file);
      super.approveSelection();
    } else { // ���û�δѡ���ļ���ѡ����ļ�������ʱ����������ʾ��
      JOptionPane.showMessageDialog(this, "�ļ������ڣ�������ѡ��", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    }
  }
}
