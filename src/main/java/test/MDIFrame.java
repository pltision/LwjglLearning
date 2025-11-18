package test;

import javax.swing.*;
import java.awt.event.ActionEvent;

//https://blog.csdn.net/m0_61840987/article/details/148845373

public class MDIFrame extends JFrame {
    private JDesktopPane desktop;
    public MDIFrame() {
        super("MDI 多文档示例");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        desktop = new JDesktopPane();
        setContentPane(desktop);
 
        // 菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenuItem newItem = new JMenuItem("新建子窗体 (Ctrl+N)");
        newItem.addActionListener(e -> InternalFrameManager.getInstance().createFrame(desktop));
        fileMenu.add(newItem);
        menuBar.add(fileMenu);
 
        JMenu windowMenu = new JMenu("窗口");
        JMenuItem tileItem = new JMenuItem("平铺排列");
        tileItem.addActionListener(e -> InternalFrameManager.getInstance().tileFrames(desktop));
        windowMenu.add(tileItem);
        menuBar.add(windowMenu);
 
        setJMenuBar(menuBar);
 
        // 快捷键
        setupShortcuts();
 
        // 显示
        setVisible(true);
    }
 
    private void setupShortcuts() {
        JRootPane root = getRootPane();
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();
        im.put(KeyStroke.getKeyStroke("control N"), "newFrame");
        am.put("newFrame", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                InternalFrameManager.getInstance().createFrame(desktop);
            }
        });
        im.put(KeyStroke.getKeyStroke("control W"), "closeFrame");
        am.put("closeFrame", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                InternalFrameManager.getInstance().closeCurrentFrame();
            }
        });
        im.put(KeyStroke.getKeyStroke("ctrl TAB"), "nextFrame");
        am.put("nextFrame", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                InternalFrameManager.getInstance().activateNextFrame();
            }
        });
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    /**
     * 应用程序入口
     */
    public static void main(String[] args) {
        // 在事件调度线程中启动应用
        SwingUtilities.invokeLater(MDIFrame::new);
    }
}