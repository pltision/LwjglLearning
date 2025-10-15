package test.chunk;

import org.joml.Vector2i;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

class ChunkVisualizer {
    private static final int RECT_SIZE = 60; // 每个区块的显示大小

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("区块可视化测试");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 创建主面板，用于放置两个可视化面板并设置间距
            JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


            ChunkCache cache = new ChunkCache(2, new Vector2i(0, 0), Level.createLevel());


            // 创建第一个面板
            ChunkVisualizerPanel panel1 = new ChunkVisualizerPanel(2,RECT_SIZE);

            // 创建第二个面板
            CacheVisualizerPanel panel2 = new CacheVisualizerPanel(cache,RECT_SIZE);

            // 只有第一个面板可以移动，并同步到两个面板
            panel1.setApplyMovePlayer(pos -> {
                cache.setPosition(pos);  // 更新缓存
                cache.fill();
                panel2.updatePlayerPosition(pos);
                panel1.updatePlayerPosition(pos); // 同步移动
            });

            // 将两个面板添加到主面板
            mainPanel.add(panel1);
            mainPanel.add(panel2);

            // 将主面板添加到框架
            frame.add(mainPanel);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

// 封装成Panel组件，可嵌入到任何Swing容器中
class ChunkVisualizerPanel extends JPanel {
    private int radius; // 玩家周围显示的区块范围
    private final Vector2i player;
    private final Vector2i prePos;
    private final Level level;
    // 记录玩家上一个区块位置

    // 新增的Consumer，用于处理移动操作，默认什么都不做
    private Consumer<Vector2i> applyMovePlayer = p->{};

    private final int rectSize;

    public ChunkVisualizerPanel(int radius,int rectSize) {
        this.rectSize = rectSize;

        setRadius(radius);

        // 初始化玩家和地图
        player = new Vector2i();
        prePos = new Vector2i();

        level = Level.createLevel();

        // 设置面板属性


        // 添加鼠标交互 - 点击移动玩家
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    public void setRadius(int radius){
        this.radius=radius;
        setPreferredSize(new Dimension(
                (radius * 2 + 1) * ChunkVisualizerPanel.this.rectSize,
                (radius * 2 + 1) * ChunkVisualizerPanel.this.rectSize
        ));
    }

    // 设置applyMovePlayer的setter方法
    public void setApplyMovePlayer(Consumer<Vector2i> applyMovePlayer) {
        this.applyMovePlayer = applyMovePlayer;
    }

    // 处理鼠标点击事件，计算目标位置并通过Consumer处理
    private void handleMouseClick(MouseEvent e) {
        // 计算点击位置对应的区块坐标
        int chunkX = player.x - radius + e.getX() / rectSize;
        int chunkY = player.y - radius + e.getY() / rectSize;

        // 通过Consumer处理移动操作
        applyMovePlayer.accept(new Vector2i(chunkX, chunkY));
    }

    // 提供设置玩家位置的方法
    public void updatePlayerPosition(Vector2i vector2i) {
        prePos.set(player);
        player.set(vector2i);
        repaint();
    }

    // 提供获取玩家当前位置的方法
    public Vector2i getPlayerPosition() {
        return new Vector2i(player.x, player.y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制玩家周围的区块
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int chunkX = player.x + dx;
                int chunkY = player.y + dy;

                // 从Level获取区块
                Chunk chunk = level.getChunk(chunkX, chunkY);

                // 计算绘制位置
                int drawX = (dx + radius) * rectSize;
                int drawY = (dy + radius) * rectSize;

                // 绘制区块背景
                if (dx == 0 && dy == 0) {
                    // 玩家所在的区块用不同颜色
                    g2.setColor(new Color(255, 240, 240));
                } else {
                    g2.setColor(new Color(240, 240, 255));
                }
                g2.fillRect(drawX, drawY, rectSize - 1, rectSize - 1);

                // 绘制区块边框
                g2.setColor(Color.BLACK);
                g2.drawRect(drawX, drawY, rectSize - 1, rectSize - 1);

                // 绘制区块坐标
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(chunk.x + "," + chunk.y,
                        drawX + 5,
                        drawY + rectSize / 2);
            }
        }

        // 绘制玩家上一个位置标记（淡红色）
        int prevDrawX = (prePos.x - player.x + radius) * rectSize + rectSize / 2;
        int prevDrawY = (prePos.y - player.y + radius) * rectSize + rectSize / 2;
        g2.setColor(new Color(255, 100, 100, 100)); // 淡红色，带透明度
        g2.fillOval(prevDrawX - 6, prevDrawY - 6, 12, 12);

        // 绘制玩家当前位置标记（红色）
        int playerDrawX = radius * rectSize + rectSize / 2;
        int playerDrawY = radius * rectSize + rectSize / 2;
        g2.setColor(Color.RED);
        g2.fillOval(playerDrawX - 6, playerDrawY - 6, 12, 12);
    }


}

class CacheVisualizerPanel extends JPanel {
    private final ChunkCache cache;
    private final Vector2i player;
    private final Vector2i prePos;

    private int size;
    private int rectSize;

    CacheVisualizerPanel(ChunkCache cache,int rectSize) {
        this.cache = cache;
        this.size = cache.getSize();
        this.player = new Vector2i();
        this.prePos = new Vector2i();
        this.rectSize = rectSize;

        setPreferredSize(new Dimension(
                size * this.rectSize,
                size * this.rectSize
        ));
    }

    public void updatePlayerPosition(Vector2i vector2i) {
        prePos.set(player);
        player.set(vector2i);
        repaint();
    }

    public void updateCache() {
        size=cache.getSize();

    }

    public void setRectSize(int rectSize) {
        this.rectSize = rectSize;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制玩家周围的区块
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {

                // 从缓存的数组获取区块
                Chunk chunk = cache.viewCacheArray(x, y);

                // 计算绘制位置
                int drawX = x * rectSize;
                int drawY = y * rectSize;

                // 绘制区块背景
                if(chunk!=null&&chunk.isNew) {
                    g2.setColor(new Color(200, 255, 200));
                }
                else{
                    g2.setColor(new Color(240, 240, 255));
                }
                g2.fillRect(drawX, drawY, rectSize - 1, rectSize - 1);

                // 绘制区块边框
                g2.setColor(Color.BLACK);
                g2.drawRect(drawX, drawY, rectSize - 1, rectSize - 1);

                // 绘制区块坐标
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(chunk!=null? chunk.x + "," + chunk.y:"null",
                        drawX + 5,
                        drawY + rectSize / 2);
            }
        }

        // 绘制玩家上一个位置标记（淡红色）
        Vector2i cachePos=new Vector2i();
        cache.mapToCacheIndex(prePos,cachePos);
        int prevDrawX = cachePos.x * rectSize + rectSize / 2;
        int prevDrawY = cachePos.y * rectSize + rectSize / 2;
        g2.setColor(new Color(255, 100, 100, 100)); // 淡红色，带透明度
        g2.fillOval(prevDrawX - 6, prevDrawY - 6, 12, 12);

        // 绘制玩家当前位置标记（红色）
        cache.mapToCacheIndex(player,cachePos);
        int playerDrawX = cachePos.x * rectSize + rectSize / 2;
        int playerDrawY = cachePos.y * rectSize + rectSize / 2;
        g2.setColor(Color.RED);
        g2.fillOval(playerDrawX - 6, playerDrawY - 6, 12, 12);
    }

}
