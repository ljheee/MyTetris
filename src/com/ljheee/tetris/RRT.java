package com.ljheee.tetris;

import java.applet.*;
import java.awt.*;

public class RRT extends Applet implements Runnable {
	private static final long serialVersionUID = 6146581752215474133L;
	Font font = new Font("Dialog", Font.BOLD, 18);// ��������
	Thread thread;// �����߳�
	public java.awt.Graphics gg;// ����ͼ�α���
	public boolean[][] screen;// ������Ϸ��ͼ
	public int[] x;// ��¼�����������
	public int[] y;// ��¼�����������
	public int[] x1;// ��¼����֮ǰ��������
	public int[] y1;// ��¼����֮ǰ��������
	public int rand;// �����
	public int[] row;// ��¼ÿһ�еķ�����
	public int mark;// ��¼ÿһ�صķ���
	public boolean open;// ��Ϸ��������
	public int pass;// ��Ϸ����
	public boolean game;// ������Ϸ��ʼ����

	/**
	 * ��������ͼ�β�����������������������
	 */
	public void begin() {
		int i;
		rand = (int) (Math.random() * 7) + 1;
		switch (rand) {
		case 1:
			x[0] = 4 + 0;
			y[0] = 0;
			x[1] = 4 + 1;
			y[1] = 0;
			x[2] = 4 + 2;
			y[2] = 0;
			x[3] = 4 + 3;
			y[3] = 0; // һ����
			x[4] = 4;
			y[4] = -1;
			break;
		case 2:
			x[0] = 4 + 0;
			y[0] = 0;
			x[1] = 4 + 1;
			y[1] = 0;
			x[2] = 4 + 1;
			y[2] = 1;
			x[3] = 4 + 2;
			y[3] = 1; // Z����
			x[4] = 4;
			y[4] = 0;
			break;
		case 3:
			x[0] = 4 + 1;
			y[0] = 0;
			x[1] = 4 + 2;
			y[1] = 0;
			x[2] = 4 + 0;
			y[2] = 1;
			x[3] = 4 + 1;
			y[3] = 1; // ��Z��
			x[4] = 4;
			y[4] = 0;
			break;
		case 4:
			x[0] = 4 + 0;
			y[0] = 0;
			x[1] = 4 + 1;
			y[1] = 0;
			x[2] = 4 + 1;
			y[2] = 1;
			x[3] = 4 + 1;
			y[3] = 2; // 7����
			x[4] = 4;
			y[4] = 0;
			break;
		case 5:
			x[0] = 4 + 1;
			y[0] = 0;
			x[1] = 4 + 2;
			y[1] = 0;
			x[2] = 4 + 1;
			y[2] = 1;
			x[3] = 4 + 1;
			y[3] = 2; // ��7����
			x[4] = 4;
			y[4] = 0;
			break;
		case 6:
			x[0] = 4 + 1;
			y[0] = 0;
			x[1] = 4 + 0;
			y[1] = 1;
			x[2] = 4 + 1;
			y[2] = 1;
			x[3] = 4 + 2;
			y[3] = 1; // ��T��
			x[4] = 4;
			y[4] = 0;
			break;
		case 7:
			x[0] = 4 + 0;
			y[0] = 0;
			x[1] = 4 + 1;
			y[1] = 0;
			x[2] = 4 + 0;
			y[2] = 1;
			x[3] = 4 + 1;
			y[3] = 1; // ������
			x[4] = 4;
			y[4] = 0;

		}
		for (i = 0; i < 4; i++)
			if (screen[x[i]][y[i] + 1]) // �ж��ڳ�ȡһ��ͼ�κ��ڸ�ͼ�ε���һ���Ƿ����ϰ���
				destroy();
		save();
		xian();
	}

