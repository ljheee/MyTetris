package com.ljheee.tetris;

import java.applet.*;
import java.awt.*;

public class RRT extends Applet implements Runnable {
	private static final long serialVersionUID = 6146581752215474133L;
	Font font = new Font("Dialog", Font.BOLD, 18);// 定义字体
	Thread thread;// 定义线程
	public java.awt.Graphics gg;// 定义图形变量
	public boolean[][] screen;// 控制游戏地图
	public int[] x;// 记录方块的行坐标
	public int[] y;// 记录方块的列坐标
	public int[] x1;// 记录方块活动之前的行坐标
	public int[] y1;// 记录方块活动之前的列坐标
	public int rand;// 随机数
	public int[] row;// 记录每一行的方块数
	public int mark;// 记录每一关的分数
	public boolean open;// 游戏清屏开关
	public int pass;// 游戏关数
	public boolean game;// 控制游戏开始开关

	/**
	 * 绘制七种图形并调用随机函数令它随机出现
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
			y[3] = 0; // 一字型
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
			y[3] = 1; // Z字型
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
			y[3] = 1; // 反Z型
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
			y[3] = 2; // 7字型
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
			y[3] = 2; // 反7字型
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
			y[3] = 1; // 反T型
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
			y[3] = 1; // 田字型
			x[4] = 4;
			y[4] = 0;

		}
		for (i = 0; i < 4; i++)
			if (screen[x[i]][y[i] + 1]) // 判断在抽取一种图形后，在该图形的下一行是否有障碍物
				destroy();
		save();
		xian();
	}

	/**
	 * 控制方块的变形
	 */
	public void change() {
		int i, j = 0;
		int m[] = new int[4];
		int n[] = new int[4];
		if (rand != 7) // 当不为田字型时，变形
		{
			for (i = 0; i < 4; i++) {
				m[i] = y[i] - y[4] + x[4];
				n[i] = 2 - x[i] + x[4] + y[4];// 用两个数组把变形后的图形存起来
				if (m[i] < 0 || m[i] > 11 || n[i] > 18)
					j = 1;
			}
			if (j != 1) {
				for (i = 0; i < 4; i++) {
					if (screen[m[i]][n[i]])// 判断变形后的坐标在没变形之前该坐标在游戏地图中是否已有点存在
						break;
				}
				if (i == 4) // 证明变形后的点不存在障碍
					for (i = 0; i < 4; i++) {
						x[i] = m[i];
						y[i] = n[i]; // 把游戏地图中变形后的图形赋给活动中的图形
					}
			}
		}
	}

	/**
	 * 清理挂起的资源。 如果 applet 是活动的，它将被停止。
	 */
	public void destroy() {
		game = false;
		pass = 1; // 关数初始化
		repaint(); // 调用屏幕初始化
	}

	/**
	 * 游戏初始化
	 */
	public void gameinit() {
		int i, j;
		for (i = 0; i < 12; i++)
			for (j = 0; j < 19; j++)
				screen[i][j] = false;// 把游戏地图中所有点都清空
		for (i = 0; i < 19; i++)
			row[i] = 0; // 把每一行的方块总数都清空
		open = true; // 打开游戏地图清屏开关
		mark = 0;
		begin();
	}

	/**
	 * 初始化整个程序
	 */
	public void init() {
		screen = new boolean[12][19];
		x = new int[5];
		y = new int[5];
		x1 = new int[4];
		y1 = new int[4];
		row = new int[19];
		pass = 1;
		game = false; // 控制游戏开关
	}

	/**
	 * 处理按键信息 在处理方向（上下左右）控制时， 首先判断它们的新位置是否可用， 可用则执行按键操作，否则不理会这次操作。
	 */
	public boolean keyDown(Event e, int key) {
		int i;
		if (key == 's' && !game) {
			game = true;// 游戏开始
			repaint();
		}
		if (game) {
			save();
			switch (key) {
			case Event.LEFT:
				for (i = 0; i < 4; i++)
					if (x[i] < 1 || screen[x[i] - 1][y[i]])// 判断是否到达屏幕最左边或活动中方块的左边一格有没有静止的方块
						break;
				if (i == 4) // 当还未到达最左边，则方块向左移动
					for (i = 0; i < 5; i++)
						x[i] -= 1;
				break;
			case Event.RIGHT:
				for (i = 0; i < 4; i++)
					if (x[i] > 10 || screen[x[i] + 1][y[i]])// 判断是否到达屏幕最右边或活动中方块的右边一格有没有静止的方块
						break;
				if (i == 4) // 当还未到达最右边，则方块向右移动
					for (i = 0; i < 5; i++)
						x[i] += 1;
				break;
			case Event.DOWN:
				for (i = 0; i < 4; i++)
					if (y[i] == 18)// 判断是否到达游戏地图最底端
						reach();
				for (i = 0; i < 4; i++)
					if (screen[x[i]][y[i] + 1])// 判断活动中的方块在向下一格是否有障碍物
						reach();
				for (i = 0; i < 5; i++)
					y[i] += 1;// 活动方块向下移动一格
				break;
			case Event.UP:
				change();
			}
			xian();
		}
		return true;
	}

	/**
	 * 绘制游戏界面
	 */
	public void paint(Graphics g) {
		g.setFont(font);
		g.setColor(Color.red);
		g.fillRect(50, 70, 320, 450);
		g.setColor(Color.black);
		g.fillRect(60, 80, 300, 430);

		if (game)
			gameinit();// 调用游戏初始化
		else {
			g.setColor(Color.green);
			g.drawString("按下Ｓ键游戏开始", 130, 200);
		}
	}

	/**
	 * 到达事件 判断满行及消行
	 */
	public void reach() {
		int i, j = 18, k = 0;
		for (i = 0; i < 4; i++) {
			screen[x[i]][y[i]] = true;
			row[y[i]]++;
		}
		// 处理消行
		while (row[j] != 0) {
			while (row[j - k] == 12)
				k++;// 几行连续在一起,统计消行数
			if (k > 0)// 使屏幕方块向下移动
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
		mark += k * 10;// 统行分数
		if (mark >= 200) {
			pass++;
			gameinit();

		}

		begin();
	}

	/**
	 * 方块的向下移动
	 */
	public void run() {
		int i, j;
		while (true) {
			if (game) {
				save();
				for (i = 0; i < 4; i++)
					if (y[i] == 18)// 判断活动方块是否到达游戏界面最底端
						reach();
				for (i = 0; i < 4; i++)
					if (screen[x[i]][y[i] + 1])// 判断活动方块在向下的一行是否有障碍物
						reach();
				for (i = 0; i < 5; i++)
					y[i] += 1;// 活动方块向下移动一行
				xian();
			}
			try {
				Thread.sleep(1200 - pass * 300);
			} // 根据关数的增加而加快游戏速度
			catch (InterruptedException e) {
				break;
			}
		}
	}

	/**
	 * 保存原有的数据
	 */
	public void save() {
		int i;
		for (i = 0; i < 4; i++) {
			x1[i] = x[i];
			y1[i] = y[i];
		}
	}

	/**
	 * 启动线程。
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * 终止线程，并将之归入无用单元收集。
	 */
	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	/**
	 * 绘制方块
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
			gg.drawString("分数: " + mark, 400, 200);
			gg.drawString("第 " + pass + " 关", 400, 300);
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
