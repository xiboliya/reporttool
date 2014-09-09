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

/**
 * ���ڱ�ʶ�ļ���չ�����͵�ö��
 * 
 * @author ��ԭ
 * 
 */
public enum FileExt {
  /**
   * Excel Sheet(Excel����ļ�)
   */
  XLS;

  /**
   * ��д����ķ���
   */
  public String toString() {
    switch (this) {
    case XLS:
      return ".xls";
    default:
      return ".xls";
    }
  }

  /**
   * ��ȡ�ļ���չ������������
   * 
   * @return �ļ���չ������������
   */
  public String getDescription() {
    switch (this) {
    case XLS:
      return "Excel���ӱ��(.xls)";
    default:
      return "Excel���ӱ��(.xls)";
    }
  }

}
