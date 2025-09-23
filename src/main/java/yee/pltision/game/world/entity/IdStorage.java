package yee.pltision.game.world.entity;

import java.util.*;
import java.util.function.BiConsumer;

public class IdStorage<E> {
    private final List<E> storage;  // null表示ID已回收
    private final Deque<Integer> recycledIds;  // 存储回收的ID

    public IdStorage() {
        this.storage = new ArrayList<>();
        this.recycledIds = new ArrayDeque<>();
    }


    /**
     * 获取下一个可用的ID（优先使用回收的ID）
     * 可能用于判断是否超出最大容量，目前不需要使用
     * @return 下一个可用ID
     */
    public int getNextId() {
        return recycledIds.isEmpty() ? storage.size() : recycledIds.peek();
    }

    /**
     * 快速分配ID并存储元素（O(1)复杂度）
     * @param element 要存储的元素（非null）
     * @return 分配的ID
     */
    public int alloc(E element) {
        if (element == null) {
            throw new NullPointerException("元素不能为null");
        }

        int id;
        if (!recycledIds.isEmpty()) {
            id = recycledIds.pop();
            storage.set(id, element);
        } else {
            id = storage.size();
            storage.add(element);
        }

        return id;
    }

    public int size() {
        return storage.size()-recycledIds.size();
    }


    public boolean isEmpty() {
        return size() == 0;
    }


    public boolean containsId(int id) {
        return id >= 0  && id < storage.size() && storage.get(id) != null;
    }


    public boolean containsValue(Object value) {
        if (value == null) return false;
        for (E val : storage) {
            if (val != null && Objects.equals(val, value)) {
                return true;
            }
        }
        return false;
    }


    public E get(int id) {
        if (id < 0 || id >= storage.size()) {
            return null;
        }
        return storage.get(id);
    }

    /**
     *  如果需要添加新元素，请使用alloc()方法自动分配ID
     *  主要用于服务端向客户端同步数据 (?)
     *  应该有最大值的限制，不然内存爆炸
     */
    public E set(int id, E value) {
        if (value == null) {
            return delete(id);
        }

        //当id超出当前容量时，扩展storage
        //记录未使用的id
        for (int i = storage.size(); i < id; i++) {
            recycledIds.push(i);
        }
        for (int i = storage.size(); i <= id; i++) {
            storage.add(null);
        }

        E oldValue = storage.get(id);
        if (oldValue == null) {
            recycledIds.remove(id);
        }

        storage.set(id, value);
        return oldValue;
    }


    public E delete(int id) {
        if (id < 0  || id >= storage.size()) {
            return null;
        }

        E oldValue = storage.get(id);
        if (oldValue != null) {
            recycledIds.push(id);
            storage.set(id, null);
        }
        return oldValue;
    }


    public void clear() {
        storage.clear();
        recycledIds.clear();
    }


    public int[] ids() {
        int[] ids = new int[size()];
        int index = 0;
        for (int id = 0; id < storage.size(); id++) {  // 遍历索引（即ID）
            if (storage.get(id) != null) {  // 有效元素的ID
                ids[index++] = id;
            }
        }
        return ids;
    }


    public Collection<E> values() {
        List<E> values = new ArrayList<>(size());
        for (E element : storage) {
            if (element != null) {
                values.add(element);
            }
        }
        return values;
    }

    public void forEach(BiConsumer<? super Integer, ? super E> action) {
        Objects.requireNonNull(action);
        for (int id = 0; id < storage.size(); id++) {
            E element = storage.get(id);
            if (element != null) {
                action.accept(id, element);
            }
        }
    }
}