	/**
	 * ���Ʒ���ı���
	 */
	public void change() {
		int i, j = 0;
		int m[] = new int[4];
		int n[] = new int[4];
		if (rand != 7) // ����Ϊ������ʱ������
		{
			for (i = 0; i < 4; i++) {
				m[i] = y[i] - y[4] + x[4];
				n[i] = 2 - x[i] + x[4] + y[4];// ����������ѱ��κ��ͼ�δ�����
				if (m[i] < 0 || m[i] > 11 || n[i] > 18)
					j = 1;
			}
			if (j != 1) {
				for (i = 0; i < 4; i++) {
					if (screen[m[i]][n[i]])// �жϱ��κ��������û����֮ǰ����������Ϸ��ͼ���Ƿ����е����
						break;
				}
				if (i == 4) // ֤�����κ�ĵ㲻�����ϰ�
					for (i = 0; i < 4; i++) {
						x[i] = m[i];
						y[i] = n[i]; // ����Ϸ��ͼ�б��κ��ͼ�θ�����е�ͼ��
					}
			}
		}
	}

	/**
	 * ����������Դ�� ��� applet �ǻ�ģ�������ֹͣ��
	 */
	public void destroy() {
		game = false;
		pass = 1; // ������ʼ��
		repaint(); // ������Ļ��ʼ��
	}

	/**
	 * ��Ϸ��ʼ��
	 */
	public void gameinit() {
		int i, j;
		for (i = 0; i < 12; i++)
			for (j = 0; j < 19; j++)
				screen[i][j] = false;// ����Ϸ��ͼ�����е㶼���
		for (i = 0; i < 19; i++)
			row[i] = 0; // ��ÿһ�еķ������������
		open = true; // ����Ϸ��ͼ��������
		mark = 0;
		begin();
	}

	/**
	 * ��ʼ����������
	 */
	public void init() {
		screen = new boolean[12][19];
		x = new int[5];
		y = new int[5];
		x1 = new int[4];
		y1 = new int[4];
		row = new int[19];
		pass = 1;
		game = false; // ������Ϸ����
	}

	/**
	 * ��������Ϣ �ڴ������������ң�����ʱ�� �����ж����ǵ���λ���Ƿ���ã� ������ִ�а������������������β�����
	 */
	public boolean keyDown(Event e, int key) {
		int i;
		if (key == 's' && !game) {
			game = true;// ��Ϸ��ʼ
			repaint();
		}
		if (game) {
			save();
			switch (key) {
			case Event.LEFT:
				for (i = 0; i < 4; i++)
					if (x[i] < 1 || screen[x[i] - 1][y[i]])// �ж��Ƿ񵽴���Ļ����߻��з�������һ����û�о�ֹ�ķ���
						break;
				if (i == 4) // ����δ��������ߣ��򷽿������ƶ�
					for (i = 0; i < 5; i++)
						x[i] -= 1;
				break;
			case Event.RIGHT:
				for (i = 0; i < 4; i++)
					if (x[i] > 10 || screen[x[i] + 1][y[i]])// �ж��Ƿ񵽴���Ļ���ұ߻��з�����ұ�һ����û�о�ֹ�ķ���
						break;
				if (i == 4) // ����δ�������ұߣ��򷽿������ƶ�
					for (i = 0; i < 5; i++)
						x[i] += 1;
				break;
			case Event.DOWN:
				for (i = 0; i < 4; i++)
					if (y[i] == 18)// �ж��Ƿ񵽴���Ϸ��ͼ��׶�
						reach();
				for (i = 0; i < 4; i++)
					if (screen[x[i]][y[i] + 1])// �жϻ�еķ���������һ���Ƿ����ϰ���
						reach();
				for (i = 0; i < 5; i++)
					y[i] += 1;// ����������ƶ�һ��
				break;
			case Event.UP:
				change();
			}
			xian();
		}
		return true;
	}

