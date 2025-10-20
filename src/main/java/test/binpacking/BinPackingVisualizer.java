package test.binpacking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;

public class BinPackingVisualizer extends JFrame {
    private final JPanel drawingPanel;
    private BinPacking.Result currentResult;
    private BinPacking.RectSource[] currentSources;
    private int currentSeed = 187; // 初始种子
    private final JTextField seedField; // 种子输入框
    private int currentStep = 0; // 当前显示的步骤（矩形数量）
    // 新增：存储高亮的网格坐标
    private Integer highlightedX = null;
    private Integer highlightedY = null;

    // 颜色数组用于区分不同的矩形
    private static final Color[] COLORS = {
            Color.RED, Color.GREEN, Color.BLUE, Color.CYAN,
            Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW,
            Color.LIGHT_GRAY, Color.DARK_GRAY
    };

    public BinPackingVisualizer() {
        setTitle("二维装箱算法可视化");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 创建控制面板
        JPanel controlPanel = new JPanel();
        JButton smallTestBtn = new JButton("测试小样例");
        JButton largeTestBtn = new JButton("测试大样例");
        JButton incrementSeedBtn = new JButton(">");
        JButton decrementSeedBtn = new JButton("<");
        // 添加上一步和下一步按钮
        JButton prevBtn = new JButton("上一步");
        JButton nextBtn = new JButton("下一步");
        seedField = new JTextField(5);
        seedField.setText(String.valueOf(currentSeed));

        // 创建绘图面板
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawPackingResult(g);
            }
        };

        // 新增：添加鼠标点击事件监听器
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentResult == null) return;

                int size = currentResult.size();
                int panelWidth = drawingPanel.getWidth();
                int panelHeight = drawingPanel.getHeight();
                int margin = 30;
                int maxDim = Math.max(size, 1);
                double scaleX = (panelWidth - 2 * margin) / (double) maxDim;
                double scaleY = (panelHeight - 2 * margin) / (double) maxDim;
                double scale = Math.min(scaleX, scaleY);

                // 计算点击位置相对于网格的坐标
                int clickX = e.getX();
                int clickY = e.getY();

                // 检查点击是否在网格区域内
                if (clickX >= margin && clickX <= margin + size * scale &&
                        clickY >= margin && clickY <= margin + size * scale) {

                    // 计算对应的网格坐标
                    highlightedX = (int) Math.round((clickX - margin) / scale);
                    highlightedY = (int) Math.round((clickY - margin) / scale);

                    // 确保坐标在有效范围内
                    highlightedX = Math.max(0, Math.min(size, highlightedX));
                    highlightedY = Math.max(0, Math.min(size, highlightedY));
                } else {
                    // 点击在网格外，清除高亮
                    highlightedX = null;
                    highlightedY = null;
                }
                drawingPanel.repaint();
            }
        });

        // 添加种子修改监听器
        seedField.addActionListener(e -> {
            try {
                int newSeed = Integer.parseInt(seedField.getText().trim());
                currentSeed = newSeed;
                // 使用新种子重新生成大样例
                onLargeTestClicked(null);
            } catch (NumberFormatException ex) {
                // 输入无效时恢复原来的值
                seedField.setText(String.valueOf(currentSeed));
            }
        });

        // +1按钮点击事件
        incrementSeedBtn.addActionListener(e -> {
            currentSeed++;
            seedField.setText(String.valueOf(currentSeed));
            onLargeTestClicked(null);
        });

        // -1按钮点击事件
        decrementSeedBtn.addActionListener(e -> {
            currentSeed--;
            seedField.setText(String.valueOf(currentSeed));
            onLargeTestClicked(null);
        });

        // 上一步按钮点击事件
        prevBtn.addActionListener(e -> {
            if (currentStep > 0) {
                currentStep--;
                drawingPanel.repaint();
            }
        });

        // 下一步按钮点击事件
        nextBtn.addActionListener(e -> {
            if (currentResult != null && currentStep < currentResult.placed().length) {
                currentStep++;
                drawingPanel.repaint();
            }
        });

        smallTestBtn.addActionListener(this::onSmallTestClicked);
        largeTestBtn.addActionListener(this::onLargeTestClicked);

        controlPanel.add(smallTestBtn);
        controlPanel.add(largeTestBtn);
        controlPanel.add(decrementSeedBtn);
        controlPanel.add(seedField);
        controlPanel.add(incrementSeedBtn);
        // 添加步骤控制按钮
        controlPanel.add(prevBtn);
        controlPanel.add(nextBtn);
        add(controlPanel, BorderLayout.NORTH);

        drawingPanel.setBackground(Color.WHITE);
        add(new JScrollPane(drawingPanel), BorderLayout.CENTER);

        pack();
        setSize(1600, 1200);
        setLocationRelativeTo(null); // 居中显示

        // 程序启动时自动调用大样例测试
        onLargeTestClicked(null);
    }

    // 绘制装箱结果
    private void drawPackingResult(Graphics g) {
        if (currentResult == null || currentSources == null) {
            g.drawString("请点击测试按钮生成装箱结果", 20, 20);
            return;
        }

        int size = currentResult.size();
        BinPacking.PlacedRect[] placedRects = currentResult.placed();

        // 计算缩放比例，确保整个装箱结果能在面板中显示
        int panelWidth = drawingPanel.getWidth();
        int panelHeight = drawingPanel.getHeight();
        int margin = 30;
        int maxDim = Math.max(size, 1);
        double scaleX = (panelWidth - 2 * margin) / (double) maxDim;
        double scaleY = (panelHeight - 2 * margin) / (double) maxDim;
        double scale = Math.min(scaleX, scaleY);

        // 绘制大矩形（装箱空间）
        g.setColor(Color.BLACK);
        g.drawRect(margin, margin,
                (int)(size * scale),
                (int)(size * scale));


        // ===================== 绘制网格线 =====================
        Color darkGreen=new Color(64,128,0);
        // 1. 绘制垂直网格线（x轴方向）
        for (int x = 1; x <= size; x++) {
            int lineX = margin + (int) (x * scale);
            if (x > 0 && (x & (x - 1)) == 0) {
                g.setColor(darkGreen);
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }
            g.drawString(String.valueOf(x), lineX, margin -10);
            g.drawLine(lineX, margin-4, lineX, margin + (int) (size * scale));
        }

        // 2. 绘制水平网格线（y轴方向）
        for (int y = 1; y <= size; y++) {
            int lineY = margin + (int) (y * scale);
            if (y > 0 && (y & (y - 1)) == 0) {
                g.setColor(darkGreen);
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }
            g.drawString(String.valueOf(y), margin-16, lineY-4);
            g.drawLine(margin-4, lineY, margin + (int) (size * scale), lineY);
        }
        // ===================== 网格线绘制结束 =====================

        // 绘制当前步骤前的所有矩形
        int displayCount = Math.min(currentStep, placedRects.length);
        for (int i = 0; i < displayCount; i++) {
            BinPacking.PlacedRect placed = placedRects[i];

            // 找到对应的矩形源数据
            BinPacking.RectSource source = findRectSource(placed.id());
            if (source == null) continue;

            // 计算绘制位置和大小
            int x = margin + (int)(placed.x() * scale);
            int y = margin + (int)(placed.y() * scale);
            int width = (int)(source.rect().width() * scale);
            int height = (int)(source.rect().height() * scale);

            // 设置颜色（根据ID取模，循环使用颜色数组）
            g.setColor(COLORS[placed.id() % COLORS.length]);

            // 绘制填充矩形
            g.fillRect(x, y, width, height);

            // 绘制边框
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }

        // 绘制信息在后防止被遮挡
        for (int i = 0; i < displayCount; i++) {
            BinPacking.PlacedRect placed = placedRects[i];

            // 找到对应的矩形源数据
            BinPacking.RectSource source = findRectSource(placed.id());
            if (source == null) continue;

            // 计算绘制位置
            int x = margin + (int)(placed.x() * scale);
            int y = margin + (int)(placed.y() * scale);

            // 绘制矩形信息
            g.setColor(new Color(0xffffff - COLORS[placed.id() % COLORS.length].getRGB()));
            g.drawString(placed.id() + ": " + source.rect().width() + "x" + source.rect().height(),
                    x + 5, y + 15);
        }

        // 显示装箱空间大小和当前步骤信息
        g.setColor(Color.BLACK);
        g.drawString(size + "x" + size, margin-10, margin - 10);

        // 新增：绘制高亮的网格线（置于最前，最后绘制）
        if (highlightedX != null && highlightedY != null) {
            Graphics2D g2 = (Graphics2D) g;
            // 保存当前画笔状态
            Stroke originalStroke = g2.getStroke();
            // 设置蓝色和加粗线条
            g2.setColor(darkGreen);
            g2.setStroke(new BasicStroke(2));

            // 绘制高亮垂直线
            int highlightX = margin + (int) (highlightedX * scale);
            g2.drawLine(highlightX, margin, highlightX, margin + (int) (size * scale));

            // 绘制高亮水平线
            int highlightY = margin + (int) (highlightedY * scale);
            g2.drawLine(margin, highlightY, margin + (int) (size * scale), highlightY);

            g.drawString(highlightedX + "," + highlightedY, highlightX+2, highlightY-4);

            // 恢复原始画笔状态
            g2.setStroke(originalStroke);
        }
    }

    // 根据ID查找对应的矩形源数据
    private BinPacking.RectSource findRectSource(int id) {
        for (BinPacking.RectSource source : currentSources) {
            if (source.id() == id) {
                return source;
            }
        }
        return null;
    }

    // 生成小样例（宽高为1~3的矩形）
    private BinPacking.RectSource[] createSmallTestCases() {
        return new BinPacking.RectSource[] {
                new BinPacking.RectSource(0, new BinPacking.Rect(3, 3)),
                new BinPacking.RectSource(1, new BinPacking.Rect(3, 2)),
                new BinPacking.RectSource(2, new BinPacking.Rect(2, 3)),
                new BinPacking.RectSource(3, new BinPacking.Rect(2, 2)),
                new BinPacking.RectSource(4, new BinPacking.Rect(3, 1)),
                new BinPacking.RectSource(5, new BinPacking.Rect(1, 3)),
                new BinPacking.RectSource(6, new BinPacking.Rect(2, 1)),
                new BinPacking.RectSource(7, new BinPacking.Rect(1, 2)),
                new BinPacking.RectSource(8, new BinPacking.Rect(1, 1)),
                new BinPacking.RectSource(9, new BinPacking.Rect(1, 1))
        };
    }

    // 生成随机大样例（使用当前种子）
    private BinPacking.RectSource[] createLargeTestCases() {
        Random random = new Random(currentSeed); // 使用当前种子

        int [] counts = new int[random.nextInt(20) + 5];
        int length=0;

        for(int i=0;i<counts.length;i++){
            length+= counts[i] = random.nextInt(2) + 1;
        }

        int index=0;
        BinPacking.RectSource[] sources = new BinPacking.RectSource[length];
        for (int count:counts) {
            int width = random.nextInt(28) + 1;
            int height = random.nextInt(28) + 1;
            //生成多个同样宽度的矩形模拟常见情况
            for(int i=0;i<count;i++) {
                sources[index++] = new BinPacking.RectSource(index, new BinPacking.Rect(width, height));
            }
        }

        return sources;
    }

    // 处理小测试按钮点击
    private void onSmallTestClicked(ActionEvent e) {
        currentSources = createSmallTestCases();
        currentResult = BinPacking.pack(Arrays.copyOf(currentSources, currentSources.length));
        currentStep = currentSources.length; // 重置步骤
        drawingPanel.repaint();
    }

    // 处理大测试按钮点击
    private void onLargeTestClicked(ActionEvent e) {
        // 确保种子输入框的值与当前种子一致
        seedField.setText(String.valueOf(currentSeed));
        currentSources = createLargeTestCases();
        currentResult = BinPacking.pack(Arrays.copyOf(currentSources, currentSources.length));
        currentStep = currentSources.length; // 重置步骤
        drawingPanel.repaint();
    }

    public static void main(String[] args) {
        // 在EDT线程中启动Swing应用
        SwingUtilities.invokeLater(() -> {
            new BinPackingVisualizer().setVisible(true);
        });
    }
}