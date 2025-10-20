/*
 * DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 * Version 2, December 2004
 *
 * Copyright (C) 2025 puamila github.com/pltision
 *
 * Everyone is permitted to copy and distribute verbatim or modified
 * copies of this license document, and changing it is allowed as long
 * as the name is changed.
 *
 * DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 * TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 * 0. You just DO WHAT THE FUCK YOU WANT TO.
 */

package test.binpacking;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BinPacking {

    public record Rect (int width, int height) implements Comparable<Rect>{
        @Override
        public int compareTo(@NotNull Rect o) {
            //宽度越大越优先
            //高度越大越优先
            int compare = Integer.compare(o.width, width);
            if(compare != 0)
                return compare;
            return Integer.compare(o.height, height);
        }

        @NotNull
        @Override
        public String toString() {
            return "Rect{" +
                    "w=" + width +
                    ", h=" + height +
                    '}';
        }

        @Override
        public int hashCode() {
            return Objects.hash(width, height);
        }
    }

    //提供给外部程序，id可以用于指向自定义对象（如某个矩形图片）
    public record RectSource(int id,Rect rect) implements Comparable<RectSource>{

        public RectSource(int id,int w,int h){
            this(id,new Rect(w,h));
        }

        public RectSource(int w,int h){
            this(0,new Rect(w,h));
        }

        @Override
        public int compareTo(@NotNull RectSource o) {
            return rect.compareTo(o.rect);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof RectSource that)) return false;
            return Objects.equals(rect, that.rect);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(rect);
        }

        @NotNull
        public String print() {
            return id+": "+rect.width+"x"+rect.height;
        }
    }

    public static class Node {
        //节点的x坐标表示大于x的位置可用放置矩形
        //也就是说 可用宽度=填充矩形的大小-x
        public int x, y;
        //节点的length表示从x到下一个节点x
        //感觉并没有那么重要，去掉吧
//        public int length;

        Node(int x,int y){
            this.x=x;
            this.y=y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ')';
        }
    }

    public record PlacedRect(int id,int x,int y){}

    public record Result(int size,PlacedRect[] placed){}

    /**
     * 打包矩形
     */
    public static Result pack(RectSource[] rectangles){

        if(rectangles.length==0)
            return new Result(0,new PlacedRect[0]);

        Arrays.sort(rectangles,Comparator.comparing(RectSource::rect));

        ArrayList<PlacedRect> result=new ArrayList<>(rectangles.length);

        int size=1;
        TreeMap<Integer,Node> nodeSet=new TreeMap<>();
        nodeSet.put(0,new Node(0,0));

        //计算所有矩形的面积和
        int totalArea=0;
        for(RectSource rectSource:rectangles){
            totalArea+=rectSource.rect.width*rectSource.rect.height;
        }

        //找到最小的size
        while (size<rectangles[0].rect.width || size<rectangles[0].rect.height || size*size<totalArea){
            size*=2;
        }

        while(true){
            int placed=fillInRect(0,0,size,size,nodeSet,rectangles,result,binarySearchHigherOrEqual(rectangles,new RectSource(size,size)));

            if(rectangles.length-placed==0){
                break;
            }

            //如果没有矩形可以放了，说明空间已经被填满
            if(placed==0) {
                //不可能为空
                //如果最左侧没有节点要添加
                if (nodeSet.firstEntry().getValue().x != 0) {
                    nodeSet.put(0, new Node(0, size));
                }
                //x=size的节点不应该存在
                //最右侧的节点不在最低位置要添加
                if (nodeSet.lastEntry().getValue().y != 0) {
                    nodeSet.put(size, new Node(size, 0));
                }
                size *= 2;
            }
            else{
                RectSource[] newArray=new RectSource[rectangles.length-placed];
                for(int i=0,j=0;j<rectangles.length;j++){
                    if(rectangles[j]!=null){
                        newArray[i++]=rectangles[j];
                    }
                }
                rectangles=newArray;
            }

        }

        return new Result(size,result.toArray(PlacedRect[]::new));
    }

    public static <T extends Comparable<T>> int binarySearchHigherOrEqual(T[] array,T target){
        int l=0;
        int r=array.length;
        while(l<r){
            int mid=l+(r-l)/2;
            if(array[mid].compareTo(target)>=0){
                r=mid;
            }
            else{
                l=mid+1;
            }
        }
        return l;
    }


    public static int fillInRect(int x, int y, int xSize, int ySize, TreeMap<Integer, Node> nodeSet, RectSource[] rectangles, ArrayList<PlacedRect> result, int startForeach){

        int placed=0;

        int maxY=y+ySize;
        int maxX=x+xSize;

        //遍历宽高都小于等于xSize和ySize的矩形，从大到小排序
        for(int i=startForeach;i<rectangles.length;i++){

            RectSource rectSource=rectangles[i];

            if(rectSource==null)
                continue;

            Rect rect=rectSource.rect;

            //仅在递归调用时会出现的问题
            if(rect.width>xSize || rect.height>ySize)
                continue;

            //获取宽度可以放的下矩形的节点（也就是 node.x<x+xSize-rect.width）
            var available=nodeSet.headMap(x+xSize-rect.width,true);

            if(available.isEmpty())
                continue;

            for(var nodeEntry:available.entrySet()) {
                Node node = nodeEntry.getValue();

                int nextNodeX = node.x + rect.width;
                int nextY = node.y + rect.height;

                //超过最大高度了
                if (nextY> maxY)
                    continue;

                result.add(new PlacedRect(rectSource.id, node.x, node.y));
                rectangles[i]=null;
                placed++;

                {
                    var higherNodes = nodeSet.tailMap(nodeEntry.getKey(), false);
                    //合并被覆盖的节点（不包含放置的节点）
                    //发现扩容情况应该可以被覆盖大于两个节点，所以应该用循环
                    int lastY = node.y;
                    while (!higherNodes.isEmpty()) {
                        Node higher = higherNodes.firstEntry().getValue();


                        //移除被覆盖的节点
                        if (higher.x <= nextNodeX) {
                            higherNodes.remove(higher.x);
                            if (higher.x < nextNodeX) {
                                //应该前面放不下的新矩形也放不下吧（至少宽度一定大于当前的）
                                //所以从i开始迭代试试
                                placed += cycleFill(higher.x, higher.y, nextNodeX - higher.x, lastY - higher.y, rectangles, result, i);
                            }
                            lastY = higher.y;
                        } else {
                            break;
                        }
                    }
                    //添加新节点
                    if (nextNodeX < maxX)
                        nodeSet.put(nextNodeX, new Node(nextNodeX, lastY));
                }

                //合并左边的节点
                var lowerNodes = nodeSet.headMap(node.x,true);
                int lastX=node.x;
                while(!lowerNodes.isEmpty()){
                    Node lower = lowerNodes.lastEntry().getValue();

                    //移除被覆盖的节点
                    if (lower.y<=nextY) {
                        lowerNodes.remove(lower.x);
                        //这里填i应该不行，宽度可能大于当前的
                        placed +=cycleFill(lower.x, lower.y, lastX - lower.x, nextY - lower.y, rectangles, result, 0);
                        lastX=lower.x;
                    } else {
                        break;
                    }
                }
                lowerNodes.put(lastX,new Node(lastX,nextY));

                System.out.println(printRectPlaced(x,y,xSize,ySize,rectSource,nodeSet));
                break;
            }
        }

        return placed;
    }

    public static int cycleFill(int x,int y,int xSize,int ySize,RectSource[] rectangles,ArrayList<PlacedRect> result,int startForeach){
        TreeMap<Integer, Node> newNodes = new TreeMap<>();
        newNodes.put(x, new Node(x, y));

        return fillInRect(x,y,xSize,ySize,newNodes,rectangles,result,startForeach);
    }

    public static StringBuilder printRectPlaced(int x,int y,int w,int h,RectSource source,TreeMap<Integer,Node> nodeSet){
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(x).append(", ").append(y).append("), [").append(w).append("x").append(h).append("] \t");
        sb.append(source.print());
        sb.append(" \t");
        nodeSet.forEach((nodeX,node)->sb.append(node).append(" "));
        return sb;
    }
}