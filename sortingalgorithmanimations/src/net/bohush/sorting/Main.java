package net.bohush.sorting;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//Visualization and Comparison of Sorting Algorithms
public class Main extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private SortPanel[] sortPanels = new SortPanel[9];
	private static int size = 1000;
	private int sleepTime = 3;
	private String animationName = "";
	private boolean keypressed = false;

	public Main() {
		setLayout(new GridLayout(1, 1));
		SortPanelHolder sortPanelHolder = new SortPanelHolder();
		sortPanelHolder.setLayout(new GridLayout(0, 3));
		sortPanelHolder.setBackground(Color.BLACK);
		sortPanelHolder.setForeground(Color.BLACK);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width / 3;
		int height = screenSize.height / 3;
		sortPanels[0] = new SelectionSortPanel(" Selection Sort ", sleepTime, width, height);
		sortPanels[1] = new ShellSortPanel(" Shell Sort ", sleepTime, width, height);
		sortPanels[2] = new InsertionSortPanel(" Insertion Sort ", sleepTime, width, height);
		sortPanels[3] = new MergeSortPanel(" Merge Sort ", sleepTime, width, height);
		sortPanels[4] = new QuickSortPanel(" Quick Sort ", sleepTime, width, height);
		sortPanels[5] = new HeapSortPanel(" Heap Sort ", sleepTime, width, height);
		sortPanels[6] = new BubbleSortPanel(" Bubble Sort ", sleepTime, width, height);
		sortPanels[7] = new CombSortPanel(" Comb Sort ", sleepTime, width, height);
		sortPanels[8] = new CocktailSortPanel(" Cocktail Sort ", sleepTime, width, height);
		sortPanels[9] = new RadixSortPanel(Integer[] toBeSorted, VisualizerFrame frame, boolean lsd)
		
		
		for (int i = 0; i < sortPanels.length; i++) {
			sortPanels[i].setVisible(false);
			sortPanelHolder.add(sortPanels[i]);				
		}
		add(sortPanelHolder);
		addKeyListener(this);
		setFocusable(true);
		//System.out.println("KeyListener: class " + sortPanelHolder.getKeyListeners());
	}
	
	class SortPanelHolder extends JPanel {
	    @Override
	    protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    g.setColor(Color.WHITE);
		    Font animationNameFont = new Font(Font.MONOSPACED, Font.BOLD, 150);
		    FontMetrics animationNameFontFontMetrix = getFontMetrics(animationNameFont);
		    g.setFont(animationNameFont);
		    int x = (getWidth() - animationNameFontFontMetrix.stringWidth(animationName)) / 2;
		    int y = (getHeight() - animationNameFontFontMetrix.getLeading()) / 2;
		    g.drawString(animationName, x, y);
	    }
	}
	
	public void keyPressed(KeyEvent k) {
	    if (keypressed)
		return;
	    keypressed = true;
	    switch (k.getKeyCode()) {
		case KeyEvent.VK_SPACE:
		    for (int i = 0; i < sortPanels.length; i++) {
			sortPanels[i].toggleState();
		    }
		    break;
		case KeyEvent.VK_ENTER:
		    for (int i = 0; i < sortPanels.length; i++) {
			sortPanels[i].singleStep();
		    }
		    break;
	    }
	}
	public void keyReleased(KeyEvent k) { 
	    keypressed = false;
	}
	public void keyTyped(KeyEvent k) { }

	public void beginAnimation(String animationName, int[] list) {
		try {
			this.animationName = animationName;
			repaint();
			Thread.sleep(2000);
			this.animationName = "";
			repaint();
			for (int i = 0; i < sortPanels.length; i++) {
				sortPanels[i].setList(list);
				sortPanels[i].setVisible(true);
			}
			Thread.sleep(1000);
			ExecutorService executor = Executors.newFixedThreadPool(sortPanels.length);
			for (int i = 0; i < sortPanels.length; i++) {
				executor.execute(sortPanels[i]);
			}
			executor.shutdown();
			while(!executor.isTerminated()) {
			    try {
				executor.awaitTermination(30L, TimeUnit.MINUTES);
			    } catch (InterruptedException e) {
			    }
			}
			Thread.sleep(1000);
			for (int i = 0; i < sortPanels.length; i++) {
				sortPanels[i].setVisible(false);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Sorting Algorithm Animations");
		Main main = new Main();
		//frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setLayout(new GridLayout(1,1));
		frame.add(main);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		int[] list = new int[size];
		
		for (int i = 0; i < list.length; i++) {
			list[i] = i + 1;
		}
		for (int i = 0; i < list.length; i++) {
			int index = (int) (Math.random() * list.length);
			int temp = list[i];
			list[i] = list[index];
			list[index] = temp;
		}
		main.beginAnimation("Random", list);
		
		for (int i = 0; i < list.length; i++) {
			list[i] = (1 + i / (size / 4) ) * (size / 4);
		}
		for (int i = 0; i < list.length; i++) {
			int index = (int) (Math.random() * list.length);
			int temp = list[i];
			list[i] = list[index];
			list[index] = temp;
		}
		main.beginAnimation("Few Unique", list);

		
		for (int i = 0; i < list.length; i++) {
			list[i] = size - i;
		}
		main.beginAnimation("Reversed", list);
		
		
		for (int i = 0; i < list.length / 2; i++) {
			list[i] = i + 1;
		}
		for (int i = list.length / 2; i < list.length; i++) {
			list[i] = i + 2;
		}
		list[list.length - 1] = list.length / 2 + 1;
		main.beginAnimation("Almost Sorted", list);

		main.animationName = "End";
		main.repaint();
		try { Thread.sleep(2000); } catch (InterruptedException e) {};
		frame.setVisible(false);
		frame.dispose();
	}
}
