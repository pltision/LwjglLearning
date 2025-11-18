package test;
 
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
 
/**
 * 内部子窗体管理器
 */
public class InternalFrameManager {
    private static InternalFrameManager instance;
    private int frameCount = 0;
    private List<JInternalFrame> frames = new ArrayList<>();
 
    private InternalFrameManager(){}
 
    public static synchronized InternalFrameManager getInstance() {
        if (instance == null) instance = new InternalFrameManager();
        return instance;
    }
 
    /** 创建并显示新子窗体 */
    public void createFrame(JDesktopPane desktop) {
        String title = "子窗体-" + (++frameCount);
        JInternalFrame frame = new JInternalFrame(title, true, true, true, true);
        frame.setSize(300, 200);
        frame.setLocation(30*frameCount, 30*frameCount);
        frame.add(new JLabel("内容区：" + title, SwingConstants.CENTER), BorderLayout.CENTER);
        desktop.add(frame);
        frame.setVisible(true);
        frames.add(frame);
 
        // 添加监听器
        frame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                frames.remove(frame);
            }
        });
        try { frame.setSelected(true); } catch (Exception ignored){}
    }
 
    /** 关闭当前激活的子窗体 */
    public void closeCurrentFrame() {
        JInternalFrame cur = getCurrentFrame();
        if (cur != null) cur.dispose();
    }
 
    /** 激活下一个子窗体 */
    public void activateNextFrame() {
        if (frames.isEmpty()) return;
        JInternalFrame cur = getCurrentFrame();
        int idx = frames.indexOf(cur);
        int next = (idx + 1) % frames.size();
        try { frames.get(next).setSelected(true); } catch (Exception ignored){}
    }
 
    /** 平铺所有子窗体 */
    public void tileFrames(JDesktopPane desktop) {
        JInternalFrame[] arr = desktop.getAllFrames();
        int n = arr.length;
        if (n == 0) return;
        int cols = (int)Math.ceil(Math.sqrt(n));
        int rows = (int)Math.ceil((double)n/cols);
        Dimension size = desktop.getSize();
        int w = (size.width - (cols-1)*5)/cols;
        int h = (size.height - (rows-1)*5)/rows;
        for (int i=0;i<n;i++){
            int r=i/cols, c=i%cols;
            arr[i].setBounds(c*(w+5), r*(h+5), w, h);
        }
    }
 
    /** 获取当前激活子窗体 */
    private JInternalFrame getCurrentFrame() {
        for (JInternalFrame f : frames) {
            if (f.isSelected()) return f;
        }
        return frames.isEmpty() ? null : frames.get(0);
    }

    public static void main(String[] args) {
        // 在事件调度线程中运行Swing组件
        SwingUtilities.invokeLater(() -> {
            // 创建MDI主窗口
            MDIFrame mdiFrame = new MDIFrame();
            // 获取桌面面板
            JDesktopPane desktop = mdiFrame.getDesktop();
            // 创建测试子窗口
            InternalFrameManager manager = InternalFrameManager.getInstance();
            manager.createFrame(desktop);
            manager.createFrame(desktop);
            manager.createFrame(desktop);
        });
    }
}