	/**
	 * ������Ϸ����
	 */
	public void paint(Graphics g) {
		g.setFont(font);
		g.setColor(Color.red);
		g.fillRect(50, 70, 320, 450);
		g.setColor(Color.black);
		g.fillRect(60, 80, 300, 430);

		if (game)
			gameinit();// ������Ϸ��ʼ��
		else {
			g.setColor(Color.green);
			g.drawString("���£Ӽ���Ϸ��ʼ", 130, 200);
		}
	}

	/**
	 * �����¼� �ж����м�����
	 */
	public void reach() {
		int i, j = 18, k = 0;
		for (i = 0; i < 4; i++) {
			screen[x[i]][y[i]] = true;
			row[y[i]]++;
		}
		// ��������
		while (row[j] != 0) {
			while (row[j - k] == 12)
				k++;// ����������һ��,ͳ��������
			if (k > 0)// ʹ��Ļ���������ƶ�
			{
				if (row[j - k] != 0) {
					for (i = 0; i < 12; i++)
						screen[i][j] = screen[i][j - k];
					row[j] = row[j - k];
				} else {
					for (i = 0; i < 12; i++)
						screen[i][j] = false;
					row[j] = 0;
				}

			}
			j--;
		}
		if (k > 0)
			open = true;
		mark += k * 10;// ͳ�з���
		if (mark >= 200) {
			pass++;
			gameinit();

		}

		begin();
	}

	/**
	 * ����������ƶ�
	 */
	public void run() {
		int i, j;
		while (true) {
			if (game) {
				save();
				for (i = 0; i < 4; i++)
					if (y[i] == 18)// �жϻ�����Ƿ񵽴���Ϸ������׶�
						reach();
				for (i = 0; i < 4; i++)
					if (screen[x[i]][y[i] + 1])// �жϻ���������µ�һ���Ƿ����ϰ���
						reach();
				for (i = 0; i < 5; i++)
					y[i] += 1;// ����������ƶ�һ��
				xian();
			}
			try {
				Thread.sleep(1200 - pass * 300);
			} // ���ݹ��������Ӷ��ӿ���Ϸ�ٶ�
			catch (InterruptedException e) {
				break;
			}
		}
	}

	/**
	 * ����ԭ�е�����
	 */
	public void save() {
		int i;
		for (i = 0; i < 4; i++) {
			x1[i] = x[i];
			y1[i] = y[i];
		}
	}

	/**
	 * �����̡߳�
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * ��ֹ�̣߳�����֮�������õ�Ԫ�ռ���
	 */
	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	/**
	 * ���Ʒ���
	 */
	public void xian() {
		int i = 0, j = 0;
		gg = getGraphics();
		if (open) {
			open = false;
			gg.setColor(Color.white);
			gg.fillRect(400, 150, 100, 350);
			gg.setFont(font);
			gg.setColor(Color.black);
			gg.drawString("����: " + mark, 400, 200);
			gg.drawString("�� " + pass + " ��", 400, 300);
			gg.setColor(Color.black);
			gg.fillRect(60, 80, 300, 430);
			for (i = 18; i > 0; i--)
				for (j = 0; j < 12; j++)
					if (screen[j][i]) {
						gg.setColor(Color.green);
						gg.fillRect(60 + j * 25 + 1, 30 + i * 25 + 1, 23, 23);
						gg.setColor(Color.blue);
						gg.fillRect(60 + j * 25 + 5, 30 + i * 25 + 5, 15, 15);

					}

		}
		gg.setColor(Color.black);
		for (i = 0; i < 4; i++)
			if (y1[i] > 1)
				gg.fillRect(60 + x1[i] * 25, 30 + y1[i] * 25, 25, 25);
		for (i = 0; i < 4; i++)
			if (y[i] > 1) {
				gg.setColor(Color.green);
				gg.fillRect(60 + x[i] * 25 + 1, 30 + y[i] * 25 + 1, 23, 23);
				gg.setColor(Color.blue);
				gg.fillRect(60 + x[i] * 25 + 5, 30 + y[i] * 25 + 5, 15, 15);
			}
	}
}
